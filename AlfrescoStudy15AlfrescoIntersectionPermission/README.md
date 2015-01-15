AlfrescoIntersectionPermission
==============================

Add functionality allowing group intersection permission capability

This is just a PoC and this module provides the limited function and build and confirmed on Alfresco community 4.2c.

---
applying this module, it is possible that only users which joins group A and group b can be Collaborator or Edit role on the nodes.

---
How to install
 - compile jar file and locate under alfresco/WEB-INF/lib
 - locate context file to shared/classes/alfresco/extension

---
How to define intersection permission to the nodes

Curretly, no UI is provided, you can define permission using javascript api just below:

document.setPermission("Collaborator", "AND(GROUP_OSS, GROUP_MANAGER)");
