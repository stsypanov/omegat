/*
 * TMXMerger - Merges two or more TMX files
 * Copyright(c) 2005-2010, Henry Pijffers (henry.pijffers@saxnot.com)
 *
 * This program is licensed to you under the terms of version 2 or later of
 * the GNU General Public License (the "GPL"), as published by the Free Software
 * Foundation.
 */

package org.omegat.tools.tmx.merge;

import java.io.*;
import java.util.*;
import java.text.MessageFormat;

import org.xml.sax.*;

/**
 * Loads TMX (Translation Memory Exchange) files, all versions
 *
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 */
public class TMX extends org.xml.sax.helpers.DefaultHandler {
	/*
     * FIX: The callback methods defined by DefaultHandler are
     *      defined public, but shouldn't be part of the TMX
     *      interface. Move all methods defined by DefaultHandler
     *      to an inline or separate class. This class can then
     *      either directly access TMX's private members, or
     *      use the public interface to add TUs.
     */
     
    /*
     * FIX: There may come other types of input besides files.
     *      Create an InputSource class, and subclasses like
     *      SAX InputSource has. Use this InputSource class
     *      in place of File/String parameters. This also
     *      reduces the number of methods.
     */
     
    /*
     * FIX: Move logging to class that uses TMX. This class should
     *      *not* emit any log statements. It may be used in
     *      different places, so it can't know what to log and
     *      how to log it. It should however indicate problems in
     *      a precise as possible manner.
     */

	/**
	 * Creates a new TMX. When load() is called, the source
	 * language is taken from the TMX file.
	 */
	public TMX() {
		super();
		initialize(null, null);
	}

	/**
	 * Creates a new TMX, using the source language as specified by the TMX file.
	 *
	 * @param file -- TMX file to be loaded.
	 */
	public TMX(File file) throws IOException {
		super();
		initialize(null, null);
		load(file, true, false);
	}

	/**
	 * Creates a new TMX, with a specific source language.
	 *
	 * @param sourceLanguage -- Language of source segments, in LL(-CC) notation
	 */
	public TMX(String sourceLanguage) {
		super();
		initialize(sourceLanguage, null);
	}

	/**
	 * Creates a new TMX, with a specific source language.
	 *
	 * @param file           -- TMX file to be loaded.
	 * @param sourceLanguage -- Language of source segments, in LL(-CC) notation
	 */
	public TMX(File file, String sourceLanguage) throws IOException {
		super();
		initialize(sourceLanguage, null);
		load(file, false, false);
	}

	/**
	 * Creates a new TMX, with a specific source and target language.
	 *
	 * @param sourceLanguage -- Language of source segments, in LL(-CC) notation
	 * @param targetLanguage -- Language of target segments, in LL(-CC) notation
	 */
	public TMX(String sourceLanguage,
			   String targetLanguage) {
		super();
		initialize(sourceLanguage, targetLanguage);
	}

	/**
	 * Creates a new TMX, with a specific source and target language.
	 *
	 * @param file           -- TMX file to be loaded.
	 * @param sourceLanguage -- Language of source segments, in LL(-CC) notation
	 * @param targetLanguage -- Language of target segments, in LL(-CC) notation
	 */
	public TMX(File file,
			   String sourceLanguage,
			   String targetLanguage) throws IOException {
		super();
		initialize(sourceLanguage, targetLanguage);
		load(file, false, false);
	}

	/**
	 * Initializes a TMX object
	 */
	private void initialize(String sourceLanguage, String targetLanguage) {
		this.sourceLanguage = sourceLanguage;
		this.targetLanguage = targetLanguage != null ? targetLanguage : "";
		tus = new HashMap();
	}

	/**
	 * The TMX's source language in LL(-CC) notation,
	 * where LL = language code, and CC = country code.
	 * Set by passing it to a constructor, or when
	 * loading a TMX file, if not set earlier.
	 */
	public String getSourceLanguage() {
		return sourceLanguage;
	}

	/**
	 * The TMX's (main) target language in LL(-CC) notation,
	 * where LL = language code, and CC = country code.
	 */
	public String getTargetLanguage() {
		return targetLanguage;
	}

	/**
	 * The source language as specified in the TMX file.
	 */
	public String getTMXSourceLanguage() {
		return tmxSourceLanguage;
	}

	/**
	 * The name of the tool that created the TMX file.
	 */
	public String getCreationTool() {
		return creationTool;
	}

	/**
	 * Sets the name of the tool that created the TMX.
	 */
	public void setCreationTool(String creationTool) {
		this.creationTool = creationTool;
	}

	/**
	 * The version of the tool that created the TMX file.
	 */
	public String getCreationToolVersion() {
		return creationToolVersion;
	}

	/**
	 * Sets the version of the tool that created the TMX.
	 */
	public void setCreationToolVersion() {
		this.creationToolVersion = creationToolVersion;
	}

	/**
	 * The segmentation type as specified in the TMX file.
	 */
	public String getSegmentationType() {
		return segmentationType;
	}

	/**
	 * Sets the TMX's segmentation type.
	 */
	public void setSegmentationType() {
		this.segmentationType = segmentationType;
	}

	/**
	 * Loads the specified TMX file.
	 * <p/>
	 * If a specific source language is not set, languages
	 * are matched against the TMX source language.
	 *
	 * @param filename -- The name of the TMX file to load.
	 */
	public void load(String filename) throws IOException {
		load(new File(filename), false, false);
	}

	/**
	 * Loads the specified TMX file.
	 * <p/>
	 * If a specific source language is not set, languages
	 * are matched against the TMX source language.
	 *
	 * @param file -- TMX file to be loaded.
	 */
	public void load(File file) throws IOException {
		load(file, false, false);
	}

	/**
	 * Loads the specified TMX file.
	 *
	 * @param filename         -- The name of the TMX file to load.
	 * @param useTMXSourceLang -- If true, the TUV language will also be matched
	 *                         against the TMX source language, if no usable
	 *                         source TUV can be determined otherwise.
	 */
	public void load(String filename, boolean useTMXSourceLang) throws IOException {
		load(new File(filename), useTMXSourceLang, false);
	}

	/**
	 * Loads the specified TMX file.
	 *
	 * @param file             -- TMX file to be loaded.
	 * @param useTMXSourceLang -- If true, the TUV language will also be matched
	 *                         against the TMX source language, if no usable
	 *                         source TUV can be determined otherwise.
	 */
	public void load(File file, boolean useTMXSourceLang) throws IOException {
		load(file, useTMXSourceLang, false);
	}

	/**
	 * Loads the specified TMX file.
	 *
	 * @param file             -- TMX file to be loaded.
	 * @param useTMXSourceLang -- If true, the TUV language will also be matched
	 *                         against the TMX source language, if no usable
	 *                         source TUV can be determined otherwise.
	 * @param merge            -- If true, TUs and TUVs are only added if they contain
	 *                         new text. Current TUs are not deleted.
	 */
	private void load(File file, boolean useTMXSourceLang, boolean merge) throws IOException {
        /*
         * IMPLEMENTATION NOTE:
         * The parser makes callbacks to the TMXReader, to the methods
         * warning, error, fatalError, startDocument, endDocument,
         * startElement, endElement, characters, ignorableWhiteSpace,
         * and resolveEntity. Together these methods implement the
         * parsing of the TMX file.
         */

		// initialise variables needed for parsing of the TMX file
		this.useTMXSourceLang = useTMXSourceLang;
		merging = merge;
		headerParsed = false;
		inTU = false;
		inTUV = false;
		inSegment = false;
		currentElement = new Stack();
		currentSegment = new Stack<>();
		currentTU = null;
		currentTUV = null;
		segmentBuffers = new ArrayList<>();

		// clear the TU list and TMX attributes, if we're not merging
		if (!merging) {
			tus.clear();
			tmxSourceLanguage = null;
			creationTool = null;
			creationToolVersion = null;
			segmentationType = null;
		}

		// parse the TMX file
		try {
			// log the parsing attempt
			System.out.println("Reading TMX file " + file.getCanonicalPath());

			// create a new SAX parser factory
			javax.xml.parsers.SAXParserFactory parserFactory =
					javax.xml.parsers.SAXParserFactory.newInstance();

			// configure the factory
			parserFactory.setValidating(false); // skips TMX validity checking

			// create a new SAX parser
			javax.xml.parsers.SAXParser parser = parserFactory.newSAXParser();

			// make this TMX reader the default entity resolver for the parser,
			// so we can handle DTD declarations ourselves
			parser.getXMLReader().setEntityResolver(this);

			// parse the TM, provide the current TMX reader as notification handler
			parser.parse(file, this);

			// if no source could be found for 1 or more TUs, log this fact
			if (sourceNotFound)
				System.err.println("Warning: Source segment could not be located for certain translation units. These units have been skipped.");

			// log the fact that parsing is done
			System.out.println("Reading of TMX file complete");
		} catch (javax.xml.parsers.ParserConfigurationException exception) {
			System.err.println("Exception while parsing:\n" + exception.getLocalizedMessage());
			exception.printStackTrace(System.err);
		} catch (SAXException exception) {
			System.err.println("Exception while parsing:\n" + exception.getLocalizedMessage());
			exception.printStackTrace(System.err);
		}

		// deallocate temp storage
		currentElement = null;
		currentSegment = null;
		currentTU = null;
		currentTUV = null;
		segmentBuffers = null;
	}

	/**
	 * Merges in the specified TMX file.
	 *
	 * @param filename -- The name of the TMX file to merge.
	 */
	public void merge(String filename) throws IOException {
		load(new File(filename), false, true);
	}

	/**
	 * Merges in the specified TMX file.
	 *
	 * @param file -- The TMX file to merge.
	 */
	public void merge(File file) throws IOException {
		load(file, false, true);
	}

	/**
	 * Merges in the specified TMX.
	 *
	 * @param tmx -- The TMX to merge.
	 */
	public void merge(TMX tmx) {
		// simply add all the TMX's TUs
		// the method addTU will do the filtering
		for (Iterator i = tmx.tus.values().iterator(); i.hasNext(); )
			addTU((TU) i.next());
	}

	/**
	 * Saves the TMX to the specified file.
	 *
	 * @param filename -- Name of the file to save to.
	 */
	public void save(String filename) throws IOException {
		save(new File(filename));
	}

	/**
	 * Saves the TMX to the specified file.
	 *
	 * @param file -- The file to save to.
	 */
	public void save(File file) throws IOException {
		System.out.println("");
		System.out.println("Writing TMX to file" + file.getCanonicalPath());

		// create a new writer
		FileOutputStream os = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
		PrintWriter out = new PrintWriter(osw);

		// write the TMX header
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");      // NOI18N
		out.println();
		out.println("<!DOCTYPE tmx SYSTEM \"tmx14.dtd\">");             // NOI18N
		out.println();
		out.println("<tmx version=\"1.4\">");                           // NOI18N
		out.println("   <header");                                      // NOI18N
		out.println("      creationtool=\"TMX Merger\"");               // NOI18N
		out.println("      creationtoolversion=\"1.1\"");               // NOI18N
		out.println("      segtype=\"" + segmentationType + "\"");      // NOI18N
		out.println("      adminlang=\"EN-US\"");                       // NOI18N
		out.println("      srclang=\"" + sourceLanguage + "\"");        // NOI18N
		out.println("   >");                                            // NOI18N
		out.println("   </header>");                                    // NOI18N
		out.println("   <body>");                                       // NOI18N

		// write the TUs
		for (Iterator iTU = tus.values().iterator(); iTU.hasNext(); ) {
			// get the next TU
			TU tu = (TU) iTU.next();

			// write the TU start tag
			out.println("      <tu>");

			// if there is more than one target, add a property to indicate this
			if (tu.tuvs.size() > 2) {
				out.println("         <prop type=\"x-merge-mt\">true</prop>");
				out.println();
			}

			// write the TUVs
			for (Iterator iTUV = tu.tuvs.iterator(); iTUV.hasNext(); ) {
				// get the next TUV
				TUV tuv = (TUV) iTUV.next();

				// write the TUV
				out.print("         <tuv xml:lang=\"" + tuv.language + "\"");
				if (tuv.changeDate != null)
					out.print(" changedate=\"" + tuv.changeDate + "\"");
				if (tuv.changeID != null)
					out.print(" changeid=\"" + tuv.changeID + "\"");
				out.println(">");
				out.println("            <seg>" + toValidXML(tuv.text) + "</seg>");
				out.println("         </tuv>");
				if (iTUV.hasNext())
					out.println();
			}

			// write the TU end tag
			out.println("      </tu>");
			if (iTU.hasNext())
				out.println();
		}

		// write the TMX footer
		out.println("   </body>");
		out.println("</tmx>");

		// flush the output stream
		out.flush();

		System.out.println("Writing of TMX file complete");
	}

	/**
	 * Adds a translation unit.
	 * <p/>
	 * If a TU with the (exact) same source text
	 * already exists, their contents will be merged.
	 *
	 * @param tu -- The translation unit to add.
	 */
	public void addTU(TU tu) {
		if (tu.source == null)
			return;

		// if no TU with the same source text exists,
		// simply add the TU and return
		TU existingTU = (TU) tus.get(tu.source.text);
		if (existingTU == null) {
			tus.put(tu.source.text, tu);
			return;
		}

		// Another TU with the same source text already exists.
		// Add the TUVs of the new TU to the TUV list of the
		// pre-existing TU. If there are TUVs with the same
		// language, but different contents, both will be kept.
		// If the contents do not differ, only one will be kept.
		// Only 1 TUV in the source language will be kept.
		for (Iterator iNew = tu.tuvs.iterator();
			 iNew.hasNext(); ) {
			// get the next new TUV
			TUV newTUV = (TUV) iNew.next();

			// if the new TUVs language is the source language, skip it
			if (newTUV.isSource)
				continue;

			// check the language and contents of the new TUV
			// against those of all existing TUVs
			boolean duplicateFound = false;
			for (Iterator iExisting = existingTU.tuvs.iterator();
				 iExisting.hasNext(); ) {
				// get the next existing TUV
				TUV existingTUV = (TUV) iExisting.next();

				// check the language and contents
				if (newTUV.language.equalsIgnoreCase(existingTUV.language)
						&& newTUV.text.equals(existingTUV.text)) {
					// if language and contents match,
					// set a flag and stop iterating
					duplicateFound = true;
					break;
				}
			}

			// if no existing TUV with matching language and contents is found,
			// add the new TUV to the existing TU
			if (!duplicateFound)
				existingTU.tuvs.add(newTUV);
		}
	}

	/**
	 * Removes a TU. Removal can only be done if the source
	 * is set, and if the source text is unmodified.
	 *
	 * @param tu -- The TU to remove.
	 */
	public void removeTU(TU tu) {
		if (tu.source == null)
			return;
		tus.remove(tu.source.text);
	}

	/**
	 * Returns a list of all translation units.
	 */
	public List getTUs() {
		return new ArrayList(tus.values());
	}

	/**
	 * Receives notification of a parser warning. Called by SAX parser.
	 */
	public void warning(SAXParseException exception) throws SAXException {
		System.err.println("Warning while parsing:\n" + exception.getLocalizedMessage());
	}

	/**
	 * Receives notification of a recoverable XML parsing error. Called by SAX parser.
	 */
	public void error(SAXParseException exception) throws SAXException {
		System.err.println("Recoverable error while parsing:\n" + exception.getLocalizedMessage());
	}

	/**
	 * Receives notification of a fatal XML parsing error. Called by SAX parser.
	 */
	public void fatalError(SAXParseException exception) throws SAXException {
		System.err.println("Fatal error while parsing:\n" + exception.getLocalizedMessage());
	}

	/**
	 * Receives notification of the start of the XML document. Called by SAX parser.
	 */
	public void startDocument() {
	}

	/**
	 * Receives notification of the end of the XML document. Called by SAX parser.
	 */
	public void endDocument() {
	}

	/**
	 * Receives notification of the start of an element. Called by SAX parser.
	 */
	public void startElement(String uri,
							 String localName,
							 String qName,
							 Attributes attributes) throws SAXException {
		// determine the type of element and handle it, if required
		if (qName.equals(TMX_TAG_HEADER))
			startElementHeader(attributes);
		else if (qName.equals(TMX_TAG_TU))
			startElementTU(attributes);
		else if (qName.equals(TMX_TAG_TUV))
			startElementTUV(attributes);
		else if (qName.equals(TMX_TAG_SEG))
			startElementSegment(attributes);
		else if (qName.equals(TMX_TAG_BPT)
				|| qName.equals(TMX_TAG_EPT)
				|| qName.equals(TMX_TAG_HI)
				|| qName.equals(TMX_TAG_IT)
				|| qName.equals(TMX_TAG_PH)
				|| qName.equals(TMX_TAG_UT))
			startElementInline(attributes);
		else if (qName.equals(TMX_TAG_SUB))
			startElementSub(attributes);
	}

	/**
	 * Receives notification of the end of an element. Called by SAX parser.
	 */
	public void endElement(String uri,
						   String localName,
						   String qName) throws SAXException {
		// determine the type of element and handle it, if required
		if (qName.equals(TMX_TAG_TU))
			endElementTU();
		else if (qName.equals(TMX_TAG_TUV))
			endElementTUV();
		else if (qName.equals(TMX_TAG_SEG))
			endElementSegment();
		else if (qName.equals(TMX_TAG_BPT)
				|| qName.equals(TMX_TAG_EPT)
				|| qName.equals(TMX_TAG_HI)
				|| qName.equals(TMX_TAG_IT)
				|| qName.equals(TMX_TAG_PH)
				|| qName.equals(TMX_TAG_UT))
			endElementInline();
		else if (qName.equals(TMX_TAG_SUB))
			endElementSub();
	}

	/**
	 * Receives character data in element content. Called by the SAX parser.
	 */
	public void characters(char[] ch,
						   int start,
						   int length) throws SAXException {
		// if not in a segment, or when in an inline element other than sub, do nothing
		if (!inSegment || currentElement.peek().equals(TMX_TAG_INLINE))
			return;

		// append the data to the current segment buffer
		StringBuilder segment = currentSegment.peek();
		segment.append(ch, start, length);
	}

	/**
	 * Receives ignorable whitespace in element content. Called by the SAX parser.
	 */
	public void ignorableWhitespace(char[] ch,
									int start,
									int length) throws SAXException {
		// if not in a segment, or when in an inline element other than sub, do nothing
		if (!inSegment || currentElement.peek().equals(TMX_TAG_INLINE))
			return;

		// append the data to the current segment buffer
		StringBuilder segment = currentSegment.peek();
		segment.append(ch, start, length);
	}

	/**
	 * Handles the start of a header element in a TMX file.
	 */
	private void startElementHeader(Attributes attributes) {
		// get the header attributes
		String tmxCreationTool = attributes.getValue(TMX_ATTR_CREATIONTOOL);
		String tmxCreationToolVersion = attributes.getValue(TMX_ATTR_CREATIONTOOLVERSION);
		String tmxSegmentationType = attributes.getValue(TMX_ATTR_SEGTYPE);
		String tmxSourceLanguage = attributes.getValue(TMX_ATTR_SRCLANG);

		// mark the header as parsed
		headerParsed = true;

		// log some details
		System.out.println("Created by:   " + tmxCreationTool);
		System.out.println("Version:      " + tmxCreationToolVersion);
		System.out.println("Segmentation: " + tmxSegmentationType);
		System.out.println("Source lang:  " + tmxSourceLanguage);

		// give a warning if the TMX source language is
		// different from the set source language
		if (sourceLanguage != null
				&& !tmxSourceLanguage.equalsIgnoreCase(sourceLanguage)) {
			System.err.println("Warning: The TMX source language is different from the project source language ("
					+ this.sourceLanguage
					+ "). The TMX file will continue to be loaded.");
		}

		// if the source language is not specified,
		// use the TMX file's source language
		if (sourceLanguage == null)
			sourceLanguage = tmxSourceLanguage;

		// if the TMX details are not set yet, do so
		// Note: this prevents merges from overwriting the TMX details
		if (!merging) {
			this.tmxSourceLanguage = tmxSourceLanguage;
			creationTool = tmxCreationTool;
			creationToolVersion = tmxCreationToolVersion;
			segmentationType = tmxSegmentationType;
		}
	}

	/**
	 * Handles the start of a translation unit.
	 */
	private void startElementTU(Attributes attributes) throws SAXException {
		// put the current element's tag name on the stack
		currentElement.push(TMX_TAG_TU);

		// ensure the header has been parsed
		// without the header info, we can't determine what's source and what's target
		if (!headerParsed)
			throw new SAXException("Error: Translation unit encountered before TMX header");

		// mark the current position as in a translation unit
		inTU = true;

		// create a new TU
		TU tu = new TU();
		currentTU = tu;
	}

	/**
	 * Handles the end of a translation unit.
	 */
	private void endElementTU() {
		// remove the current element's tag name from the stack
		currentElement.pop();

		// mark the current position as *not* in a translation unit
		inTU = false;

		// determine the source and target TUV for the current TU
		TUV sourceC = null; // candidate for source TUV according to set source language
		TUV targetC = null; // candidate for target TUV according to set target language
		TUV sourceT = null; // source TUV according to TMX source language
		TUV sourceTC = null; // candidate for source TUV according to TMX source language
		for (Iterator i = currentTU.tuvs.iterator(); i.hasNext(); ) {
			// get the next TUV
			TUV tuv = (TUV) i.next();

			// first match TUV language against entire source language (lang code + reg code)
			if ((currentTU.source == null) && tuv.language.equalsIgnoreCase(sourceLanguage))
				// the current TUV is the source
				currentTU.source = tuv;
				// against entire target language
			else if ((currentTU.target == null) && tuv.language.equalsIgnoreCase(targetLanguage))
				// the current TUV is the target
				currentTU.target = tuv;
				// against source language code only
			else if ((sourceC == null) && tuv.language.regionMatches(true, 0, sourceLanguage, 0, 2))
				// the current TUV is a candidate for the source
				sourceC = tuv;
				// against target language code only
			else if ((targetC == null) && tuv.language.regionMatches(true, 0, targetLanguage, 0, 2))
				// the current TUV is a candidate for the target
				targetC = tuv;
				// if nothing matches, then try matching against the TMX source language
			else if (useTMXSourceLang) {
				// match against entire TMX source language
				if ((sourceT == null)
						&& (tuv.language.equalsIgnoreCase(tmxSourceLanguage)
						|| tmxSourceLanguage.equalsIgnoreCase("*all*")))
					// the current TUV is the source according to the TMX source language
					sourceT = tuv;
					// match against TMX source language code only
				else if ((sourceTC == null)
						&& tuv.language.regionMatches(true, 0, tmxSourceLanguage, 0, 2))
					// the current TUV is a candidate for the source according to the TMX source language
					sourceTC = tuv;
			}

			// stop looking for source and target if both have been located
			if ((currentTU.source != null)
					&& (currentTU.target != null))
				break;
		}

		// determine which source TUV to use
		if (currentTU.source == null)
			currentTU.source = sourceC; // try source candidate
		if (currentTU.source == null)
			currentTU.source = sourceT; // try source according to TMX
		if (currentTU.source == null)
			currentTU.source = sourceTC; // try source candidate according to TMX

		// if no source was found, log a warning and skip the current TU
		if (currentTU.source == null) {
			sourceNotFound = true;
			return;
		}

		// set the isSource flag of the current TU's source TUV
		currentTU.source.isSource = true;

		// determine what target TUV to use
		if (currentTU.target == null)
			currentTU.target = targetC;

		// NOTE: DON'T do this, it results in empty TUVs when saving the file.
		// The tool using the TMX can create new TUVs when necessary and set
		// the target TUV if it likes. The target is not required for saving
		// files, it's only a conveniency marker for code using the TMX.
		//
		// if no target TUV was found, create an empty TUV in the set target language
		//if (currentTU.target == null) {
		//    currentTU.target = new TUV();
		//    currentTU.target.language = targetLanguage;
		//    currentTU.tuvs.add(currentTU.target);
		//}

		// add the current TU
		addTU(currentTU);

		// clear the current TU
		currentTU = null;
	}

	/**
	 * Handles the start of a tuv element.
	 */
	private void startElementTUV(Attributes attributes) {
		// put the current element's tag name on the stack
		currentElement.push(TMX_TAG_TUV);

		// ensure we're in a translation unit
		if (!inTU) {
			System.err.println("Warning: Translation unit variant encountered outside translation unit. Variant skipped.");
			return;
		}

		// get the language of the tuv
		// try "lang" first, then "xml:lang"
		String language = attributes.getValue(TMX_ATTR_LANG);
		if (language == null)
			language = attributes.getValue(TMX_ATTR_LANG_NS);

		// if the language is not specified, skip the tuv
		if (language == null) {
			System.err.println("Warning: Language not specified for translation unit variant. Variant skipped.");
			return;
		}

		// get the change date and ID of the TUV
		String changeDate = attributes.getValue(TMX_ATTR_CHANGEDATE);
		String changeID = attributes.getValue(TMX_ATTR_CHANGEID);

		// create a new TUV and set it as the current TUV
		currentTUV = new TUV(language, "", changeDate, changeID);

		// mark the current position as in a tuv
		inTUV = true;
	}

	/**
	 * Handles the end of a tuv element.
	 */
	private void endElementTUV() {
		// remove the current element's tag name from the stack
		currentElement.pop();

		// mark the current position as *not* in a tuv element
		inTUV = false;

		// if no segments have been found, skip the TUV
		if (segmentBuffers.isEmpty())
			return;

		// copy the contents of the segment buffers to the TUV
		// (i.e. convert the StringBuilders to Strings)
		// the first item in the segment buffer list is the
		// main segment, the rest are sub segments
		currentTUV.text = segmentBuffers.get(0).toString();
		for (int i = 1; i < segmentBuffers.size(); i++)
			currentTUV.subSegments.add(segmentBuffers.get(0).toString());

		// add the current TUV to the current TU
		currentTU.tuvs.add(currentTUV);

		// clear the current TUV
		currentTUV = null;
	}

	/**
	 * Handles the start of a segment.
	 */
	private void startElementSegment(Attributes attributes) {
		// put the current element's tag name on the stack
		currentElement.push(TMX_TAG_SEG);

		// ensure we are currently in a tuv
		if (!inTUV) {
			System.err.println("Warning: Segment encountered outside translation unit variant. Segment skipped.");
			return;
		}

		// create new entries in the segment buffer list and on the stack
		StringBuilder buffer = new StringBuilder();
		segmentBuffers.clear();
		segmentBuffers.add(buffer);
		currentSegment.clear();
		currentSegment.push(buffer);

		// mark the current position as in a segment
		inSegment = true;
	}

	/**
	 * Handles the end of a segment.
	 */
	private void endElementSegment() {
		// remove the current element's tag name from the stack
		currentElement.pop();

		// clear the segment stack
		currentSegment.clear();

		// mark the current position as *not* in a segment
		inSegment = false;
	}

	/**
	 * Handles the start of a TMX inline element (<bpt>,  <ept>, <hi>,  <it>, <ph>,  <ut>).
	 */
	private void startElementInline(Attributes attributes) {
		// put the fictive tag name "inline" on the stack
		// the methods characters and ignorableWhitespace
		// check for this fictive tag name only, instead
		// of all inline tag names
		currentElement.push(TMX_TAG_INLINE);
	}

	/**
	 * Handles the end of a TMX inline element (<bpt>,  <ept>, <hi>,  <it>, <ph>,  <ut>).
	 */
	private void endElementInline() {
		// remove the current element's tag name from the stack
		currentElement.pop();
	}

	/**
	 * Handles the start of a SUB inline element.
	 */
	private void startElementSub(Attributes attributes) {
		// put the current element's tag name on the stack
		currentElement.push(TMX_TAG_SUB);

		// create new entries in the segment buffer list and on the stack
		// NOTE: the assumption is made here that sub segments are
		// in the same order in both source and target segments
		StringBuilder sub = new StringBuilder("");
		segmentBuffers.add(sub);
		currentSegment.push(sub);
	}

	/**
	 * Handles the end of a SUB inline element.
	 */
	private void endElementSub() throws SAXException {
		// remove the current element's tag name from the stack
		currentElement.pop();

		// remove the current sub from the segment stack
		currentSegment.pop();
	}

	/**
	 * Makes the parser skip DTDs.
	 */
	public org.xml.sax.InputSource resolveEntity(String publicId,
												 String systemId) throws SAXException {
		// simply return an empty dtd
		return new org.xml.sax.InputSource(new java.io.StringReader(""));
	}

	/**
	 * Converts a String into valid XML.
	 */
	public static String toValidXML(String text) {
		StringBuilder out = new StringBuilder();
		int l = text.length();
		for (int i = 0; i < l; i++) {
			char c = text.charAt(i);
			switch (c) {
				case '&':
					out.append("&amp;");
					break;
				case '>':
					out.append("&gt;");
					break;
				case '<':
					out.append("&lt;");
					break;
				case '"':
					out.append("&quot;");
					break;
				default:
					out.append(c);
			}
		}
		return out.toString();
	}

	// Constants for certain TMX tag names/attributes
	private final static String TMX_TMX_TAG = "tmx";
	private final static String TMX_TAG_HEADER = "header";
	private final static String TMX_TAG_BODY = "body";
	private final static String TMX_TAG_TU = "tu";
	private final static String TMX_TAG_TUV = "tuv";
	private final static String TMX_TAG_SEG = "seg";
	private final static String TMX_TAG_INLINE = "inline"; // made up for convenience
	private final static String TMX_TAG_BPT = "bpt";
	private final static String TMX_TAG_EPT = "ept";
	private final static String TMX_TAG_HI = "hi";
	private final static String TMX_TAG_IT = "it";
	private final static String TMX_TAG_PH = "ph";
	private final static String TMX_TAG_UT = "ut";
	private final static String TMX_TAG_SUB = "sub";

	private final static String TMX_ATTR_LANG = "lang";
	private final static String TMX_ATTR_LANG_NS = "xml:lang";
	private final static String TMX_ATTR_CREATIONTOOL = "creationtool";
	private final static String TMX_ATTR_CREATIONTOOLVERSION = "creationtoolversion";
	private final static String TMX_ATTR_SEGTYPE = "segtype";
	private final static String TMX_ATTR_SRCLANG = "srclang";
	private final static String TMX_ATTR_CHANGEDATE = "changedate";
	private final static String TMX_ATTR_CHANGEID = "changeid";

	private Map tus;                 // Map of all translation units (key = source text)
	private String sourceLanguage;      // Required source language (lang/country: LL(-CC))
	private String targetLanguage;      // Required target language (lang/country: LL(-CC))
	private String tmxSourceLanguage;   // TMX source language (lang/country: LL(-CC))
	private String creationTool;        // Name of the tool that created the TMX
	private String creationToolVersion; // Version of the tool that created the TMX
	private String segmentationType;    // Segmentation type: paragraph, sentence, or block
	private boolean useTMXSourceLang;    // True if langs must also be matched against TMX source lang
	private boolean headerParsed;        // True if the TMX header has been parsed correctly
	private boolean inTU;                // True if the current parsing point is in a TU element
	private boolean inTUV;               // True if in a TUV element
	private boolean inSegment;           // True if in a SEG element
	private boolean sourceNotFound;      // True if no source segment was found for one or more TUs
	private Stack currentElement;      // Stack of tag names up to the current parsing point
	private Stack<StringBuilder> currentSegment;      // Stack of (sub) segment buffers
	private TU currentTU;           // Current translation unit
	private TUV currentTUV;          // Current translation unit variant
	private List<StringBuilder> segmentBuffers;      // Segment buffers for current TU, first is seg, rest is sub
	private boolean merging;             // True when merging TMX's

}
