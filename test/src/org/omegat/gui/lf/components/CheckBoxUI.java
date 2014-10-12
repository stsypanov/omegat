package org.omegat.gui.lf.components;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 12:48
 */
public class CheckBoxUI extends BasicCheckBoxUI {

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JCheckBox checkBox = (JCheckBox)c;
        checkBox.setFocusable(false);
        checkBox.setOpaque(false);
        checkBox.setForeground(Color.LIGHT_GRAY);
    }

    public static ComponentUI createUI(JComponent c) {
        return new CheckBoxUI();
    }
}
