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
                <xsl:when test="pos/@pos='abr'">abbreviation</xsl:when>
                <xsl:when test="pos/@pos='adj'"><a class="abbr-pos" href="?abbr:adjective">adj</a></xsl:when>
                <xsl:when test="pos/@pos='adv'"><a class="abbr-pos" href="?abbr:adverb">adv</a></xsl:when>
                <xsl:when test="pos/@pos='conj'">Konjunktion</xsl:when>
                <xsl:when test="pos/@pos='f'"><a class="abbr-pos" href="?abbr:feminine">f</a></xsl:when>
                <xsl:when test="pos/@pos='fpl'"><a class="abbr-pos" href="?abbr:feminine plural">f/pl</a></xsl:when>
                <xsl:when test="pos/@pos='int'">Interjektion</xsl:when>
                <xsl:when test="pos/@pos='m'"><a class="abbr-pos" href="?abbr:masculine">m</a></xsl:when>
                <xsl:when test="pos/@pos='mpl'"><a class="abbr-pos" href="?abbr:masculine plural">m/pl</a></xsl:when>
                <xsl:when test="pos/@pos='n'"><a class="abbr-pos" href="?abbr:neuter">n</a></xsl:when>
                <xsl:when test="pos/@pos='npl'"><a class="abbr-pos" href="?abbr:neuter plural">n/pl</a></xsl:when>
                <xsl:when test="pos/@pos='pr'">pronoun</xsl:when>
                <xsl:when test="pos/@pos='pr-dem'">demonstrative pronoun</xsl:when>
                <xsl:when test="pos/@pos='pref'">prefix</xsl:when>
                <xsl:when test="pos/@pos='prefid'">like a prefix</xsl:when>
                <xsl:when test="pos/@pos='prep'">preposition</xsl:when>
                <xsl:when test="pos/@pos='pr-ind'">indefine pronoun</xsl:when>
                <xsl:when test="pos/@pos='pr-int'">interrogative pronoun</xsl:when>
                <xsl:when test="pos/@pos='prop'">proper name</xsl:when>
                <xsl:when test="pos/@pos='pr-per'">personal pronoun</xsl:when>
                <xsl:when test="pos/@pos='pr-pos'">possessive pronoun</xsl:when>
                <xsl:when test="pos/@pos='pr-rel'">relative pronoun</xsl:when>
                <xsl:when test="pos/@pos='suff'">suffix</xsl:when>
                <xsl:when test="pos/@pos='suffid'">like a suffix</xsl:when>
                <xsl:when test="pos/@pos='v'"><a class="abbr-pos" href="?abbr:verb">v</a></xsl:when>
                <xsl:when test="pos/@pos='v-aux'">auxiliary verb</xsl:when>
                <xsl:when test="pos/@pos='v-i'"><a class="abbr-pos" href="?abbr:intransitive verb">v/intr.</a></xsl:when>
                <xsl:when test="pos/@pos='v-imp'"><a class="abbr-pos" href="?abbr:impersonal verb">v/impersonal</a></xsl:when>
                <xsl:when test="pos/@pos='v-ref'"><a class="abbr-pos" href="?abbr:reflexive verb">v/ref.</a></xsl:when>
                <xsl:when test="pos/@pos='v-t'"><a class="abbr-pos" href="?abbr:transitive verb">v/tr.</a></xsl:when>
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
                <xsl:when test="@pos='abr'">abbreviation</xsl:when>
                <xsl:when test="@pos='adj'"><a class="abbr-pos" href="?abbr:adjective">adj</a></xsl:when>
                <xsl:when test="@pos='adv'"><a class="abbr-pos" href="?abbr:adverb">adv</a></xsl:when>
                <xsl:when test="@pos='conj'">Konjunktion</xsl:when>
                <xsl:when test="@pos='f'"><a class="abbr-pos" href="?abbr:feminine">f</a></xsl:when>
                <xsl:when test="@pos='fpl'"><a class="abbr-pos" href="?abbr:feminine plural">f/pl</a></xsl:when>
                <xsl:when test="@pos='int'">Interjektion</xsl:when>
                <xsl:when test="@pos='m'"><a class="abbr-pos" href="?abbr:masculine">m</a></xsl:when>
                <xsl:when test="@pos='mpl'"><a class="abbr-pos" href="?abbr:masculine plural">m/pl</a></xsl:when>
                <xsl:when test="@pos='n'"><a class="abbr-pos" href="?abbr:neuter">n</a></xsl:when>
                <xsl:when test="@pos='npl'"><a class="abbr-pos" href="?abbr:neuter plural">n/pl</a></xsl:when>
                <xsl:when test="@pos='pr'">pronoun</xsl:when>
                <xsl:when test="@pos='pr-dem'">demonstrative pronoun</xsl:when>
                <xsl:when test="@pos='pref'">prefix</xsl:when>
                <xsl:when test="@pos='prefid'">like a prefix</xsl:when>
                <xsl:when test="@pos='prep'">preposition</xsl:when>
                <xsl:when test="@pos='pr-ind'">interrogative pronoun</xsl:when>
                <xsl:when test="@pos='pr-int'">Interrogativpronoun</xsl:when>
                <xsl:when test="@pos='prop'">proper name</xsl:when>
                <xsl:when test="@pos='pr-per'">personal pronoun</xsl:when>
                <xsl:when test="@pos='pr-pos'">possessive pronoun</xsl:when>
                <xsl:when test="@pos='pr-rel'">relative pronoun</xsl:when>
                <xsl:when test="@pos='suff'">suffix</xsl:when>
                <xsl:when test="@pos='suffid'">like a suffix</xsl:when>
                <xsl:when test="@pos='v'">V</xsl:when>
                <xsl:when test="@pos='v-aux'">auxiliary verb</xsl:when>
                <xsl:when test="@pos='v-i'"><a class="abbr-pos" href="?abbr:intransitive verb">v/intr.</a></xsl:when>
                <xsl:when test="@pos='v-imp'"><a class="abbr-pos" href="?abbr:impersonal verb">v/impersonal</a></xsl:when>
                <xsl:when test="@pos='v-ref'"><a class="abbr-pos" href="?abbr:reflexive verb">v/ref.</a></xsl:when>
                <xsl:when test="@pos='v-t'"><a class="abbr-pos" href="?abbr:transitive verb">v/tr.</a></xsl:when>
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
