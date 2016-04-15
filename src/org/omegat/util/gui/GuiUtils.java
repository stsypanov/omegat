package org.omegat.util.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
}
