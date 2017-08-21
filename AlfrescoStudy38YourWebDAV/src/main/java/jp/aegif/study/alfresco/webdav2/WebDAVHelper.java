package jp.aegif.study.alfresco.webdav2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.webdav.WebDAVServerException;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.namespace.QName;


/**
 * WebDAV Protocol Helper Class
 * 
 * <p>Provides helper methods for repository access using the WebDAV protocol.
 * 
 * @author GKSpencer
 */
public class WebDAVHelper extends org.alfresco.repo.webdav.WebDAVHelper
{
	private static final String YOUR_HOME = "YourHome";

	private PersonService personService;

	private List<NodeRef> rootNodeRefs = new ArrayList<NodeRef>();

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public FileInfo getNodeForPath(NodeRef rootNodeRef, String path) throws FileNotFoundException
	{
		if (rootNodeRef == null)
		{
			throw new IllegalArgumentException("Root node may not be null");
		}
		else if (path == null)
		{
			throw new IllegalArgumentException("Path may not be null");
		}

		FileFolderService fileFolderService = getFileFolderService();
		// Check for the root path
		if ( path.length() == 0 || path.equals(PathSeperator))
		{
			if ( !rootNodeRefs.contains(rootNodeRef) ) {
				rootNodeRefs.add(rootNodeRef);
			}
			FileInfo fileInfo = fileFolderService.getFileInfo(rootNodeRef);
			return fileInfo;
		}

		if ( path.startsWith("/" + YOUR_HOME)) {
			// resolve your your name
			String userName = this.getAuthenticationService().getCurrentUserName();
			NodeRef person = this.personService.getPerson(userName);
			NodeRef homeFolderRef = 
					(NodeRef)this.getNodeService().getProperty(person, ContentModel.PROP_HOMEFOLDER);


			if ( homeFolderRef != null ) {
				FileInfo userHome =  this.getFileFolderService().getFileInfo(homeFolderRef);
				String pathLeft = path.replace("/" + YOUR_HOME, "");
				if ( pathLeft.length() > 0 ) {            	
					List<String> splitPath = splitAllPaths(pathLeft);
					return this.getFileFolderService().resolveNamePath(userHome.getNodeRef(), splitPath);
				}
				else {
					return this.getFileFolderService().getFileInfo(homeFolderRef);
				}
			}
		}

		// split the paths up
		List<String> splitPath = splitAllPaths(path);

		// find it
		FileInfo fileInfo = this.getFileFolderService().resolveNamePath(rootNodeRef, splitPath);
		String fileName = splitPath.get(splitPath.size() - 1);


		if (!fileInfo.getName().equals(fileName))
		{
			throw new FileNotFoundException("Requested filename " + fileName +
					" does not match case of " + fileInfo.getName());
		}

		// done
		if (logger.isDebugEnabled())
		{
			logger.debug("Fetched node for path: \n" +
					"   root: " + rootNodeRef + "\n" +
					"   path: " + path + "\n" +
					"   result: " + fileInfo);
		}
		return fileInfo;
	}

	public List<FileInfo> getChildren(FileInfo fileInfo) throws WebDAVServerException
	{
		List<FileInfo> children = this.getFileFolderService().list(fileInfo.getNodeRef());

		if ( this.rootNodeRefs.contains(fileInfo.getNodeRef())) {
			YourHomeFileInfo yourHome = new YourHomeFileInfo();
			children.add(yourHome);
		}

		return children;
	}	

	public static class YourHomeFileInfo implements FileInfo {

		private NodeRef userSpace;

		public void setNodeRef(NodeRef userSpace) {
			this.userSpace = userSpace;
		}

		@Override
		public NodeRef getNodeRef() {
			return userSpace;
		}

		@Override
		public boolean isFolder() {
			return true;
		}

		@Override
		public boolean isLink() {
			return false;
		}

		@Override
		public boolean isHidden() {
			return false;
		}

		@Override
		public NodeRef getLinkNodeRef() {
			return null;
		}

		@Override
		public String getName() {
			return YOUR_HOME;
		}

		@Override
		public Date getCreatedDate() {
			return new Date();
		}

		@Override
		public Date getModifiedDate() {
			return new Date();
		}

		@Override
		public ContentData getContentData() {
			return null;
		}

		@Override
		public Map<QName, Serializable> getProperties() {

			Map<QName, Serializable> properties = new HashMap<QName, Serializable>();
			properties.put(ContentModel.PROP_NAME, YOUR_HOME);
			properties.put(ContentModel.PROP_CREATED, new Date());
			properties.put(ContentModel.PROP_MODIFIED, new Date());
			return properties;
		}

		@Override
		public QName getType() {
			return ContentModel.TYPE_FOLDER;
		}

	}
}
