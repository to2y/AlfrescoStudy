function main() {
	
	if ( args.nodeRef == undefined || args.nodeRef.length == 0 ) {
		   status.code = 400;
		   status.message = "nodeRef is not specified";
		   status.redirect = true;
		   return;
	}
	
	if ( args.userName == undefined || args.userName.length == 0 ) {
		   status.code = 400;
		   status.message = "userName is not specified";
		   status.redirect = true;
		   return;
	}
	
	if ( args.siteId == undefined || args.siteId.length == 0 ) {
		   status.code = 400;
		   status.message = "siteId is not specified";
		   status.redirect = true;
		   return;
	}
	var siteId = args.siteId;
	
	//copy to alldivisions site
	var allDivisionSite = siteService.getSite("alldivisions");
	var doclib = allDivisionSite.getContainer("documentLibrary");
	
	//specify source node
	var sourceNode = search.findNode(args.nodeRef);
	
	var targetFolder = doclib.childByNamePath(siteId);
	if ( targetFolder == null ) {
		targetFolder = doclib.createFolder(siteId);
	}
	
	var copied = sourceNode.copy(targetFolder);
	if ( copied == null ) {
		status.code = 500;
		status.message = "cannot publish";
		status.redirect = true;
		return;
	}
	
	//record metadata to source node
	sourceNode.properties["study:publishDate"] = new Date();
	sourceNode.properties["study:approver"] = args.userName;
	sourceNode.save();
	sourceNode.createAssociation(copied, "study:target");
	
}

main();