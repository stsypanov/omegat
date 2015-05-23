package org.omegat.gui.clipboard;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 29.07.2014
 * Time: 12:18
 */
public class ClipboardUtils {

    private static ClipboardController controller;

    //todo don't create controller every time
    public static void bind(JTextComponent component, Frame owner) {
        controller = new ClipboardController(ClipboardDialog.getInstance(owner));
        InputMap im = component.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = component.getActionMap();
        String key = "insert from clipboard";
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_DOWN_MASK, true);
        im.put(keyStroke, key);
        am.put(key, getAction(component, owner));

        bindCopyIntoClipboardItemsAction(im, am);

    }

    private static void bindCopyIntoClipboardItemsAction(InputMap im, ActionMap am) {
        String key1 = "copy-to-dialog-clipboard";
        KeyStroke keyStroke1 = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK, true);
        im.put(keyStroke1, key1);
        am.put(key1, new DefaultEditorKit.CopyAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable contents = systemClipboard.getContents(null);

                if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        String s = (String) contents.getTransferData(DataFlavor.stringFlavor);
                        controller.addString(s);
                    } catch (UnsupportedFlavorException | IOException ex) {
                        Log.severe("cannot paste into clipboard", ex);
                    }
                }
            }
        });
    }

    private static Action getAction(final JTextComponent component, final Frame owner) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialogAndPaste(component, owner);
            }
        };
    }

    private static void showDialogAndPaste(JTextComponent component, Frame owner) {
        controller.showDialog();
        String selected = controller.getSelected();
        if (component instanceof JTextArea) {
            ((JTextArea) component).insert(selected, component.getCaretPosition());
            controller.hideDialog();
        }
        if (component instanceof JEditorPane) {
            Document document = component.getDocument();
            try {
                document.insertString(component.getCaretPosition(), selected, null);
                controller.hideDialog();
            } catch (BadLocationException e) {
                Log.log(e);
            }
        }
        controller.setSelected(null);
    }
}
