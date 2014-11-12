package org.omegat.gui.lf.components;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.Color;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 12:39
 */
//public class ToggleButtonUI extends MetalToggleButtonUI {
public class ToggleButtonUI extends BasicToggleButtonUI {

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JToggleButton button = (JToggleButton)c;
        button.setFocusable(false);
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        Dimension size = b.getSize();
        FontMetrics fm = g.getFontMetrics();

        Insets i = c.getInsets();

        Rectangle viewRect = new Rectangle(size);

        viewRect.x += i.left;
        viewRect.y += i.top;
        viewRect.width -= i.right + viewRect.x;
        viewRect.height -= i.bottom + viewRect.y;

        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(c.getForeground());

        Font f = c.getFont();
        g.setFont(f);

        // layout the text and icon
        String text = SwingUtilities.layoutCompoundLabel(
                c, fm, b.getText(), b.getIcon(),
                b.getVerticalAlignment(), b.getHorizontalAlignment(),
                b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
                viewRect, iconRect, textRect,
                b.getText() == null ? 0 : b.getIconTextGap());

        g.setColor(b.getBackground());

        if (model.isArmed() && model.isPressed() || model.isSelected()) {
            paintButtonPressed(g,b);
        }

        // Paint the Icon
        if(b.getIcon() != null) {
            paintIcon(g, b, iconRect);
        }

        // Draw the Text
        if(text != null && !text.equals("")) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, b, textRect, text);
            }
        }

        // draw the dashed focus line.
        if (b.isFocusPainted() && b.hasFocus()) {
            paintFocus(g, b, viewRect, textRect, iconRect);
        }


//        g2d.setPaint(Color.WHITE);
//        g2d.setColor(Color.WHITE);
    }

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new ToggleButtonUI();
    }
}
