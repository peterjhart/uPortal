/**
 * Copyright � 2004 The JA-SIG Collaborative.  All rights reserved.
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

package org.jasig.portal.container.deploy;

import java.io.InputStream;

import org.jasig.portal.utils.ResourceLoader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Uses a local copy of the DTD which is normally located
 * at http://java.sun.com/dtd/web-app_2_3.dtd.  If we don't
 * do this, we are likely to get screwed if the java.sun.com
 * webserver goes down.  This actually happened yesterday,
 * March 1, 2004, and it caused the portlet container
 * initialization to hang until it issued a socket connection
 * timeout exception.
 * @author Ken Weiner, kweiner@unicon.net
 * @version $Revision$
 */
public class WebAppDtdResolver implements EntityResolver {

    /**
     * Sets the input source to the dtd normally
     * located at http://java.sun.com/dtd/web-app_2_3.dtd.
     * @param publicId the public ID
     * @param systemId the system ID
     * @return an input source based on the web-app dtd
     */
    public InputSource resolveEntity (String publicId, String systemId) {
        InputSource inputSource = null;

        try {
            InputStream inStream = ResourceLoader.getResourceAsStream(this.getClass(), "/org/jasig/portal/container/deploy/web-app_2_3.dtd");
            if (inStream != null) {
                inputSource =  new InputSource(inStream);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
      
        return inputSource;            
    }

}
