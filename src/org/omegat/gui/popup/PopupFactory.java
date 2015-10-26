package org.omegat.gui.popup;

import org.omegat.util.gui.GuiUtils;

import javax.swing.*;
import javax.swing.Popup;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by stsypanov on 21.10.2015.
 */
public class PopupFactory {

	public static Popup showPopupCentered(Component owner, ContentProvider<JComponent> contentProvider){
		final JComponent contents = contentProvider.getContent();

		Point point = GuiUtils.getPositionForCenteredComponent(contents, owner);

		final Popup popup = javax.swing.PopupFactory.getSharedInstance().getPopup(owner, contents, point.x, point.y);

		KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
		Action escapeAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.hide();
			}
		};

		contents.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
		contents.getRootPane().getActionMap().put("ESCAPE", escapeAction);

		popup.show();
		contents.requestFocusInWindow();
		return popup;
	}
}
