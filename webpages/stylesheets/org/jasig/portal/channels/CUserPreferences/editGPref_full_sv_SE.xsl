<?xml version='1.0'?>

<!--xsl:stylesheet xmlns:xsl='http://www.w3.org/XSL/Transform/1.0'-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:param name="baseActionURL">index.jsp</xsl:param>
<xsl:param name="profileName">default profile</xsl:param>
<xsl:param name="locale">sv_SE</xsl:param>

<xsl:template match="gpref">
<html>
<body>

<!-- header table -->
<form action="{$baseActionURL}" method="post">
<xsl:attribute name="action"><xsl:value-of select="$baseActionURL"/></xsl:attribute>
<input type="hidden" name="action" value="submitEditChoice"/>
<table width="100%" border="0"><tr><td align="left">profilnamn: <xsl:value-of select="$profileName"/></td><td align="right">
<input type="submit" name="submit" value="manage"/>
<select name="userPreferencesAction">
<option selected="" value="layout">kanaler och flikar</option>
<option value="gpref">globala inställningar</option>
<option value="manageProfiles">profiler</options>
</select>
</td>
</tr></table>
</form>
<!-- end of the header table -->

<p align="center">Ändra stylesheet-parametrar</p>
<form method="post">
<xsl:attribute name="action"><xsl:value-of select="$baseActionURL"/></xsl:attribute>
<input type="hidden" name="action" value="submitEditValues"/>


<table border="1" cellpadding="5" cellspacing="0" align="center">
<tr><td><b>parameter</b></td><td><b>value</b></td><td><b>beskrivning</b></td></tr>

<!-- eventually this should check if there are any parameters at all of this class before drawing a table row-->
<!-- process structure stylesheet-parametrar-->
<tr><td colspan="3" align="right">structure stylesheet-parametrar</td></tr>
<xsl:for-each select="structureparameters/parameter">
<xsl:call-template name="processParameter"/>
</xsl:for-each>

<!-- process theme stylesheet-parametrar-->
<tr><td colspan="3" align="right">theme stylesheet-parametrar</td></tr>
<xsl:for-each select="themeparameters/parameter">
<xsl:call-template name="processParameter"/>
</xsl:for-each>
</table>

<p align="center">
<input type="submit" name="submit" value="Spara"/>
<input type="submit" name="submit" value="Avbryt"/>
</p>
</form>
</body>
</html>
</xsl:template>

<xsl:template name="processParameter">
<tr><td><xsl:value-of select="name"/></td>
<td><input type="text" name="{name}" value="{value}"/></td>
<td><xsl:value-of select="description"/></td>
</tr>
</xsl:template>

</xsl:stylesheet>
