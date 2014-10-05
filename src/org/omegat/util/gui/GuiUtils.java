package org.omegat.util.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
}
