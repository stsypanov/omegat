package org.omegat.gui.lf;

import org.omegat.gui.lf.components.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 20.06.2014
 * Time: 16:40
 */
public class PeroDarkLookAndFeel extends MetalLookAndFeel {
    private static final String NAME = "pero-dark-look-and-feel";

    public PeroDarkLookAndFeel() {
        setCurrentTheme(new DarculaMetalTheme());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getID() {
        return "omegat";
    }

    @Override
    public String getDescription() {
        return "look and feel for OmegaT";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    public void installUI(JComponent c) {

    }

    public void uninstallUI(JComponent c) {

    }


    public void paint(Graphics g, JComponent c) {

    }

    public void update(Graphics g, JComponent c) {

    }

    public Dimension getPreferredSize(JComponent c) {
        return new Dimension();
    }

    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        table.put("ToggleButtonUI", ToggleButtonUI.class.getCanonicalName());
        table.put("ButtonUI", ButtonUI.class.getCanonicalName());
        table.put("CheckBoxUI", CheckBoxUI.class.getCanonicalName());
        table.put("RadioButtonUI", RadioButtonUI.class.getCanonicalName());
        table.put("TextAreaUI", TextAreaUI.class.getCanonicalName());
        table.put("LabelUI", LabelUI.class.getCanonicalName());
        table.put("MenuItemUI", MenuItemUI.class.getCanonicalName());
    }

    public Dimension getMinimumSize(JComponent c) {
        return new Dimension();
    }

    public Dimension getMaximumSize(JComponent c) {
        return new Dimension();
    }

    public boolean contains(JComponent c, int x, int y) {
        return false;
    }

    @Override
    public LayoutStyle getLayoutStyle() {
        return LayoutStyle.getInstance();
    }

    @Override
    public void provideErrorFeedback(Component component) {
    }

    @Override
    public Icon getDisabledIcon(JComponent component, Icon icon) {
        return icon;
    }

    @Override
    public Icon getDisabledSelectedIcon(JComponent component, Icon icon) {
        return icon;
    }

    @Override
    public boolean getSupportsWindowDecorations() {
        return true;
    }


}
