package org.omegat.gui.lf.components;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 19.07.14
 * Time: 16:31
 */
public class MenuItemUI extends BasicMenuItemUI {

    @Override
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        FontUIResource fontUIResource = (FontUIResource) UIManager.get("MenuItem.acceleratorFont");
        String name = fontUIResource.getName();
        acceleratorFont = new FontUIResource(new Font(name, Font.BOLD, 12));
    }

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new MenuItemUI();
    }
}
