<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : tei2dicml.xsl
    Created on : 26. September 2006, 20:44
    Author     : thomas
    Description:
        Very simple comversion from the TEI-files used by freedict.org to dicML.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml"/>

    <xsl:template match="/">
        <dicml>
          <head>
            <title><xsl:value-of select="/TEI.2/teiHeader/fileDesc/titleStmt/title" /></title>
          </head>
          <body>
            <xsl:for-each select="/TEI.2/text/body/entry">
              <entry>
                <form>
                  <xsl:apply-templates select="form" />
		</form>
		<xsl:if test="gramGrp">
		  <grammar>
		    <xsl:apply-templates select="gramGrp" />
		  </grammar>
		</xsl:if>
                <sense>
                  <xsl:apply-templates select="trans" />
                </sense>
              </entry>
            </xsl:for-each>
          </body>
        </dicml>
    </xsl:template>
    
    
    <xsl:template name="pos" match="gramGrp/pos">
      <pos><xsl:attribute name="pos"><xsl:value-of select="." /></xsl:attribute></pos>
    </xsl:template>
    
    <xsl:template name="gen" match="gramGrp/gen">
      <gen.gr><gen><xsl:attribute name="gen"><xsl:value-of select="." /></xsl:attribute></gen></gen.gr>
    </xsl:template>
      <num><xsl:attribute name="num"><xsl:value-of select="." /></xsl:attribute></num>
    <xsl:template name="num" match="gramGrp/num"></xsl:template>
    
    <xsl:template name="form" match="form">
      <orth.gr>
        <xsl:apply-templates select="orth" />
      </orth.gr>      
        <xsl:apply-templates select="pron" />
    </xsl:template>
    
    <xsl:template match="orth">
      <orth><xsl:value-of select="." /></orth>
    </xsl:template>


    
    <xsl:template match="pron">      
      <phon.gr><phon><xsl:value-of select="." /></phon></phon.gr>
    </xsl:template>
    
    <xsl:template name="trans" match="trans">
          <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="tr">
      <p.gr><p>
        <t><xsl:value-of select="." /></t>
      </p></p.gr>
    </xsl:template>
    
</xsl:stylesheet>
