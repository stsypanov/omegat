package org.omegat.gui.clipboard;

import org.omegat.gui.common.PeroFrame;
import org.omegat.util.gui.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 23.07.2014
 * Time: 11:18
 */
public class ClipboardTest extends PeroFrame {
	private static final String TEXT = "Use this text for clipboard test." +
			"Just place a caret within the text and press Ctrl+Shift+V";

	public ClipboardTest() throws HeadlessException {
		super("clipboard test");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setPreferredSize(new Dimension(600, 400));
		JTextArea comp = new JTextArea(TEXT);
		comp.setLineWrap(true);
		getContentPane().add(comp);
		ClipboardUtils.bind(comp, this);
		pack();
	}

    @Override
    public String getPreferenceBaseName() {
        return "clipboard_test";
    }

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ClipboardTest test = new ClipboardTest();
				test.setVisible(true);
			}
		});
	}
}
