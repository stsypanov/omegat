/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2009 Alex Buloichik
               2012 Jean-Christophe Helary
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

package org.omegat.gui.dictionaries;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTMLDocument;

import org.jetbrains.annotations.NotNull;
import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.data.IProject;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.dictionaries.DictionariesManager;
import org.omegat.core.dictionaries.DictionaryEntry;
import org.omegat.core.events.IEditorEventListener;
import org.omegat.core.events.IFontChangedEventListener;
import org.omegat.gui.common.EntryInfoSearchThread;
import org.omegat.gui.common.EntryInfoThreadPane;
import org.omegat.gui.main.DockableScrollPane;
import org.omegat.tokenizer.ITokenizer;
import org.omegat.util.Log;
import org.omegat.tokenizer.ITokenizer.StemmingMode;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.StringUtil;
import org.omegat.util.gui.AlwaysVisibleCaret;
import org.omegat.util.gui.Styles.EditorColor;
import org.omegat.util.gui.UIThreadsUtil;

/**
 * This is a Dictionaries pane that displays dictionaries entries.
 * 
 * @author Alex Buloichik <alex73mail@gmail.com>
 * @author Jean-Christophe Helary 
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class DictionariesTextArea extends EntryInfoThreadPane<List<DictionaryEntry>> implements IDictionaries {

	private static final String EXPLANATION = OStrings.getString("GUI_DICTIONARYWINDOW_explanation");

    protected final DictionariesManager manager = new DictionariesManager(this);

    protected final List<String> displayedWords = new ArrayList<>();

    protected ITokenizer tokenizer;

    public DictionariesTextArea() {
        super(true);

        setContentType("text/html");
        ((HTMLDocument) getDocument()).setPreservesUnknownTags(false);

        // setEditable(false);
        String title = OStrings.getString("GUI_MATCHWINDOW_SUBWINDOWTITLE_Dictionary");
        Core.getMainWindow().addDockable(new DockableScrollPane("DICTIONARY", title, this, true));

        addMouseListener(mouseCallback);

        setEditable(false);
        AlwaysVisibleCaret.apply(this);
        setText(EXPLANATION);
    	applyFont();
        setMinimumSize(new Dimension(100, 50));

        CoreEvents.registerEditorEventListener(new IEditorEventListener() {
            @Override
            public void onNewWord(String newWord) {
                callDictionary(newWord);
            }
        });
        
        // register font changes callback
        CoreEvents.registerFontChangedEventListener(new IFontChangedEventListener() {
            @Override
            public void onFontChanged(Font newFont) {
 				applyFont(newFont);          
            }
        });
    }

    private void applyFont() {
    	applyFont(Core.getMainWindow().getApplicationFont());
		
	}

	private void applyFont(Font font) {
		MutableAttributeSet attr = new SimpleAttributeSet();
		HTMLDocument doc = (HTMLDocument) getDocument();
		

    	doc.getStyleSheet().addCSSAttribute(attr, CSS.Attribute.FONT_FAMILY, font.getFontName());
    	doc.getStyleSheet().addCSSAttribute(attr, CSS.Attribute.FONT_SIZE, font.getSize() + "pt");
    	doc.setCharacterAttributes(0, doc.getLength(), attr, false);
	}

    @Override
    protected void onProjectOpen() {
        clear();
        IProject project = Core.getProject();
        tokenizer = project.getSourceTokenizer();
        manager.start(project.getProjectProperties().getDictRoot());
    }

    @Override
    protected void onProjectClose() {
        clear();
        setText(EXPLANATION);
        applyFont();
        manager.stop();
        tokenizer = null;
    }

    /** Clears up the pane. */
    protected void clear() {
        UIThreadsUtil.mustBeSwingThread();

        setText("");
    }

    /**
     * Move position in pane to the currently selected word.
     */
    protected void callDictionary(String word) {
        UIThreadsUtil.mustBeSwingThread();

        HTMLDocument doc = (HTMLDocument) getDocument();

        int i = displayedWords.indexOf(word.toLowerCase());
        if (i >= 0) {
            final Element el = doc.getElement(Integer.toString(i));
            if (el != null) {
                try {
                    // rectangle to be visible
                    Rectangle rect = getUI().modelToView(this, el.getStartOffset());
                    // show 2 lines
                    if (rect != null) {
                        rect.height *= 2;
                        scrollRectToVisible(rect);
                    }
                } catch (BadLocationException ex) {
                    Log.log(Level.SEVERE, "callDictionary", ex);
                }
            }
        }
    }

    @Override
    protected void startSearchThread(SourceTextEntry newEntry) {
        new DictionaryEntriesSearchThread(newEntry).start();
    }

    /**
     * Refresh content on dictionary file changed.
     */
    @Override
    public void refresh() {
        SourceTextEntry ste = Core.getEditor().getCurrentEntry();
        if (ste != null) {
            startSearchThread(ste);
        }
    }

    public void setFoundResult(final List<DictionaryEntry> data){
        setFoundResult(null, data);
    }

    @Override
    protected void setFoundResult(final SourceTextEntry se, final List<DictionaryEntry> data) {
        UIThreadsUtil.mustBeSwingThread();

        displayedWords.clear();

        if (data == null) {
            setText("");
            return;
        }

        String text = buildStringFromDictionaryData(data);

        setText(text);
        applyFont();
        setCaretPosition(0);
    }

    @NotNull
    private String buildStringFromDictionaryData(List<DictionaryEntry> data) {
        StringBuilder txt = new StringBuilder();
        boolean wasPrev = false;
        int i = 0;
        for (DictionaryEntry de : data) {
            if (wasPrev) {
                txt.append("<br><hr>");
            } else {
                wasPrev = true;
            }
            txt.append("<b><span id=\"").append(i).append("\">");
            txt.append(de.getWord());
            txt.append("</span></b>");
            txt.append(" - ").append(de.getArticle());

            displayedWords.add(de.getWord().toLowerCase());
            i++;
        }
        return txt.toString();
    }

    protected final MouseAdapter mouseCallback = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                UIThreadsUtil.mustBeSwingThread();

                JPopupMenu popup = new JPopupMenu();
                int mousepos = viewToModel(e.getPoint());
                HTMLDocument doc = (HTMLDocument) getDocument();
                for (int i = 0; i < displayedWords.size(); i++) {
                    Element el = doc.getElement(Integer.toString(i));
                    if (el != null) {
                        if (el.getStartOffset() <= mousepos && el.getEndOffset() >= mousepos) {
                            final String w = displayedWords.get(i);
                            String hideW = StringUtil.format(OStrings.getString("DICTIONARY_HIDE"), w);
                            JMenuItem item = popup.add(hideW);
                            item.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    manager.addIgnoreWord(w);
                                }
                            });
                        }
                    }
                }
                popup.show(DictionariesTextArea.this, e.getX(), e.getY());
            }
        }
    };

    /**
     * Thread for search data in dictionaries.
     */
    private class DictionaryEntriesSearchThread extends EntryInfoSearchThread<List<DictionaryEntry>> {
        protected final String src;
        protected final ITokenizer tok;

        public DictionaryEntriesSearchThread(final SourceTextEntry newEntry) {
            super(DictionariesTextArea.this, newEntry);
            src = newEntry.getSrcText();
            tok = tokenizer;
        }

        @Override
        protected List<DictionaryEntry> search() {
            if (tok == null) {
                return null;
            }

            StemmingMode mode = Preferences.isPreferenceDefault(Preferences.DICTIONARY_FUZZY_MATCHING, true)
                    ? StemmingMode.MATCHING : StemmingMode.NONE;
            String[] tokenList = tok.tokenizeWordsToStrings(src, mode);
            Set<String> words = new TreeSet<String>();
            for (String tok : tokenList) {
                checkEntryChanged();
                words.add(tok);
            }
            List<DictionaryEntry> result = manager.findWords(words);

            Collections.sort(result, new Comparator<DictionaryEntry>() {
                @Override
                public int compare(DictionaryEntry o1, DictionaryEntry o2) {
                    return o1.getWord().compareTo(o2.getWord());
                }
            });
            return result;
        }
    }
}
