/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2006-2007 Henry Pijffers
               2010 Alex Buloichik, Didier Briel
               2014 Piotr Kulik
               2015 Yu Tang
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

package org.omegat.gui.search;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;

import org.omegat.core.Core;
import org.omegat.core.search.SearchMatch;
import org.omegat.core.search.SearchResultEntry;
import org.omegat.core.search.Searcher;
import org.omegat.gui.editor.EditorController;
import org.omegat.gui.editor.EditorController.CaretPosition;
import org.omegat.gui.editor.IEditor;
import org.omegat.util.Log;
import org.omegat.util.OConsts;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.StaticUtils;
import org.omegat.util.gui.AlwaysVisibleCaret;
import org.omegat.util.gui.Styles;
import org.omegat.util.gui.UIThreadsUtil;

/**
 * EntryListPane displays translation segments and, upon doubleclick of a
 * segment, instructs the main UI to jump to that segment this replaces the
 * previous huperlink interface and is much more flexible in the fonts it
 * displays than the HTML text
 * 
 * @author Keith Godfrey
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Didier Briel
 */
@SuppressWarnings("serial")
class EntryListPane extends JTextPane {
    protected static final AttributeSet FOUND_MARK = Styles.createAttributeSet(Color.BLUE, null, true, null);
    protected static final int MARKS_PER_REQUEST = 100;

    public EntryListPane() {
        setDocument(new DefaultStyledDocument());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2 && m_entryList.size() > 0) {
                    final Cursor oldCursor = getCursor();
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    // user double clicked on viewer pane - send message
                    // to org.omegat.gui.TransFrame to jump to this entry
                    int pos = getCaretPosition();
                    int off;
                    for (int i = 0; i < m_offsetList.size(); i++) {
                        off = m_offsetList.get(i);
                        if (off >= pos) {
                            final int entry = m_entryList.get(i);
                            if (entry >= 0) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        IEditor editor = Core.getEditor();
                                        if (m_firstMatchList.containsKey(entry)
                                                && editor instanceof EditorController) {
                                            // Select search word in Editor pane
                                            CaretPosition pos = m_firstMatchList.get(entry);
                                            ((EditorController) editor).gotoEntry(entry, pos);
                                        } else {
                                            editor.gotoEntry(entry);
                                        }
                                        setCursor(oldCursor);
                                    }
                                });
                            } else {
                                setCursor(oldCursor);
                            }
                            break;
                        }
                    }
                }
            }
        });

        setEditable(false);
        AlwaysVisibleCaret.apply(this);
    }

    /**
     * Show search result for user
     */
    public void displaySearchResult(Searcher searcher, int numberOfResults) {
        UIThreadsUtil.mustBeSwingThread();

        m_searcher = searcher;

        this.numberOfResults = numberOfResults;

        currentlyDisplayedMatches = null;
        m_entryList.clear();
        m_offsetList.clear();
        m_firstMatchList.clear();

        if (searcher == null || searcher.getSearchResults() == null) {
            // empty marks - just reset
            setText("");
            return;
        }

        currentlyDisplayedMatches = new DisplayMatches(searcher.getSearchResults());
    }

    protected class DisplayMatches implements Runnable {
        protected final DefaultStyledDocument doc;

        private final List<SearchMatch> matches = new ArrayList<SearchMatch>();

        public DisplayMatches(final List<SearchResultEntry> entries) {
            UIThreadsUtil.mustBeSwingThread();

            this.doc = new DefaultStyledDocument();

            StringBuilder m_stringBuf = new StringBuilder();
            // display what's been found so far
            if (entries.size() == 0) {
                // no match
                addMessage(m_stringBuf, OStrings.getString("ST_NOTHING_FOUND"));
            }

            if (entries.size() >= numberOfResults) {
                addMessage(m_stringBuf, StaticUtils.format(OStrings.getString("SW_MAX_FINDS_REACHED"),
                        new Object[] { numberOfResults }));
            }

            for (SearchResultEntry e : entries) {
                addEntry(m_stringBuf, e.getEntryNum(), e.getPreamble(), e.getSrcPrefix(), e.getSrcText(),
                        e.getTranslation(), e.getNote(), e.getSrcMatch(), e.getTargetMatch(), e.getNoteMatch());
            }

            try {
                doc.remove(0, doc.getLength());
                doc.insertString(0, m_stringBuf.toString(), null);
            } catch (Exception ex) {
                Log.log(ex);
            }
            setDocument(doc);
            setCaretPosition(0);

            setFont();

            if (matches.size() > 0) {
                SwingUtilities.invokeLater(this);
            }
        }

        // add entry text - remember what its number is and where it ends
        public void addEntry(StringBuilder m_stringBuf, int num, String preamble, String srcPrefix,
                String src, String loc, String note, SearchMatch[] srcMatches,
                SearchMatch[] targetMatches, SearchMatch[] noteMatches) {
            if (m_stringBuf.length() > 0)
                m_stringBuf.append("---------\n");

            if (preamble != null && !preamble.equals(""))
                m_stringBuf.append(preamble + "\n");
            if (src != null && !src.equals("")) {
                m_stringBuf.append("-- ");
                if (srcPrefix != null) {
                    m_stringBuf.append(srcPrefix);
                }
                if (srcMatches != null) {
                    for (SearchMatch m : srcMatches) {
                        m.move(m_stringBuf.length());
                        matches.add(m);
                    }
                }
                m_stringBuf.append(src);
                m_stringBuf.append('\n');
            }
            if (loc != null && !loc.equals("")) {
                m_stringBuf.append("-- ");
                if (targetMatches != null) {
                    // Save first match position to select it in Editor pane later
                    if (num > 0) {
                        SearchMatch m = targetMatches[0];
                        m_firstMatchList.put(num, new CaretPosition(m.getStart(), m.getEnd()));
                    }

                    for (SearchMatch m : targetMatches) {
                        m.move(m_stringBuf.length());
                        matches.add(m);
                    }
                }
                m_stringBuf.append(loc);
                m_stringBuf.append('\n');
            }

            if (note != null && !note.equals("")) {
                m_stringBuf.append("= ");
                if (noteMatches != null) {
                    for (SearchMatch m : noteMatches) {
                        m.move(m_stringBuf.length());
                        matches.add(m);
                    }
                }
                m_stringBuf.append(note);
                m_stringBuf.append('\n');
            }

            m_entryList.add(num);
            m_offsetList.add(m_stringBuf.length());
        }

        @Override
        public void run() {
            UIThreadsUtil.mustBeSwingThread();

            if (currentlyDisplayedMatches != this) {
                // results changed - shouldn't mark old results
                return;
            }

            List<SearchMatch> display = matches.subList(0, Math.min(MARKS_PER_REQUEST, matches.size()));
            for (SearchMatch m : display) {
                doc.setCharacterAttributes(m.getStart(), m.getLength(), FOUND_MARK, true);
            }
            display.clear();

            if (matches.size() > 0) {
                SwingUtilities.invokeLater(this);
            }
        }
    }

    /**
     * Adds a message text to be displayed. Used for displaying messages that
     * aren't results.
     * 
     * @param message
     *            The message to display
     * 
     * @author Henry Pijffers (henry.pijffers@saxnot.com)
     */
    private void addMessage(StringBuilder m_stringBuf, String message) {
        // Insert entry/message separator if necessary
        if (m_stringBuf.length() > 0)
            m_stringBuf.append("---------\n");

        // Insert the message text
        m_stringBuf.append(message);
    }

    public void setFont() {
        String srcFont = Preferences.getPreference(OConsts.TF_SRC_FONT_NAME);
        if (!srcFont.equals("")) {
            int fontsize;
            try {
                fontsize = Integer.parseInt(Preferences.getPreference(OConsts.TF_SRC_FONT_SIZE));
            } catch (NumberFormatException nfe) {
                fontsize = 12;
            }
            setFont(new Font(srcFont, Font.PLAIN, fontsize));
        }
    }

    public void reset() {
        displaySearchResult(null, 0);
    }

    public int getNrEntries() {
        return m_entryList.size();
    }

    public List<Integer> getEntryList() {
        return m_entryList;
    }

    public Searcher getSearcher() {
        return m_searcher;
    }

    private volatile Searcher m_searcher;
    private final List<Integer> m_entryList = new ArrayList<Integer>();
    private final List<Integer> m_offsetList = new ArrayList<Integer>();
    private final Map<Integer, CaretPosition> m_firstMatchList = new HashMap<Integer, CaretPosition>();
    private DisplayMatches currentlyDisplayedMatches;
    private int numberOfResults;
}
