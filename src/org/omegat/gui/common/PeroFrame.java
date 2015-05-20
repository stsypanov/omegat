package org.omegat.gui.common;

import org.omegat.util.gui.GuiUtils;
import org.omegat.util.gui.StaticUIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 28.07.2014
 * Time: 15:46
 */
public abstract class PeroFrame extends JFrame {

	public PeroFrame() throws HeadlessException {
		super();
		init();
	}

	public PeroFrame(GraphicsConfiguration gc) {
		super(gc);
		init();
	}

	public PeroFrame(String title) throws HeadlessException {
		super(title);
		init();
	}

	public PeroFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		init();
	}

	public void init(){
		GuiUtils.setOmegatIcons(this);
		StaticUIUtils.setEscapeClosable(this);
		loadLayoutPreferences();
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING || e.getID() == WindowEvent.WINDOW_CLOSED){
			saveLayoutPreferences();
		}
		super.processWindowEvent(e);
	}

	/**
	 * Loads and sets the position and size of the window.
	 */
	protected void loadLayoutPreferences() {
		try {
			GuiUtils.loadLayoutPreferences(this, getPrefX(), getPrefY(), getPrefWidth(), getPrefHeight());
		} catch (Exception e) {
			// set default size and position
			setSize(600, 500);
			setLocation(0, 0);
		}
	}

	/**
	 * Saves the position and size of the window.
	 */
	protected void saveLayoutPreferences() {
		GuiUtils.saveLayoutPreferences(getPrefX(), getPrefY(), getPrefWidth(), getPrefHeight(),
				getX(), getY(), getWidth(), getHeight());
	}

	/**
	 *
	 * @return name of the preference denoting X coordinate of a frame
	 */
	public String getPrefX(){
		return getPreferenceBaseName() + "_x";
	}

	/**
	 *
	 * @return name of the preference denoting Y coordinate of a frame
	 */
	public String getPrefY(){
		return getPreferenceBaseName() + "_y";
	}

	/**
	 *
	 * @return name of the preference denoting width of a frame
	 */
	public String getPrefWidth(){
		return getPreferenceBaseName() + "_width";
	}

	/**
	 *
	 * @return name of the preference denoting height of a frame
	 */
	public String getPrefHeight(){
		return getPreferenceBaseName() + "_height";
	}

	/**
	 *
	 * @return base name for window size and location preferences
	 */
	public abstract String getPreferenceBaseName();
}
