package jp.aegif.study.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.alfresco.repo.web.scripts.content.StreamContent;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ShowQRCodeWebScript extends StreamContent {

	@Override
	public void execute(WebScriptRequest request, WebScriptResponse response) throws IOException {

		String nodeRefStr = request.getParameter("noderef");
		
		if ( nodeRefStr == null ) {
			throw new WebScriptException("nodeRef is mandatory.");
		}
				
        String encoding = "UTF-8";
        int size = 100;

        Path tempFilePath = Files.createTempFile(Paths.get("/tmp"), "prefix-", null);
        File tempFile = new File(tempFilePath.toUri());

        //生成処理
        ConcurrentHashMap<EncodeHintType, Comparable> hints = new ConcurrentHashMap<>();
        //エラー訂正レベル指定
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        //エンコーディング指定
        hints.put(EncodeHintType.CHARACTER_SET, encoding);
        //マージン指定
        hints.put(EncodeHintType.MARGIN, 0);
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix;
		try {
			bitMatrix = writer.encode(nodeRefStr, BarcodeFormat.QR_CODE, size, size, hints);
	        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
	        ImageIO.write(image, "png", tempFile);
	        
//	        this.streamContent(request, response, tempFile);
	        this.streamContent(request, response, tempFile, true, "qr-code.png", null);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		finally {
			tempFile.delete();
		}
	}

	
}
