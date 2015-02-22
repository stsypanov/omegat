package org.omegat.gui.clipboard;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.omegat.gui.common.PeroDialog;
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
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 23.07.2014
 * Time: 11:18
 */

public class ClipboardDialog extends PeroDialog implements Clibboard {
	private static final Pattern PATTERN = Pattern.compile("\r?\n");

	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextPane textArea;

	private String selected;
	private List<String> storedStrings;
	private StyledDocument doc;
	private static ClipboardDialog instance;

	public static ClipboardDialog getInstance(Frame owner) {
		if (instance == null) {
			instance = new ClipboardDialog(owner);
		}
		return instance;
	}

	private ClipboardDialog(Frame owner) {
		super(owner);
		setTitle("Clipboard");
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);
		setPreferredSize(new Dimension(600, 400));
		pack();

		new LinePainter(textArea);

		storedStrings = new ArrayList<>(5);

		doc = textArea.getStyledDocument();
		textArea.setEditable(false);


		addTextAreaListener();

		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		contentPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				try {
					updateStoredList();
				} catch (BadLocationException | UnsupportedFlavorException | IOException ex) {
					Log.log(ex);
				}
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}

	private void addTextAreaListener() {
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					performAction();
				}
			}
		});
	}

	private void performAction() {
		int currentLine = TextUtils.getLineAtCaret(textArea);

		String[] lines = PATTERN.split(textArea.getText());

		this.selected = lines[currentLine - 1];
		//        System.out.println(lines[currentLine - 1]);
		dispose();
	}

	private void updateStoredList() throws BadLocationException, IOException, UnsupportedFlavorException {
		textArea.setText(null);
		String fromSystem = getSelectionFromSystemClipboard();
		if (fromSystem != null && !fromSystem.isEmpty() && !storedStrings.contains(fromSystem)) {
			storedStrings.add(fromSystem);
		}
		for (int i = storedStrings.size(); i > 0; --i) {
			if (i != 1) {
				doc.insertString(doc.getLength(), storedStrings.get(i - 1) + '\n', null);
			} else {
				doc.insertString(doc.getLength(), storedStrings.get(i - 1), null);
			}
		}
		textArea.setCaretPosition(0);
	}

	private String getSelectionFromSystemClipboard() throws IOException, UnsupportedFlavorException {
		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = systemClipboard.getContents(null);

		if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return (String) contents.getTransferData(DataFlavor.stringFlavor);
		}
		return null;
	}

	private void onOK() {
		performAction();
		dispose();
	}

	private void onCancel() {
		dispose();
	}

	@Override
	public void insertSelection(JTextComponent component, String string) {
		int position = component.getCaretPosition();
		((JTextArea) component).insert(string, position);
	}

	@Override
	public List<String> getStoredStrings() {
		return storedStrings;
	}

	@Override
	public void addString(String s) {
		storedStrings.add(s);
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public void showDialog() {
		setVisible(true);
	}

	public void hideDialog() {
		dispose();
	}

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
		panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		buttonOK = new JButton();
		buttonOK.setText("OK");
		panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		buttonCancel = new JButton();
		buttonCancel.setText("Cancel");
		panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		textArea = new JTextPane();
		panel3.add(textArea, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return contentPane;
	}
}