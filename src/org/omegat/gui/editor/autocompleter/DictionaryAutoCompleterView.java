package org.omegat.gui.editor.autocompleter;

import org.omegat.gui.dictionaries.Callback;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class DictionaryAutoCompleterView extends AbstractAutoCompleterView {
    public static final int CELL_HEIGHT = 12;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 60;

    private JPanel container;
    private JTextField textField;
    private JList<String> itemsList;
    private Callback<String> callback;

    /**
     * Creates a new auto-completer view.
     *
     * @param name the name of this view
     */
    public DictionaryAutoCompleterView() {
        super("");
        container = new JPanel(new BorderLayout());

        textField = new JTextField();
        textField.requestFocus();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
                    itemsList.requestFocus();
                } else {
                    textField.requestFocus();
                }
                if (keyCode == KeyEvent.VK_ENTER){
                    callback.execute(textField.getText());
                }
            }
        });

        itemsList = new JList<>();
        itemsList.setFixedCellWidth(WIDTH - 50);
        itemsList.setCellRenderer(new CellRenderer());
        itemsList.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_BACK_SPACE){
                    textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                    textField.requestFocus();
                } else if (Character.isLetter(keyChar)){
                    textField.setText(textField.getText() + keyChar);
                    textField.requestFocus();
                } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN){
                    itemsList.requestFocus();
                } else if (keyCode == KeyEvent.VK_ENTER){
                    callback.execute(itemsList.getSelectedValue());
                    hide();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    itemsList.requestFocus();
                } else {
                    textField.requestFocus();
                }
            }
        });

        container.add(new JScrollPane(textField), BorderLayout.NORTH);
        container.add(itemsList, BorderLayout.CENTER);
    }

    @Override
    public boolean processKeys(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            callback.execute(itemsList.getSelectedValue());
        }
        return false;
    }

    @Override
    public int getRowCount() {
        return itemsList.getVisibleRowCount();
    }

    @Override
    public int getPreferredHeight() {
        return 200;
    }

    @Override
    public int getPreferredWidth() {
        return 400;
    }

    @Override
    public AutoCompleterItem getSelectedValue() {
        return null;
    }

    @Override
    public boolean updateViewData() {
        return false;
    }

    @Override
    public Component getViewContent() {
        return container;
    }

    @Override
    public boolean shouldPopUp() {
        return false;
    }

    @Override
    public void onShow() {
        textField.requestFocusInWindow();
    }

    public void addKeyListener(KeyAdapter listener) {
        textField.addKeyListener(listener);
    }

    public void setCallback(Callback<String> callback) {
        this.callback = callback;
    }

    public String getText() {
        return textField.getText();
    }

    public void setModel(List<String> model) {
        String[] strings = model.toArray(new String[model.size()]);
        itemsList.setListData(strings);
    }

    public void redraw() {
        itemsList.repaint();
    }

    public void hide() {
        completer.setVisible(false);
    }

    private static class CellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, final Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            setText(String.valueOf(value));
            setBorder(new EmptyBorder(2, 10, 2, 10));
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }
}
