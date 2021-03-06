/**
 * Copyright � 2001 The JA-SIG Collaborative.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the JA-SIG Collaborative
 *    (http://www.jasig.org/)."
 *
 * THIS SOFTWARE IS PROVIDED BY THE JA-SIG COLLABORATIVE "AS IS" AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JA-SIG COLLABORATIVE OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package  org.jasig.portal.channels.groupsmanager.wrappers;

import org.jasig.portal.channels.groupsmanager.CGroupsManagerUnrestrictedSessionData;
import org.jasig.portal.channels.groupsmanager.GroupsManagerConstants;
import org.jasig.portal.channels.groupsmanager.GroupsManagerXML;
import org.jasig.portal.channels.groupsmanager.IGroupsManagerPermissions;
import org.jasig.portal.channels.groupsmanager.IGroupsManagerWrapper;
import org.jasig.portal.channels.groupsmanager.Utility;
import org.jasig.portal.groups.IGroupMember;
import org.jasig.portal.security.IAuthorizationPrincipal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Returns an xml element for an IGroupMember.
 * @author Don Fracapane
 * @version $Revision$
 */
public abstract class GroupMemberWrapper
      implements IGroupsManagerWrapper, GroupsManagerConstants {
   protected String ELEMENT_TAGNAME ;

   /** Creates new GroupMemberWrapper */
   public GroupMemberWrapper () {
   }

   /**
    * Returns an xml element for a given IGroupMember key. The element that is returned
    * could be the same one that is passed in (usually with the expanded attribute
    * set to "true" or a new element (all attributes have to be set after the
    * GroupMember is retrieved).
    * @param aKey String
    * @param aType String
    * @param anElem Element
    * @param sessionData CGroupsManagerUnrestrictedSessionData
    * @return Element
    */
   public Element getXml (String aKey, String aType, Element anElem, CGroupsManagerUnrestrictedSessionData sessionData) {
      Document aDoc = sessionData.model;
      Utility.logMessage("DEBUG", "GroupMemberWrapper::getXml(" + aKey + "): START");
      Element rootElem = (anElem != null ? anElem : GroupsManagerXML.createElement(ELEMENT_TAGNAME, aDoc, false));
      Utility.logMessage("DEBUG", "GroupMemberWrapper::getXml(" + aKey + "): rootElem: " + rootElem);
      IGroupMember gm = retrieveGroupMember(aKey, aType);
      Utility.logMessage("DEBUG", "GroupMemberWrapper::getXml(" + aKey + "): grp: " + gm);
      getXml(gm, rootElem, sessionData);
      return  rootElem;
   }

   /**
    * Adds permission attributes to the group member element.
    * @param rootElem Element
    * @param gm IGroupMember
    * @param gmp IGroupsManagerPermissions
    * @param ap IAuthorizationPrincipal
    */
   public void applyPermissions (Element rootElem, IGroupMember gm, IGroupsManagerPermissions gmp, IAuthorizationPrincipal ap) {
      // set user permissions for group
      rootElem.setAttribute("canAssignPermissions", String.valueOf(gmp.canAssignPermissions(ap, gm)));
      rootElem.setAttribute("canCreateGroup", String.valueOf(gmp.canCreateGroup(ap, gm)));
      rootElem.setAttribute("canManageMembers", String.valueOf(gmp.canManageMembers(ap, gm)));
      rootElem.setAttribute("canDelete", String.valueOf(gmp.canDelete(ap, gm)));
      rootElem.setAttribute("canSelect", String.valueOf(gmp.canSelect(ap, gm)));
      rootElem.setAttribute("canUpdate", String.valueOf(gmp.canUpdate(ap, gm)));
      rootElem.setAttribute("canView", String.valueOf(gmp.canView(ap, gm)));
      rootElem.setAttribute("canViewProperties", String.valueOf(gmp.canViewProperties(ap, gm)));
      return;
   }

   /**
    * Returns an xml element for a given IGroupMember.
    * @param gm IGroupMember
    * @param anElem Element
    * @param sessionData CGroupsManagerUnrestrictedSessionData
    * @return Element
    */
   public abstract Element getXml (IGroupMember gm, Element anElem, CGroupsManagerUnrestrictedSessionData sessionData) ;

    /**
    * Returns a GroupMember for a key.
    * @param aKey
    * @param aType
    * @return IGroupMember
    */
   protected abstract IGroupMember retrieveGroupMember (String aKey, String aType) ;
}