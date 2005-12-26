<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- begin common templates -->
<xsl:template name="pos"><xsl:if test="boolean(@pos)">
    <span class="pos">&#160;<xsl:choose>
                <xsl:when test="@pos='abr'">abr&#x00E9;viation</xsl:when>
                <xsl:when test="@pos='adj'"><a class="abbr-pos" href="?abbr:adjectif">adj</a></xsl:when>
                <xsl:when test="@pos='adv'"><a class="abbr-pos" href="?abbr:adverbe">adv</a></xsl:when>
                <xsl:when test="@pos='conj'">conjontion</xsl:when>
                <xsl:when test="@pos='f'"><a class="abbr-pos" href="?abbr:f&#x00E9;minin">f</a></xsl:when>
                <xsl:when test="@pos='fpl'"><a class="abbr-pos" href="?abbr:f&#x00E9;minin pluriel">f/pl</a></xsl:when>
                <xsl:when test="@pos='int'">interjection</xsl:when>
                <xsl:when test="@pos='m'"><a class="abbr-pos" href="?abbr:masculin">m</a></xsl:when>
                <xsl:when test="@pos='mpl'"><a class="abbr-pos" href="?abbr:masculin pluriel">m/pl</a></xsl:when>
                <xsl:when test="@pos='n'"><a class="abbr-pos" href="?abbr:neutre">n</a></xsl:when>
                <xsl:when test="@pos='npl'"><a class="abbr-pos" href="?abbr:neutre pluriel">n/pl</a></xsl:when>
                <xsl:when test="@pos='pr'">pronom</xsl:when>
                <xsl:when test="@pos='pr-dem'">pronom d&#x00E9;monstratif</xsl:when>
                <xsl:when test="@pos='pref'">pr&#x00E9;fix</xsl:when>
                <xsl:when test="@pos='prefid'">comme un pr&#x00E9;fix</xsl:when>
                <xsl:when test="@pos='prep'">pr&#x00E9;position</xsl:when>
                <xsl:when test="@pos='pr-ind'">pronom ind&#x00E9;fini</xsl:when>
                <xsl:when test="@pos='pr-int'">pronom interrogatif</xsl:when>
                <xsl:when test="@pos='prop'">nom propre</xsl:when>
                <xsl:when test="@pos='pr-per'">pronom personnel</xsl:when>
                <xsl:when test="@pos='pr-pos'">pronom possessif</xsl:when>
                <xsl:when test="@pos='pr-rel'">pronom relatif</xsl:when>
                <xsl:when test="@pos='suff'">suffixe</xsl:when>
                <xsl:when test="@pos='suffid'">comme un suffixe</xsl:when>
                <xsl:when test="@pos='v'"><a class="abbr-pos" href="?abbr:verbe">V</a></xsl:when>
                <xsl:when test="@pos='v-aux'">verbe auxiliaire</xsl:when>
                <xsl:when test="@pos='v-i'"><a class="abbr-pos" href="?abbr:verbe intransitif">V/intr.</a></xsl:when>
                <xsl:when test="@pos='v-imp'"><a class="abbr-pos" href="?abbr:verbe impersonnel">V/impersonnel</a></xsl:when>
                <xsl:when test="@pos='v-ref'"><a class="abbr-pos" href="?abbr:verbe r&#x00E9;fl&#x00E9;chi">V/r&#x00E9;fl&#x00E9;chi</a></xsl:when>
                <xsl:when test="@pos='v-t'"><a class="abbr-pos" href="?abbr:verbe transitif">V/tr.</a></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose></span></xsl:if>
</xsl:template>

<xsl:template name="niv">
     <xsl:if test="boolean(@niv)"><span class="niv">&#160;<xsl:choose>
                <xsl:when test="@niv='arg'">argot</xsl:when>
                <xsl:when test="@niv='contp'">m&#x00E9;prisant</xsl:when>
                <xsl:when test="@niv='de-AT'"><a class="abbr-niv" href="?abbr:allemand autrichien">Autriche</a></xsl:when>
                <xsl:when test="@niv='de-CH'"><a class="abbr-niv" href="?abbr:allemand suisse">Suisse</a></xsl:when>
                <xsl:when test="@niv='en-UK'"><a class="abbr-niv" href="?abbr:anglais britannique">BrE</a></xsl:when>
                <xsl:when test="@niv='en-US'"><a class="abbr-niv" href="?abbr:anglais am&#x00E9;ricain">NAm</a></xsl:when>
                <xsl:when test="@niv='euph'">euph&#x00E9;mique</xsl:when>
                <xsl:when test="@niv='fam'"><a class="abbr-niv" href="?abbr:familier">fam</a></xsl:when>
                <xsl:when test="@niv='fig'"><a class="abbr-niv" href="?abbr:sens figur&#x00E9;">fig</a></xsl:when>
                <xsl:when test="@niv='fr-BE'"><a class="abbr-niv" href="?abbr:français belge">Belgie</a></xsl:when>
                <xsl:when test="@niv='fr-CA'"><a class="abbr-niv" href="?abbr:français canadien">Canada</a></xsl:when>
                <xsl:when test="@niv='fr-CH'"><a class="abbr-niv" href="?abbr:français, suiss">Suisse</a></xsl:when>
                <xsl:when test="@niv='hum'">plaisant</xsl:when>
                <xsl:when test="@niv='iro'">ironique</xsl:when>
                <xsl:when test="@niv='lit'">litt&#x00E9;raire</xsl:when>
                <xsl:when test="@niv='p'">populaire</xsl:when>
                <xsl:when test="@niv='poet'">po&#x00E9;tique</xsl:when>
                <xsl:when test="@niv='reg'">r&#x00E9;gional</xsl:when>
                <xsl:when test="@niv='sc'">scientifique</xsl:when>
                <xsl:when test="@niv='st'">style soutenu</xsl:when>
                <xsl:when test="@niv='vulg'">vulgaire</xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>&#160;</span></xsl:if>
</xsl:template>

<xsl:template name="n">
<xsl:if test="boolean(@n)"><span class="n">(<xsl:value-of select="@n"/>)&#160;</span></xsl:if>
</xsl:template>




<xsl:template name="dom">

 <xsl:if test="boolean(@dom)">
   <span class="dom"><xsl:choose>
     <xsl:when test="@dom='agri'">agriculture</xsl:when>
     <xsl:when test="@dom='anat'">anatomie</xsl:when>
     <xsl:when test="@dom='arch'">architecture</xsl:when>
     <xsl:when test="@dom='avia'">navigation a&#x00E9;rienne</xsl:when>
     <xsl:when test="@dom='bibl'">biblique</xsl:when>
     <xsl:when test="@dom='bio'">biologie</xsl:when>
     <xsl:when test="@dom='bot'">botanique</xsl:when>
     <xsl:when test="@dom='chem'">chimie</xsl:when>
     <xsl:when test="@dom='coll'">terme g&#x00E9;n&#x00E9;rique</xsl:when>
     <xsl:when test="@dom='comm'">commerce</xsl:when>
     <xsl:when test="@dom='comp'">ordinateur</xsl:when>
     <xsl:when test="@dom='cuis'">cuisine</xsl:when>
     <xsl:when test="@dom='eccl'">eccl&#x00E9;siastique</xsl:when>
     <xsl:when test="@dom='elec'">&#x00E9;lectronique</xsl:when>
     <xsl:when test="@dom='elet'">&#x00E9;lectrotechnique</xsl:when>
     <xsl:when test="@dom='fenc'">escrime</xsl:when>
     <xsl:when test="@dom='gast'">gastronomie</xsl:when>
     <xsl:when test="@dom='geog'">g&#x00E9;ographie</xsl:when>
     <xsl:when test="@dom='geol'">g&#x00E9;ologie</xsl:when>
     <xsl:when test="@dom='hera'">h&#x00E9;raldique</xsl:when>
     <xsl:when test="@dom='hist'">historique</xsl:when>
     <xsl:when test="@dom='info'">informatique</xsl:when>
     <xsl:when test="@dom='lega'">droit</xsl:when>
     <xsl:when test="@dom='ling'">linguistique</xsl:when>
     <xsl:when test="@dom='math'">math&#x00E9;matiques</xsl:when>
     <xsl:when test="@dom='medic'">m&#x00E9;decine</xsl:when>
     <xsl:when test="@dom='metall'">m&#x00E9;tallurgie</xsl:when>
     <xsl:when test="@dom='meteo'">m&#x00E9;t&#x00E9;orologie</xsl:when>
     <xsl:when test="@dom='mili'">militaire</xsl:when>
     <xsl:when test="@dom='mine'">min&#x00E9;ralogie</xsl:when>
     <xsl:when test="@dom='mini'">industrie minière</xsl:when>
     <xsl:when test="@dom='moto'">circulation</xsl:when>
     <xsl:when test="@dom='music'">musique</xsl:when>
     <xsl:when test="@dom='myth'">mythologie</xsl:when>
     <xsl:when test="@dom='naut'">nautique</xsl:when>
     <xsl:when test="@dom='optic'">optique</xsl:when>
     <xsl:when test="@dom='orni'">ornithologie</xsl:when>
     <xsl:when test="@dom='parl'">parlement</xsl:when>
     <xsl:when test="@dom='peda'">p&#x00E9;dagogique</xsl:when>
     <xsl:when test="@dom='phar'">pharmacie</xsl:when>
     <xsl:when test="@dom='phil'">philosophie</xsl:when>
     <xsl:when test="@dom='phot'">photographie</xsl:when>
     <xsl:when test="@dom='phyo'">physiologie</xsl:when>
     <xsl:when test="@dom='phys'">physique</xsl:when>
     <xsl:when test="@dom='poli'">politique</xsl:when>
     <xsl:when test="@dom='post'">postes</xsl:when>
     <xsl:when test="@dom='psyc'">psychologie</xsl:when>
     <xsl:when test="@dom='rail'">chemin de fer</xsl:when>
     <xsl:when test="@dom='rhet'">rh&#x00E9;torique</xsl:when>
     <xsl:when test="@dom='tech'">technique</xsl:when>
     <xsl:when test="@dom='tele'">t&#x00E9;l&#x00E9;phone</xsl:when>
     <xsl:when test="@dom='thea'">th&#x00E9;âtre</xsl:when>
     <xsl:when test="@dom='tv'">t&#x00E9;l&#x00E9;vision</xsl:when>
     <xsl:when test="@dom='typo'">imprimerie</xsl:when>
     <xsl:when test="@dom='univ'">universit&#x00E9;</xsl:when>
     <xsl:when test="@dom='vete'">m&#x00E9;decine v&#x00E9;t&#x00E9;rinaire</xsl:when>
     <xsl:when test="@dom='zool'">zoologie</xsl:when>
     <xsl:otherwise>inconnu</xsl:otherwise></xsl:choose>&#160;</span></xsl:if>
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
                <xsl:when test="@govern='abe'">abessif</xsl:when>
                <xsl:when test="@govern='abl'">ablatif</xsl:when>
                <xsl:when test="@govern='abs'">absolutif</xsl:when>
                <xsl:when test="@govern='acc'">accusatif</xsl:when>
                <xsl:when test="@govern='ade'">adessif</xsl:when>
                <xsl:when test="@govern='dat'">datif</xsl:when>
                <xsl:when test="@govern='ela'">&#x00E9;latif</xsl:when>
                <xsl:when test="@govern='erg'">ergatif</xsl:when>
                <xsl:when test="@govern='gen'">g&#x00E9;nitif</xsl:when>
                <xsl:when test="@govern='ger'">g&#x00E9;rondif</xsl:when>
                <xsl:when test="@govern='ill'">illatif</xsl:when>
                <xsl:when test="@govern='ind'">indikatif</xsl:when>
                <xsl:when test="@govern='ine'">inessif</xsl:when>
                <xsl:when test="@govern='ins'">instrumental</xsl:when>
                <xsl:when test="@govern='kon'">subjonctif</xsl:when>
                <xsl:when test="@govern='loc'">locatif</xsl:when>
                <xsl:when test="@govern='nom'">nominatif</xsl:when>
                <xsl:when test="@govern='pro'">prolatif</xsl:when>
                <xsl:when test="@govern='sub'">subj.</xsl:when>
                <xsl:when test="@govern='ter'">terminatif</xsl:when>
                <xsl:when test="@govern='voc'">vokatif</xsl:when>
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
