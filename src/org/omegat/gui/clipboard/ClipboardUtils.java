package org.omegat.gui.clipboard;

import org.jetbrains.annotations.NotNull;
import org.omegat.util.Log;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
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
	public static final KeyStroke INSERT_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_DOWN_MASK, true);
	public static final KeyStroke KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK, true);
	public static final String KEY = "copy-to-dialog-clipboard";
	public static final String INSERT_KEY = "insert from clipboard";

	private static final DefaultEditorKit.CopyAction action = new DefaultEditorKit.CopyAction() {
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
	};

	public static void bind(JTextComponent component, Frame owner) {
		controller = getClipboardController(owner);
		InputMap im = component.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am = component.getActionMap();
		im.put(INSERT_STROKE, INSERT_KEY);
		am.put(INSERT_KEY, getAction(component));

		bindCopyIntoClipboardItemsAction(im, am);
	}

	@NotNull
	protected static ClipboardController getClipboardController(Frame owner) {
		if (controller == null) {
			controller = new ClipboardController(ClipboardDialog.getInstance(owner));
		}
		return controller;
	}

	private static void bindCopyIntoClipboardItemsAction(InputMap im, ActionMap am) {
		im.put(KEY_STROKE, KEY);
		am.put(KEY, action);
	}

	private static Action getAction(final JTextComponent component) {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDialogAndPaste(component);
			}
		};
	}

	private static void showDialogAndPaste(JTextComponent component) {
		controller.showDialog();
		String selected = controller.getSelected();
		if (selected != null) {
			if (component instanceof JTextArea) {
				((JTextArea) component).replaceRange(selected, component.getSelectionStart(), component.getSelectionEnd());
			} else if (component instanceof JEditorPane) {
				component.replaceSelection(selected);
			}
		}
		controller.hideDialog();
	}
}
