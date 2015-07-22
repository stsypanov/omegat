package org.omegat.util.gui;

import org.omegat.util.Log;
import org.omegat.util.Preferences;

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
		try {
			icons.add(ResourcesUtil.getImage(resources + "OmegaT_small.gif"));
			icons.add(ResourcesUtil.getImage(resources + "OmegaT.gif"));
		} catch (FileNotFoundException e) {
			Log.log(Level.SEVERE, "failed to load image", e);
		}

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
		window.setPreferredSize(new Dimension(width, height));
	}

	public static void saveLayoutPreferences(String prefX, String prefY, String prefWidth, String prefHeight,
											 int x, int y, int width, int height) {
		Preferences.setPreference(prefX, x);
		Preferences.setPreference(prefY, y);
		Preferences.setPreference(prefWidth, width);
		Preferences.setPreference(prefHeight, height);
	}
}
