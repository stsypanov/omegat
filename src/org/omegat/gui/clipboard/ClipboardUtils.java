package org.omegat.gui.clipboard;

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
 * User: stsypanov
 * Date: 29.07.2014
 * Time: 12:18
 */
public class ClipboardUtils {

	public static void bind(JTextComponent component, Frame owner) {
		InputMap im = component.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am = component.getActionMap();
		String key = "insert from clipboard";
		KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_DOWN_MASK, true);
		im.put(keyStroke, key);
		am.put(key, getAction(component, owner));
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
		ClipboardDialog dialog = ClipboardDialog.getInstance(owner);
		dialog.showDialog();
		String selected = dialog.getSelected();
		if (component instanceof JTextArea) {
			((JTextArea) component).insert(selected, component.getCaretPosition());
			dialog.hideDialog();
		}
		if (component instanceof JEditorPane) {
			Document document = component.getDocument();
			try {
				document.insertString(component.getCaretPosition(), selected, null);
				dialog.hideDialog();
			} catch (BadLocationException e) {
				Log.log(e);
			}
		}
		dialog.setSelected(null);
	}
}
