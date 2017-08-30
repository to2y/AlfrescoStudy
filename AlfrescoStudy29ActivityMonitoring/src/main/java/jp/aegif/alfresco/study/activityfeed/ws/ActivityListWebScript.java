package jp.aegif.alfresco.study.activityfeed.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.repo.domain.activities.ActivityFeedEntity;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.activities.ActivityService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class ActivityListWebScript extends DeclarativeWebScript {

	private ActivityService activityService;
	private NodeService nodeService;

	private static final String[] FILTERED_EVENTS = {
		"org.alfresco.documentlibrary.file-liked",
		"org.alfresco.documentlibrary.file-added",
		"org.alfresco.documentlibrary.file-previewed",
		"org.alfresco.documentlibrary.file-downloaded",
		"org.alfresco.comments.comment-created"
	};
	
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}
	
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	protected Map<String, Object> executeImpl(
			WebScriptRequest req, Status status, Cache cache) {
		
		final Map<String, Object> model = new HashMap<String, Object>();

		AuthenticationUtil.runAsSystem(
				new RunAsWork<String>(){
					public String doWork() throws Exception{

						long minId = -1;

						String minIdStr = req.getParameter("minId");
						if ( minIdStr != null && minIdStr.length() > 0) {
							minId = Long.parseLong(minIdStr);
						}

						List<ActivityFeedEntity> feeds =
								activityService.getUserFeedEntries("admin", null, false, false, minId);
												
						List<String> feedsData = new ArrayList<String>();
						for(ActivityFeedEntity feed : feeds) {
							try {
								for(String eventType : FILTERED_EVENTS) {
									if ( eventType.equals(feed.getActivityType())) {
										JSONObject obj = new JSONObject(feed.getActivitySummary());
										JSONObject feedJSON = new JSONObject(feed.getJSONString());
										//nodeRef
										feedJSON.put("nodeRef", obj.getString("nodeRef"));
										feedJSON.put("documentName", obj.getString("title"));
										//document path
										Path path = nodeService.getPath(new NodeRef(obj.getString("nodeRef")));
										
										feedJSON.put("documentPath", path.toString());
										
										feedsData.add(feedJSON.toString());
									}
								}
								
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						model.put("feeds", feedsData);

						return null;
					}
				});

		return model;
	}


}
