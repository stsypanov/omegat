/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2009 Alex Buloichik
               2009 Didier Briel
               2010 Wildrich Fourie
               2013 Zoltan Bartko
               2014 Aaron Madlon-Kay
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

package org.omegat.gui.editor;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.Utilities;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.data.ProtectedPart;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.clipboard.ClipboardUtils;
import org.omegat.gui.dictionaries.DictionaryPopupController;
import org.omegat.gui.dictionaries.VoidCallback;
import org.omegat.gui.editor.autocompleter.AutoCompleter;
import org.omegat.gui.main.IMainWindow;
import org.omegat.gui.main.MainWindow;
import org.omegat.gui.popup.*;
import org.omegat.gui.popup.PopupFactory;
import org.omegat.gui.popup.dictionary.DictionaryPopup;
import org.omegat.util.Log;
import org.omegat.util.Platform;
import org.omegat.util.Log;
import org.omegat.util.StaticUtils;
import org.omegat.util.StringUtil;
import org.omegat.util.gui.DockingUI;
import org.omegat.util.gui.SwingUtils;

/**
 * Changes of standard JEditorPane implementation for support custom behavior.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Didier Briel
 * @author Wildrich Fourie
 * @author Zoltan Bartko
 */
@SuppressWarnings("serial")
public class EditorTextArea3 extends JEditorPane {

    /** Undo Manager to store edits */
    protected final TranslationUndoManager undoManager = new TranslationUndoManager(this);

    protected final EditorController controller;

    protected final List<PopupMenuConstructorInfo> popupConstructors = new ArrayList<>();

    protected String currentWord;

    protected AutoCompleter autoCompleter;
    private DictionaryPopupController dictionaryPopupController;

    /**
     * Whether or not we are confining the cursor to the editable part of the
     * text area. The user can optionally allow the caret to roam freely.
     *
     * @see {@link #checkAndFixCaret(boolean)}
     */
    protected boolean lockCursorToInputArea = true;

    public EditorTextArea3(EditorController controller) {
        this.controller = controller;
        setEditorKit(new StyledEditorKit() {
            @Override
            public ViewFactory getViewFactory() {
                return factory3;
            }

            @Override
            protected void createInputAttributes(Element element, MutableAttributeSet set) {
                set.removeAttributes(set);
                EditorController c = EditorTextArea3.this.controller;
                try {
                    c.m_docSegList[c.displayedEntryIndex].createInputAttributes(element, set);
                } catch (Exception ex) {
                }
            }
        });

        addDictionaryAction();

        addMouseListener(mouseListener);

        ClipboardUtils.bind(this, Core.getMainWindow().getApplicationFrame());

        addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                try {
                    int start = EditorUtils.getWordStart(EditorTextArea3.this, e.getMark());
                    int end = EditorUtils.getWordEnd(EditorTextArea3.this, e.getMark());
                    if (end - start <= 0) {
                        // word not defined
                        return;
                    }
                    String newWord = getText(start, end - start);
                    if (!newWord.equals(currentWord)) {
                        currentWord = newWord;
                        CoreEvents.fireEditorNewWord(newWord);
                    }
                } catch (BadLocationException ex) {
                    Log.log(ex);
                }
            }
        });
        setToolTipText("");
        setDragEnabled(true);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        Document3 doc = getOmDocument();
        if (doc != null) {
            doc.setFont(font);
        }
    }

    private void addDictionaryAction() {
       addKeyListener(new KeyAdapter() {
           private static final int INTERVAL = 250;
           private long lastShiftStroke;

           @Override
           public void keyReleased(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                   boolean suits = System.currentTimeMillis() - lastShiftStroke <= INTERVAL;
                   if (suits) {
                       final DictionaryPopup popup = new DictionaryPopup();
                       new DictionaryPopupController(popup, Core.getDictionariesTextArea());

                       final Popup parentPopup = PopupFactory.showPopupCentered(EditorTextArea3.this, new ContentProvider<JComponent>() {
                           @Override
                           public JComponent getContent() {
                               return popup.getRoot();
                           }
                       });

                       popup.setParentPopupCallback(new VoidCallback() {
                           @Override
                           public void execute() {
                               parentPopup.hide();
                           }
                       });
                   }
                   lastShiftStroke = System.currentTimeMillis();
               }
           }
       });
    }

    /**
     * Return OmDocument instead just a Document. If editor was not initialized
     * with OmDocument, it will contains other Document implementation. In this
     * case we don't need it.
     */
    public Document3 getOmDocument() {
        try {
            return (Document3) getDocument();
        } catch (ClassCastException ex) {
            return null;
        }
    }

    /**
     * Return true if the specified position is within the active translation
     * @param position
     * @return 
     */
    public boolean isInActiveTranslation(int position) {
        return (position >= getOmDocument().getTranslationStart()
                && position <= getOmDocument().getTranslationEnd());
    }

    protected MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            getAutoCompleter().setVisible(false);
            
            // where is the mouse
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                int mousepos = viewToModel(e.getPoint());
                boolean changed = controller.goToSegmentAtLocation(getCaretPosition());
                if (!changed) {
                    if (selectTag(mousepos)) {
                        e.consume();
                    }
                }
            }
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                int mousepos = viewToModel(e.getPoint());
                JPopupMenu popup = makePopupMenu(mousepos);
                if (popup.getComponentCount() > 0) {
                    popup.show(EditorTextArea3.this, (int) e.getPoint().getX(), (int) e.getPoint().getY());
                }
            }
        }
    };

    private JPopupMenu makePopupMenu(int pos) {

        PopupMenuConstructorInfo[] cons;
        synchronized (popupConstructors) {
            /**
             * Copy constructors - for disable blocking in the processing
             * time.
             */
            cons = popupConstructors.toArray(new PopupMenuConstructorInfo[popupConstructors.size()]);
        }

        int ae = controller.displayedEntryIndex;
        SegmentBuilder sb = controller.m_docSegList[ae];

        boolean isInActiveEntry = sb.isActive() && pos >= sb.getStartPosition() && pos <= sb.getEndPosition();

        JPopupMenu popup = new JPopupMenu();
        for (PopupMenuConstructorInfo c : cons) {
            // call each constructor
            c.constructor.addItems(popup, EditorTextArea3.this, pos, isInActiveEntry,
                    isInActiveTranslation(pos), sb);
        }

        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

                List<JPopupMenu> popups = SwingUtils.getDescendantsOfType(JPopupMenu.class, (Container) c, true);

                for (JPopupMenu p : popups) {
                    p.setVisible(false);
                }
            }
        };

        popup.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        popup.getActionMap().put("ESCAPE", escapeAction);

        DockingUI.removeUnusedMenuSeparators(popup);

        return popup;
    }

    /**
     * Add new constructor into list and sort full list by priority.
     */
    protected void registerPopupMenuConstructors(int priority, IPopupMenuConstructor constructor) {
        synchronized (popupConstructors) {
            popupConstructors.add(new PopupMenuConstructorInfo(priority, constructor));
            Collections.sort(popupConstructors, new Comparator<PopupMenuConstructorInfo>() {
                @Override
                public int compare(PopupMenuConstructorInfo o1, PopupMenuConstructorInfo o2) {
                    return o1.priority - o2.priority;
                }
            });
        }
    }

    /**
     * Redefine some keys behavior. We can't use key listeners, because we have
     * to make something AFTER standard keys processing.
     */
    @Override
    protected void processKeyEvent(KeyEvent e) {
        int keyEvent = e.getID();
        if (keyEvent == KeyEvent.KEY_RELEASED) {
            // key released
            super.processKeyEvent(e);
            return;
        } else if (keyEvent == KeyEvent.KEY_TYPED) {
            //key typed
            super.processKeyEvent(e);
            return;
        }

        boolean processed = false;

        boolean mac = Platform.isMacOSX();

        Document3 doc = getOmDocument();

        // non-standard processing
        if (getAutoCompleter().processKeys(e)) {
            // The AutoCompleter needs special treatment.
            processed = true;
        } else if (StaticUtils.isKey(e, KeyEvent.VK_CONTEXT_MENU, 0)
                || (mac && StaticUtils.isKey(e, KeyEvent.VK_ESCAPE, InputEvent.SHIFT_MASK))) {
            // Context Menu key for contextual (right-click) menu (Shift+Esc on Mac)
            JPopupMenu popup = makePopupMenu(getCaretPosition());
            if (popup.getComponentCount() > 0) {
                popup.show(EditorTextArea3.this,
                        (int) getCaret().getMagicCaretPosition().getX(),
                        (int) getCaret().getMagicCaretPosition().getY());
                processed = true;
            }
        } else if (StaticUtils.isKey(e, KeyEvent.VK_TAB, 0)) {
            // press TAB when 'Use TAB to advance'
            if (controller.settings.isUseTabForAdvance()) {
                controller.nextEntry();
                processed = true;
            }
        } else if (StaticUtils.isKey(e, KeyEvent.VK_TAB, InputEvent.SHIFT_MASK)) {
            // press Shift+TAB when 'Use TAB to advance'
            if (controller.settings.isUseTabForAdvance()) {
                controller.prevEntry();
                processed = true;
            }
        } else if (StaticUtils.isKey(e, KeyEvent.VK_ENTER, 0)) {
            // press ENTER
            if (!controller.settings.isUseTabForAdvance()) {
                controller.nextEntry();
                processed = true;
            } else {
                IMainWindow mainWindow = Core.getMainWindow();
                // Timed warning is not available for console window
                if (mainWindow instanceof MainWindow) {
                    MainWindow window = (MainWindow) mainWindow;
                    window.showTimedStatusMessageRB("ETA_WARNING_TAB_ADVANCE");
                }
                processed = true;
            }
        } else if ((StaticUtils.isKey(e, KeyEvent.VK_ENTER,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))) {
            // press Ctrl+ENTER (Cmd+Enter for MacOS)
            if (!controller.settings.isUseTabForAdvance()) {
                controller.prevEntry();
                processed = true;
            }
        } else if (StaticUtils.isKey(e, KeyEvent.VK_ENTER, InputEvent.SHIFT_MASK)) {
            // convert Shift+Enter event to straight enter key
            KeyEvent ke = new KeyEvent(e.getComponent(), e.getID(), e.getWhen(), 0, KeyEvent.VK_ENTER, '\n');
            super.processKeyEvent(ke);
            processed = true;
        } else if (StaticUtils.isKey(e, KeyEvent.VK_A,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {
            // handling Ctrl+A manually (Cmd+A for MacOS)
            setSelectionStart(doc.getTranslationStart());
            setSelectionEnd(doc.getTranslationEnd());
            processed = true;
        } else if (StaticUtils.isKey(e, KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)) {
            // handle Ctrl+Shift+O - toggle orientation LTR-RTL
            Cursor oldCursor = getCursor();
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            controller.toggleOrientation();
            setCursor(oldCursor);

            IMainWindow mainWindow = Core.getMainWindow();
            // Timed warning is not available for console window
            if (mainWindow instanceof MainWindow) {
                MainWindow window = (MainWindow) mainWindow;
                window.showTimedStatusMessageRB("ETA_INFO_TOGGLE_LTR_RTL");
            }
            processed = true;
        } else if (StaticUtils.isKey(e, KeyEvent.VK_BACK_SPACE,
                mac ? InputEvent.ALT_MASK : InputEvent.CTRL_MASK)) {
            // handle Ctrl+Backspace (Alt+Backspace for MacOS)
            try {
                processed = wholeTagDelete(false);
                if (!processed) {
                    int offset = getCaretPosition();
                    int prevWord = Utilities.getPreviousWord(this, offset);
                    int c = Math.max(prevWord, doc.getTranslationStart());
                    setSelectionStart(c);
                    setSelectionEnd(offset);
                    replaceSelection("");

                    processed = true;
                }
            } catch (BadLocationException ex) {
                // do nothing
            }
        } else if (StaticUtils.isKey(e, KeyEvent.VK_DELETE,
                mac ? InputEvent.ALT_MASK : InputEvent.CTRL_MASK)) {
            // handle Ctrl+Backspace (Alt+Delete for MacOS)
            try {
                processed = wholeTagDelete(true);
                if (!processed) {
                    int offset = getCaretPosition();
                    int nextWord = Utilities.getNextWord(this, offset);
                    int c = Math.min(nextWord, doc.getTranslationEnd());
                    setSelectionStart(offset);
                    setSelectionEnd(c);
                    replaceSelection("");

                    processed = true;
                }
            } catch (BadLocationException ex) {
                // do nothing
            }
        } else if (StaticUtils.isKey(e, KeyEvent.VK_PAGE_UP,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {
            // Ctrl+PgUp - to the begin of document(Cmd+PgUp for MacOS)
            setCaretPosition(0);
            processed = true;
        } else if (StaticUtils.isKey(e, KeyEvent.VK_PAGE_DOWN,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {
            // Ctrl+PgDn - to the end of document(Cmd+PgDn for MacOS)
            setCaretPosition(getOmDocument().getLength());
            processed = true;
        } else if (StaticUtils.isKey(e, KeyEvent.VK_LEFT, mac ? InputEvent.ALT_MASK : InputEvent.CTRL_MASK)
                || StaticUtils.isKey(e, KeyEvent.VK_LEFT, (mac ? InputEvent.ALT_MASK : InputEvent.CTRL_MASK) | InputEvent.SHIFT_MASK)) {
            // Ctrl+Left - skip to the end of tag (Alt+Left for MacOS)
            processed = moveCursorOverTag((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0, false);
        } else if (StaticUtils.isKey(e, KeyEvent.VK_RIGHT, mac ? InputEvent.ALT_MASK : InputEvent.CTRL_MASK)
                || StaticUtils.isKey(e, KeyEvent.VK_RIGHT, (mac ? InputEvent.ALT_MASK : InputEvent.CTRL_MASK) | InputEvent.SHIFT_MASK)) {
            // Ctrl+Right - skip to the end of tag (Alt+Right for MacOS)
            processed = moveCursorOverTag((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0, true);
        } else if (StaticUtils.isKey(e, KeyEvent.VK_F2, 0)) {
            boolean lockEnabled = !lockCursorToInputArea;
            final String key = lockEnabled ? "MW_STATUS_CURSOR_LOCK_ON" : "MW_STATUS_CURSOR_LOCK_OFF";
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Core.getMainWindow().showStatusMessageRB(key);
                }
            });
            lockCursorToInputArea = lockEnabled;
        }

        // leave standard processing if need
        if (processed) {
            e.consume();
        } else {
            if ((e.getModifiers() & (KeyEvent.CTRL_MASK | KeyEvent.META_MASK | KeyEvent.ALT_MASK)) == 0) {
                // there is no Alt,Ctrl,Cmd keys, i.e. it's char
                if (e.getKeyCode() != KeyEvent.VK_SHIFT && !isNavigationKey(e.getKeyCode())) {
                    // it's not a single 'shift' press or navigation key
                    // fix caret position prior to inserting character
                    checkAndFixCaret(true);
                }
            }
            super.processKeyEvent(e);
            //note that the translation start/end position are not updated yet. This has been updated when then keyreleased event occurs. 
        }

        // some after-processing catches
        if (!processed && e.getKeyChar() != 0 && isNavigationKey(e.getKeyCode())) {
            //if caret is moved over existing chars, check and fix caret position
            checkAndFixCaret(false); //works only in after-processing if translation length (start and end position) has not changed, because start and end position are not updated yet.
            autoCompleter.updatePopup();
        }
    }

    private boolean isNavigationKey(int keycode) {
        switch (keycode) {
        // if caret is moved over existing chars, check and fix caret position
        case KeyEvent.VK_HOME:
        case KeyEvent.VK_END:
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_KP_LEFT:
        case KeyEvent.VK_KP_RIGHT:
        case KeyEvent.VK_KP_UP:
        case KeyEvent.VK_KP_DOWN:
            return true;
        }
        return false;
    }

    /**
     * Move cursor over tag(possible, with selection)
     * 
     * @param withShift
     *            true if selection need
     * @param checkTagStart
     *            true if check tag start, false if check tag end
     * @return true if tag processed
     */
    boolean moveCursorOverTag(boolean withShift, boolean checkTagStart) {
        Document3 doc = getOmDocument();
        SourceTextEntry ste = doc.controller.getCurrentEntry();
        String text = doc.extractTranslation();
        int off = getCaretPosition() - doc.getTranslationStart();
        // iterate by 'protected parts'
        if (ste != null) {
            for (ProtectedPart pp : ste.getProtectedParts()) {
                if (checkTagStart) {
                    if (StringUtil.isSubstringAfter(text, off, pp.getTextInSourceSegment())) {
                        int pos = off + doc.getTranslationStart() + pp.getTextInSourceSegment().length();
                        if (withShift) {
                            getCaret().moveDot(pos);
                        } else {
                            getCaret().setDot(pos);
                        }
                        return true;
                    }
                } else {
                    if (StringUtil.isSubstringBefore(text, off, pp.getTextInSourceSegment())) {
                        int pos = off + doc.getTranslationStart() - pp.getTextInSourceSegment().length();
                        if (withShift) {
                            getCaret().moveDot(pos);
                        } else {
                            getCaret().setDot(pos);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Whole tag delete before or after cursor
     * 
     * @param checkTagStart
     *            true if check tag start, false if check tag end
     * @return true if tag deleted
     */
    boolean wholeTagDelete(boolean checkTagStart) throws BadLocationException {
        Document3 doc = getOmDocument();
        SourceTextEntry ste = doc.controller.getCurrentEntry();
        String text = doc.extractTranslation();
        int off = getCaretPosition() - doc.getTranslationStart();
        // iterate by 'protected parts'
        if (ste != null) {
            for (ProtectedPart pp : ste.getProtectedParts()) {
                if (checkTagStart) {
                    if (StringUtil.isSubstringAfter(text, off, pp.getTextInSourceSegment())) {
                        int pos = off + doc.getTranslationStart();
                        doc.remove(pos, pp.getTextInSourceSegment().length());
                        return true;
                    }
                } else {
                    if (StringUtil.isSubstringBefore(text, off, pp.getTextInSourceSegment())) {
                        int pos = off + doc.getTranslationStart() - pp.getTextInSourceSegment().length();
                        doc.remove(pos, pp.getTextInSourceSegment().length());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Try to select full tag on specified position, in the source and
     * translation part of segment.
     * 
     * @param pos
     *            position
     * @return true if selected
     */
    boolean selectTag(int pos) {
        int s = controller.getSegmentIndexAtLocation(pos);
        if (s < 0) {
            return false;
        }
        SegmentBuilder segment = controller.m_docSegList[s];
        if (pos < segment.getStartPosition() || pos >= segment.getEndPosition()) {
            return false;
        }
        SourceTextEntry ste = getOmDocument().controller.getCurrentEntry();
        if (ste != null) {
            try {
                String text = getOmDocument().getText(segment.getStartPosition(),
                        segment.getEndPosition() - segment.getStartPosition());
                int off = pos - segment.getStartPosition();
                if (off < 0 || off >= text.length()) {
                    return false;
                }
                for (ProtectedPart pp : ste.getProtectedParts()) {
                    int p = -1;
                    while ((p = text.indexOf(pp.getTextInSourceSegment(), p + 1)) >= 0) {
                        if (p <= off && off < p + pp.getTextInSourceSegment().length()) {
                            p += segment.getStartPosition();
                            select(p, p + pp.getTextInSourceSegment().length());
                            return true;
                        }
                    }
                }
            } catch (BadLocationException ex) {
            }
        }
        return false;
    }

    /**
     * Checks whether the selection & caret is inside editable text, and changes
     * their positions accordingly if not. Convenience method for
     * {@link #checkAndFixCaret(boolean)} that always forcibly fixes the caret.
     */
    void checkAndFixCaret() {
        checkAndFixCaret(true);
    }

    /**
     * Checks whether the selection & caret is inside editable text, and changes
     * their positions accordingly if not.
     *
     * @param force
     *            When true, ignore {@link #lockCursorToInputArea} and always
     *            fix the caret even if the user has enabled free roaming
     */
    void checkAndFixCaret(boolean force) {
        if (!force && !lockCursorToInputArea) {
            return;
        }

        Document3 doc = getOmDocument();
        if (doc == null) {
            // doc is not active
            return;
        }
        if (!doc.isEditMode()) {
            return;
        }

        // int pos = m_editor.getCaretPosition();
        int spos = getSelectionStart();
        int epos = getSelectionEnd();
        int start = doc.getTranslationStart();
        int end = doc.getTranslationEnd();

        if (spos != epos) {
            // dealing with a selection here - make sure it's w/in bounds
            if (spos < start) {
                fixSelectionStart(start);
            } else if (spos > end) {
                fixSelectionStart(end);
            }
            if (epos > end) {
                fixSelectionEnd(end);
            } else if (epos < start) {
                fixSelectionStart(start);
            }
        } else {
            // non selected text
            if (spos < start) {
                setCaretPosition(start);
            } else if (spos > end) {
                setCaretPosition(end);
            }
        }
        getAutoCompleter().updatePopup();
    }

    /**
     * Need to use own implementation, because standard method moves caret at
     * the end.
     */
    private void fixSelectionStart(int start) {
        if (getCaretPosition() <= start) {
            // caret at the left - mark from ent to start
            setCaretPosition(getSelectionEnd());
            moveCaretPosition(start);
        } else {
            setSelectionStart(start);
        }
    }

    /**
     * Need to use own implementation, because standard method moves caret at
     * the end.
     */
    private void fixSelectionEnd(int end) {
        setSelectionEnd(end);
    }

    /**
     * Allow to paste into segment, even selection outside editable segment. In
     * this case selection will be truncated into segment's boundaries.
     */
    @Override
    public void paste() {
        checkAndFixCaret();
        super.paste();
    }

    /**
     * Allow to cut segment, even selection outside editable segment. In this
     * case selection will be truncated into segment's boundaries.
     */
    @Override
    public void cut() {
        checkAndFixCaret();
        super.cut();
    }

    /**
     * Remove invisible direction chars on the copy text into clipboard.
     */
    @Override
    public String getSelectedText() {
        String st = super.getSelectedText();
        return st != null ? EditorUtils.removeDirectionChars(st) : null;
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        int pos = viewToModel(event.getPoint());
        int s = controller.getSegmentIndexAtLocation(pos);
        return controller.markerController.getToolTips(s, pos);
    }

    public AutoCompleter getAutoCompleter(){
        if (autoCompleter ==null){
            autoCompleter = new AutoCompleter(this);
        }
        return autoCompleter;
    }

    /**
     * Factory for create own view.
     */
    public static ViewFactory factory3 = new ViewFactory() {
        @Override
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new ViewLabel(elem);
                    case AbstractDocument.ParagraphElementName:
                        return new ViewParagraph(elem);
                    case AbstractDocument.SectionElementName:
                        return new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(elem);
                    case StyleConstants.IconElementName:
                        return new IconView(elem);
                }
            }

            // default to text display
            return new ViewLabel(elem);
        }
    };

    private static class PopupMenuConstructorInfo {
        final int priority;
        final IPopupMenuConstructor constructor;

        public PopupMenuConstructorInfo(int priority, IPopupMenuConstructor constructor) {
            this.priority = priority;
            this.constructor = constructor;
        }
    }
}
