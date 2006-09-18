<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="entry">
<html>
  <head>
  <title>example of use - entry id "<xsl:value-of select="@id"/>"</title>
  
  <link rel="stylesheet" media="all" href="dicml_2-0.css"/>
  
  </head>

  <body>
   <span class="lemma">
   <p class="lemma">
     <span class="l"><xsl:for-each select="lemma/l.gr/l"><xsl:call-template name="l"/></xsl:for-each></span>
     <span class="pos"><xsl:value-of select="lemma/pos.gr/pos/@pos"/></span>
   </p>
   <p class="lemmaalt">
     <xsl:if test="lemma/l.gr/l.alt"><span class="lalt-group"><xsl:for-each select="lemma/l.gr/l.alt"><span class="lalt"><xsl:call-template name="lalt"/></span></xsl:for-each></span></xsl:if>
   </p>
  <xsl:if test="lemma/phon.gr/phon">
  <p class="phon">
  [<span class="phon"><xsl:value-of select="lemma/phon.gr/phon"/></span>]
  </p>
  </xsl:if>
  </span>
  
  <ul id="sense">
  <xsl:for-each select="sense/p.gr"><li><xsl:call-template name="p.gr"/></li></xsl:for-each></ul>
  
 </body></html>
</xsl:template>


<xsl:template name="l">
 <xsl:apply-templates/>
</xsl:template>

<xsl:template name="lalt">
 <xsl:apply-templates/>
</xsl:template>


<xsl:template match="sep">
 <span class="hyphsep">●</span>
</xsl:template>

<xsl:template match="hyph">
 <span class="hyphsep">|</span>
</xsl:template>






<xsl:template name="p.gr">
    <xsl:for-each select="p"><p>
      <xsl:for-each select="d"><xsl:call-template name="t"/></xsl:for-each>
      <xsl:for-each select="t"><xsl:call-template name="t"/></xsl:for-each>
      <xsl:for-each select="ex"><xsl:call-template name="ex"/></xsl:for-each>
      <xsl:for-each select="ant.gr"><xsl:call-template name="p-ant.gr"/></xsl:for-each>
      <xsl:for-each select="syn.gr"><xsl:call-template name="p-syn.gr"/></xsl:for-each>
      </p></xsl:for-each>
    <xsl:for-each select="p.col"><p>
      <xsl:for-each select="s"><xsl:call-template name="s"/></xsl:for-each>
      <xsl:for-each select="d"><xsl:call-template name="d"/></xsl:for-each>
      <xsl:for-each select="t"><xsl:call-template name="t"/></xsl:for-each>
      <xsl:for-each select="ex"><xsl:call-template name="ex"/></xsl:for-each>
      <xsl:for-each select="ant.gr"><xsl:call-template name="p-ant.gr"/></xsl:for-each>
      <xsl:for-each select="syn.gr"><xsl:call-template name="p-syn.gr"/></xsl:for-each>
      </p></xsl:for-each>
    <xsl:for-each select="p.idm"><p>
      <xsl:for-each select="s"><xsl:call-template name="s"/></xsl:for-each>
      <xsl:for-each select="d"><xsl:call-template name="d"/></xsl:for-each>
      <xsl:for-each select="t"><xsl:call-template name="t"/></xsl:for-each>
      <xsl:for-each select="ex"><xsl:call-template name="ex"/></xsl:for-each>
      <xsl:for-each select="ant.gr"><xsl:call-template name="p-ant.gr"/></xsl:for-each>
      <xsl:for-each select="syn.gr"><xsl:call-template name="p-syn.gr"/></xsl:for-each>
      </p></xsl:for-each>
    <xsl:for-each select="p.prv"><p>
      <xsl:for-each select="s"><xsl:call-template name="s"/></xsl:for-each>
      <xsl:for-each select="d"><xsl:call-template name="d"/></xsl:for-each>
      <xsl:for-each select="t"><xsl:call-template name="t"/></xsl:for-each>
      <xsl:for-each select="ex"><xsl:call-template name="ex"/></xsl:for-each>
      <xsl:for-each select="ant.gr"><xsl:call-template name="p-ant.gr"/></xsl:for-each>
      <xsl:for-each select="syn.gr"><xsl:call-template name="p-syn.gr"/></xsl:for-each>
      </p></xsl:for-each>
    
    <xsl:for-each select="p.gr"><ul><li><xsl:call-template name="p.gr"/></li></ul></xsl:for-each>
</xsl:template>









<xsl:template name="d">
 <span class="d"><xsl:apply-templates/><xsl:if test="position() != last()"><xsl:text>; </xsl:text></xsl:if></span>
</xsl:template>

<xsl:template name="s">
  <span class="s"><xsl:apply-templates/></span>
</xsl:template>

<xsl:template name="t">
 <span class="t"><xsl:apply-templates/><xsl:if test="position() != last()"><xsl:text>; </xsl:text></xsl:if></span>
</xsl:template>

<xsl:template name="ex">
 <div class="ex"><xsl:text>▪ </xsl:text>
   <xsl:for-each select="s"><xsl:call-template name="s"/></xsl:for-each>
   <xsl:for-each select="d"><xsl:call-template name="d"/></xsl:for-each>
   <xsl:for-each select="t"><xsl:call-template name="t"/></xsl:for-each></div>
</xsl:template>

<xsl:template name="p-ant.gr">
 <div class="antgr"><span style="font-variant: small-caps;"><abbr title="antonym">ant</abbr>: </span><xsl:for-each select="ant"><xsl:apply-templates/><xsl:if test="position() != last()"><xsl:text>; </xsl:text></xsl:if></xsl:for-each></div>
</xsl:template>

<xsl:template name="p-syn.gr">
 <div class="syngr"><span style="font-variant: small-caps;"><abbr title="synonym">syn</abbr>: </span><xsl:for-each select="syn"><xsl:apply-templates/><xsl:if test="position() != last()"><xsl:text>; </xsl:text></xsl:if></xsl:for-each></div>
</xsl:template>




<xsl:template match="high">
  <span class="high"><xsl:apply-templates/></span>
</xsl:template>

<xsl:template match="w">
  <xsl:value-of select="."/><span class="pos"><xsl:value-of select="@pos"/></span>
</xsl:template>

<xsl:template match="alt.gr">
 <span class="altgr"><xsl:for-each select="alt"><xsl:apply-templates/><xsl:if test="position() != last()"><xsl:text>/</xsl:text></xsl:if></xsl:for-each></span>
</xsl:template>


<xsl:template match="de.etw"><abbr title="etwas">etw.</abbr></xsl:template>
<xsl:template match="en.sth"><abbr title="something">sth</abbr></xsl:template>


</xsl:stylesheet>