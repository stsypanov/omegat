package org.omegat.gui.clipboard;

import org.omegat.gui.common.PeroFrame;
import org.omegat.util.Log;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: rad1kal
 * Date: 23.07.2014
 * Time: 11:18
 */
public class ClipboardTest extends PeroFrame {
    private JFrame owner;
    private static final String TEXT = "Use this text for clipboard test." +
            "Just place a caret within the text and press Ctrl+Shift+V";

    public ClipboardTest() throws HeadlessException {
        super("clipboard test");
        owner = this;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(600, 400));
        JTextArea comp = new JTextArea(TEXT);
        comp.setLineWrap(true);
        getContentPane().add(comp);
        ClipboardUtils.bind(comp, this);
        pack();
    }

    private void addClipboardListener(JTextComponent component){
        KeyBinding.bind(component, getAction(component));
    }

    private AbstractAction getAction(final JTextComponent component) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ctrl + shift + v");
                ClipboardDialog dialog = ClipboardDialog.getInstance(owner);
                dialog.showDialog();
                String selected = dialog.getSelected();
                if (component instanceof JTextArea){
                    ((JTextArea)component).insert(selected, component.getCaretPosition());
                    dialog.hideDialog();
                }
                if (component instanceof JEditorPane){
                    Document document = component.getDocument();
                    try {
                        document.insertString(component.getCaretPosition(), selected, null);
                        dialog.hideDialog();
                    } catch (BadLocationException ex) {
                        Log.log(ex);
                    }
                }
            }
        };
    }

    static class KeyBinding{

        public static void bind(JComponent component, AbstractAction action) {
            InputMap im = component.getInputMap(JComponent.WHEN_FOCUSED);
            ActionMap am = component.getActionMap();
            String key = "insert from clipboard";
            KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.SHIFT_DOWN_MASK + KeyEvent.CTRL_DOWN_MASK, true);
            im.put(keyStroke, key);
            am.put(key, action);

        }
    }

    public static void main(String[] args) {
        ClipboardTest test = new ClipboardTest();
        test.setVisible(true);
    }


}
