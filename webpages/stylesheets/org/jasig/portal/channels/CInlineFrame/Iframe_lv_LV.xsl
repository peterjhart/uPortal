<?xml version='1.0'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:param name="locale">lv_LV</xsl:param>

<xsl:template match="iframe" >
  <iframe src="{url}" height="{height}" frameborder="no" width="100%">dummyText</iframe>
</xsl:template>

</xsl:stylesheet>
