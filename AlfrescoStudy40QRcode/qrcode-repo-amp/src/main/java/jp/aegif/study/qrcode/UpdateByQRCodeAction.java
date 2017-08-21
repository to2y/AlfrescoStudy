package jp.aegif.study.qrcode;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.io.IOUtils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class UpdateByQRCodeAction extends ActionExecuterAbstractBase {

	private NodeService nodeService;
	private ContentService contentService;

	public static final String NAME="update-by-qr";

	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {

		try {
			if ( this.nodeService.exists(actionedUponNodeRef)) {

				ContentReader reader = 
						contentService.getReader(actionedUponNodeRef, ContentModel.PROP_CONTENT);
				InputStream is = reader.getContentInputStream();

				BufferedImage image = ImageIO.read(is);
				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
				Reader mfreader = new MultiFormatReader();
				Result decodeResult = mfreader.decode(bitmap);

				String result = decodeResult.getText();

				//assume result is noderef
				System.out.println("### result:" + result);
				if ( result.startsWith("workspace://SpacesStore") ) {
					NodeRef targetNodeRef = new NodeRef(result);
					//update targetNodeRef
					if ( this.nodeService.hasAspect(targetNodeRef, ContentModel.ASPECT_VERSIONABLE)) {
						this.nodeService.addAspect(targetNodeRef, ContentModel.ASPECT_VERSIONABLE, null);
					}

					ContentWriter writer = this.contentService.getWriter(targetNodeRef, ContentModel.PROP_CONTENT, true);
					try(OutputStream os = writer.getContentOutputStream()) {
						ContentReader reader2 = 
								contentService.getReader(actionedUponNodeRef, ContentModel.PROP_CONTENT);
						is = reader2.getContentInputStream();
						IOUtils.copy(is, os);
					}
				}
				else {
					throw new Exception("QR code is not noderef");
				}
				
				nodeService.deleteNode(actionedUponNodeRef);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		//nothing
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

}
