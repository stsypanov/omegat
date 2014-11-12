package org.omegat.gui.lf.components;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 19.07.2014
 * Time: 15:21
 */
public class TextAreaUI extends BasicTextAreaUI {

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JTextArea textComponent = (JTextArea)c;
        textComponent.setForeground(Color.LIGHT_GRAY);
    }

    public static ComponentUI createUI(JComponent c) {
        return new TextAreaUI();
    }
}
