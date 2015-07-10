package org.omegat.gui.dictionaries;

import org.madlonkay.supertmxmerge.util.GuiUtil;
import org.omegat.gui.common.PeroFrame;
import org.omegat.util.OStrings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Created by Сергей on 18.04.2015.
 */
public class DictionaryPopup extends PeroFrame {
    public static final int CELL_HEIGHT = 12;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 60;

    private JPopupMenu popupMenu;
    private JList<String> container;
    private JTextField textField;
    private Callback<String> callback;

    public DictionaryPopup(String title) throws HeadlessException {
        super(title);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initComponents();
        setResizable(false);
        pack();
        GuiUtil.displayWindowCentered(this);
    }

    @Override
    public String getPreferenceBaseName() {
        return "dictionary_popup";
    }

    public DictionaryPopup() {
        this(OStrings.getString("find.in.dictionary.dialogue.title"));
    }

    private void initComponents() {
        container = new JList<>();
        container.addKeyListener(new KeyAdapter() {

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
                    container.requestFocus();
                } else if (keyCode == KeyEvent.VK_ENTER){
                    callback.execute(container.getSelectedValue());
                    hidePopup();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    container.requestFocus();
                } else {
                    textField.requestFocus();
                }
            }
        });

        container.setFixedCellWidth(WIDTH - 50);
        container.setCellRenderer(new CellRenderer());

        popupMenu = new JPopupMenu();
        popupMenu.setPreferredSize(new Dimension(WIDTH, CELL_HEIGHT));
        popupMenu.add(new JScrollPane(container));
        popupMenu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    hidePopup();
                }
            }
        });


        textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
                    container.requestFocus();
                } else {
                    textField.requestFocus();
                }
                if (keyCode == KeyEvent.VK_ENTER){
                    callback.execute(textField.getText());
                    popupMenu.setVisible(false);
                    setVisible(false);
                }
            }
        });
        getContentPane().add(textField);
        textField.requestFocus();
    }

    public void addKeyListener(KeyListener listener) {
        textField.addKeyListener(listener);
    }

    public JList getContainer() {
        return container;
    }

    public void setModel(List<String> keys) {
        String[] strings = keys.toArray(new String[keys.size()]);
        container.setListData(strings);
    }

    public void setCallback(Callback<String> callback) {
        this.callback = callback;
    }

    public void showPopup() {
        popupMenu.show(this, textField.getX(), textField.getY() + getHeight());
    }

    public void hidePopup() {
        popupMenu.setVisible(false);
        setVisible(false);
        dispose();
    }

    public String getText() {
        return textField.getText();
    }

    public void redraw() {
        int size = container.getModel().getSize();
        if (size == 0){
            popupMenu.setVisible(false);
        } else if (size <= 10){
            popupMenu.setPreferredSize(new Dimension(WIDTH, size * 20 + 10));
        } else {
            popupMenu.setPreferredSize(new Dimension(WIDTH, 200));
        }
        popupMenu.pack();
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
