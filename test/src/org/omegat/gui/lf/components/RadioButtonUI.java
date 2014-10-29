package org.omegat.gui.lf.components;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalRadioButtonUI;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 12:09
 */
public class RadioButtonUI extends MetalRadioButtonUI {

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JRadioButton radioButton = (JRadioButton) c;
        radioButton.setFocusable(false);
        radioButton.setOpaque(false);
        radioButton.setForeground(Color.LIGHT_GRAY);
    }

    public static ComponentUI createUI(JComponent c) {
        return new RadioButtonUI();
    }
}
