package org.omegat.util.gui;

import org.omegat.util.Log;
import org.omegat.util.Preferences;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 04.07.2014
 * Time: 12:11
 */
public class GuiUtils {

	private static List<Image> icons;

	static {
		String resources = "/org/omegat/gui/resources/";
		icons = new ArrayList<>(2);
		icons.add(ResourcesUtil.getImage(resources + "OmegaT_small.gif"));
		icons.add(ResourcesUtil.getImage(resources + "OmegaT.gif"));
	}

	public static void setOmegatIcons(Window window) {
		window.setIconImages(icons);
	}

	public static void loadLayoutPreferences(Window window, String prefX, String prefY, String prefWidth, String prefHeight) {
		int x = Integer.parseInt(Preferences.getPreference(prefX));
		int y = Integer.parseInt(Preferences.getPreference(prefY));
		window.setLocation(x, y);

		int width = Integer.parseInt(Preferences.getPreference(prefWidth));
		int height = Integer.parseInt(Preferences.getPreference(prefHeight));

		if (width == 0 || height == 0) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			width = (int) (screenSize.getWidth() / 2);
			height = (int) (screenSize.getHeight() / 2);
		}

		window.setPreferredSize(new Dimension(width, height));
	}

	public static void saveLayoutPreferences(String prefX, String prefY, String prefWidth, String prefHeight,
											 int x, int y, int width, int height) {
		Preferences.setPreference(prefX, x);
		Preferences.setPreference(prefY, y);
		Preferences.setPreference(prefWidth, width);
		Preferences.setPreference(prefHeight, height);
	}

	public static void centreWindow(Window window) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() / 2 - window.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() / 2 - window.getHeight()) / 2);
		window.setLocation(x, y);
	}

	public static Point getPositionForCenteredComponent(Component component, Component owner) {

		double width = component.getPreferredSize().getWidth();
		double height = component.getPreferredSize().getHeight();

		double ownerWidth;
		double ownerHeight;

		if (owner instanceof JComponent) {
			ownerWidth = ((JComponent) owner).getVisibleRect().getWidth();
			ownerHeight = ((JComponent) owner).getVisibleRect().getHeight();
		} else {
			ownerWidth = owner.getSize().getWidth();
			ownerHeight = owner.getSize().getHeight();
		}

		Point ownerLocation = owner.getParent().getLocationOnScreen();

		int x = (int) (ownerLocation.getX() + (ownerWidth / 2 - width / 2));
		int y = (int) (ownerLocation.getY() + (ownerHeight / 2 - height / 2));
		return new Point(x, y);
	}
}
