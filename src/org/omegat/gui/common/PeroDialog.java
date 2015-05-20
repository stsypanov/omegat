package org.omegat.gui.common;

import org.omegat.util.gui.GuiUtils;
import org.omegat.util.gui.StaticUIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 28.07.2014
 * Time: 15:25
 */
public class PeroDialog extends JDialog {

    public PeroDialog(Frame owner) {
        this(owner, false);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Frame owner, boolean modal) {
        this(owner, "", modal);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Frame owner, String title) {
        this(owner, title, false);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        GuiUtils.setOmegatIcons(this);
        StaticUIUtils.setEscapeClosable(this);
    }

    public PeroDialog(Dialog owner) {
        this(owner, false);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Dialog owner, boolean modal) {
        super(owner, modal);
        GuiUtils.setOmegatIcons(this);
        StaticUIUtils.setEscapeClosable(this);
    }
}
