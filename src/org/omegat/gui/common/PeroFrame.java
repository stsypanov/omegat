package org.omegat.gui.common;

import org.omegat.util.gui.GuiUtils;

import javax.swing.*;
import java.awt.*;

public class PeroFrame extends JFrame{

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
}
