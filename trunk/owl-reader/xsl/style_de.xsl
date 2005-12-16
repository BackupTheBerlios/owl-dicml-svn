<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- begin common templates -->
<xsl:template name="pos"><xsl:if test="boolean(@pos)">
    <span class="pos">&#160;<xsl:choose>
                <xsl:when test="@pos='abr'">Abk&#x00FC;rzung</xsl:when>
                <xsl:when test="@pos='adj'"><a class="abbr-pos" href="?abbr:Adjektiv">adj</a></xsl:when>
                <xsl:when test="@pos='adv'"><a class="abbr-pos" href="?abbr:Adverb">adv</a></xsl:when>
                <xsl:when test="@pos='conj'">Konjunktion</xsl:when>
                <xsl:when test="@pos='f'"><a class="abbr-pos" href="?abbr:Femininum">f</a></xsl:when>
                <xsl:when test="@pos='fpl'"><a class="abbr-pos" href="?abbr:Femininum Plural">f/pl</a></xsl:when>
                <xsl:when test="@pos='int'">Interjektion</xsl:when>
                <xsl:when test="@pos='m'"><a class="abbr-pos" href="?abbr:Maskulinum">m</a></xsl:when>
                <xsl:when test="@pos='mpl'"><a class="abbr-pos" href="?abbr:Maskulinum Plural">m/pl</a></xsl:when>
                <xsl:when test="@pos='n'"><a class="abbr-pos" href="?abbr:Neutrum">n</a></xsl:when>
                <xsl:when test="@pos='npl'"><a class="abbr-pos" href="?abbr:Neutrum Plural">n/pl</a></xsl:when>
                <xsl:when test="@pos='pr'">Pronomen</xsl:when>
                <xsl:when test="@pos='pr-dem'">Demonstrativpronomen</xsl:when>
                <xsl:when test="@pos='pref'">Pr&#x00E4;fix</xsl:when>
                <xsl:when test="@pos='prefid'">Pr&#x00E4;fixoid</xsl:when>
                <xsl:when test="@pos='prep'">Pr&#x00E4;position</xsl:when>
                <xsl:when test="@pos='pr-ind'">Indefinitpronomen</xsl:when>
                <xsl:when test="@pos='pr-int'">Interrogativpronomen</xsl:when>
                <xsl:when test="@pos='prop'">Eigenname</xsl:when>
                <xsl:when test="@pos='pr-per'">Personalpronomen</xsl:when>
                <xsl:when test="@pos='pr-pos'">Possessipronomen</xsl:when>
                <xsl:when test="@pos='pr-rel'">Relativpronomen</xsl:when>
                <xsl:when test="@pos='suff'">Suffix</xsl:when>
                <xsl:when test="@pos='suffid'">Suffixoid</xsl:when>
                <xsl:when test="@pos='v'"><a class="abbr-pos" href="?abbr:Verb">V</a></xsl:when>
                <xsl:when test="@pos='v-aux'">Hilfsverb</xsl:when>
                <xsl:when test="@pos='v-i'"><a class="abbr-pos" href="?abbr:intransitives Verb">V/intr.</a></xsl:when>
                <xsl:when test="@pos='v-imp'"><a class="abbr-pos" href="?abbr:unpers&#x00F6;nliches Verb">V/unpers&#x00F6;nlich</a></xsl:when>
                <xsl:when test="@pos='v-ref'"><a class="abbr-pos" href="?abbr:reflexives Verb">V/ref.</a></xsl:when>
                <xsl:when test="@pos='v-t'"><a class="abbr-pos" href="?abbr:transitives Verb">V/tr.</a></xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose></span></xsl:if>
</xsl:template>

<xsl:template name="niv">
     <xsl:if test="boolean(@niv)"><span class="niv">&#160;<xsl:choose>
                <xsl:when test="@niv='arg'">Argot</xsl:when>
                <xsl:when test="@niv='contp'">verächtlich</xsl:when>
                <xsl:when test="@niv='de-AT'"><a class="abbr-niv" href="?abbr:Österreichische Deutsch">Österr.</a></xsl:when>
                <xsl:when test="@niv='de-CH'"><a class="abbr-niv" href="?abbr:Schweizerisches Deutsch">Schweiz</a></xsl:when>
                <xsl:when test="@niv='en-UK'"><a class="abbr-niv" href="?abbr:Britisches Englisch">BrE</a></xsl:when>
                <xsl:when test="@niv='en-US'"><a class="abbr-niv" href="?abbr:Nordamerikanisches Englisch">NAm</a></xsl:when>
                <xsl:when test="@niv='euph'">beschönigend</xsl:when>
                <xsl:when test="@niv='fam'"><a class="abbr-niv" href="?abbr:familiär, umgangssprachlich">fam</a></xsl:when>
                <xsl:when test="@niv='fig'"><a class="abbr-niv" href="?abbr:figürlich, im übertragenen Sinne">fig</a></xsl:when>
                <xsl:when test="@niv='fr-BE'"><a class="abbr-niv" href="?abbr:Belgisches Französisch">Belgien</a></xsl:when>
                <xsl:when test="@niv='fr-CA'"><a class="abbr-niv" href="?abbr:Kanadisches Französisch">Kanada</a></xsl:when>
                <xsl:when test="@niv='fr-CH'"><a class="abbr-niv" href="?abbr:Schweizerisches Französisch">Schweiz</a></xsl:when>
                <xsl:when test="@niv='hum'">scherzhaft</xsl:when>
                <xsl:when test="@niv='iro'">ironisch</xsl:when>
                <xsl:when test="@niv='lit'">literarisch</xsl:when>
                <xsl:when test="@niv='p'">derb</xsl:when>
                <xsl:when test="@niv='poet'">poetisch</xsl:when>
                <xsl:when test="@niv='reg'">regional</xsl:when>
                <xsl:when test="@niv='sc'">wissenschaftlich</xsl:when>
                <xsl:when test="@niv='st'">gehoben</xsl:when>
                <xsl:when test="@niv='vulg'">vulgär</xsl:when>
              <xsl:otherwise></xsl:otherwise></xsl:choose>&#160;</span></xsl:if>
</xsl:template>

<xsl:template name="n">
<xsl:if test="boolean(@n)"><span class="n">(<xsl:value-of select="@n"/>)&#160;</span></xsl:if>
</xsl:template>




<xsl:template name="dom">

 <xsl:if test="boolean(@dom)">
   <span class="dom"><xsl:choose>
     <xsl:when test="@dom='agri'">Landwirtschaft</xsl:when>
     <xsl:when test="@dom='anat'">Anatomie</xsl:when>
     <xsl:when test="@dom='arch'">Architektur</xsl:when>
     <xsl:when test="@dom='avia'">Luftfahrt</xsl:when>
     <xsl:when test="@dom='bibl'">biblisch</xsl:when>
     <xsl:when test="@dom='bio'">Biologie</xsl:when>
     <xsl:when test="@dom='bot'">Botanik</xsl:when>
     <xsl:when test="@dom='chem'">Chemie</xsl:when>
     <xsl:when test="@dom='coll'">Kollektivbegriff</xsl:when>
     <xsl:when test="@dom='comm'">Wirtschaft, Handel</xsl:when>
     <xsl:when test="@dom='comp'">Computer</xsl:when>
     <xsl:when test="@dom='cuis'">Küche</xsl:when>
     <xsl:when test="@dom='eccl'">kirchlich</xsl:when>
     <xsl:when test="@dom='elec'">Elektronik</xsl:when>
     <xsl:when test="@dom='elet'">Elektrotechnik</xsl:when>
     <xsl:when test="@dom='fenc'">Fechten</xsl:when>
     <xsl:when test="@dom='gast'">Gastronomie</xsl:when>
     <xsl:when test="@dom='geog'">Geografie</xsl:when>
     <xsl:when test="@dom='geol'">Geologie</xsl:when>
     <xsl:when test="@dom='hera'">Wappenkunde</xsl:when>
     <xsl:when test="@dom='hist'">historisch</xsl:when>
     <xsl:when test="@dom='info'">Informatik</xsl:when>
     <xsl:when test="@dom='lega'">Rechtswesen</xsl:when>
     <xsl:when test="@dom='ling'">Sprachwissenschaft</xsl:when>
     <xsl:when test="@dom='math'">Mathematik</xsl:when>
     <xsl:when test="@dom='medic'">Medizin</xsl:when>
     <xsl:when test="@dom='metall'">Metallurgie</xsl:when>
     <xsl:when test="@dom='meteo'">Meteorologie</xsl:when>
     <xsl:when test="@dom='mili'">militärisch</xsl:when>
     <xsl:when test="@dom='mine'">Mineralogie</xsl:when>
     <xsl:when test="@dom='mini'">Bergbau</xsl:when>
     <xsl:when test="@dom='moto'">Auto, Verkehr</xsl:when>
     <xsl:when test="@dom='music'">Musik</xsl:when>
     <xsl:when test="@dom='myth'">Mythologie</xsl:when>
     <xsl:when test="@dom='naut'">Schifffahrt</xsl:when>
     <xsl:when test="@dom='optic'">Optik</xsl:when>
     <xsl:when test="@dom='orni'">Vogelkunde</xsl:when>
     <xsl:when test="@dom='parl'">Parlament</xsl:when>
     <xsl:when test="@dom='peda'">Pädagogik</xsl:when>
     <xsl:when test="@dom='phar'">Pharmazie</xsl:when>
     <xsl:when test="@dom='phil'">Philosophie</xsl:when>
     <xsl:when test="@dom='phot'">Fotografie</xsl:when>
     <xsl:when test="@dom='phyo'">Physiologie</xsl:when>
     <xsl:when test="@dom='phys'">Physik</xsl:when>
     <xsl:when test="@dom='poli'">Politik</xsl:when>
     <xsl:when test="@dom='post'">Post</xsl:when>
     <xsl:when test="@dom='psyc'">Psychologie</xsl:when>
     <xsl:when test="@dom='rail'">Eisenbahn</xsl:when>
     <xsl:when test="@dom='rhet'">Rhetorik</xsl:when>
     <xsl:when test="@dom='tech'">Technik</xsl:when>
     <xsl:when test="@dom='tele'">Telefon</xsl:when>
     <xsl:when test="@dom='thea'">Theater</xsl:when>
     <xsl:when test="@dom='tv'">Fernsehen</xsl:when>
     <xsl:when test="@dom='typo'">Buchdruck</xsl:when>
     <xsl:when test="@dom='univ'">Universität</xsl:when>
     <xsl:when test="@dom='vete'">Veterinärmedizin</xsl:when>
     <xsl:when test="@dom='zool'">Zoologie</xsl:when>
     <xsl:otherwise>unbekannt</xsl:otherwise></xsl:choose>&#160;</span></xsl:if>
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
                <xsl:when test="@govern='abe'">Abessiv</xsl:when>
                <xsl:when test="@govern='abl'">Ablativ</xsl:when>
                <xsl:when test="@govern='abs'">Absolutiv</xsl:when>
                <xsl:when test="@govern='acc'">Akk.</xsl:when>
                <xsl:when test="@govern='ade'">Adessiv</xsl:when>
                <xsl:when test="@govern='dat'">Dat.</xsl:when>
                <xsl:when test="@govern='ela'">Elativ</xsl:when>
                <xsl:when test="@govern='erg'">Ergativ</xsl:when>
                <xsl:when test="@govern='gen'">Genitiv</xsl:when>
                <xsl:when test="@govern='ger'">Gerundium</xsl:when>
                <xsl:when test="@govern='ill'">Illativ</xsl:when>
                <xsl:when test="@govern='ind'">Indikativ</xsl:when>
                <xsl:when test="@govern='ine'">Inessiv</xsl:when>
                <xsl:when test="@govern='ins'">Instrumental</xsl:when>
                <xsl:when test="@govern='kon'">Konjunktiv</xsl:when>
                <xsl:when test="@govern='loc'">Lokativ</xsl:when>
                <xsl:when test="@govern='nom'">Nominativ</xsl:when>
                <xsl:when test="@govern='pro'">Prolativ</xsl:when>
                <xsl:when test="@govern='sub'">Subj.</xsl:when>
                <xsl:when test="@govern='ter'">Terminativ</xsl:when>
                <xsl:when test="@govern='voc'">Vokativ</xsl:when>
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
