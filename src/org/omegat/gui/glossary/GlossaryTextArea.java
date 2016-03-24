/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2007 Didier Briel
               2009-2010 Wildrich Fourie
               2010 Alex Buloichik
               2012 Jean-Christophe Helary
               2013 Aaron Madlon-Kay, Alex Buloichik
               2015 Yu Tang, Aaron Madlon-Kay
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

package org.omegat.gui.glossary;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.*;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;

import org.omegat.core.Core;
import org.omegat.core.data.ProjectProperties;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.common.EntryInfoThreadPane;
import org.omegat.gui.dialogs.CreateGlossaryEntryDialog;
import org.omegat.gui.editor.EditorUtils;
import org.omegat.gui.main.DockableScrollPane;
import org.omegat.gui.main.IMainWindow;
import org.omegat.util.Log;
import org.omegat.util.OConsts;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.StringUtil;
import org.omegat.util.gui.DragTargetOverlay;
import org.omegat.util.gui.DragTargetOverlay.FileDropInfo;
import org.omegat.util.gui.IPaneMenu;
import org.omegat.util.gui.JTextPaneLinkifier;
import org.omegat.util.gui.StaticUIUtils;
import org.omegat.util.gui.Styles;
import org.omegat.util.gui.UIThreadsUtil;

/**
 * This is a Glossary pane that displays glossary entries.
 * 
 * @author Keith Godfrey
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 * @author Wildrich Fourie
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Jean-Christophe Helary
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class GlossaryTextArea extends EntryInfoThreadPane<List<GlossaryEntry>> implements IPaneMenu {

    private static final String EXPLANATION = OStrings.getString("GUI_GLOSSARYWINDOW_explanation");

    private static final AttributeSet NO_ATTRIBUTES = Styles.createAttributeSet(null, null, false, null);
    private static final AttributeSet PRIORITY_ATTRIBUTES = Styles.createAttributeSet(null, null, true, null);

    /**
     * Holds the current GlossaryEntries for the TransTips
     */
    protected List<GlossaryEntry> nowEntries;

    private CreateGlossaryEntryDialog createGlossaryEntryDialog;

    private final DockableScrollPane scrollPane;

    /** Creates new form MatchGlossaryPane */
    public GlossaryTextArea(IMainWindow mw) {
        super(true);

        nowEntries = new ArrayList<>();

        String title = OStrings.getString("GUI_MATCHWINDOW_SUBWINDOWTITLE_Glossary");
        scrollPane = new DockableScrollPane("GLOSSARY", title, this, true);
        mw.addDockable(scrollPane);

        setEditable(false);
        StaticUIUtils.makeCaretAlwaysVisible(this);
        setText(EXPLANATION);
        setMinimumSize(new Dimension(100, 50));

        addMouseListener(mouseListener);

        Core.getEditor().registerPopupMenuConstructors(300, new TransTipsPopup(this));
        
        if (!GraphicsEnvironment.isHeadless()) {
            DragTargetOverlay.apply(this, new FileDropInfo(false) {
                @Override
                public boolean canAcceptDrop() {
                    return Core.getProject().isProjectLoaded();
                }
                @Override
                public String getOverlayMessage() {
                    return OStrings.getString("DND_ADD_GLOSSARY_FILE");
                }
                @Override
                public String getImportDestination() {
                    return Core.getProject().getProjectProperties().getGlossaryRoot();
                }
                @Override
                public boolean acceptFile(File pathname) {
                    String name = pathname.getName().toLowerCase();
                    return name.endsWith(OConsts.EXT_CSV_UTF8) || name.endsWith(OConsts.EXT_TBX)
                            || name.endsWith(OConsts.EXT_TSV_DEF) || name.endsWith(OConsts.EXT_TSV_TXT)
                            || name.endsWith(OConsts.EXT_TSV_UTF8);
                }
                @Override
                public Component getComponentToOverlay() {
                    return scrollPane;
                }
            });
        }

        JTextPaneLinkifier.linkify(this);
    }

    @Override
    protected void onProjectOpen() {
        clear();
        Core.getGlossaryManager().start();
    }

    @Override
    protected void onProjectClose() {
        clear();
        setText(EXPLANATION);
        Core.getGlossaryManager().stop();
    }

    @Override
    protected void startSearchThread(SourceTextEntry newEntry) {
        new FindGlossaryThread(GlossaryTextArea.this, newEntry, Core.getGlossaryManager()).start();
    }

    /**
     * Refresh content on glossary file changed.
     */
    public void refresh() {
        SourceTextEntry ste = Core.getEditor().getCurrentEntry();
        if (ste != null) {
            startSearchThread(ste);
        }
    }

    @Override
    public void onEntryActivated(SourceTextEntry newEntry) {
        scrollPane.stopNotifying();
        super.onEntryActivated(newEntry);
    }

    /**
     * Sets the list of glossary entries to show in the pane. Each element of the list should be an instance
     * of {@link GlossaryEntry}.
     */
    @Override
    protected void setFoundResult(SourceTextEntry en, List<GlossaryEntry> entries) {
        UIThreadsUtil.mustBeSwingThread();

        clear();

        if (entries == null) {
            return;
        }

        if (!entries.isEmpty() && Preferences.isPreference(Preferences.NOTIFY_GLOSSARY_HITS)) {
            scrollPane.notify(true);
        }

        nowEntries = entries;

        // If the TransTips is enabled then underline all the matched glossary
        // entries
        if (Preferences.isPreference(Preferences.TRANSTIPS)) {
            Core.getEditor().remarkOneMarker(TransTipsMarker.class.getName());
        }

        GlossaryEntry.StyledString buf = new GlossaryEntry.StyledString();
        for (GlossaryEntry entry : entries) {
            GlossaryEntry.StyledString str = entry.toStyledString();
            buf.append(str);
            buf.append("\n\n");
        }
        setText(buf.text.toString());
        StyledDocument doc = getStyledDocument();
        doc.setCharacterAttributes(0, doc.getLength(), NO_ATTRIBUTES, true); // remove old bold settings first
        for (int i = 0; i < buf.boldStarts.size(); i++) {
            doc.setCharacterAttributes(buf.boldStarts.get(i), buf.boldLengths.get(i), PRIORITY_ATTRIBUTES,
                    true);
        }
    }

    /** Clears up the pane. */
    @Override
    public void clear() {
        super.clear();
        nowEntries = Collections.emptyList();
    }

    List<GlossaryEntry> getDisplayedEntries() {
        return nowEntries;
    }

    /**
     * MouseListener for the GlossaryTextArea.
     */
    protected MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                JPopupMenu popup = new JPopupMenu();
                populateContextMenu(popup);
                Point p = e.getPoint();
                popup.show(GlossaryTextArea.this, p.x, p.y);
            }
        }
    };

    private void populateContextMenu(JPopupMenu popup) {
        boolean projectLoaded = Core.getProject().isProjectLoaded();

        final String selection = getSelectedText();
        JMenuItem item = popup.add(OStrings.getString("GUI_GLOSSARYWINDOW_insertselection"));
        item.setEnabled(projectLoaded && !StringUtil.isEmpty(selection));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Core.getEditor().insertText(selection);
            }
        });
        item = popup.add(OStrings.getString("GUI_GLOSSARYWINDOW_addentry"));
        item.setEnabled(projectLoaded);
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreateGlossaryEntryDialog();
            }
        });
    }

    public void showCreateGlossaryEntryDialog() {
        Frame parent = Core.getMainWindow().getApplicationFrame();
        showCreateGlossaryEntryDialog(parent);
    }

    public void showCreateGlossaryEntryDialog(final Frame parent) {
        CreateGlossaryEntryDialog d = createGlossaryEntryDialog;
        if (d != null) {
            d.requestFocus();
            return;
        }

        ProjectProperties props = Core.getProject().getProjectProperties();
        final File out = new File(props.getWriteableGlossary());

        final CreateGlossaryEntryDialog dialog = new CreateGlossaryEntryDialog(parent);
        String txt = dialog.getGlossaryFileText().getText();
        txt = MessageFormat.format(txt, out.getAbsolutePath());
        dialog.getGlossaryFileText().setText(txt);
        dialog.setVisible(true);
        dialog.getSourceText().requestFocus();

        dialog.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowLostFocus(WindowEvent e) {
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                String sel = null;
                Component component = parent.getMostRecentFocusOwner();
                if (component instanceof JTextComponent) {
                    sel = ((JTextComponent) component).getSelectedText();
                    if (!StringUtil.isEmpty(sel)) {
                        sel = EditorUtils.removeDirectionChars(sel);
                    }
                }
                if (!StringUtil.isEmpty(sel)) {
                    if (StringUtil.isEmpty(dialog.getSourceText().getText())) {
                        setText(dialog.getSourceText(), sel);
                    } else if (StringUtil.isEmpty(dialog.getTargetText().getText())) {
                        setText(dialog.getTargetText(), sel);
                    } else if (StringUtil.isEmpty(dialog.getCommentText().getText())) {
                        setText(dialog.getCommentText(), sel);
                    }
                }
            }
            
            private void setText(JTextComponent comp, String text) {
                comp.setText(text);
                comp.requestFocus();
                comp.selectAll();
            }
        });

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                createGlossaryEntryDialog = null;
                if (dialog.getReturnStatus() == CreateGlossaryEntryDialog.RET_OK) {
                    String src = StringUtil.normalizeUnicode(dialog.getSourceText().getText());
                    String loc = StringUtil.normalizeUnicode(dialog.getTargetText().getText());
                    String com = StringUtil.normalizeUnicode(dialog.getCommentText().getText());
                    if (!StringUtil.isEmpty(src) && !StringUtil.isEmpty(loc)) {
                        try {
                            GlossaryReaderTSV.append(out, new GlossaryEntry(src, loc, com, true));
                        } catch (Exception ex) {
                            Log.log(ex);
                        }
                    }
                }
            }
        });
        createGlossaryEntryDialog = dialog;
    }

    @Override
    public void populatePaneMenu(JPopupMenu menu) {
        populateContextMenu(menu);
        menu.addSeparator();
        final JMenuItem openFile = new JMenuItem(OStrings.getString("GUI_GLOSSARYWINDOW_SETTINGS_OPEN_FILE"));
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Core.getMainWindow().getMainMenu().invokeAction("projectAccessWriteableGlossaryMenuItem", e.getModifiers());
            }
        });
        openFile.setEnabled(false);
        if (Core.getProject().isProjectLoaded()) {
            String glossaryPath = Core.getProject().getProjectProperties().getWriteableGlossary();
            openFile.setEnabled(!StringUtil.isEmpty(glossaryPath) && new File(glossaryPath).isFile());
        }
        menu.add(openFile);
        menu.addSeparator();
        final JMenuItem notify = new JCheckBoxMenuItem(OStrings.getString("GUI_GLOSSARYWINDOW_SETTINGS_NOTIFICATIONS"));
        notify.setSelected(Preferences.isPreference(Preferences.NOTIFY_GLOSSARY_HITS));
        notify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences.setPreference(Preferences.NOTIFY_GLOSSARY_HITS, notify.isSelected());
            }
        });
        menu.add(notify);
    }

    public List<GlossaryEntry> getNowEntries() {
        return nowEntries;
    }
}
