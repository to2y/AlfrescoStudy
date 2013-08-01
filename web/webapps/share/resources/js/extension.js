(function() {
	YAHOO.Bubbling.fire("registerAction",
			{   
		actionName: "onActionPublishDoc",
		fn: function _onPublishDoc(file)
	{      
			
			this.modules.actions.genericAction(
					{
						success:
						{
							message: this.msg("message.publishdoc.success", file.displayName)
						},
						failure:
						{
							message: this.msg("message.publishdoc.failure", file.displayName)
						},
						webscript:
						{
							name: "study/publishdoc?nodeRef={nodeRef}&userName={userName}&siteId={siteId}",
							stem: Alfresco.constants.PROXY_URI,
							method: Alfresco.util.Ajax.GET,
							params:
							{
								nodeRef: file.nodeRef,
								userName: Alfresco.constants.USERNAME,
								siteId: this.options.siteId
							}
						},
						config:
						{
						}

					});

	}
			});
})();
