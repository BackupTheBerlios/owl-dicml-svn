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
                <lemma>
                  <xsl:apply-templates select="form" />
                  <xsl:apply-templates select="gramGrp" />
                </lemma>
                <sense>
                  <xsl:apply-templates select="trans" />
                </sense>
              </entry>
            </xsl:for-each>
          </body>
        </dicml>
    </xsl:template>
    
    
    <xsl:template name="gen" match="gramGrp/gen">
      <pos.gr><pos><xsl:attribute name="pos"><xsl:value-of select="." /></xsl:attribute></pos></pos.gr>
    </xsl:template>
    
    <!-- leave empty -->
    <xsl:template name="pos" match="gramGrp/pos"></xsl:template>
    <xsl:template name="num" match="gramGrp/num"></xsl:template>
    
    <xsl:template name="form" match="form">
      <l.gr>
        <xsl:apply-templates select="orth" />
      </l.gr>      
        <xsl:apply-templates select="pron" />
    </xsl:template>
    
    <xsl:template match="orth">
      <l><xsl:value-of select="." /></l>
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