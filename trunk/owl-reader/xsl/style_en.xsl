<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- begin common templates -->
<xsl:template name="pos"><xsl:if test="boolean(@pos)">
    <span class="pos">&#160;<xsl:choose>
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
                <xsl:when test="@pos='pr-ind'">indefine pronoun</xsl:when>
                <xsl:when test="@pos='pr-int'">interrogative pronoun</xsl:when>
                <xsl:when test="@pos='prop'">proper name</xsl:when>
                <xsl:when test="@pos='pr-per'">personal pronoun</xsl:when>
                <xsl:when test="@pos='pr-pos'">possessive pronoun</xsl:when>
                <xsl:when test="@pos='pr-rel'">relative pronoun</xsl:when>
                <xsl:when test="@pos='suff'">suffix</xsl:when>
                <xsl:when test="@pos='suffid'">like a suffix</xsl:when>
                <xsl:when test="@pos='v'"><a class="abbr-pos" href="?abbr:verb">v</a></xsl:when>
                <xsl:when test="@pos='v-aux'">auxiliary verb</xsl:when>
                <xsl:when test="@pos='v-i'"><a class="abbr-pos" href="?abbr:intransitive verb">v/intr.</a></xsl:when>
                <xsl:when test="@pos='v-imp'"><a class="abbr-pos" href="?abbr:impersonal verb">v/impersonal</a></xsl:when>
                <xsl:when test="@pos='v-ref'"><a class="abbr-pos" href="?abbr:reflexive verb">v/ref.</a></xsl:when>
                <xsl:when test="@pos='v-t'"><a class="abbr-pos" href="?abbr:transitive verb">v/tr.</a></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose></span></xsl:if>
</xsl:template>

<xsl:template name="niv">
     <xsl:if test="boolean(@niv)"><span class="niv">&#160;<xsl:choose>
                <xsl:when test="@niv='arg'">underworld jargon</xsl:when>
                <xsl:when test="@niv='contp'">contemptuously</xsl:when>
                <xsl:when test="@niv='de-AT'"><a class="abbr-niv" href="?abbr:Austrian German">Austria</a></xsl:when>
                <xsl:when test="@niv='de-CH'"><a class="abbr-niv" href="?abbr:Swiss German">Swiss G</a></xsl:when>
                <xsl:when test="@niv='en-UK'"><a class="abbr-niv" href="?abbr:Britisch English">Br.</a></xsl:when>
                <xsl:when test="@niv='en-US'"><a class="abbr-niv" href="?abbr:American English">Am.</a></xsl:when>
                <xsl:when test="@niv='euph'">euphemistically</xsl:when>
                <xsl:when test="@niv='fam'">colloquial</xsl:when>
                <xsl:when test="@niv='fig'"><a class="abbr-niv" href="?abbr:figuratively">fig</a></xsl:when>
                <xsl:when test="@niv='fr-BE'"><a class="abbr-niv" href="?abbr:Belgian French">Belgia</a></xsl:when>
                <xsl:when test="@niv='fr-CA'"><a class="abbr-niv" href="?abbr:Canadian French">Canada</a></xsl:when>
                <xsl:when test="@niv='fr-CH'"><a class="abbr-niv" href="?abbr:Swiss French">Swiss</a></xsl:when>
                <xsl:when test="@niv='hum'">humorously</xsl:when>
                <xsl:when test="@niv='iro'">ironic</xsl:when>
                <xsl:when test="@niv='lit'">literary</xsl:when>
                <xsl:when test="@niv='p'">vernacular</xsl:when>
                <xsl:when test="@niv='poet'">poetical</xsl:when>
                <xsl:when test="@niv='reg'">regional, dialectal</xsl:when>
                <xsl:when test="@niv='sc'">scientific</xsl:when>
                <xsl:when test="@niv='st'">elevated style</xsl:when>
                <xsl:when test="@niv='vulg'">vulgar</xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>&#160;</span></xsl:if>
</xsl:template>

<xsl:template name="n">
<xsl:if test="boolean(@n)"><span class="n">(<xsl:value-of select="@n"/>)&#160;</span></xsl:if>
</xsl:template>




<xsl:template name="dom">

 <xsl:if test="boolean(@dom)">
   <span class="dom"><xsl:choose>
     <xsl:when test="@dom='agri'">agriculture</xsl:when>
     <xsl:when test="@dom='anat'">anatomy</xsl:when>
     <xsl:when test="@dom='arch'">architecture</xsl:when>
     <xsl:when test="@dom='avia'">aviation</xsl:when>
     <xsl:when test="@dom='bibl'">biblical</xsl:when>
     <xsl:when test="@dom='bio'">biology</xsl:when>
     <xsl:when test="@dom='bot'">botany</xsl:when>
     <xsl:when test="@dom='chem'">chemistry</xsl:when>
     <xsl:when test="@dom='coll'">collectively</xsl:when>
     <xsl:when test="@dom='comm'">commercial term</xsl:when>
     <xsl:when test="@dom='comp'">computer</xsl:when>
     <xsl:when test="@dom='cuis'">cooking</xsl:when>
     <xsl:when test="@dom='eccl'">ecclesiastical</xsl:when>
     <xsl:when test="@dom='elec'">electronics</xsl:when>
     <xsl:when test="@dom='elet'">electrical engineering</xsl:when>
     <xsl:when test="@dom='fenc'">fencing</xsl:when>
     <xsl:when test="@dom='gast'">gastronomy</xsl:when>
     <xsl:when test="@dom='geog'">geography</xsl:when>
     <xsl:when test="@dom='geol'">geology</xsl:when>
     <xsl:when test="@dom='hera'">heraldry</xsl:when>
     <xsl:when test="@dom='hist'">historical</xsl:when>
     <xsl:when test="@dom='info'">information science</xsl:when>
     <xsl:when test="@dom='lega'">legal term</xsl:when>
     <xsl:when test="@dom='ling'">linguistics</xsl:when>
     <xsl:when test="@dom='math'">mathematics</xsl:when>
     <xsl:when test="@dom='medic'">medicine</xsl:when>
     <xsl:when test="@dom='metall'">metallurgy</xsl:when>
     <xsl:when test="@dom='meteo'">meteorology</xsl:when>
     <xsl:when test="@dom='mili'">military term</xsl:when>
     <xsl:when test="@dom='mine'">mineralogy</xsl:when>
     <xsl:when test="@dom='mini'">mining</xsl:when>
     <xsl:when test="@dom='moto'">motoring</xsl:when>
     <xsl:when test="@dom='music'">musical term</xsl:when>
     <xsl:when test="@dom='myth'">mythology</xsl:when>
     <xsl:when test="@dom='naut'">nautical term</xsl:when>
     <xsl:when test="@dom='optic'">optics</xsl:when>
     <xsl:when test="@dom='orni'">ornithology</xsl:when>
     <xsl:when test="@dom='parl'">parliamentary term</xsl:when>
     <xsl:when test="@dom='peda'">pedagogics</xsl:when>
     <xsl:when test="@dom='phar'">pharmacy</xsl:when>
     <xsl:when test="@dom='phil'">philosophy</xsl:when>
     <xsl:when test="@dom='phot'">photography</xsl:when>
     <xsl:when test="@dom='phyo'">physiology</xsl:when>
     <xsl:when test="@dom='phys'">physics</xsl:when>
     <xsl:when test="@dom='poli'">politics</xsl:when>
     <xsl:when test="@dom='post'">postal affairs</xsl:when>
     <xsl:when test="@dom='psyc'">psychology</xsl:when>
     <xsl:when test="@dom='rail'">railway</xsl:when>
     <xsl:when test="@dom='rhet'">rhetoric</xsl:when>
     <xsl:when test="@dom='tech'">technology</xsl:when>
     <xsl:when test="@dom='tele'">telephone</xsl:when>
     <xsl:when test="@dom='thea'">theatre</xsl:when>
     <xsl:when test="@dom='tv'">television</xsl:when>
     <xsl:when test="@dom='typo'">typography, printing</xsl:when>
     <xsl:when test="@dom='univ'">university</xsl:when>
     <xsl:when test="@dom='vete'">veterinary medicine</xsl:when>
     <xsl:when test="@dom='zool'">zoology</xsl:when>
     <xsl:otherwise>unknown</xsl:otherwise></xsl:choose>&#160;</span></xsl:if>
</xsl:template>
<!-- end common templates -->

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
 <p><span class="lemma"><xsl:for-each select="l"><xsl:apply-templates/></xsl:for-each></span>
 <!-- pos --> <xsl:for-each select="pos"><xsl:call-template name="pos"/></xsl:for-each>

 <!-- phon --> <xsl:if test="boolean(phon)"><span class="phon">[<xsl:value-of select="phon"/>]</span></xsl:if>

</p>
</div>
</xsl:template>


<xsl:template match="hyph">&#x25AB;</xsl:template>




<xsl:template match="sense.gr">
<table class="block">
  <tr><td class="block-links" valign="top"><p>
 <!-- num --> <xsl:if test="boolean(@num)"><span class="num"><xsl:value-of select="@num"/></span></xsl:if>
       </p></td>
      <td class="block-rechts" valign="top">
 <!-- dom --> <xsl:call-template name="dom"/>
 <!-- n -->   <xsl:call-template name="n"/>
 <!-- niv --> <xsl:call-template name="niv"/>
      <xsl:apply-templates/></td>

      </tr>
</table>
</xsl:template>





<xsl:template match="p">
<p>
 <!-- dom --> <xsl:call-template name="dom"/>
 <!-- n -->   <xsl:call-template name="n"/>
 <!-- niv --> <xsl:call-template name="niv"/>
 <!-- -->     <xsl:for-each select="t"><xsl:call-template name="t"/></xsl:for-each>
 <!-- -->     <xsl:for-each select="d"><xsl:call-template name="d"/></xsl:for-each>
</p>
 <!-- -->     <xsl:apply-templates select="ex"/>
</xsl:template>


<xsl:template match="p.col">
<p>
 <!-- dom --> <xsl:call-template name="dom"/>
 <!-- n -->   <xsl:call-template name="n"/>
 <!-- niv --> <xsl:call-template name="niv"/>
 <!-- -->     <xsl:apply-templates select="s"/>
 <!-- -->     <xsl:for-each select="t"><xsl:call-template name="t"/></xsl:for-each>
 <!-- -->     <xsl:for-each select="d"><xsl:call-template name="d"/></xsl:for-each>
</p>
 <!-- -->     <xsl:apply-templates select="ex"/>
</xsl:template>

<xsl:template match="p.idm">
<p>
 <!-- dom --> <xsl:call-template name="dom"/>
 <!-- n -->   <xsl:call-template name="n"/>
 <!-- niv --> <xsl:call-template name="niv"/>
 <!-- -->     <xsl:apply-templates select="s"/>
 <!-- -->     <xsl:for-each select="t"><xsl:call-template name="t"/></xsl:for-each>
 <!-- -->     <xsl:for-each select="d"><xsl:call-template name="d"/></xsl:for-each>
</p>
 <!-- -->     <xsl:apply-templates select="ex"/>
</xsl:template>




<xsl:template match="ex">
<p class="ex">&#x25AB;
 <!-- -->     <xsl:apply-templates select="s"/>
 <!-- -->     <xsl:for-each select="t"><xsl:call-template name="t"/></xsl:for-each>
 <!-- -->     <xsl:for-each select="d"><xsl:call-template name="d"/></xsl:for-each>
 <!-- -->     <xsl:apply-templates select="ex"/>
</p>
</xsl:template>



<xsl:template match="s">
<span class="s"><xsl:apply-templates/>&#160;</span>
</xsl:template>

<xsl:template name="d">
<span class="d"><xsl:if test="position() != last()"><xsl:apply-templates/><xsl:text>; </xsl:text></xsl:if><xsl:if test="position() = last()"><xsl:apply-templates/><xsl:text></xsl:text></xsl:if></span>
</xsl:template>

<xsl:template name="t">
<span class="t"><xsl:if test="position() != last()"><xsl:apply-templates/><xsl:text>; </xsl:text></xsl:if><xsl:if test="position() = last()"><xsl:apply-templates/><xsl:text></xsl:text></xsl:if></span>
</xsl:template>






<xsl:template match="w">
 <!-- -->     <xsl:apply-templates/>
 <!-- pos --> <xsl:call-template name="pos" />
 <!-- govern --><xsl:if test="boolean(@govern)"><span class="govern">&#160;+<xsl:choose>
                <xsl:when test="@govern='abe'">abessive case</xsl:when>
                <xsl:when test="@govern='abl'">ablative case</xsl:when>
                <xsl:when test="@govern='abs'">absolutive case</xsl:when>
                <xsl:when test="@govern='acc'">accusative case</xsl:when>
                <xsl:when test="@govern='ade'">adessive case</xsl:when>
                <xsl:when test="@govern='dat'">dative case</xsl:when>
                <xsl:when test="@govern='ela'">elative case</xsl:when>
                <xsl:when test="@govern='erg'">ergative case</xsl:when>
                <xsl:when test="@govern='gen'">genitive case</xsl:when>
                <xsl:when test="@govern='ger'">gerund</xsl:when>
                <xsl:when test="@govern='ill'">illative case</xsl:when>
                <xsl:when test="@govern='ind'">indicative</xsl:when>
                <xsl:when test="@govern='ine'">inessive case</xsl:when>
                <xsl:when test="@govern='ins'">infinitive</xsl:when>
                <xsl:when test="@govern='kon'">subjonctif (German)</xsl:when>
                <xsl:when test="@govern='loc'">locative case</xsl:when>
                <xsl:when test="@govern='nom'">nominative case</xsl:when>
                <xsl:when test="@govern='pro'">prolative case</xsl:when>
                <xsl:when test="@govern='sub'">subjonctif (roman
languages)</xsl:when>
                <xsl:when test="@govern='ter'">terminative case</xsl:when>
                <xsl:when test="@govern='voc'">vocative case</xsl:when>
                <xsl:otherwise></xsl:otherwise></xsl:choose></span></xsl:if>
</xsl:template>


<xsl:template match="alt.gr">
<xsl:for-each select="alt"><xsl:apply-templates/><span class="alt">
  <xsl:if test="position() != last()">/</xsl:if>
  <xsl:if test="position() = last()"></xsl:if>
</span></xsl:for-each>
</xsl:template>





<xsl:template match="tilde">&#x007E;</xsl:template>

<xsl:template match="ca.uc">una cosa</xsl:template>
<xsl:template match="ca.up">una persona</xsl:template>
<xsl:template match="de.etw">etwas</xsl:template>
<xsl:template match="de.j">jemand</xsl:template>
<xsl:template match="de.j-m">jemandem</xsl:template>
<xsl:template match="de.j-n">jemanden</xsl:template>
<xsl:template match="en.sb">somebody</xsl:template>
<xsl:template match="en.sth">something</xsl:template>
<xsl:template match="fr.qc">quelque chose</xsl:template>
<xsl:template match="fr.qn">quelqu'un</xsl:template>






<xsl:attribute-set name="link">
  <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
</xsl:attribute-set>


<xsl:template match="link">
    <xsl:element name="a" use-attribute-sets="link"><xsl:value-of select="."/></xsl:element>
</xsl:template>







</xsl:stylesheet>
