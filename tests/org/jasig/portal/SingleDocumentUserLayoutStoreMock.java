package org.jasig.portal;


import org.w3c.dom.Document;
import org.jasig.portal.security.IPerson;
import java.util.Random;

/**
 * A mock of IUserLayoutStore interface that works with a single user layout
 * DOM document.
 *
 * @author <a href="mailto:pkharchenko@interactivebusiness.com">Peter Kharchenko</a>
 * @version 1.0
 */
public class SingleDocumentUserLayoutStoreMock extends UserLayoutStoreMock {
    private static final Random rnd=new Random();
    Document userLayout=null;

    public SingleDocumentUserLayoutStoreMock(Document doc) {
        this.userLayout=doc;
    }
    public Document getUserLayout(org.jasig.portal.security.IPerson person, UserProfile profile) throws Exception {
        return this.userLayout;
    }

    public void setUserLayout(org.jasig.portal.security.IPerson person, UserProfile profile, org.w3c.dom.Document layoutXML, boolean channelsAdded) throws Exception {
        this.userLayout=layoutXML;
    }

    public String generateNewChannelSubscribeId(IPerson person) throws Exception {
        return new String("rid"+Integer.toString(rnd.nextInt()));
    }
    public String generateNewFolderId(IPerson person) throws Exception {
        return new String("rid"+Integer.toString(rnd.nextInt()));
    }
}
