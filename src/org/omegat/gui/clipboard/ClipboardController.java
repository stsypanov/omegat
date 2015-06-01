package org.omegat.gui.clipboard;

import org.omegat.util.Log;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Сергей on 23.05.2015.
 */
public class ClipboardController implements Clibboard {
    private static final Pattern PATTERN = Pattern.compile("\r?\n");

    protected ClipboardDialog dialog;
    protected List<String> clipboardItems;
    protected StyledDocument document;
    private String selected;

    public ClipboardController(final ClipboardDialog dialog) {
        this.dialog = dialog;
        this.clipboardItems = new ArrayList<>();
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
                } catch (BadLocationException | UnsupportedFlavorException | IOException ex) {
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

    private void updateStoredList() throws BadLocationException, IOException, UnsupportedFlavorException {
        dialog.getTextArea().setText(null);
        String fromSystem = getSelectionFromSystemClipboard();
        if (fromSystem != null && !fromSystem.isEmpty() && !clipboardItems.contains(fromSystem)) {
            clipboardItems.add(fromSystem);
        }
        for (int i = clipboardItems.size(); i > 0; --i) {
            if (i != 1) {
                document.insertString(document.getLength(), clipboardItems.get(i - 1) + '\n', null);
            } else {
                document.insertString(document.getLength(), clipboardItems.get(i - 1), null);
            }
        }
        dialog.getTextArea().setCaretPosition(0);
    }

    private void performAction() {
        String[] lines = PATTERN.split(dialog.getTextArea().getText());
        int currentLine = TextUtils.getLineAtCaret(dialog.getTextArea());
        this.selected = lines[currentLine - 1];
        dialog.dispose();
    }

    private String getSelectionFromSystemClipboard() throws IOException, UnsupportedFlavorException {
        Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = systemClipboard.getContents(null);

        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String) contents.getTransferData(DataFlavor.stringFlavor);
        }
        return null;
    }

    @Override
    public void insertSelection(JTextComponent component, String string) {
        int position = component.getCaretPosition();
        ((JTextArea) component).insert(string, position);
    }

    @Override
    public List<String> getStoredStrings() {
        return clipboardItems;
    }

    @Override
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
        dialog.dispose();
    }

    private void onCancel() {
        dialog.dispose();
    }

    public void showDialog() {
        dialog.showDialog();
    }

    public void hideDialog() {
        dialog.hideDialog();
    }
}
