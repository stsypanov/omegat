@@TRANSLATION_NOTICE@@

==============================================================================
  ==============================================================================
OmegaT 3.0, Lees Mij-bestand

  1.  Informatie over OmegaT
  2.  Wat is OmegaT?
  3.  Installeren van OmegaT
  4.  Deelnemen aan OmegaT
  5.  Heeft u problemen met OmegaT ? Heeft u hulp nodig?
  6.  Uitgavedetails

==============================================================================
  1.  Informatie over OmegaT


De meest recente informatie over OmegaT is te vinden op
      http://www.omegat.org/

User support, at the Yahoo user group (multilingual), where the archives are
searchable without subscription:
     http://tech.groups.yahoo.com/group/OmegaT/

Verzoeken tot verbeteringen (in het Engels) op de SourceForge-website:
     https://sourceforge.net/p/omegat/feature-requests/

Foutrapportages (in het Engels) op de SourceForge-website:
     https://sourceforge.net/p/omegat/bugs/

==============================================================================
  2.  Wat is OmegaT?

OmegaT is een computer-assisterend vertaalprogramma (CAT). Het is vrij, hetgeen betekent 
dat u niets hoeft te betalen om het te gebruiken, zelfs niet voor professioneel gebruik 
en dat het u vrij staat om het aan te passen en opnieuw te distribueren, zo lang als u zich houdt aan 
de gebruikerslicentie.

De belangrijkste mogelijkheden van OmegaT zijn:
  - de mogelijkheid om te worden uitgevoerd op elk besturingssysteem dat Java ondersteunt
  - gebruiken van elk geldig TMX-bestand als een verwijzing voor de vertaling
  - flexibele zinsegmentatie (met behulp van een SRX-achtige methode)
  - zoekacties in het project en de vertaalgeheugens die als verwijzingen worden gebruikt
  - zoekacties in bestanden in ondersteunde formaten in elke map 
  - fuzzy overeenkomsten
  - slimme afhandeling van projecten inclusief complexe hiërarchieën van mappen
  - ondersteuning voor woordenlijsten (controle van terminologie) 
  - ondersteuning voor OpenSource spellingscontrole terwijl u werkt
  - ondersteuning voor woordenboeken van StarDict
  - ondersteuning voor de services van machinevertaling van Google Translate
  - duidelijke en uitgebreide documentatie en handleiding
  - lokalisatie in een aantal talen.

OmegaT ondersteunt direct de volgende bestandsformaten:

- Platte tekst-bestandsformaten

  - ASCII tekst (.txt, etc.)
  - Gecodeerde tekst (*.UTF8)
  - Java bronbundels (.properties)
  - PO-bestanden (*.po)
  - INI (sleutel=waarde) bestanden (*.ini)
  - DTD-bestanden (*.DTD)
  - DocuWiki-bestanden (*.txt)
  - SubRip titelbestanden (*.srt)
  - Magento CE Locale CSV (*.csv)

- Getagde tekst bestandsformaten

  - OpenOffice.org / OpenDocument (*.odt, *.ott, *.ods, *.ots, *.odp, *.otp)
  - Microsoft Open XML (*.docx, *.xlsx, *.pptx)
  - (X)HTML (*.html, *.xhtml,*.xht)
  - HTML Help Compiler (*.hhc, *.hhk)
  - DocBook (*.xml)
  - ééntalige XLIFF (*.xlf, *.xliff, *.sdlxliff)
  - QuarkXPress CopyFlowGold (*.tag, *.xtg)
  - ResX-bestanden (*.resx)
  - Android-bronbestanden (*.xml)
  - LaTex (*.tex, *.latex)
  - Help- (*.xml) en Manual- (*.hmxp) bestanden
  - Typo3 LocManager (*.xml)
  - WiX Localization (*.wxl)
  - Iceni Infix (*.xml)
  - Flash XML exporteren (*.xml)
  - Wordfast TXML (*.txml)
  - Camtasia voor Windows (*.camproj)
  - Visio (*.vxd)

OmegaT kan worden aangepast om ook andere bestandsindelingen te ondersteunen.

OmegaT zal automatisch zelfs de meest complexe hiërarchieën voor de bronmap parsen
, om toegang te verkrijgen tot alle ondersteunde bestanden en een doelmap te produceren
met exact dezelfde structuur, inclusief kopieën van niet ondersteunde bestanden.

Voor een handleiding om snel te kunnen beginnen, start OmegaT en lees de weergegeven handleiding Snel 
starten.

De gebruikershandleiding zit in het pakket dat u zojuist heeft gedownload. U kunt die starten vanuit
het menu [Help] na het starten van OmegaT.

==============================================================================
 3. Installeren van OmegaT

3.1 Algemeen
OmegaT vereist dat een Java Runtime Environment (JRE) versie 
1.5 of hoger is geïnstalleerd op uw systeem om uitgevoerd te kunnen worden. OmegaT pakketten die de Java Runtime Environment bevatten
zijn nu beschikbaar om gebruikers de moeite van het 
selecteren, verkrijgen en installeren te besparen. 

Als u al Java hebt is één manier om de huidige versie van OmegaT te installeren  
het gebruiken van Java Web Start. 
Download voor dit doel het volgende bestand en voer het uit:

   http://omegat.sourceforge.net/webstart/OmegaT.jnlp

Het zal de juiste omgeving voor uw computer installeren en de toepassing 
zelf bij de eerste keer dat het wordt uitgevoerd. Latere aanroepen behoeven niet online te worden gedaan.

Gedurende de installatie, afhankelijk van uw besturingssysteem, zou u verschillende 
beveiligingswaarschuwingen kunnen ontvangen. Het certificaat is van "PnS Concept". 
De rechten die u aan deze versie geeft (welke kunnen lijken op 
"onbeperkte toegang tot de computer") zijn identiek aan de rechten die u geeft 
aan de lokale versie, zoals geïnstalleerd door een procedure, later beschreven: zij geven 
toegang tot de harde schijf van de computer. Opvolgende klikken op OmegaT.jnlp 
zal leiden tot het controleren op upgrades, als u online bent, ze installeren als ze er zijn, 
en dan OmegaT starten. 

De alternatieve manieren en mogelijkheden voor het downloaden en installeren van OmegaT worden
hieronder weergegeven. 

Windows- en Linuxgebruikers: als u er van overtuigd bent dat op uw systeem al 
een passende versie van de JRE is geïnstalleerd, dan kunt u de versie van OmegaT 
zonder de JRE installeren (dit wordt aangegeven in de naam van de versie, "Without_JRE"). 
Als u twijfelt raden wij u aan om de versie die wordt geleverd met 
de JRE te gebruiken. Dat is veilig omdat, zelfs als er al een JRE op uw systeem geïnstalleerd is, 
deze versie die niet zal beïnvloeden.

Linuxgebruikers: 
OmegaT kan worden uitgevoerd op de open-implementatie bron Java 
die is verpakt in vele Linuxdistributies (bijvoorbeeld Ubuntu), maar het kan zijn
dat u problemen, problemen met de weergave of ontbrekende mogelijkheden ervaart. We raden daarom aan om
ofwel de Oracle Java Runtime Environment (JRE)  
of het OmegaT-pakket, gebundeld met JRE (de .tar.bz2-bundel gemarkeerd 
"Linux") te downloaden en te installeren. Indien u een versie van Java installeert op systeemniveau moet u 
er ofwel voor zorgen dat het is vermeld in uw pad voor opstarten, of het expliciet aanroepen bij het opstarten van 
Omegat. Indien u niet echt bekend bent met Linux raden we u daarom aan 
om een versie van OmegaT te installeren waarin de JRE is opgenomen. Dit is veilig, 
omdat deze "lokale" JRE geen problemen zal veroorzaken met andere geïnstalleerde JRE's 
op uw systeem.

Macgebruikers 
De JRE is al geïnstalleerd op Mac OS X vóór Mac OS X 10.7  
(Lion). Liongebruikers zullen er door het systeem naar worden gevraagd als zij voor de eerste keer 
een toepassing starten die Java vereist en het systeem zal het eventueel 
automatisch downloaden en installeren.

Linux op PowerPC-systemen:  
Gebruikers moeten IBM's JRE downloaden omdat Oracle  
geen JRE voor PPC-systemen levert. Download in dat geval vanaf:

    http://www.ibm.com/developerworks/java/jdk/linux/download.html 


3.2 Installeren
* Windows-gebruikers: 
Start eenvoudigweg het installatieprogramma. Het installatieprogramma kan snelkoppelingen maken om 
OmegaT te starten, als u dat wilt.

* Linuxgebruikers:
Plaats het archief in een toepasselijke map en pak het uit; 
OmegaT is dan gereed om te worden opgestart. U kunt echter een nettere en meer gebruikersvriendelijker 
installatie krijgen door het installatiescript te gebruiken (linux-install.sh). Open, om dit script te gebruiken, 
een terminalvenster (console), wijzig de map 
naar de map die OmegaT.jar bevat en het linux-install.sh script 
en voer het script uit met ./linux-install.sh.

* Macgebruikers:
Kopieer het archief OmegaT.zip naar een toepasselijke locatie en pak het daar uit 
om een map te krijgen die een indexbestand voor HTMLdocumentatie bevat en 
het toepassingsbestand OmegaT.app.

* Anderen (bijv. Solaris, FreeBSD: 
Maak eenvoudigweg een toepasselijke map voor OmegaT om in te installeren. Kopieer het 
OmegaT zip of tar.bz2 archief naar deze map en pak het daar uit.

3.3 OmegaT starten
Start OmegaT als volgt op.

* Windows-gebruikers: 
Als u, gedurende de installatie, een snelkoppeling op het bureaublad hebt gemaakt, 
dubbelklik dan op die snelkoppeling. Dubbelklik, als alternatief, op het bestand 
OmegaT.exe. Als u het bestand OmegaT wel in uw bestandsbeheer (Windows Verkenner) kunt zien 
maar niet OmegaT.exe, wijzig dan de instellingen zodat 
bestandsextensies worden weergegeven.

* Linuxgebruikers:
Indien u het meegeleverde installatiescript gebruikte, zou u OmegaT moeten kunnen opstarten met:
Alt+F2
en dan:
omegat

* Macgebruikers:
Dubbelklik op het bestand OmegaT.app.

* Vanuit uw bestandsbeheerder (alle systemen):
Dubbelklik op het bestand OmegaT.jar. Dit zal alleen werken als het bestandstype .jar
op uw systeem is gekoppeld aan Java.

* Vanaf de opdrachtregel (alle systemen):  
De opdracht om OmegaT te starten is:

cd <map waar het bestand OmegaT.jar is opgeslagen>

<naam en pad van het uitvoerbare Java-bestand> -jar OmegaT.jar

(Het uitvoerbare Java-bestand is het bestand java op Linux en java.exe op Windows.
Indien Java is geïnstalleerd op systeemniveau en in het pad voor de opdracht staat, 
hoeft niet het volledige pad te worden ingevoerd.)

Het starten van OmegaT aanpassen:

* Windows-gebruikers: 
Het installatieprogramma kan voor u snelkoppelingen maken in het menu Start, 
op het bureaublad en in het gebied voor snel starten. U kunt ook handmatig het bestand OmegaT.exe slepen 
naar het startmenu, op het bureaublad en in het gebied voor snel starten
om het van daaruit te koppelen.

* Linuxgebruikers:
Voor een meer gebruikersvriendelijker manier om OmegaT op te starten 
kunt u gebruik maken van het meegeleverde Kaptain-script (omegat.kaptn). U moet eerst Kaptain installeren 
om dit script te kunnen gebruiken. U kunt dan het Kaptain opstartscript starten met
Alt+F2
omegat.kaptn

Voor meer informatie over het Kaptain-script en het toevoegen van menuitems en 
pictogrammen om op te starten op Linux, bekijk de OmegaT on Linux HowTo.

Macgebruikers
Sleep OmegaT.app naar uw dock of naar de werkbalk van een Finder-venster 
om het vanaf elke locatie te kunnen starten. U kunt het ook aanroepen 
in het zoekveld van Spotlight.

==============================================================================
 4. Deelnemen aan het OmegaT-project

Om deel te nemen aan de ontwikkeling van OmegaT neemt u contact op met de ontwikkelaars via:
    http://lists.sourceforge.net/lists/listinfo/omegat-development

Lees voor het vertalen van OmegaT's gebruikersinterface, gebruikershandleiding of andere gerelateerde
documenten:
      
      http://www.omegat.org/en/howtos/localizing_omegat.php

En abonneert u zich op de lijst van vertalers:
      https://lists.sourceforge.net/lists/listinfo/omegat-l10n

Voor het op andere manieren bijdragen abonneert u zich eerst op de gebruikersgroep op:
      http://tech.groups.yahoo.com/group/Omegat/

en krijg daar een indruk van wat er gaande is in de wereld van OmegaT...

  OmegaT is van origine het werk van Keith Godfrey.
  Didier Briel is de projectmanager voor OmegaT.

Eerdere bijdragen van:
(alfabetische volgorde)

Code is bijgedragen door
  Zoltan Bartko
  Volker Berlin
  Didier Briel
  Kim Bruning
  Alex Buloichik (hoofd ontwikkeling)
  Sandra Jean Chua
  Thomas Cordonnier
  Enrique Estévez Fernández
  Martin Fleurke  
  Wildrich Fourie
  Phillip Hall
  Jean-Christophe Helary
  Thomas Huriaux
  Hans-Peter Jacobs
  Kyle Katarn
  Piotr Kulik
  Ibai Lakunza Velasco
  Guido Leenders
  Aaron Madlon-Kay (integratiemanager)
  Fabián Mandelbaum
  Manfred Martin
  Adiel Mittmann
  John Moran
  Maxym Mykhalchuk 
  Arno Peters
  Henry Pijffers 
  Briac Pilpré
  Tiago Saboga
  Andrzej Sawuła
  Benjamin Siband
  Yu Tang
  Rashid Umarov  
  Antonio Vilei
  Ilia Vinogradov
  Martin Wunderlich
  Michael Zakharov

Andere bijdragen door
  Sabine Cretella
  Dmitri Gabinski
  Jean-Christophe Helary (manager lokalisatie)
  Vincent Bidaux (documentatie-manager)
  Samuel Murray
  Marc Prior (webmaster)
  en vele, vele andere zeer behulpzame mensen

(Als u denkt dat u een significante bijdrage heeft geleverd aan het OmegaT-project, 
maar ziet u uw naam niet op deze lijst, neem dan alstublieft contact met ons op.)

OmegaT gebruikt de volgende bibliotheken:
  HTMLParser 1.6 van Somik Raha, Derrick Oswald en anderen (LGPL License)
  VLDocking Framework 3.0.5-SNAPSHOT (LGPL License)
  Hunspell van László Németh en anderen (LGPL License)
  JNA van Todd Fast, Timothy Wall en anderen (LGPL License)
  Swing-Layout 1.0.4 (LGPL License)
  Jmyspell 2.1.4 (LGPL License)
  SVNKit 1.8.5 (TMate License)
  Sequence Library (Sequence Library License)
  ANTLR 3.4 (ANTLR 3 licentie)
  SQLJet 1.1.10 (GPL v2)
  JGit (Eclipse Distribution License)
  JSch (JSch License)
  Base64 (publieke domein)
  Diff (GPL)
  trilead-ssh2-1.0.0-build217 (Trilead SSH-licentie)
  lucene-*.jar (Apache License 2.0)
  De Engelse tokenizers (org.omegat.tokenizer.SnowballEnglishTokenizer en
org.omegat.tokenizer.LuceneEnglishTokenizer) gebruiken originele stopwoorden van
Okapi (http://okapi.sourceforge.net) (LGPL-licentie)
  tinysegmenter.jar (Aangepaste BSD-licentie)
  commons-*.jar (Apache License 2.0)
  jWordSplitter (Apache License 2.0)
  LanguageTool.jar (LGPL-licentie)
  morfologik-*.jar (Morfologik licentie)
  segment-1.4.1.jar (Segment-licentie)
  pdfbox-app-1.8.1.jar (Apache License 2.0)
  KoreanAnalyzer-3x-120223.jar (Apache License 2.0)
  SuperTMXMerge-for_OmegaT.jar (LGPL License)
  groovy-all-2.2.2.jar (Apache License 2.0)
  slf4j (MIT License)
  juniversalchardet-1.0.3.jar (GPL v2)

==============================================================================
 5.  Heeft u problemen met OmegaT ? Heeft u hulp nodig?

Overtuig u ervan dat u de documentatie zorgvuldig
heeft doorgenomen voordat u een probleem rapporteert. Wat u meemaakt kan in feite een karakteristiek
iets van OmegaT zijn dat u net heeft ontdekt. Als u het OmegaT-log controleert en u ziet woorden zoals
"Fout", "Waarschuwing", "Uitzondering" of "onverwacht afgebroken" dan heeft u waarschijnlijk
een echt probleem ontdekt (het bestand log.txt is geplaatst in de map met
gebruikersvoorkeuren. Zie de handleiding voor de juiste locatie).

Het volgende wat u zou moeten doen is dat wat u gevonden heeft bevestigd te krijgen 
van andere gebruikers om er zeker van te zijn dat dit al niet reeds gerapporteerd is. U kunt de foutenrapport ook verifiëren op de pagina
van SourceForge. Alleen als u er zeker van bent dat u de eerste bent die een opnieuw
te produceren reeks van gebeurtenissen heeft ontdekt die leidt tot iets wat niet zou moeten gebeuren
zou u een foutenrapport moeten indienen.

Elk goed foutenrapport heeft exact drie dingen nodig.
  - Stappen om het te reproduceren,
  - Wat u verwachtte te zien, en
  - Wat u in plaats daarvan zag.

U kunt kopieën van bestanden, delen uit het logbestand, schermafdrukken of iets waarvan
u denkt dat dat de ontwikkelaars zal helpen bij het oplossen van uw probleem, toevoegen.

Bladeren door de archieven van de gebruikersgroep kunt u via:
     http://tech.groups.yahoo.com/group/OmegaT/

Bladeren door de pagina met foutrapportages en, indien nodig, indienen van een nieuw foutenrapport via:
     https://sourceforge.net/p/omegat/bugs/

U wilt zich misschien registreren als gebruiker van SourceForge
om te kunnen zien wat er met uw foutenrapport gebeurd.

==============================================================================
6.   Uitgavedetails

Bekijk het bestand 'changes.txt' voor gedetailleerde informatie over wijzigingen in
deze en alle eerdere uitgaven.


==============================================================================
