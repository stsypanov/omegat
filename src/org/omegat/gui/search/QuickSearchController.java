package org.omegat.gui.search;

import org.omegat.gui.editor.EditorController;
import org.omegat.util.Log;
import org.omegat.util.gui.StaticUIUtils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;

/**
 * Created by Сергей on 17.05.2015.
 */
public class QuickSearchController {
    private final DefaultComboBoxModel<String> comboBoxModel;
    private QuickSearchPanel searchPanel;
    private final WordSearcher searcher;

    public QuickSearchController(final QuickSearchPanel searchPanel, final EditorController controller) {
        this.searchPanel = searchPanel;
        StaticUIUtils.setEscapeAction(searchPanel, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
        searchPanel.setCloseButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
        searcher = new WordSearcher(controller.getEditor());

        comboBoxModel = new DefaultComboBoxModel<>();
        searchPanel.getTextField().setModel(comboBoxModel);
        searchPanel.getTextField().getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchPanel.getText();
                if (text.isEmpty()) {
                    revertSearchFieldHighlight(searchPanel);
                }
                int offset = searcher.search(text);
                if (offset != -1) {
                    revertSearchFieldHighlight(searchPanel);
                    try {
                        controller.getEditor().scrollRectToVisible(controller.getEditor().modelToView(offset));
                    } catch (BadLocationException ex) {
                        Log.log(Level.SEVERE, "QuickSearchController.keyReleased()", ex);
                    }
                } else if (!text.isEmpty()) {
                    highlightTextFieldOnWrongInput(searchPanel);
                }
            }
        });
    }

    private void highlightTextFieldOnWrongInput(QuickSearchPanel searchPanel) {
        searchPanel.getTextField().setBackground(new Color(188, 58, 58));
        searchPanel.getTextField().getEditor().getEditorComponent().setForeground(Color.WHITE);
        ((JTextField) searchPanel.getTextField().getEditor().getEditorComponent()).setCaretColor(Color.WHITE);
    }

    private void revertSearchFieldHighlight(QuickSearchPanel searchPanel) {
        searchPanel.getTextField().setBackground(Color.WHITE);
        searchPanel.getTextField().getEditor().getEditorComponent().setForeground(Color.BLACK);
        ((JTextField) searchPanel.getTextField().getEditor().getEditorComponent()).setCaretColor(Color.BLACK);
    }

    public void show() {
        searchPanel.setVisible(true);
        searchPanel.getTextField().requestFocus();
    }

    public void hide() {
        searcher.removeAllHighlight();
        ComboBoxEditor editor = searchPanel.getTextField().getEditor();
        String string = editor.getItem().toString();
        comboBoxModel.addElement(string);
        ((JTextField) editor.getEditorComponent()).setText("");
        searchPanel.setVisible(false);
    }
}
