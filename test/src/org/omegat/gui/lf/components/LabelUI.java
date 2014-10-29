package org.omegat.gui.lf.components;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 12:52
 */
public class LabelUI extends BasicLabelUI {

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JLabel textComponent = (JLabel) c;
        textComponent.setForeground(Color.LIGHT_GRAY);
    }

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new LabelUI();
    }

}
