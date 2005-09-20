<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="dicml">
<xsl:apply-templates />
</xsl:template>

<xsl:template match="head">
</xsl:template>

<xsl:template match="body">
<xsl:apply-templates />
</xsl:template>

<xsl:template match="entry">
<html><head>
<title><xsl:value-of select="lemma.gr/l"/></title>
<link rel="stylesheet" media="all" href="css.css" type="text/css"/>
</head>

<body>

<xsl:apply-templates />

</body>
</html>
</xsl:template>





<xsl:template match="lemma.gr">
<div class="lemma-gr">
 <p><span class="lemma">
 <!-- num --> <xsl:choose><xsl:when test="boolean(num)"><span class="num"><xsl:value-of select="num"/></span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
              <xsl:value-of select="l"/></span>
 <!-- phon --><xsl:choose><xsl:when test="boolean(phon)">&#160;[<span class="phon"><xsl:value-of select="phon"/></span>]</xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- pos --> <span class="pos">&#160;<xsl:choose>
                <xsl:when test="pos/@pos='abr'">forkortelse</xsl:when>
                <xsl:when test="pos/@pos='adj'"><a class="abbr-pos" href="?abbr:adjektiv">adj</a></xsl:when>
                <xsl:when test="pos/@pos='adv'"><a class="abbr-pos" href="?abbr:adverb">adv</a></xsl:when>
                <xsl:when test="pos/@pos='conj'">Konjunktion</xsl:when>
                <xsl:when test="pos/@pos='f'"><a class="abbr-pos" href="?abbr:femininum">f</a></xsl:when>
                <xsl:when test="pos/@pos='fpl'"><a class="abbr-pos" href="?abbr:femininum flertall">f/fl</a></xsl:when>
                <xsl:when test="pos/@pos='int'">Interjektion</xsl:when>
                <xsl:when test="pos/@pos='m'"><a class="abbr-pos" href="?abbr:maskulinum">m</a></xsl:when>
                <xsl:when test="pos/@pos='mpl'"><a class="abbr-pos" href="?abbr:maskulinum flertall">m/fl</a></xsl:when>
                <xsl:when test="pos/@pos='n'"><a class="abbr-pos" href="?abbr:intetkjønn">i</a></xsl:when>
                <xsl:when test="pos/@pos='npl'"><a class="abbr-pos" href="?abbr:intetkjønn flertall">i/fl</a></xsl:when>
                <xsl:when test="pos/@pos='pr'">pronomen</xsl:when>
                <xsl:when test="pos/@pos='pr-dem'">demonstrativpronomen</xsl:when>
                <xsl:when test="pos/@pos='pref'">prefix</xsl:when>
                <xsl:when test="pos/@pos='prefid'">som prefix</xsl:when>
                <xsl:when test="pos/@pos='prep'">preposisjon</xsl:when>
                <xsl:when test="pos/@pos='pr-ind'">indefinitpronomen</xsl:when>
                <xsl:when test="pos/@pos='pr-int'">interrogativpronomen</xsl:when>
                <xsl:when test="pos/@pos='prop'">egennavn</xsl:when>
                <xsl:when test="pos/@pos='pr-per'">personalpronomen</xsl:when>
                <xsl:when test="pos/@pos='pr-pos'">possessipronomen</xsl:when>
                <xsl:when test="pos/@pos='pr-rel'">relativpronomen</xsl:when>
                <xsl:when test="pos/@pos='suff'">suffix</xsl:when>
                <xsl:when test="pos/@pos='suffid'">som suffix</xsl:when>
                <xsl:when test="pos/@pos='v'"><a class="abbr-pos" href="?abbr:Verb">v</a></xsl:when>
                <xsl:when test="pos/@pos='v-aux'">hjelpeverb</xsl:when>
                <xsl:when test="pos/@pos='v-i'"><a class="abbr-pos" href="?abbr:itransitiv verb">v/itr.</a></xsl:when>
                <xsl:when test="pos/@pos='v-imp'"><a class="abbr-pos" href="?abbr:upersonelig verb">v/upersonelig</a></xsl:when>
                <xsl:when test="pos/@pos='v-ref'"><a class="abbr-pos" href="?abbr:reflexiv verb">v/ref.</a></xsl:when>
                <xsl:when test="pos/@pos='v-t'"><a class="abbr-pos" href="?abbr:transitiv verb">v/tr.</a></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose></span>
</p>
</div>
</xsl:template>






<xsl:template match="sense.gr">
<table class="block">
  <tr><td class="block-links" valign="top"><p>
 <!-- num --> <xsl:choose><xsl:when test="boolean(@num)"><span class="num"><xsl:value-of select="@num"/></span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
       </p></td>
      <td class="block-rechts" valign="top"><xsl:apply-templates/></td>

      </tr>
</table>
</xsl:template>





<xsl:template match="p">
<p>
 <!-- dom --> <xsl:choose><xsl:when test="boolean(@dom)"><span class="dom"><xsl:value-of select="@dom"/></span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- n -->   <xsl:choose><xsl:when test="boolean(@n)"><span class="n">(<xsl:value-of select="@n"/>)&#160;</span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- niv --> <xsl:choose><xsl:when test="boolean(@niv)"><span class="niv"><xsl:value-of select="@niv"/>&#160;</span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- -->     <xsl:for-each select="t">
              <span class="t"><xsl:if test="position() != last()"><xsl:apply-templates/><xsl:text>; </xsl:text></xsl:if>
              <xsl:if test="position() = last()"><xsl:apply-templates/><xsl:text></xsl:text></xsl:if></span></xsl:for-each>
</p>
 <!-- -->     <xsl:apply-templates select="ex"/>
</xsl:template>

<xsl:template match="p.col">
<p>
 <!-- dom --> <xsl:choose><xsl:when test="boolean(@dom)"><span class="dom"><xsl:value-of select="@dom"/></span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- n -->   <xsl:choose><xsl:when test="boolean(@n)"><span class="n">(<xsl:value-of select="@n"/>)&#160;</span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- niv --> <xsl:choose><xsl:when test="boolean(@niv)"><span class="niv"><xsl:value-of select="@niv"/>&#160;</span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- -->     <xsl:apply-templates select="s"/>
 <!-- -->     <xsl:for-each select="t">
              <span class="t"><xsl:if test="position() != last()"><xsl:apply-templates/><xsl:text>; </xsl:text></xsl:if>
              <xsl:if test="position() = last()"><xsl:apply-templates/><xsl:text></xsl:text></xsl:if></span></xsl:for-each>
</p>
 <!-- -->     <xsl:apply-templates select="ex"/>
</xsl:template>

<xsl:template match="p.idm">
<p>
 <!-- dom --> <xsl:choose><xsl:when test="boolean(@dom)"><span class="dom"><xsl:value-of select="@dom"/></span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- n -->   <xsl:choose><xsl:when test="boolean(@n)"><span class="n">(<xsl:value-of select="@n"/>)&#160;</span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- niv --> <xsl:choose><xsl:when test="boolean(@niv)"><span class="niv"><xsl:value-of select="@niv"/>&#160;</span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- -->     <xsl:apply-templates select="s"/>
 <!-- -->     <xsl:for-each select="t">
              <span class="t"><xsl:if test="position() != last()"><xsl:apply-templates/><xsl:text>; </xsl:text></xsl:if>
              <xsl:if test="position() = last()"><xsl:apply-templates/><xsl:text></xsl:text></xsl:if></span></xsl:for-each>
</p>
 <!-- -->     <xsl:apply-templates select="ex"/>
</xsl:template>




<xsl:template match="ex">
<p class="ex">&#x25AB;
 <!-- dom --> <xsl:choose><xsl:when test="boolean(@dom)"><span class="dom"><xsl:value-of select="@dom"/></span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- n -->   <xsl:choose><xsl:when test="boolean(@n)"><span class="n">(<xsl:value-of select="@n"/>)&#160;</span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- niv --> <xsl:choose><xsl:when test="boolean(@niv)"><span class="niv"><xsl:value-of select="@niv"/>&#160;</span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- -->     <xsl:apply-templates select="s"/>
 <!-- -->     <xsl:for-each select="t">
              <span class="t"><xsl:if test="position() != last()"><xsl:apply-templates/><xsl:text>; </xsl:text></xsl:if>
              <xsl:if test="position() = last()"><xsl:apply-templates/><xsl:text></xsl:text></xsl:if></span></xsl:for-each>
 <!-- -->     <xsl:apply-templates select="ex"/>
</p>
</xsl:template>



<xsl:template match="s">
<span class="s"><xsl:apply-templates/>&#160;</span>
</xsl:template>








<xsl:template match="w">
 <!-- niv --> <xsl:choose><xsl:when test="boolean(@niv)"><span class="niv"><xsl:value-of select="@niv"/>&#160;</span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- dom --> <xsl:choose><xsl:when test="boolean(@dom)"><span class="dom"><xsl:value-of select="@dom"/></span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- n -->   <xsl:choose><xsl:when test="boolean(@n)"><span class="n">(<xsl:value-of select="@n"/>)&#160;</span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
 <!-- -->     <xsl:value-of select="."/>
 <!-- pos --> <span class="pos">&#160;<xsl:choose>
                <xsl:when test="pos/@pos='abr'">forkortelse</xsl:when>
                <xsl:when test="pos/@pos='adj'"><a class="abbr-pos" href="?abbr:adjektiv">adj</a></xsl:when>
                <xsl:when test="pos/@pos='adv'"><a class="abbr-pos" href="?abbr:adverb">adv</a></xsl:when>
                <xsl:when test="pos/@pos='conj'">konjunksjon</xsl:when>
                <xsl:when test="pos/@pos='f'"><a class="abbr-pos" href="?abbr:femininum">f</a></xsl:when>
                <xsl:when test="pos/@pos='fpl'"><a class="abbr-pos" href="?abbr:femininum flertall">f/fl</a></xsl:when>
                <xsl:when test="pos/@pos='int'">interjeksjon</xsl:when>
                <xsl:when test="pos/@pos='m'"><a class="abbr-pos" href="?abbr:maskulinum">m</a></xsl:when>
                <xsl:when test="pos/@pos='mpl'"><a class="abbr-pos" href="?abbr:maskulinum flertall">m/fl</a></xsl:when>
                <xsl:when test="pos/@pos='n'"><a class="abbr-pos" href="?abbr:intetkjønn">i</a></xsl:when>
                <xsl:when test="pos/@pos='npl'"><a class="abbr-pos" href="?abbr:intetkjønn flertall">i/fl</a></xsl:when>
                <xsl:when test="pos/@pos='pr'">pronomen</xsl:when>
                <xsl:when test="pos/@pos='pr-dem'">demonstrativpronomen</xsl:when>
                <xsl:when test="pos/@pos='pref'">prefix</xsl:when>
                <xsl:when test="pos/@pos='prefid'">som prefix</xsl:when>
                <xsl:when test="pos/@pos='prep'">preposisjon</xsl:when>
                <xsl:when test="pos/@pos='pr-ind'">indefinitpronomen</xsl:when>
                <xsl:when test="pos/@pos='pr-int'">interrogativpronomen</xsl:when>
                <xsl:when test="pos/@pos='prop'">egennavn</xsl:when>
                <xsl:when test="pos/@pos='pr-per'">personalpronomen</xsl:when>
                <xsl:when test="pos/@pos='pr-pos'">possessipronomen</xsl:when>    
                <xsl:when test="pos/@pos='pr-rel'">relativpronomen</xsl:when>
                <xsl:when test="pos/@pos='suff'">suffix</xsl:when>
                <xsl:when test="pos/@pos='suffid'">som suffix</xsl:when>
                <xsl:when test="pos/@pos='v'"><a class="abbr-pos" href="?abbr:Verb">v</a></xsl:when>
                <xsl:when test="pos/@pos='v-aux'">hjelpeverb</xsl:when>
                <xsl:when test="pos/@pos='v-i'"><a class="abbr-pos" href="?abbr:itransitiv verb">v/itr.</a></xsl:when>
                <xsl:when test="pos/@pos='v-imp'"><a class="abbr-pos" href="?abbr:upersonelig verb">v/upersonelig</a></xsl:when>
                <xsl:when test="pos/@pos='v-ref'"><a class="abbr-pos" href="?abbr:reflexiv verb">v/ref.</a></xsl:when>
                <xsl:when test="pos/@pos='v-t'"><a class="abbr-pos" href="?abbr:transitiv verb">v/tr.</a></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose></span>
 <!-- gram --><xsl:choose><xsl:when test="boolean(@gram)"><span class="gram"><xsl:value-of select="@gram"/></span></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>
<xsl:apply-templates select="w"/>
</xsl:template>


<xsl:template match="alt.gr">
<xsl:for-each select="alt"><xsl:apply-templates/><span class="alt">
  <xsl:if test="position() != last()"><xsl:text>&#160;oder&#160;</xsl:text></xsl:if>
  <xsl:if test="position() = last()"><xsl:text></xsl:text></xsl:if>
</span></xsl:for-each>
</xsl:template>





<xsl:template match="tilde">
<xsl:value-of select="."/>
</xsl:template>





<xsl:attribute-set name="link">
  <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
</xsl:attribute-set>


<xsl:template match="link">
    <xsl:element name="a" use-attribute-sets="link"><xsl:value-of select="."/></xsl:element>
</xsl:template>







</xsl:stylesheet>
