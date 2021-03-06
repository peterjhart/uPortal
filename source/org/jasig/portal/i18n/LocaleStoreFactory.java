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
package org.jasig.portal.i18n;

import org.jasig.portal.PropertiesManager;
import org.jasig.portal.services.LogService;

/**
 * Produces an implementation of ILocaleStore
 * @author Ken Weiner, kweiner@unicon.net
 * @version $Revision$
 */
public class LocaleStoreFactory {

    private static ILocaleStore localeStoreImpl = null;

    static {
      // Retrieve the class name of the concrete ILocaleStore implementation
      String className = PropertiesManager.getProperty("org.jasig.portal.i18n.LocaleStoreFactory.implementation");
      // Fail if this is not found
      if (className == null)
        LogService.log(LogService.ERROR, "LocaleStoreFactory: org.jasig.portal.i18n.LocaleStoreFactory.implementation must be specified in portal.properties");
      try {
        // Create an instance of the ILocaleStore as specified in portal.properties
        localeStoreImpl = (ILocaleStore)Class.forName(className).newInstance();
      } catch (Exception e) {
        LogService.log(LogService.ERROR, "LocaleStoreFactory: Could not instantiate " + className, e);
      }
    }

    /**
     * Returns an instance of the ILocaleStore specified in portal.properties
     * @return an ILocaleStore implementation
     */
    public static ILocaleStore getLocaleStoreImpl() {
      return localeStoreImpl;
    }

}
