/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2013 Zoltan Bartko, Aaron Madlon-Kay
               2014-2015 Aaron Madlon-Kay
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

package org.omegat.gui.editor.autocompleter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.BadLocationException;

import org.omegat.gui.editor.EditorTextArea3;
import org.omegat.gui.editor.TagAutoCompleterView;
import org.omegat.gui.editor.autotext.AutotextAutoCompleterView;
import org.omegat.gui.editor.chartable.CharTableAutoCompleterView;
import org.omegat.gui.glossary.GlossaryAutoCompleterView;
import org.omegat.util.Log;
import org.omegat.util.OStrings;
import org.omegat.util.Platform;
import org.omegat.util.Preferences;
import org.omegat.util.StaticUtils;
import org.omegat.util.StringUtil;

/**
 * The controller part of the auto-completer
 * 
 * @author Zoltan Bartko (bartkozoltan@bartkozoltan.com)
 * @author Aaron Madlon-Kay
 */
public class AutoCompleter implements IAutoCompleter {    
    
    private final static boolean ON_MAC = Platform.isMacOSX();

    // On OS X: Esc (matches system word autocomplete; Cmd+Space conflicts with IME)
    // Otherwise: Ctrl+Space (matches input assist in some IDEs, etc.)
    private final static int TRIGGER_KEY = ON_MAC ? KeyEvent.VK_ESCAPE : KeyEvent.VK_SPACE;
    private final static int TRIGGER_KEY_MASK = ON_MAC ? 0 : Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    // On OS X: Cmd+Left / Cmd+Right
    // Otherwise: Ctrl+Space / Ctrl+Shift+Space
    private final static int GO_NEXT_KEY = ON_MAC ? KeyEvent.VK_RIGHT : KeyEvent.VK_SPACE;
    private final static int GO_NEXT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private final static int GO_PREV_KEY = ON_MAC ? KeyEvent.VK_LEFT : KeyEvent.VK_SPACE;
    private final static int GO_PREV_MASK = ON_MAC ? GO_NEXT_MASK : GO_NEXT_MASK | KeyEvent.SHIFT_MASK;
    
    private final static int MIN_VIEWPORT_HEIGHT = 50;
    private final static int MAX_POPUP_WIDTH = 500;
    
    private JPopupMenu popup;
    private EditorTextArea3 editor;

    public final static int PAGE_ROW_COUNT = 10;
    
    boolean didPopUpAutomatically = false;
    
    /**
     * a list of the views associated with this auto-completer
     */
    private final List<AbstractAutoCompleterView> views;
    
    /**
     * the current view
     */
    int currentView = -1;
    
    JScrollPane scroll;
    JLabel viewLabel;
    
    public AutoCompleter(EditorTextArea3 editor) { 
        this.editor = editor; 
        
        scroll = new JScrollPane();
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        scroll.setPreferredSize(new Dimension(200, 200));
        scroll.setColumnHeaderView(null);
        scroll.setFocusable(false);
 
        scroll.getVerticalScrollBar().setFocusable(false); 
        scroll.getHorizontalScrollBar().setFocusable(false); 
        
        // add any views here
        views = new ArrayList<>(5);
        addView(new GlossaryAutoCompleterView());
        addView(new AutotextAutoCompleterView());
        addView(new TagAutoCompleterView());
        addView(new CharTableAutoCompleterView());

        viewLabel = new JLabel();
        viewLabel.setOpaque(true);

        popup = new JPopupMenu();
        popup.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        popup.setLayout(new BorderLayout());
        popup.add(scroll, BorderLayout.CENTER); 
        popup.add(viewLabel, BorderLayout.SOUTH);
    }

    @Override
    public void addView(AbstractAutoCompleterView view) {
        view.setParent(this);
        views.add(view);
    }

    EditorTextArea3 getEditor() {
        return editor;
    }
    
    /**
     * Process the autocompletion keys
     * @param e the key event to process
     * @return true if a key has been processed, false if otherwise.
     */
    public boolean processKeys(KeyEvent e) {
        
        if (!isVisible() && StaticUtils.isKey(e, TRIGGER_KEY, TRIGGER_KEY_MASK)) {

            if (!editor.isInActiveTranslation(editor.getCaretPosition())) {
                return false;
            }

            if (!canBecomeVisible()) {
                return false;
            }

            updatePopup(false);
            setVisible(true);
            
            return true;
        }
        
        if (isVisible()) {
            if (getCurrentView().processKeys(e)) {
                return true;
            }
            
            if ((StaticUtils.isKey(e, KeyEvent.VK_ENTER, 0))) {
                doSelection();
                return true;
            }
            
            if ((StaticUtils.isKey(e, KeyEvent.VK_INSERT, 0))) {
                acceptedListItem(getSelectedValue()); 
                updatePopup(false);
                return true;
            }

            if ((StaticUtils.isKey(e, KeyEvent.VK_ESCAPE, 0))) {
                setVisible(false);
                return true;
            }
            
            if (StaticUtils.isKey(e, GO_PREV_KEY, GO_PREV_MASK)) {
                selectPreviousView();
                return true;
            }
            
            if (StaticUtils.isKey(e, GO_NEXT_KEY, GO_NEXT_MASK)) {
                selectNextView();
                return true;
            }
        }
        
        // otherwise
        return false;
    }
    
    public void doSelection() {
        acceptedListItem(getSelectedValue());
        if (getCurrentView().shouldCloseOnSelection()) {
            setVisible(false);
        }
    }
    
    /**
     * Returns the currently selected value.
     * @return 
     */
    private AutoCompleterItem getSelectedValue() {
        return views.get(currentView).getSelectedValue();
    }
       
    /**
     * Show the popup list.
     */
    public void updatePopup(boolean onlyIfVisible) {
        if (onlyIfVisible && !isVisible()) {
            return;
        }

        if (!canBecomeVisible()) {
            return;
        }

        AbstractAutoCompleterView view = getCurrentView();
        
        view.updateViewData();
        scroll.setPreferredSize(new Dimension(Math.min(view.getPreferredWidth(), MAX_POPUP_WIDTH),
                Math.max(view.getPreferredHeight(), MIN_VIEWPORT_HEIGHT)));
        popup.validate();
        popup.pack();
        if (isVisible()) {
            Point p = getDisplayPoint();
            popup.show(editor, p.x, p.y);
        }
        view.onShow();
    }
    
    /**
     * Determine the x,y coordinate at which to place the popup.
     */
    private Point getDisplayPoint() {
        int x = 0;
        int y = editor.getHeight();
        int fontSize = editor.getFont().getSize();
        try {
            int pos = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            x = editor.getUI().modelToView(editor, pos).x;
            y = editor.getUI().modelToView(editor, editor.getCaret().getDot()).y + fontSize;
        } catch(BadLocationException e) {
            // this should never happen!!!
            Log.log(e);
        }
        return new Point(x, y);
    }

    /**
     * Replace the text in the editor with the accepted item.
     * @param selected 
     */
    protected void acceptedListItem(AutoCompleterItem selected) { 
        if (selected == null) {
            return;
        }

        int offset = editor.getCaretPosition();

        String selection = editor.getSelectedText();

        if (StringUtil.isEmpty(selection)) {
            editor.setSelectionStart(offset - selected.replacementLength);
            editor.setSelectionEnd(offset);
        }
        editor.replaceSelection(selected.payload);
        
        if (selected.cursorAdjust != 0) {
            editor.getCaret().setDot(editor.getCaretPosition() + selected.cursorAdjust);
        }
        
        if (selected.keepSelection) {
            editor.replaceSelection(selection);
        }
    }

    /**
     * get the view number of the next view
     * @return the number
     */
    private int nextViewNumber(int start) {
        for (int n = 1; n <= views.size(); n++) {
            int index = (start + n) % views.size();
            if (views.get(index).isEnabled()) {
                return index;
            }
        }
        return -1;
    }
    
    /**
     * Get the view number of the previous view.
     * @return 
     */
    private int prevViewNumber(int start) {
        for (int n = 1; n <= views.size(); n++) {
            int index = (currentView + views.size() - n) % views.size();
            if (views.get(index).isEnabled()) {
                return index;
            }
        }
        return -1;
    }
    
    /**
     * Update the view label
     */
    private void updateViewLabel() {
        StringBuilder sb = new StringBuilder("<html><b>");
        sb.append(getCurrentView().getName());
        sb.append("</b>");
        
        if (views.size() != 1) {
            String nextKeyString = keyText(GO_NEXT_KEY, GO_NEXT_MASK);
            String prevKeyString = keyText(GO_PREV_KEY, GO_PREV_MASK);
            
            int nextViewN = nextViewNumber(currentView);
            if (views.size() >= 2 && nextViewN != -1) {
                sb.append("<br>");
                sb.append(StringUtil.format(OStrings.getString("AC_NEXT_VIEW"),
                        nextKeyString,
                        views.get(nextViewN).getName()));
            }
            
            int prevViewN = prevViewNumber(currentView);
            if (views.size() > 2 && prevViewN != -1) {
                sb.append("<br>");
                sb.append(StringUtil.format(OStrings.getString("AC_PREV_VIEW"),
                        prevKeyString,
                        views.get(prevViewN).getName()));
            }
        }
        sb.append("</html>");
        
        viewLabel.setText(sb.toString());
    }

    public AbstractAutoCompleterView getCurrentView() {
        return views.get(currentView);
    }
    
    /** go to the next view */
    private boolean selectNextView() {
        int nextViewN = nextViewNumber(currentView);
        if (nextViewN == -1) {
            return false;
        }
        currentView = nextViewN;
        activateView(true);
        return true;
    }

    /** activate the current view */
    private void activateView(boolean onlyIfVisible) {
        scroll.setViewportView(getCurrentView().getViewContent());
        updateViewLabel();
        updatePopup(onlyIfVisible);
    }
    
    /** select the previous view */
    private boolean selectPreviousView() {
        int prevViewN = prevViewNumber(currentView);
        if (prevViewN == -1) {
            return false;
        }
        currentView = prevViewN;
        activateView(true);
        return true;
    }

    public boolean isVisible() {
        return popup.isVisible();
    }

    public void setVisible(boolean isVisible) {
        if (isVisible) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Point p = getDisplayPoint();
                    popup.show(editor, p.x, p.y);
                    editor.requestFocus();
                }
            });
        } else {
            popup.setVisible(false);
            didPopUpAutomatically = false;
        }
    }
    
    private boolean canBecomeVisible() {
        return currentView != -1 || selectNextView();
    }

    /**
     * get the key text
     * @param base
     * @param modifier
     * @return 
     */
    public String keyText(int base, int modifier) {
         return KeyEvent.getKeyModifiersText(modifier) + '+' + KeyEvent.getKeyText(base);
    }
    
    public void textDidChange() {
        if (isVisible() && !didPopUpAutomatically) {
            updatePopup(true);
            return;
        }
        if (!Preferences.isPreference(Preferences.AC_SHOW_SUGGESTIONS_AUTOMATICALLY)) {
            return;
        }
        
        if (!canBecomeVisible()) {
            return;
        }

        // Cycle through each view, stopping and showing it if it has some relevant content.
        int i = currentView;
        while (true) {
            if (views.get(i).shouldPopUp()) {
                currentView = i;
                didPopUpAutomatically = true;
                activateView(false);
                setVisible(true);
                return;
            }
            i = nextViewNumber(i);
            if (i == currentView) {
                // We made it full circle with no results.
                break;
            }
        }
        // If we get here, no views had relevant content, so we close the popup.
        setVisible(false);
    }
}
