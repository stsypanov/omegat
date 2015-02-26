package org.omegat.util.gui;

import org.omegat.util.Preferences;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
		icons = new ArrayList<>();
		icons.add(ResourcesUtil.getIcon(resources + "OmegaT_small.gif").getImage());
		icons.add(ResourcesUtil.getIcon(resources + "OmegaT.gif").getImage());
	}

	public static void setOmegatIcons(Window window) {
		window.setIconImages(icons);
	}

	public static void loadLayoutPreferences(Window window, String prefX, String prefY, String prefWidth, String prefHeight) {
		try {
			loadPreferences(window, prefX, prefY, prefWidth, prefHeight);
		} catch (NumberFormatException nfe) {
			// set default size and position
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			window.setBounds((screenSize.width - 640) / 2, (screenSize.height - 400) / 2, 640, 400);
		}
	}

	public static void loadPreferences(Window window, String prefX, String prefY, String prefWidth, String prefHeight) {
		int x = Integer.parseInt(Preferences.getPreference(prefX));
		int y = Integer.parseInt(Preferences.getPreference(prefY));
		window.setLocation(x, y);

		int width = Integer.parseInt(Preferences.getPreference(prefWidth));
		int height = Integer.parseInt(Preferences.getPreference(prefHeight));
		window.setSize(width, height);
	}

	public static void saveLayoutPreferences(String prefX, String prefY, String prefWidth, String prefHeight,
											 int x, int y, int width, int height) {
		Preferences.setPreference(prefX, x);
		Preferences.setPreference(prefY, y);
		Preferences.setPreference(prefWidth, width);
		Preferences.setPreference(prefHeight, height);
	}
}
