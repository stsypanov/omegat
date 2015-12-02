Tento překlad vypracoval [Josef Molnár], copyright© [2015].

==============================================================================
  OmegaT 3.0, soubor Read Me / Čti mě

  1.  Informace o aplikaci OmegaT
  2.  Co je OmegaT?
  3.  Instalace programu OmegaT
  4.  Podpora projektu OmegaT
  5.  Máte s aplikací OmegaT problémy? Potřebujete pomoc?
  6.  Podrobnosti k vydání

==============================================================================
  1.  Informace o aplikaci OmegaT


Nejaktuálnější informace o aplikaci OmegaT naleznete na adrese:
      http://www.omegat.org/

Uživatelská podpora, v rámci uživatelské skupiny na Yahoo! (vícejazyčně), je zde možno prohledávat archívy i bez registrace:
     http://tech.groups.yahoo.com/group/OmegaT/

Požadavky a návrhy na zlepšení (anglicky), na stránce SourceForge:
     https://sourceforge.net/p/omegat/feature-requests/

Požadavky a návrhy na zlepšení (anglicky), na stránce SourceForge:
     https://sourceforge.net/p/omegat/bugs/

==============================================================================
  2.  Co je OmegaT?

OmegaT je nástroj pro překlad podporovaný počítačem (CAT – computer assisted translation). Jedná se o svobodný program, to znamená,
že za jeho používání nemusíte nic platit, dokonce ani při používání ve firmě, a můžete jej, při respektování uživatelské licence, upravovat anebo dále šířit.

Hlavní znaky programu:
  - schopnost běhu na libovolném operačním systému, který podporuje Javu
  - lze použít jakýkoliv platný soubor TMX jakožto překladovou referenci
  - flexibilní segmentace vět (za použití metody typu SRX)
  - vyhledávání v projektu a paměťových souborech překladu
  - vyhledávání souborů podporovaných formátů v libovolném adresáři 
  - nabízení přibližných překladů
  - program přehledně zvládne zpracovat projekty s komplexní adresářovou strukturou
  - podpora glosářů, tj. vlastních slovníků (kontrola terminologie) 
  - podpora průběžné kontroly pravopisu na základě OpenSource
  - podpora slovníků StarDict
  - podpora služby strojového překladu Google Překladač
  - jasná a ucelená dokumentace a návod k programu
  - lokalizace do spousty jazyků.

OmegaT bez problémů zvládá zpracovat následující formáty:

- formáty prostých textových souborů

  - text ASCII (.txt, atd.)
  - Kódovaný text (*.UTF8)
  - lokalizační balíčky Java resource bundles (.properties)
  - Soubory PO (.po)
  - Soubory INI (klíč=hodnota) (*.ini)
  - Soubory DTD (*.DTD)
  - Soubory DocuWiki (*.txt)
  - Soubory titulků SubRip (*.srt)
  - Magento CE Locale CSV (*.csv)

- různé formáty souborů obsahující tagy

  - OpenOffice.org / OpenDocument (*.odt, *.ott, *.ods, *.ots, *.odp, *.otp)
  - Microsoft Open XML (*.docx, *.xlsx, *.pptx)
  - (X)HTML (*.html, *.xhtml,*.xht)
  - HTML Help Compiler (*.hhc, *.hhk)
  - DocBook (*.xml)
  - jednojazyčné XLIFF (*.xlf, *.xliff, *.sdlxliff)
  - QuarkXPress CopyFlowGold (*.tag, *.xtg)
  - ResX files (*.resx)
  - Zdrojové soubory pro Android (.*xml)
  - LaTex (*.tex, *.latex)
  - Soubory Nápovědy (*.xml) a Příručky (*.hmxp)
  - Typo3 LocManager (*.xml)
  - WiX Localization (*.wxl)
  - Iceni Infix (*.xml)
  - Flash XML export (*.xml)
  - Wordfast TXML (*.txml)
  - Camtasia pro soubory Windows (*.camproj)
  - Visio (*.vxd)

Aplikaci OmegaT lze stejně dobře přizpůsobit i jiným formátům.

OmegaT automaticky zpracuje dokonce i ty nejkomplexnější struktury zdrojových adresářů, dostane se tak ke všem podporovaným souborům, a vytvoří cílový adresář s přesně stejnou strukturou, včetně kopií jakýchkoliv nepodporovaných souborů.

Návod pro rychlý start (Stručný úvodní průvodce) se zobrazí po spuštění programu OmegaT.

Uživatelská příručka je v balíčku, který jste právě stáhli, po spuštění aplikace OmegaT si ji můžete zobrazit z menu [Nápověda].

==============================================================================
 3. Instalace programu OmegaT

3.1 Obecné informace
Ke spuštění vyžaduje OmegaT ve vašem systému nainstalované prostředí Java Runtime Environment (JRE) verzi 1.6 nebo vyšší. Balíčky OmegaT, které obsahují běhové prostředí Java jsou dostupné a ušetří tak uživatelům problémy s výběrem, získáním prostředí a jeho instalací. 

Jestliže Javu již máte, jeden způsob instalace aktuální verze programu OmegaT je použití Java Web Start. 
V takovém případě stačí stáhnout následující soubor a spustit jej:

   http://omegat.sourceforge.net/webstart/OmegaT.jnlp

Při prvním spuštění bude nainstalováno nejen prostředí přesně pro váš počítač ale také samotná aplikace. Při dalším spuštění již nemusíte být online.

V závislosti na operačním systému se může objevit několik bezpečnostních výstrah. Certifikát odpovídá „PnS Concept“. 
Práva, která udělíte této verzi (která lze nazývat jako „neomezený přístup k počítači“) jsou identická s právy, která jste přidělili lokální verzi při její instalaci, což znamená: tato práva povolí přístup na hard disk počítače. Při pozdějším spuštění souboru OmegaT.jnlp dojde ke kontrole verze, pokud tedy jste online, a když bude nalezena novější verze, tak ji nainstaluje a aplikaci OmegaT spustí. 

Jiné způsoby stahování a instalace programu OmegaT si ukážeme později. 

Pro uživatele Windows a Linuxu: jestli jste si jisti, že váš systém má již vhodnou verzi JRE nainstalovánu, můžete instalovat verzi programu OmegaT bez JRE (to je naznačeno v samotném názvu verze, „Without_JRE“, tedy bez Javy). 
Pokud máte jakékoliv pochybnosti, doporučujeme použít verzi s JRE. Tato volba je bezpečná, dokonce i když už máte JRE ve vašem systému instalováno, tato verze s tímto již nainstalovaným prostředím nebude kolidovat.

Uživatelé Linuxu: OmegaT bude běžet i na otevřené implementaci balíčku Java, který je dodáván se spoustou Linuxových distribucí (například Ubuntu), ale mohou se vyskytnout chyby, zobrazovat problémy nebo některé vlastnosti nebudou dostupné. Proto doporučujeme, abyste si stáhli a instalovali buď JRE od Oracle nebo balíček OmegaT, který obsahuje JRE (archiv .tar.bz2), je to balíček s označením „Linux“. Když instalujete verzi Javy na úrovni systému, musíte se buď ujistit, že je správně přiřazena při startu, nebo ji explicitně vyvolat při spouštění OmegaT. Pokud si ale v Linuxu nejste natolik jistí, pak vám doporučujeme instalovat verzi OmegaT, kde je JRE obsaženo. Je to bezpečná volba, neboť „místní“ JRE se nebude rušit s jakýmkoliv jiným JRE, které je instalováno na vašem systému. 

Uživatelé Maců: JRE je na Mac OS X (před verzí OS X „Lion“) už instalováno. Uživatele verze Lion systém sám vyzve při prvním spuštění aplikace, která vyžaduje Javu a případně ji automaticky stáhne a instaluje.

Pro uživatele provozující Linux na systémech PowerPC: bude zapotřebí stáhnout JRE od IBM,
protože Sun neposkytuje JRE pro systémy PPC. V tomto případě stahujte z adresy:

    http://www.ibm.com/developerworks/java/jdk/linux/download.html 


3.2 Instalace
* uživatelé Windows: Jednoduše spusťte instalátor. Pokud budete chtít, instalátor může vytvořit zástupce pro spouštění programu OmegaT.

* Uživatelé Linuxu:
uložte archív do vhodného adresáře a archív zde rozbalte; OmegaT je připravena ke spuštění. Nicméně úhlednější a přívětivější instalaci spustíte použitím instalačního skriptu (linux-install.sh). Abyste spustili tento skript, otevřete okno terminálu (konzole), změňte aktivní adresář na adresář, který obsahuje OmegaT.jar a skript linux-install.sh, a spusťe skript příkazem ./linux-install.sh. 

* Uživatelé Maců:
zkopírujte archiv OmegaT.zip na vhodnou pozici a rozbalte jej zde, získáte tak adresář, který obsahuje soubor index HTML dokumentace a samotný soubor apliace OmegaT.app.

* Ostatní (např. Solaris, FreeBSD):
Aby šlo nainstalovat OmegaT, tak jednoduše vytvořte vhodný adresář pro tuto aplikaci. Zkopírujte archív OmegaT .zip nebo tar.bz2 do tohoto umístění a rozbalte jej zde.

3.3 Spouštění OmegaT
Spouštějte OmegaT následujícím způsobem.

* Uživatelé Windows:
Pokud jste během instalace vytvořili ikonu na ploše, stačí na ni dvakrát kliknout. Nebo dvojklik na soubor OmegaT.exe. Pokud ve vašem Správci souborů vidíte soubor OmegaT a ne OmegaT.exe (Průzkumník Windows), změňte nastavení tak, aby byly zobrazovány přípony souborů.

* Uživatelé Linuxu:
Pokud jste použili instalaci přes skript, mělo by stačit spouštět OmegaT přes zkratku Alt+F2 a pak zadat: omegat

* Mac uživatelé:
Dvojklik na soubor OmegaT.app.

* Z vašeho správce souborů (všechny systémy):
Dvojklik na soubor OmegaT.jar. Toto bude fungovat, jen když je ve vašem systému typ souboru .jar asociován s Javou.

* Z příkazového řádku (všechny systémy):
příkaz pro spuštění OmegaT je:

cd <adresář, kde je uložený soubor OmegaT.jar>

<jméno a cesta k souboru spustitelného Javou> -jar OmegaT.jar

(Soubor spustitelný Javou je soubor „java“ v Linuxu a „java.exe“ ve Windows.
Pokud je Java instalována na úrovni systému, a když je také zadaná cesta v příkazu pro spouštění, nemusíte vkládat úplnou cestu.)

Přizpůsobení spouštění OmegaT:

* uživatelé Windows: Instalátor může vytvořit zástupce v menu Start, na Ploše nebo v panelu Snadného spuštění. Stejně tak můžete ručně přetáhnout soubor OmegaT.exe do menu Start, na Plochu nebo na panel Snadné spuštění.

* uživatelé Linuxu:
Přívětivější způsob spouštění OmegaT nabízí skript Kaptain (omegat.kaptn), který je součástí staženého balíčku. Aby šlo tento skript spouštět, je nutno první instalovat Kaptain. Spouštěcí skript Kaptain pak můžete spouštět přes Alt+F2 omegat.kaptn

Více informací ke skriptu Kaptain a přidání položek do nabídky systému a k spouštěcím ikonám v Linuxu, najdete v nápovědě v tématu OmegaT pod Linuxem.

Uživatelé Mac:
Přetáhněte OmegaT.app na svůj Dock nebo do nástrojové lišty v okně Finder, a tak můžete OmegaT spouštět z různých lokací. Stejně tak můžete postupovat přes vyhledávací políčko Spotlight.

==============================================================================
 4. Jak se zapojit do projektu OmegaT

Jestli chcete přispívat při vývoji aplikace OmegaT, spojte se s vývojáři na adrese:
    http://lists.sourceforge.net/lists/listinfo/omegat-development

Jestli máte zájem o překlad uživatelského rozhraní OmegaT, uživatelské příručky nebo jiných příbuzných dokumentů, tak si nejprve přečtěte tyto informace:
      
      http://www.omegat.org/en/howtos/localizing_omegat.php

a přihlaste se do konference pro překladatele:
      https://lists.sourceforge.net/lists/listinfo/omegat-l10n

Když byste chtěli přispívat jinak, přihlaste se nejdříve do uživatelské skupiny na adrese:
      http://tech.groups.yahoo.com/group/omegat/

A seznamte se s děním okolo aplikace OmegaT ...

  Autorem původního projektu OmegaT je Keith Godfrey.
  Projektovým managerem OmegaT je Didier Briel.

Mezi dřívější přispěvatele patří:
(podle abecedy)

Do kódu přispěli
  Zoltan Bartko
  Volker Berlin
  Didier Briel
  Kim Bruning
  Alex Buloichik (vedoucí vývojář)
  Sandra Jean Chua
  Thomas Cordonnier
  Enrique Estévez Fernández
  Martin Fleurke  
  Wildrich Fourie
  Phillip Hall
  Jean-Christophe Helary
  Chihiro Hio
  Thomas Huriaux
  Hans-Peter Jacobs
  Kyle Katarn
  Piotr Kulik
  Ibai Lakunza Velasco
  Guido Leenders
  Aaron Madlon-Kay (manager integrace)
  Fabián Mandelbaum
  Manfred Martin
  Adiel Mittmann
  Hiroshi Miura 
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

Ostatní přispěvatelé
  Sabine Cretella
  Dmitri Gabinski
  Jean-Christophe Helary (správce lokalizace)
  Vincent Bidaux (správce dokumentace)
  Samuel Murray
  Marc Prior (webmaster)
  a mnoho, mnoho dalších velmi nápomocných lidí

(Pokud si myslíte, že jste významně přispěli k projektu OmegaT,
ale své jméno nevidíte v těchto záznamech, klidně nás kontaktujte.)

OmegaT používá následující knihovny:
  HTMLParser 1.6 od autorů Somik Raha, Derrick Oswald a další (LGPL Licence)
  VLDocking Framework 3.0.5-SNAPSHOT (LGPL Licence)
  Hunspell autora László Németh a dalších (LGPL Licence).
  JNA dodali Todd Fast, Timothy Wall a dalších (LGPL Licence)
  Swing-Layout 1.0.4 (Licence LGPL)
  Jmyspell 2.1.4 (Licence LGPL)
  SVNKit 1.8.5 (Licence TMate)
  Sequence Library (Licence Sequence Library)
  ANTLR 3.4 (Licence ANTLR 3)
  SQLJet 1.1.10 (GPL v2)
  JGit (Licence Eclipse Distribution)
  JSch (Licence JSch)
  Base64 (public domain)
  Diff (GPL)
  trilead-ssh2-1.0.0-build217 (Trilead SSH license)
  lucene-*.jar (Apache License 2.0)
  Anglické tokenizery (org.omegat.tokenizer.SnowballEnglishTokenizer a
org.omegat.tokenizer.LuceneEnglishTokenizer) používají stop slova původně z Okapi (http://okapi.sourceforge.net) (LGPL license).
  tinysegmenter.jar (Modified BSD license)
  commons-*.jar (Apache License 2.0)
  jWordSplitter (Apache License 2.0)
  LanguageTool.jar (LGPL license)
  morfologik-*.jar (Morfologik license)
  segment-1.4.1.jar (Segment license)
  pdfbox-app-1.8.1.jar (Apache License 2.0)
  KoreanAnalyzer-3x-120223.jar (Apache License 2.0)
  SuperTMXMerge-for_OmegaT.jar (LGPL license)
  groovy-all-2.2.2.jar (Apache Licence 2.0)
  slf4j (MIT License)
  juniversalchardet-1.0.3.jar (GPL v2)
  DictZip from JDictd (GPL v2)

==============================================================================
 5.  Máte s aplikací OmegaT problémy? Potřebujete pomoc?

Než ohlásíte jakoukoliv chybu tak se ujistěte, že jste si důkladně prošli dokumentaci. To co vidíte může být vlastností OmegaT, kterou jste právě objevili. Když se podíváte na log (protokol) OmegaT a vidíte slova jako „Error“ (Chyba), „Warning“ (Upozornění), „Exception“ (Výjimka) nebo „died unexpectedly“ (neočekávané ukončení), tak jste pravděpodobně narazili na opravdový problém (soubor log.txt se nachází v adresáři předvoleb uživatele, jeho umístění naleznete v příručce).

Další věc, kterou učiníte, je ověřit si to co jste nalezli i u ostatních uživatelů, aby jste se ujistili, zda to samé už někdy nebylo hlášeno. Můžete si to také ověřit na stránce pro hlášení chyb na SourceForge. Jedině pokud jste si jisti, že jste první, kdo našel nějakou
zopakovatelnou sekvenci událostí, která spustila něco, co se nemělo stát,
teprve pak byste měli podat hlášení o chybě.

Každé dobré hlášení o chybě potřebuje přesně tři věci.
  - Kroky, které je zapotřebí zopakovat
  - co jste čekali, že uvidíte, a
  - co jste uviděli místo toho

Můžete přidat kopie souborů, části logu, snímky obrazovky, prostě cokoliv, o čem si myslíte, že pomůže vývojářům nalézt a opravit vámi hlášenou chybu.

Archívy uživatelské skupiny můžete prohlížet na adrese:
     http://tech.groups.yahoo.com/group/OmegaT/

Prohlížet stránku hlášení o chybách a v případě potřeby přidat nové hlášení můžete zde:
     https://sourceforge.net/p/omegat/bugs/

Abyste byli informování o tom, co se děje s vaším hlášením o chybě, můžete se zaregistrovat jako uživatel Source Forge.

==============================================================================
6.   Podrobnosti k vydání

Podrobné informace o změnách v tomto a všech předcházejících vydáních naleznete v souborech ‚changes.txt‛.


==============================================================================
