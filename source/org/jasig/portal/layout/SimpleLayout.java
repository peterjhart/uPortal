/**
 * Copyright � 2003 The JA-SIG Collaborative.  All rights reserved.
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

package org.jasig.portal.layout;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.xpath.XPathAPI;
import org.jasig.portal.PortalException;
import org.jasig.portal.utils.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;

/**
 * The simple user layout implementation. This
 * layout is based on a Document.
 *
 * @author Ken Weiner, kweiner@unicon.net
 * @version $Revision$
 */
public class SimpleLayout implements IUserLayout {
    
    private Document layout;
    private String layoutId;
    private String cacheKey;

    public SimpleLayout(String layoutId, Document layout) {
        this.layoutId = layoutId;
        this.layout = layout;
    }

    public void writeTo(ContentHandler ch) throws PortalException {
        try {
            XML.dom2sax(layout, ch);
        } catch (Exception e) {
            throw new PortalException(e);
        }
    }

    public void writeTo(String nodeId, ContentHandler ch) throws PortalException {
        try {
            XML.dom2sax(layout.getElementById(nodeId), ch);
        } catch (Exception e) {
            throw new PortalException(e);
        }
    }

    public void writeTo(Document document) throws PortalException {
        document.appendChild(document.importNode(layout.getDocumentElement(), true));
    }

    public void writeTo(String nodeId, Document document) throws PortalException {
        document.appendChild(document.importNode(layout.getElementById(nodeId), true));
    }

    public IUserLayoutNodeDescription getNodeDescription(String nodeId) throws PortalException {
        Element element = (Element) layout.getElementById(nodeId);
        return UserLayoutNodeDescription.createUserLayoutNodeDescription(element);
    }

    public String getParentId(String nodeId) throws PortalException {
        String parentId = null;
        Element element = (Element)layout.getElementById(nodeId);
        if (element != null) {
            Node parent = element.getParentNode();
            if (parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
                Element parentE = (Element)parent;
                parentId = parentE.getAttribute("ID");
            }
        }
        return parentId;
    }

    public Enumeration getChildIds(String nodeId) throws PortalException {
        Vector v = new Vector();
        IUserLayoutNodeDescription node = getNodeDescription(nodeId);
        if (node instanceof IUserLayoutFolderDescription) {
            Element element = (Element)layout.getElementById(nodeId);
            for (Node n = element.getFirstChild(); n != null; n = n.getNextSibling()) {
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element)n;
                    if (e.getAttribute("ID") != null) {
                        v.add(e.getAttribute("ID"));
                    }
                }
            }
        }
        return v.elements();
    }

    public String getNextSiblingId(String nodeId) throws PortalException {
        String nextSiblingId = null;
        Element element = (Element)layout.getElementById(nodeId);
        if (element != null) {
            Node sibling = element.getNextSibling();
            // Find the next element node
            while (sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE) {
                sibling = sibling.getNextSibling();
            }
            if (sibling != null) {
                Element e = (Element)sibling;
                nextSiblingId = e.getAttribute("ID");
            }
        }
        return nextSiblingId;
    }

    public String getPreviousSiblingId(String nodeId) throws PortalException {
        String prevSiblingId = null;
        Element element = (Element)layout.getElementById(nodeId);
        if (element != null) {
            Node sibling = element.getPreviousSibling();
            // Find the previous element node
            while (sibling != null && sibling.getNodeType() != Node.ELEMENT_NODE) {
                sibling = sibling.getPreviousSibling();
            }
            if (sibling != null) {
                Element e = (Element)sibling;
                prevSiblingId = e.getAttribute("ID");
            }
        }
        return prevSiblingId;
    }

    public String getCacheKey() throws PortalException {
        return cacheKey;
    }

    public boolean addLayoutEventListener(LayoutEventListener l) {
        // TODO: Implement this!
        return false;
    }

    public boolean removeLayoutEventListener(LayoutEventListener l) {
        // TODO: Implement this!
        return false;
    }

    public String getId() {
        return layoutId;
    }

    public String getNodeId(String fname) throws PortalException {
        String nodeId = null;
        NodeList nl = layout.getElementsByTagName("channel");
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element channelE = (Element)node;
                if (fname.equals(channelE.getAttribute("fname"))) {
                    nodeId = channelE.getAttribute("ID");
                    break;
                }
            }
        }
        return nodeId;
    }

    public Enumeration getNodeIds() throws PortalException {
        Vector v = new Vector();
        try {
            NodeList nl = XPathAPI.selectNodeList(layout, "*");
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element)node;
                    v.add(e.getAttribute("ID"));
                }
            }
        } catch (Exception e) {
            // Do nothing for now
        }
        return v.elements();
    }

    public String getRootId() {
        String rootNode = null;
        try {
            Element rootNodeE = (Element) XPathAPI.selectSingleNode(layout, "/layout/folder");
            rootNode = rootNodeE.getAttribute("ID");
        } catch (Exception e) {
            // Do nothing for now
        }
        return rootNode;
    }

}
