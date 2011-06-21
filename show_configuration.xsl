<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
<xsl:output method="xml"
            encoding="UTF-8" 
            version="1.0" 
            indent="no" 
            omit-xml-declaration="yes"
            cdata-section-elements="description script log_mail_to log_mail_cc log_mail_bcc" />
<xsl:strip-space elements="*"/>

<xsl:template match="//source">
  <div class="">
  <xsl:apply-templates select="child::*" mode="copy_node">
    <xsl:with-param name="indent" select="20"/>
  </xsl:apply-templates>
  </div> 
</xsl:template>


<xsl:template match="@*">
  <xsl:text> </xsl:text><span class="attribute_name"><xsl:value-of select="name()" />=</span><span class="attribute_val">&quot;<xsl:value-of select="." />&quot;</span>
</xsl:template>

<xsl:template match="*" mode="copy_node">
  <xsl:param name="indent" select="20"/>
  <br/><span class="bracket" style="padding-left:{$indent}px;">&lt;</span><span class="element_name"><xsl:value-of select="name()" /></span>
  <xsl:apply-templates select="@*" />
  <xsl:if test="child::* or text()">
      <span class="bracket">&gt;</span>
  </xsl:if>
  <xsl:if test="child::*" >
      <xsl:apply-templates select="child::*"  mode="copy_node">
        <xsl:with-param name="indent" select="20+$indent"/>
      </xsl:apply-templates>
      <br/>
  </xsl:if>
  <xsl:if test="text()">
      <pre class="cdata" style="display:inline;"><span class="cdata">&lt;![CDATA[</span><xsl:value-of select="text()" /><span class="cdata">]]&gt;</span></pre>
  </xsl:if>
  <xsl:if test="child::* or text()">
    <xsl:choose>
      <xsl:when test="text()">
        <span class="bracket">&lt;/</span><span class="element_name"><xsl:value-of select="name()" /></span><span class="bracket">&gt;</span>
      </xsl:when>
      <xsl:otherwise>
        <span class="bracket" style="padding-left:{$indent}px;">&lt;/</span><span class="element_name"><xsl:value-of select="name()" /></span><span class="bracket">&gt;</span>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:if>
  <xsl:if test="not(child::* or text())">
      <span class="bracket">/&gt;</span>
  </xsl:if>
</xsl:template>
  
</xsl:stylesheet>
