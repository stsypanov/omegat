/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2011 Alex Buloichik
               2012 Jean-Christophe Helary
               2014 Aaron Madlon-Kay
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
package org.omegat.gui.multtrans;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import org.omegat.core.Core;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.common.EntryInfoThreadPane;
import org.omegat.gui.editor.IPopupMenuConstructor;
import org.omegat.gui.editor.SegmentBuilder;
import org.omegat.gui.main.DockableScrollPane;
import org.omegat.util.OStrings;
import org.omegat.util.StringUtil;
import org.omegat.util.gui.AlwaysVisibleCaret;
import org.omegat.util.gui.UIThreadsUtil;

/**
 * Pane for display information about multiple translations.
 * 
 * @author Alex Buloichik <alex73mail@gmail.com>
 * @author Jean-Christophe Helary
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class MultipleTransPane extends EntryInfoThreadPane<List<MultipleTransFoundEntry>> {

    private static final String EXPLANATION = OStrings.getString("GUI_MULTIPLETRANSLATIONSWINDOW_explanation");

    private List<DisplayedEntry> entries = new ArrayList<>();

    public MultipleTransPane() {
        super(true);

        String title = OStrings.getString("MULT_TITLE");
        Core.getMainWindow().addDockable(new DockableScrollPane("MULTIPLE_TRANS", title, this, true));

        setEditable(false);
        AlwaysVisibleCaret.apply(this);
        this.setText(EXPLANATION);
        setMinimumSize(new Dimension(100, 50));

        Core.getEditor().registerPopupMenuConstructors(600, new IPopupMenuConstructor() {
            public void addItems(JPopupMenu menu, JTextComponent comp, int mousepos, boolean isInActiveEntry,
                    boolean isInActiveTranslation, final SegmentBuilder sb) {
                if (isInActiveEntry
                        && Core.getProject().getProjectProperties().isSupportDefaultTranslations()) {
                    JMenuItem miDefault = menu.add(OStrings.getString("MULT_MENU_DEFAULT"));
                    JMenuItem miMultiple = menu.add(OStrings.getString("MULT_MENU_MULTIPLE"));
                    miDefault.setEnabled(!sb.isDefaultTranslation());
                    miMultiple.setEnabled(sb.isDefaultTranslation());

                    miDefault.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            Core.getEditor().setAlternateTranslationForCurrentEntry(false);
                            Core.getEditor().commitAndLeave();
                        }
                    });
                    miMultiple.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            Core.getEditor().setAlternateTranslationForCurrentEntry(true);
                        }
                    });
                }
            }
        });

        addMouseListener(mouseListener);
    }

    @Override
    protected void setFoundResult(SourceTextEntry processedEntry, List<MultipleTransFoundEntry> data) {
        UIThreadsUtil.mustBeSwingThread();

        entries.clear();
        String o = "";
        
        // Check case if current segment has default translation and there are no alternative translations.
        if (data.size() == 1 && data.get(0).key == null) {
            setText(o);
            return;
        }
        
        for (MultipleTransFoundEntry e : data) {
            DisplayedEntry de = new DisplayedEntry();
            de.entry = e;
            de.start = o.length();
            if (e.entry.translation == null) continue;
            if (e.key != null) {
                o += e.entry.translation + '\n';
                o += "<" + e.key.file;
                if (e.key.id != null) {
                    o += "/" + e.key.id;
                }
                o += ">\n";
                if (e.key.prev != null && e.key.next != null) {
                    o += "(" + StringUtil.firstN(e.key.prev, 10) + " <...> "
                            + StringUtil.firstN(e.key.next, 10) + ")\n";
                }
            } else {
                o += e.entry.translation + '\n';
            }
            de.end = o.length();
            entries.add(de);
            o += "\n";
        }

        setText(o);
    }
    
    @Override
    protected void onProjectOpen() {
        UIThreadsUtil.mustBeSwingThread();

        entries.clear();
        setText("");
    }

    @Override
    protected void onProjectClose() {
        UIThreadsUtil.mustBeSwingThread();

        entries.clear();
        this.setText(EXPLANATION);
    }

    @Override
    protected void startSearchThread(SourceTextEntry newEntry) {
        new MultipleTransFindThread(this, Core.getProject(), newEntry).start();
    }

    protected MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) { // righ click
                // is there anything?
                if (entries.isEmpty())
                    return;

                // where did we click?
                int mousepos = MultipleTransPane.this.viewToModel(e.getPoint());

                // find clicked entry
                for (DisplayedEntry de : entries) {
                    if (de.start <= mousepos && de.end >= mousepos) {
                        mouseRightClick(de, e.getPoint());
                        break;
                    }
                }
            }
        }
    };

    private void mouseRightClick(final DisplayedEntry de, final Point clickedPoint) {
        // create the menu
        JPopupMenu popup = new JPopupMenu();

        JMenuItem item;
        if (de.entry.key != null) {
            // default translation
            item = popup.add(OStrings.getString("MULT_POPUP_DEFAULT"));
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Core.getEditor().replaceEditText(de.entry.entry.translation);
                    Core.getEditor().setAlternateTranslationForCurrentEntry(false);
                    Core.getEditor().commitAndLeave();
                }
            });
        }
        // non-default translation
        item = popup.add(OStrings.getString("MULT_POPUP_REPLACE"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Core.getEditor().replaceEditText(de.entry.entry.translation);
            }
        });

        item = popup.add(OStrings.getString("MULT_POPUP_GOTO"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Core.getEditor().gotoEntry(de.entry.sourceText, de.entry.key);
            }
        });

        popup.show(this, clickedPoint.x, clickedPoint.y);
    }

    protected static class DisplayedEntry {
        int start, end;
        MultipleTransFoundEntry entry;
    }
}
