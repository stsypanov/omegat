package org.omegat.gui.common;

import org.omegat.util.gui.GuiUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 28.07.2014
 * Time: 15:46
 */
public class PeroFrame extends JFrame {

	public PeroFrame() throws HeadlessException {
		GuiUtils.setOmegatIcons(this);
	}

	public PeroFrame(GraphicsConfiguration gc) {
		super(gc);
		GuiUtils.setOmegatIcons(this);
	}

	public PeroFrame(String title) throws HeadlessException {
		super(title);
		GuiUtils.setOmegatIcons(this);
	}

	public PeroFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		GuiUtils.setOmegatIcons(this);
	}

	/**
	 * Loads and sets the position and size of the help window.
	 */
	protected void loadLayoutPreferences(String prefX, String prefY, String prefWidth, String prefHeight) {
		try {
			GuiUtils.loadPreferences(this, prefX, prefY, prefWidth, prefHeight);
		} catch (NumberFormatException nfe) {
			// set default size and position
			setSize(600, 500);
			setLocation(0, 0);
		}
	}

	protected void saveLayoutPreferences(String prefX, String prefY, String prefWidth, String prefHeight,
										 int x, int y, int width, int height) {
		GuiUtils.saveLayoutPreferences(prefX, prefY, prefWidth, prefHeight, x, y, width, height);
	}
}
