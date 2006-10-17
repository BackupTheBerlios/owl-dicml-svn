<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="entry">
  <span class="dicml">  
  <span class="form">
   <p class="form">
     <span class="orth"><xsl:for-each select="form/orth.gr/orth"><xsl:call-template name="orth"/></xsl:for-each></span>
     <span class="pos">
       <xsl:choose>
       <xsl:when test="grammar/pos/@pos = 'n'">
         <xsl:choose>
	   <xsl:when test="grammar/gen.gr/gen/@gen = 'f'"><xsl:call-template name="pos_n-f" /></xsl:when>
	   <xsl:when test="grammar/gen.gr/gen/@gen = 'fm'"><xsl:call-template name="pos_n-fm" /></xsl:when>
	   <xsl:when test="grammar/gen.gr/gen/@gen = 'fn'"><xsl:call-template name="pos_n-fn" /></xsl:when>
	   <xsl:when test="grammar/gen.gr/gen/@gen = 'm'"><xsl:call-template name="pos_n-m" /></xsl:when>
	   <xsl:when test="grammar/gen.gr/gen/@gen = 'mn'"><xsl:call-template name="pos_n-mn" /></xsl:when>
	   <xsl:when test="grammar/gen.gr/gen/@gen = 'n'"><xsl:call-template name="pos_n-n" /></xsl:when>
	   <xsl:when test="grammar/gen.gr/gen/@gen = 'u'"><xsl:call-template name="pos_n-u" /></xsl:when>
	   <xsl:otherwise>noun</xsl:otherwise>
	 </xsl:choose>
       </xsl:when>
       <xsl:when test="form/pos/@pos = 'v'"><abbr title="verb">v</abbr></xsl:when>
       <xsl:otherwise />
     </xsl:choose>
   </span>
   
   </p>
   <p class="orthalt">
     <xsl:if test="form/orth.gr/orth.alt"><span class="orthalt-group"><xsl:for-each select="form/orth.gr/orth.alt"><span class="orthalt"><xsl:call-template name="orthalt"/></span></xsl:for-each></span></xsl:if>
   </p>
  <xsl:if test="form/phon.gr/phon">
  <p class="phon">
  [<span class="phon"><xsl:value-of select="form/phon.gr/phon" /></span>]
  </p>
  </xsl:if>
  </span>
  
  <ol id="sense">
  <xsl:variable name="PGRANZAHL" select="count(//sense/p.gr)" />
  <xsl:variable name="PGRPGRANZAHL" select="count(//sense/p.gr/p.gr)" />
  <xsl:choose>
    <xsl:when test="$PGRANZAHL = 1"><xsl:attribute name="class">simple</xsl:attribute></xsl:when>
    <xsl:otherwise />
  </xsl:choose>
    <xsl:choose>
    <xsl:when test="$PGRPGRANZAHL != 0"><xsl:attribute name="class">deep</xsl:attribute></xsl:when>
    <xsl:otherwise />
  </xsl:choose>
  
  
  
  <xsl:for-each select="sense/p.gr"><li><xsl:call-template name="p.gr" /></li></xsl:for-each></ol>
  </span>  
</xsl:template>






<xsl:template name="orth">
 <xsl:apply-templates/>
</xsl:template>

<xsl:template name="orthalt">
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
    
    <xsl:if test="boolean(p.gr)"><ul><xsl:for-each select="p.gr"><li><xsl:call-template name="p.gr" /></li></xsl:for-each></ul>
    </xsl:if>
</xsl:template>









<xsl:template name="d">
 <span class="d"><xsl:apply-templates/><xsl:if test="position() != last()"><xsl:text>; </xsl:text></xsl:if></span>
</xsl:template>

<xsl:template name="s">
  <span class="s"><xsl:apply-templates /></span>
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
  <span class="high"><xsl:apply-templates /></span>
</xsl:template>

<xsl:template match="w">
  <span class="w"><xsl:value-of select="." />
  
  <span class="pos">
    <xsl:choose>
      <xsl:when test="@pos = 'n'">
        <xsl:choose>
          <xsl:when test="@gen = 'f'">
	    <xsl:choose>
	       <xsl:when test="@num = 'pl'"><xsl:call-template name="pos_n-f-pl" /></xsl:when>
	       <xsl:otherwise><xsl:call-template name="pos_n-f" /></xsl:otherwise>
	     </xsl:choose>
	  </xsl:when>
          <xsl:when test="@gen = 'fm'"><xsl:call-template name="pos_n-fm" /></xsl:when>
          <xsl:when test="@gen = 'fn'"><xsl:call-template name="pos_n-fn" /></xsl:when>
          <xsl:when test="@gen = 'm'"><xsl:call-template name="pos_n-m" /></xsl:when>
          <xsl:when test="@gen = 'mn'"><xsl:call-template name="pos_n-mn" /></xsl:when>
          <xsl:when test="@gen = 'n'"><xsl:call-template name="pos_n-n" /></xsl:when>
          <xsl:when test="@gen = 'u'">
	    <xsl:choose>
	       <xsl:when test="@num = 'pl'"><xsl:call-template name="pos_n-u-pl" /></xsl:when>
	       <xsl:otherwise><xsl:call-template name="pos_n-u" /></xsl:otherwise>
	     </xsl:choose>
	  </xsl:when>
          <xsl:otherwise>noun</xsl:otherwise>
        </xsl:choose>
      </xsl:when>
    </xsl:choose>
  </span>
  
  
  </span>
</xsl:template>

<xsl:template match="alt.gr">
 <span class="altgr"><xsl:for-each select="alt"><xsl:apply-templates/><xsl:if test="position() != last()"><xsl:text>/</xsl:text></xsl:if></xsl:for-each></span>
</xsl:template>


<xsl:template match="de.etw"><abbr title="etwas">etw</abbr></xsl:template>
<xsl:template match="en.sth"><abbr title="something">sth</abbr></xsl:template>



<xsl:template name="pos_n-f"><abbr title="feminine noun">f</abbr></xsl:template>
<xsl:template name="pos_n-f-pl"><abbr title="feminine noun - plural">f/pl</abbr></xsl:template>
<xsl:template name="pos_n-fm"><abbr title="feminine or masculine noun">f/m</abbr></xsl:template>
<xsl:template name="pos_n-fn"><abbr title="feminine or neuter noun">f/n</abbr></xsl:template>
<xsl:template name="pos_n-m"><abbr title="masculine noun">m</abbr></xsl:template>
<xsl:template name="pos_n-mn"><abbr title="masculine or neuter noun">m/n</abbr></xsl:template>
<xsl:template name="pos_n-n"><abbr title="neuter noun">n</abbr></xsl:template>
<xsl:template name="pos_n-u"><abbr title="utrum noun (common gender)">u</abbr></xsl:template>
<xsl:template name="pos_n-u-pl"><abbr title="utrum noun (common gender) - plural">u/pl</abbr></xsl:template>





</xsl:stylesheet>
