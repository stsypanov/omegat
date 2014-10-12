package org.omegat.gui.lf;

import org.omegat.gui.lf.components.ButtonUI;
import org.omegat.gui.lf.components.CheckBoxUI;
import org.omegat.gui.lf.components.RadioButtonUI;
import org.omegat.gui.lf.components.ToggleButtonUI;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 14:57
 */
public class PeroLightLookAndFeel extends NimbusLookAndFeel {
    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        table.put("ToggleButtonUI", ToggleButtonUI.class.getCanonicalName());
        table.put("ButtonUI", ButtonUI.class.getCanonicalName());
        table.put("CheckBoxUI", CheckBoxUI.class.getCanonicalName());
        table.put("RadioButtonUI", RadioButtonUI.class.getCanonicalName());
    }
}
