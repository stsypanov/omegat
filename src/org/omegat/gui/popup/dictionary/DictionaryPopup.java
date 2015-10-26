package org.omegat.gui.popup.dictionary;

import org.omegat.gui.dictionaries.Callback;
import org.omegat.gui.dictionaries.VoidCallback;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DictionaryPopup {
	public static final int CELL_HEIGHT = 12;
	public static final int WIDTH = 250;
	public static final int HEIGHT = 30;

	private JPanel root;
	private JPopupMenu popupMenu;
	private JList<String> container;
	private JTextField textField;
	private Callback<String> callback;
	private VoidCallback parentPopupCallback;

	public DictionaryPopup() throws HeadlessException {
		super();
		root = new JPanel();
		root.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		root.addAncestorListener(new AncestorAdapter());
		root.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textField.requestFocus();
			}
		});
		initComponents();
	}

	private void initComponents() {
		container = new JList<>();
		container.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				char keyChar = e.getKeyChar();
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_BACK_SPACE) {
					textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
					textField.requestFocusInWindow();
				} else if (Character.isLetter(keyChar)) {
					textField.setText(textField.getText() + keyChar);
					textField.requestFocusInWindow();
				} else if (keyCode == KeyEvent.VK_ENTER) {
					callback.execute(container.getSelectedValue());
					hidePopup();
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

		textField = new JTextField(20);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_ENTER) {
					callback.execute(textField.getText());
					hidePopup();
				}
			}
		});

		textField.getInputMap().put(KeyStroke.getKeyStroke("UP"), "doSomething");
		textField.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "doSomething");

		textField.getActionMap().put("doSomething", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				container.clearSelection();
				container.requestFocusInWindow();
			}
		});
		root.add(textField);
		textField.requestFocus();
	}

	public void addKeyListener(KeyListener listener) {
		textField.addKeyListener(listener);
	}

	public JList<String> getContainer() {
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
		popupMenu.show(root, textField.getX(), textField.getY() + root.getHeight());
		textField.requestFocus();
	}

	public void hidePopup() {
		popupMenu.setVisible(false);
		parentPopupCallback.execute();
		if (root.isVisible()) root.setVisible(false);
	}

	public String getText() {
		return textField.getText();
	}

	public void redraw() {
		int size = container.getModel().getSize();
		if (size == 0) {
			popupMenu.setVisible(false);
		} else if (size <= 10) {
			popupMenu.setPreferredSize(new Dimension(WIDTH, size * 20 + 10));
		} else {
			popupMenu.setPreferredSize(new Dimension(WIDTH, 200));
		}
		popupMenu.pack();
	}

	public void hide() {
		root.setVisible(false);
	}

	public void setPreferredSize(Dimension dimension) {
		root.setPreferredSize(dimension);
	}

	public JComponent getRoot() {
		return root;
	}

	public boolean isVisible() {
		return popupMenu.isShowing();
	}

	public boolean notVisible() {
		return !popupMenu.isShowing();
	}

	public void setParentPopupCallback(VoidCallback parentPopupCallback) {
		this.parentPopupCallback = parentPopupCallback;
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

	protected class AncestorAdapter implements AncestorListener {

		@Override
		public void ancestorAdded(AncestorEvent event) {
		}

		@Override
		public void ancestorRemoved(AncestorEvent event) {
			Timer timer = new Timer(10, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (popupMenu.isShowing()) {
						hidePopup();
					}
				}
			});
			timer.start();
		}

		@Override
		public void ancestorMoved(AncestorEvent event) {
		}
	}
}
