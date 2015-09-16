package org.omegat.gui.clipboard;

import org.omegat.util.Log;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Сергей on 23.05.2015.
 */
public class ClipboardController {
    private static final Pattern PATTERN = Pattern.compile("\r?\n");

    protected ClipboardDialog dialog;
    protected Set<String> clipboardItems;
    protected StyledDocument document;
    private String selected;

    public ClipboardController(final ClipboardDialog dialog) {
        this.dialog = dialog;
        this.clipboardItems = new HashSet<>();
        this.document = dialog.getTextArea().getStyledDocument();

        addComponentListener(dialog);
        addTextAreaListener();

        dialog.setOkButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        dialog.setCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        dialog.getContentPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void addComponentListener(ClipboardDialog dialog) {
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                try {
                    updateStoredList();
                } catch (BadLocationException ex) {
                    Log.log(ex);
                }
            }
        });
    }

    private void addTextAreaListener() {
        dialog.getTextArea().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performAction();
                }
            }
        });
    }

    private void updateStoredList() throws BadLocationException {
        dialog.getTextArea().setText(null);
        //todo uncomment if selection from system CB needed
//        String fromSystem = getSelectionFromSystemClipboard();
//        if (fromSystem != null && !fromSystem.isEmpty()) {
//            clipboardItems.add(fromSystem);
//        }
        for (int i = clipboardItems.size(); i > 0; --i) {
            if (i != 1) {
                document.insertString(document.getLength(), clipboardItems.toArray(new String[clipboardItems.size()])[i - 1] + '\n', null);
            } else {
                document.insertString(document.getLength(), clipboardItems.toArray(new String[clipboardItems.size()])[i - 1], null);
            }
        }
        dialog.getTextArea().setCaretPosition(0);
    }

    private void performAction() {
        String[] lines = PATTERN.split(dialog.getTextArea().getText());
        int currentLine = TextUtils.getLineAtCaret(dialog.getTextArea());
        this.selected = lines[currentLine - 1];
        hideDialog();
    }

    private String getSelectionFromSystemClipboard() throws IOException, UnsupportedFlavorException {
        Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = systemClipboard.getContents(null);

        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String) contents.getTransferData(DataFlavor.stringFlavor);
        }
        return null;
    }

//    @Override
//    public void insertSelection(JTextComponent component, String string) {
//        int position = component.getCaretPosition();
//        ((JTextArea) component).insert(string, position);
//    }

    public Set<String> getStoredStrings() {
        return clipboardItems;
    }

    public void addString(String s) {
        clipboardItems.add(s);
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    private void onOK() {
        performAction();
        dialog.hideDialog();
    }

    private void onCancel() {
        selected = null;
        dialog.hideDialog();
    }

    public void showDialog() {
        dialog.showDialog();
    }

    public void hideDialog() {
        dialog.hideDialog();
    }
}
