/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2006 Henry Pijffers
               2009 Didier Briel
               2010 Martin Fleurke, Antonio Vilei, Alex Buloichik, Didier Briel
               2013 Aaron Madlon-Kay, Alex Buloichik
               2014 Alex Buloichik, Piotr Kulik, Aaron Madlon-Kay
               2015 Aaron Madlon-Kay
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.core.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omegat.core.Core;
import org.omegat.core.data.EntryKey;
import org.omegat.core.data.ExternalTMX;
import org.omegat.core.data.IProject;
import org.omegat.core.data.IProject.FileInfo;
import org.omegat.core.data.ParseEntry;
import org.omegat.core.data.PrepareTMXEntry;
import org.omegat.core.data.ProjectProperties;
import org.omegat.core.data.ProjectTMX;
import org.omegat.core.data.ProtectedPart;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.data.TMXEntry;
import org.omegat.core.threads.LongProcessThread;
import org.omegat.filters2.FilterContext;
import org.omegat.filters2.IParseCallback;
import org.omegat.filters2.master.FilterMaster;
import org.omegat.gui.glossary.GlossaryEntry;
import org.omegat.util.Language;
import org.omegat.util.OStrings;
import org.omegat.util.StaticUtils;
import org.omegat.util.StaticUtils.ITreeIteratorCallback;
import org.omegat.util.StringUtil;

/**
 * This class implements search functionality. It is non-reentrant: each searcher instance must be used by a
 * single thread.
 * 
 * @author Keith Godfrey
 * @author Maxym Mykhalchuk
 * @author Henry Pijffers
 * @author Didier Briel
 * @author Martin Fleurke
 * @author Antonio Vilei
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Aaron Madlon-Kay
 * @author Piotr Kulik
 */
public class Searcher {

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile(" ");
    private static final Pattern COMPILE = Pattern.compile("\\\\s");

    /**
     * Create new searcher instance.
     * 
     * @param project
     *            Current project
     */
    public Searcher(final IProject project, final SearchExpression expression) {
        m_project = project;
        this.expression = expression;
    }

    /** 
     * Set thread for checking interruption.
     */
    public void setThread(LongProcessThread thread) {
        checkStop = thread;
    }

    public SearchExpression getExpression() {
        return expression;
    }

    /**
     * Returns list of search results
     */
    public List<SearchResultEntry> getSearchResults() {
        if (m_preprocessResults) {
            // function can be called multiple times after search
            // results preprocess should occur only one time
            m_preprocessResults = false;
            if (!expression.allResults) {
                for (SearchResultEntry entry : m_searchResults) {
                    String key = entry.getSrcText() + entry.getTranslation();
                    if (entry.getEntryNum() == ENTRY_ORIGIN_TRANSLATION_MEMORY) {
                        if (m_tmxMap.containsKey(key) && (m_tmxMap.get(key) > 0)) {
                        	String newPreamble = StringUtil.format(OStrings.getString("SW_FILE_AND_NR_OF_MORE"),
                        			entry.getPreamble(), m_tmxMap.get(key));
                            entry.setPreamble(newPreamble);
                        }
                    } else if (entry.getEntryNum() > ENTRY_ORIGIN_PROJECT_MEMORY) {
                        // at this stage each PM entry num is increased by 1
                        if (m_entryMap.containsKey(key) && (m_entryMap.get(key) > 0)) {
                        	String newPreamble = StringUtil.isEmpty(entry.getPreamble())
                        			? StringUtil.format(OStrings.getString("SW_NR_OF_MORE"),
                            				m_entryMap.get(key))
                    				: StringUtil.format(OStrings.getString("SW_FILE_AND_NR_OF_MORE"),
                                            entry.getPreamble(), m_entryMap.get(key));
                            entry.setPreamble(newPreamble);
                        }
                    }
                }
            }
        }
        return m_searchResults;
    }

    /**
     * Search for an expression and return a list of results.
     * 
     * @param expression
     *            what to search for (search text and options)
     * @param maxResults
     *            maximum number of search results
     * @throws Exception
     */
    public void search() throws Exception {
        m_searchExpression = expression;
        String text = expression.text;
        String author = expression.author;

        m_searchResults = new ArrayList<>();
        m_numFinds = 0;
        // ensures that results will be preprocessed only one time
        m_preprocessResults = true;

        m_entryMap = null; // HP

        m_entryMap = new HashMap<>(); // HP
        
        m_tmxMap = new HashMap<>();

        // create a list of matchers
        m_matchers = new ArrayList<>();

        // determine pattern matching flags
        int flags = expression.caseSensitive ? 0 : Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;

        // Normalize width of search string if width insensitivity is requested.
        // Then, instead of modifying the regex, we also normalize the
        // comparison strings later on.
        if (m_searchExpression.widthInsensitive) {
            text = StringUtil.normalizeWidth(text);
        }

        // if exact search, just use the entire search string as a single
        // search string; otherwise, if keyword, break up the string into
        // separate words (= multiple search strings)

        switch (expression.searchExpressionType) {
        case EXACT:
        default:
            // escape the search string, it's not supposed to be a regular
            // expression
            text = StaticUtils.escapeNonRegex(text, false);

            // space match nbsp (\u00a0)
            if (expression.spaceMatchNbsp) {
                text = WHITESPACE_PATTERN.matcher(text).replaceAll("( |\u00A0)");
            }

            // create a matcher for the search string
            m_matchers.add(Pattern.compile(text, flags).matcher(""));
            break;
        case KEYWORD:
            // break the search string into keywords,
            // each of which is a separate search string
            text = text.trim();
            if (!text.isEmpty()) {
                int wordStart = 0;
                while (wordStart < text.length()) {
                    // get the location of the next space
                    int spacePos = text.indexOf(' ', wordStart);

                    // get the next word
                    String word = (spacePos == -1) // last word reached
                    ? text.substring(wordStart, text.length()).trim()
                            : text.substring(wordStart, spacePos).trim();

                    if (!word.isEmpty()) {
                        // escape the word, if it's not supposed to be a regular
                        // expression
                        word = StaticUtils.escapeNonRegex(word, false);

                        // create a matcher for the word
                        m_matchers.add(Pattern.compile(word, flags).matcher(""));
                    }

                    // set the position for the start of the next word
                    wordStart = (spacePos == -1) ? text.length() : spacePos + 1;
                }
            }
            break;
        case REGEXP:
            // space match nbsp (\u00a0)
            if (expression.spaceMatchNbsp) {
                text = WHITESPACE_PATTERN.matcher(text).replaceAll("( |\u00A0)");
                text = COMPILE.matcher(text).replaceAll("(\\\\s|\u00A0)");
            }

            // create a matcher for the search string
            m_matchers.add(Pattern.compile(text, flags).matcher(""));
            break;
        }
        // create a matcher for the author search string
        if (expression.searchExpressionType != SearchExpression.SearchExpressionType.REGEXP)
            author = StaticUtils.escapeNonRegex(author, false);

        m_author = Pattern.compile(author, flags).matcher("");

        if (expression.rootDir == null) {
            // if no search directory specified, then we are
            // searching current project only
            searchProject();
        } else {
            searchFiles();
        }
    }

    // ////////////////////////////////////////////////////////////
    // internal functions

    private void addEntry(int num, String preamble, String srcPrefix, String src, String target,
            String note, SearchMatch[] srcMatch, SearchMatch[] targetMatch, SearchMatch[] noteMatch) {
        SearchResultEntry entry = new SearchResultEntry(num, preamble, srcPrefix,
                src, target, note, srcMatch,targetMatch, noteMatch);
        m_searchResults.add(entry);
        m_numFinds++;
    }

    /**
     * Queue found string. Removes duplicate segments (by Henry Pijffers) except if m_allResults = true
     */
    private void foundString(int entryNum, String intro, String src, String target, String note,
            SearchMatch[] srcMatches, SearchMatch[] targetMatches, SearchMatch[] noteMatches) {
        if (m_numFinds >= expression.numberOfResults) {
            return;
        }

        String key = src + target;
        // entries from project memory
        if (entryNum >= ENTRY_ORIGIN_PROJECT_MEMORY) {
            if (!m_entryMap.containsKey(key) || expression.allResults) {
                // HP, duplicate entry prevention
                // entries are referenced at offset 1 but stored at offset 0
                String file = expression.fileNames ? getFileForEntry(entryNum + 1) : null;
                addEntry(entryNum + 1, file, (entryNum + 1) + "> ", src, target,
                        note, srcMatches, targetMatches, noteMatches);
                if (!expression.allResults) // If we filter results
                    m_entryMap.put(key, 0); // HP
            } else if (!expression.allResults) {
                m_entryMap.put(key, m_entryMap.get(key) + 1);
            }
        } else if (entryNum == ENTRY_ORIGIN_TRANSLATION_MEMORY) {
        // entries from translation memory
            if (!m_tmxMap.containsKey(key) || expression.allResults) {
                addEntry(entryNum, intro, null, src, target, note,
                        srcMatches, targetMatches, noteMatches);
                if (!expression.allResults)
                    // first occurence
                    m_tmxMap.put(key, 0);
            } else if (!expression.allResults) {
                // next occurence
                m_tmxMap.put(key, m_tmxMap.get(key) + 1);
            }
        } else {
        // all other entries
            addEntry(entryNum, intro, null, src, target, note,
                    srcMatches, targetMatches, noteMatches);
        }
    }

    private void searchProject() {
        // reset the number of search hits
        m_numFinds = 0;

        // search the Memory, if requested
        if (m_searchExpression.memory) {
            // search through all project entries
            IProject dataEngine = m_project;
            for (int i = 0; i < m_project.getAllEntries().size(); i++) {
                // stop searching if the max. nr of hits has been reached
                if (m_numFinds >= expression.numberOfResults) {
                    return;
                }
                // get the source and translation of the next entry
                SourceTextEntry ste = dataEngine.getAllEntries().get(i);
                TMXEntry te = m_project.getTranslationInfo(ste);

                checkEntry(ste.getSrcText(), te.translation, te.note, ste.getComment(), te, i, null);
                checkStop.checkInterrupted();
            }

            // search in orphaned
            if (!m_searchExpression.excludeOrphans) {
                m_project.iterateByDefaultTranslations(new IProject.DefaultTranslationsIterator() {
                    final String file = OStrings.getString("CT_ORPHAN_STRINGS");
    
                    public void iterate(String source, TMXEntry en) {
                        // stop searching if the max. nr of hits has been reached
                        if (m_numFinds >= expression.numberOfResults) {
                            return;
                        }
                        checkStop.checkInterrupted();
                        if (m_project.isOrphaned(source)) {
                            checkEntry(en.source, en.translation, en.note, null, en, ENTRY_ORIGIN_ORPHAN, file);
                        }
                    }
                });
                m_project.iterateByMultipleTranslations(new IProject.MultipleTranslationsIterator() {
                    final String file = OStrings.getString("CT_ORPHAN_STRINGS");

                    public void iterate(EntryKey source, TMXEntry en) {
                        // stop searching if the max. nr of hits has been
                        // reached
                        if (m_numFinds >= expression.numberOfResults) {
                            return;
                        }
                        checkStop.checkInterrupted();
                        if (m_project.isOrphaned(source)) {
                            checkEntry(en.source, en.translation, en.note, null, en, ENTRY_ORIGIN_ORPHAN, file);
                        }
                    }
                });
            }
        }

        // search the TM, if requested
        if (m_searchExpression.tm) {
            // Search TM entries, unless we search for date or author.
            // They are not loaded from external TM, so skip the search in
            // that case.
            if (!expression.searchAuthor && !expression.searchDateAfter && !expression.searchDateBefore) {
                for (Map.Entry<String, ExternalTMX> tmEn : m_project.getTransMemories().entrySet()) {
                    final String fileTM = tmEn.getKey();
                    if (!searchEntries(tmEn.getValue().getEntries(), fileTM)) return;
                    checkStop.checkInterrupted();
                }
                for (Map.Entry<Language, ProjectTMX> tmEn : m_project.getOtherTargetLanguageTMs().entrySet()) {
                    final Language langTM = tmEn.getKey();
                    if (!searchEntriesAlternative(tmEn.getValue().getDefaults(), langTM.getLanguage())) return;
                    if (!searchEntriesAlternative(tmEn.getValue().getAlternatives(), langTM.getLanguage())) return;
                    checkStop.checkInterrupted();
                }
            }
        }

        // search the glossary, if requested
        if (m_searchExpression.glossary) {
            String intro = OStrings.getString("SW_GLOSSARY_RESULT");
            List<GlossaryEntry> entries = Core.getGlossaryManager().search(m_searchExpression.text);
            for (GlossaryEntry en : entries) {
                checkEntry(en.getSrcText(), en.getLocText(), null, null, null, ENTRY_ORIGIN_GLOSSARY, intro);
                // stop searching if the max. nr of hits has been reached
                if (m_numFinds >= expression.numberOfResults) {
                    return;
                }
                checkStop.checkInterrupted();
            }
        }
    }

    private String getFileForEntry(int i) {
        List<FileInfo> fileList = Core.getProject().getProjectFiles();
        for (FileInfo fi : fileList) {
            int first = fi.entries.get(0).entryNum();
            int last = fi.entries.get(fi.entries.size() - 1).entryNum();
            if (i >= first && i <= last) {
                return fi.filePath;
            }
        }
        return null;
    }

    /**
     * Loops over collection of TMXEntries and checks every entry.
     * If max nr of hits have been reached or serach has been stopped,
     * the function stops and returns false. Else it finishes and returns true;
     * 
     * @param tmEn collection of TMX Entries to check.
     * @param tmxID identifier of the TMX. E.g. the filename or language code
     * @return true when finished and all entries checked,
     *         false when search has stopped before all entries have been checked.
     */
    private boolean searchEntries(Collection<PrepareTMXEntry> tmEn, final String tmxID) {
        for (PrepareTMXEntry tm : tmEn) {
            // stop searching if the max. nr of hits has been
            // reached
            if (m_numFinds >= expression.numberOfResults) {
                return false;
            }

            //for alternative translations:
            //- it is not feasible to get the sourcetextentry that matches the tm.source, so we cannot get the entryNum and real translation
            //- although the 'trnalsation' is used as 'source', we search it as translation, else we cannot show to which real source it belongs
            checkEntry(tm.source, tm.translation, tm.note, null, null, ENTRY_ORIGIN_TRANSLATION_MEMORY, tmxID);

            checkStop.checkInterrupted();
        }
        return true;
    }

    private boolean searchEntriesAlternative(Collection<TMXEntry> tmEn, final String tmxID) {
        for (TMXEntry tm : tmEn) {
            // stop searching if the max. nr of hits has been
            // reached
            if (m_numFinds >= expression.numberOfResults) {
                return false;
            }

            //for alternative translations:
            //- it is not feasible to get the sourcetextentry that matches the tm.source, so we cannot get the entryNum and real translation
            //- although the 'trnalsation' is used as 'source', we search it as translation, else we cannot show to which real source it belongs
            checkEntry(tm.source, tm.translation, tm.note, null, null, ENTRY_ORIGIN_ALTERNATIVE, tmxID);

            checkStop.checkInterrupted();
        }
        return true;
    }

    /**
     * Check if specified entry should be found.
     * 
     * @param srcText
     *            source text
     * @param locText
     *            translation text
     * @param note
     *            note text
     * @param comment
     *            comment text
     * @param entry
     *            entry. Null for external tmx entries (so we can only search for source and translation in external tmx)
     * @param entryNum
     *            entry number
     * @param intro
     *            file
     */
    protected void checkEntry(String srcText, String locText, String note,
            String comment, TMXEntry entry, int entryNum, String intro) {
        SearchMatch[] srcMatches = null;
        SearchMatch[] targetMatches = null;
        SearchMatch[] srcOrTargetMatches = null;
        SearchMatch[] noteMatches = null;
        SearchMatch[] commentMatches = null;

        switch (m_searchExpression.mode) {
        case SEARCH:
            if (expression.searchTranslated && !expression.searchUntranslated && locText == null) {
                return;
            }
            if (!expression.searchTranslated && expression.searchUntranslated && locText != null) {
                return;
            }
            if (expression.searchSource && searchString(srcText)) {
                srcMatches = foundMatches.toArray(new SearchMatch[foundMatches.size()]);
            }
            if (expression.searchTarget && searchString(locText)) {
                targetMatches = foundMatches.toArray(new SearchMatch[foundMatches.size()]);
            }
            // If
            // - we are searching both source and target
            // - we and haven't found a match in either so far
            // - we have a target
            // then we also search the concatenation of source and target per
            // https://sourceforge.net/p/omegat/feature-requests/1185/
            // We join with U+E000 (private use) to prevent spuriously matching
            // e.g. "abc" in "fab" + "cat"
            if (expression.searchSource && expression.searchTarget && locText != null && srcMatches == null
                    && targetMatches == null && searchString(srcText + '\ue000' + locText)) {
                srcOrTargetMatches = foundMatches.toArray(new SearchMatch[foundMatches.size()]);
            }
            if (expression.searchNotes && searchString(note)) {
                noteMatches = foundMatches.toArray(new SearchMatch[foundMatches.size()]);
            }
            if (expression.searchComments && searchString(comment)) {
                commentMatches = foundMatches.toArray(new SearchMatch[foundMatches.size()]);
            }
            break;
        case REPLACE:
            if (m_searchExpression.replaceTranslated && locText != null) {
                if (searchString(locText)) {
                    targetMatches = foundMatches.toArray(new SearchMatch[foundMatches.size()]);
                }
            } else if (m_searchExpression.replaceUntranslated && locText == null) {
                if (searchString(srcText)) {
                    srcMatches = foundMatches.toArray(new SearchMatch[foundMatches.size()]);
                }
            }
            break;
        }
        // if the search expression is satisfied, report the hit
        if ((srcMatches != null || targetMatches != null || srcOrTargetMatches != null || noteMatches != null
                || commentMatches != null)
                && (!expression.searchAuthor || entry != null && searchAuthor(entry))
                && (!expression.searchDateBefore
                        || entry != null && entry.changeDate != 0 && entry.changeDate < expression.dateBefore)
                && (!expression.searchDateAfter
                        || entry != null && entry.changeDate != 0 && entry.changeDate > expression.dateAfter)) {
            // found
            foundString(entryNum, intro, srcText, locText, note,
                    srcMatches, targetMatches, noteMatches);
        }
    }

    private void searchFiles() throws Exception {
        if (!expression.rootDir.endsWith(File.separator))
            expression.rootDir += File.separator;

        final FilterMaster fm = Core.getFilterMaster();

        final SearchCallback searchCallback = new SearchCallback(m_project.getProjectProperties());
        
        StaticUtils.iterateFileTree(new File(expression.rootDir), expression.recursive, new ITreeIteratorCallback() {
            @Override
            public void processFile(File file) throws Exception {
                String filename = file.getPath();
                FileInfo fi = new FileInfo();
                // determine actual file name w/ no root path info
                fi.filePath = filename.substring(expression.rootDir.length());

                searchCallback.setCurrentFile(fi);
                fm.loadFile(filename, new FilterContext(m_project.getProjectProperties()), searchCallback);
                searchCallback.fileFinished();

                checkStop.checkInterrupted();
            }
        });
    }

    protected class SearchCallback extends ParseEntry implements IParseCallback {
        private String filename;

        public SearchCallback(ProjectProperties config) {
            super(config);
        }

        @Override
        public void setCurrentFile(FileInfo fi) {
            super.setCurrentFile(fi);
            filename = fi.filePath;
        }

        @Override
        protected void fileFinished() {
            super.fileFinished();
        }

        @Override
        protected void addSegment(String id, short segmentIndex, String segmentSource,
                List<ProtectedPart> protectedParts, String segmentTranslation, boolean segmentTranslationFuzzy,
                String comment, String prevSegment, String nextSegment, String path) {
            searchText(segmentSource, segmentTranslation, filename);
        }
    }

    // /////////////////////////////////////////////////////////////////////
    // search algorithm

    /**
     * Looks for an occurrence of the search string(s) in the supplied text string.
     * 
     * @param text
     *            The text string to search in
     * 
     * @return True if the text string contains all search strings
     */
    public boolean searchString(String origText) {
        if (origText == null || m_matchers == null || m_matchers.isEmpty()) {
            return false;
        }
        
        String text = m_searchExpression.widthInsensitive ? StringUtil.normalizeWidth(origText) : origText;

        foundMatches.clear();
        // check the text against all matchers
        for (Matcher matcher : m_matchers) {
            // check the text against the current matcher
            // if one of the search strings is not found, don't
            // bother looking for the rest of the search strings
            matcher.reset(text);
            if (!matcher.find()) {
                return false;
            }

            // Check if we searched a string of different length from the
            // original. If so, then we give up on highlighting this hit
            // because the offsets and length will not match. We still return
            // true so the hit will still be recorded.
            if (text != origText && text.length() != origText.length()) {
                continue;
            }
            while (true) {
                int start = matcher.start();
                foundMatches.add(new SearchMatch(start, matcher.end()));
                if (start >= text.length() || !matcher.find(start + 1)) {
                    break;
                }
            }
        }

        // if we arrive here, all search strings have been matched,
        // so this is a hit

        // merge overlapped matches for better performance to mark on UI
        Collections.sort(foundMatches);
        for (int i = 1; i < foundMatches.size();) {
            SearchMatch pr = foundMatches.get(i - 1);
            SearchMatch cu = foundMatches.get(i);
            // check for overlapped
            if (pr.getStart() <= cu.getStart() && pr.getEnd() >= cu.getStart()) {
                int end = Math.max(cu.getEnd(), pr.getEnd());
                // leave only one region
                pr = new SearchMatch(pr.getStart(), end);
                foundMatches.set(i-1, pr);
                foundMatches.remove(i);
            } else {
                i++;
            }
        }
        return true;
    }

    public List<SearchMatch> getFoundMatches() {
        return foundMatches;
    }

    /**
     * Looks for an occurrence of the author search string in the supplied text string.
     * 
     * @param author
     *            The text string to search in
     * 
     * @return True if the text string contains the search string
     */
    private boolean searchAuthor(TMXEntry te) {
        if (te == null || m_author == null)
            return false;
        
        if (m_author.pattern().pattern().equals("")) {
            // Handle search for null author.
            return te.changer == null && te.creator == null;
        }

        if (te.changer != null) {
            m_author.reset(te.changer);
            if (m_author.find()) {
                return true;
            }
        }

        if (te.creator != null) {
            m_author.reset(te.creator);
            if (m_author.find()) {
                return true;
            }
        }

        return false;
    }

    // ///////////////////////////////////////////////////////////////
    // interface used by FileHandlers

    public void searchText(String seg, String translation, String filename) {
        // don't look further if the max. nr of hits has been reached
        if (m_numFinds >= expression.numberOfResults)
            return;

        checkStop.checkInterrupted();

        if (!m_searchExpression.searchTranslated) {
            if (translation == null) {
                return;
            }
        }
        if (searchString(seg)) {
            SearchMatch[] matches = foundMatches.toArray(new SearchMatch[foundMatches.size()]);
            // found a match - do something about it
            foundString(ENTRY_ORIGIN_TEXT, filename, seg, null, null, matches, null, null);
        }
    }

    public interface ISearchCheckStop {
        boolean isStopped();
    }

    private volatile List<SearchResultEntry> m_searchResults;
    private boolean m_preprocessResults;
    private IProject m_project;
    private Map<String, Integer> m_tmxMap; // keeps track of previous results not from project memory
    private Map<String, Integer> m_entryMap; // HP: keeps track of previous results, to
                                    // avoid duplicate entries
    private List<Matcher> m_matchers; // HP: contains a matcher for each search
                                      // string
    // (multiple if keyword search)
    private Matcher m_author;

    private int m_numFinds;

    private SearchExpression m_searchExpression;
    private final SearchExpression expression;
    private LongProcessThread checkStop;
    private final List<SearchMatch> foundMatches = new ArrayList<>();
    
    // PM entries 0+
    // Only PM and TM are counted (separately) for '+X more' statistics
    private final int ENTRY_ORIGIN_PROJECT_MEMORY = 0;
    private final int ENTRY_ORIGIN_TRANSLATION_MEMORY = -1;
    private final int ENTRY_ORIGIN_ORPHAN = -2;
    private final int ENTRY_ORIGIN_ALTERNATIVE = -3;
    private final int ENTRY_ORIGIN_GLOSSARY = -4;
    private final int ENTRY_ORIGIN_TEXT = -5;
}
