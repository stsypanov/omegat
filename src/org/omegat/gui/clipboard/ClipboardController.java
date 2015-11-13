package org.omegat.gui.clipboard;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

/**
 * Created by ������ on 23.05.2015.
 */
public class ClipboardController {

	protected ClipboardDialog dialog;
	@Nullable
	private String selected;
	private ClipboadrListModel model;

	public ClipboardController(final ClipboardDialog dialog) {
		this.dialog = dialog;
		configureListSelectionListener(dialog.getList());

		addComponentListener(dialog);

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

	private void configureListSelectionListener(final JList<String> list) {
		list.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("rawtypes")
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int firstIndex = ((JList)e.getSource()).getSelectedIndex();
				String item = list.getModel().getElementAt(firstIndex);
				dialog.getTextArea().setText(item);
			}
		});
	}

	private void addComponentListener(ClipboardDialog dialog) {
		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				updateTextArea();
			}
		});
	}

	@SuppressWarnings("ObjectEquality")
	private void updateTextArea() {
		ClipboadrListModel model = getModel();
		if (dialog.getList().getModel() != model) {
			dialog.getList().setModel(model);
		}
//todo uncomment if selection from system CB needed
//        String fromSystem = getSelectionFromSystemClipboard();
//        if (fromSystem != null && !fromSystem.isEmpty()) {
//            clipboardItems.add(fromSystem);
//        }

		dialog.getTextArea().setText(model.getElementAt(0));
		dialog.getList().revalidate();
	}

	private ClipboadrListModel getModel() {
		if (model == null) {
			model = new ClipboadrListModel();
		}
		return model;
	}

	private void performAction() {
		selected = dialog.getList().getSelectedValue();
		hideDialog();
	}

//todo uncomment if selection from system CB needed
//	private String getSelectionFromSystemClipboard() throws IOException, UnsupportedFlavorException {
//		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//		Transferable contents = systemClipboard.getContents(null);
//
//		if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//			return (String) contents.getTransferData(DataFlavor.stringFlavor);
//		}
//		return null;
//	}


	public void addString(String s) {
		getModel().insert(s);
	}

	@Nullable
	public String getSelected() {
		return selected;
	}

	public void setSelected(@Nullable String selected) {
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
