<?xml version='1.0' encoding='utf-8' ?>

<!--
Copyright (c) 2001 The JA-SIG Collaborative.  All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in
   the documentation and/or other materials provided with the
   distribution.
   
3. Redistributions of any form whatsoever must retain the following
   acknowledgment:
   "This product includes software developed by the JA-SIG Collaborative
   (http://www.jasig.org/)."
   
THIS SOFTWARE IS PROVIDED BY THE JA-SIG COLLABORATIVE "AS IS" AND ANY
EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JA-SIG COLLABORATIVE OR
ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
OF THE POSSIBILITY OF SUCH DAMAGE.

Author: Ken Weiner, kweiner@interactivebusiness.com
$Revision$
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:param name="baseActionURL">render.userLayoutRootNode.uP</xsl:param>

  <xsl:template match="layout">
    <wml>
      <template>
        <!-- A quick way to return to the top level of the layout -->
        <do type="accept" label="Home">
          <go href="{$baseActionURL}?uP_root=root#layout"></go>
        </do>
      </template>

      <!-- Splash screen -->
      <!-- I thought about having a splash screen so we wouldn't have to display the header on every
           page, but it caused problems with screen refreshing -Ken -->
      <!--
      <card id="header" title="Welcome">
        <do type="accept" label="Continue"><go href="#layout"></go></do>
        <xsl:apply-templates select="header//channel[@name != 'Login']" mode="render-channel"/>
      </card>
      -->

      <!-- Navigate layout -->
      <card id="layout">
        <!-- Card title depends on name of current category or channel -->
        <xsl:attribute name="title">
          <xsl:choose>
            <xsl:when test="not(count(content/category) + count(content/channel) = 1)">Home</xsl:when>
            <xsl:otherwise><xsl:value-of select="@name"/></xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>

        <!-- Display all the header channels except the login channel -->
        <xsl:apply-templates select="header//channel[@name != 'Login']" mode="render-channel"/>

        <!-- It would be nice to toggle the label, but I'm not sure how (please help!) -->
        <do type="options" label="Login/Logout"><go href="#login"></go></do>

        <!-- Render the list of categories and/or channels.  If the list if just one channel, it will render -->
        <xsl:apply-templates select="content"/>

        <!-- Display the footer, if any -->
        <xsl:apply-templates select="footer"/>
      </card>

      <!-- Login screen/logout link -->
      <card id="login" title="Login">
        <!-- should maybe select fname instead of name, come back and fix later -->
        <xsl:apply-templates select="header/channel[@name = 'Login']" mode="render-channel"/>    
      </card>
    
    </wml>
  </xsl:template>

  <xsl:template match="header">
    <xsl:for-each select="channel">
      <xsl:copy-of select="."/>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="content">
  <xsl:choose>
    <xsl:when test="not(count(category) + count(channel) = 1)">
      <xsl:if test="category"><p mode="nowrap"><small>Categories:</small></p></xsl:if>
      <xsl:apply-templates select="category"/>
      <xsl:if test="channel"><p mode="nowrap"><small>Channels:</small></p></xsl:if>
      <xsl:apply-templates select="channel"/> 
    </xsl:when>
    <xsl:otherwise>
      <xsl:apply-templates select="channel" mode="render-channel"/>
    </xsl:otherwise>
  </xsl:choose>
    <do type="prev" label="Back"><prev/></do>
  </xsl:template>

  <xsl:template match="footer">
    <xsl:for-each select="channel">
      <xsl:copy-of select="."/>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="category">
    <p mode="nowrap"><a href="{$baseActionURL}?uP_root={@ID}#layout"><xsl:value-of select="@name"/></a></p>
  </xsl:template>

  <xsl:template match="channel">
    <p mode="nowrap"><a href="{$baseActionURL}?uP_root={@ID}#layout"><xsl:value-of select="@name"/></a></p>
  </xsl:template>

  <xsl:template match="channel" mode="render-channel">
    <xsl:copy-of select="."/>
  </xsl:template>

</xsl:stylesheet>
