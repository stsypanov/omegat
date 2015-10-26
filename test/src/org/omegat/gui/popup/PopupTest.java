package org.omegat.gui.popup;

import org.omegat.gui.popup.dictionary.DictionaryPopup;
import org.omegat.util.gui.GuiUtils;
import org.omegat.util.gui.StaticUIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by stsypanov on 21.10.2015.
 */
public class PopupTest extends JFrame {

	private final JTextArea textArea;

	public PopupTest(String title) throws HeadlessException {
		super(title);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) (screenSize.getHeight() / 2);
		int width = (int) (screenSize.getWidth() / 2);
		setPreferredSize(new Dimension(width, height));
		getContentPane().setLayout(new BorderLayout());

		textArea = new JTextArea();
		textArea.addKeyListener(new Adapter());

		getContentPane().add(textArea, BorderLayout.CENTER);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		GuiUtils.centreWindow(this);
		StaticUIUtils.setEscapeClosable(this);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		new PopupTest("Test popup");
	}

	private class Adapter extends KeyAdapter {
		private static final int INTERVAL = 250;
		private long lastShiftStroke;

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				boolean suits = System.currentTimeMillis() - lastShiftStroke <= INTERVAL;
				if (suits) {
					final DictionaryPopup popup = new DictionaryPopup();
					new MockDictionaryPopupController(popup, null);

					PopupFactory.showPopupCentered(textArea, new ContentProvider<JComponent>() {
						@Override
						public JComponent getContent() {
							return popup.getRoot();
						}
					});
				}
				lastShiftStroke = System.currentTimeMillis();
			}
		}
	}
}
