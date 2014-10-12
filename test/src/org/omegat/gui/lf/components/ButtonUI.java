package org.omegat.gui.lf.components;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 20.06.2014
 * Time: 17:15
 */
public class ButtonUI extends BasicButtonUI {
    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static ComponentUI createUI(JComponent c) {
        return new ButtonUI();
    }

    public static boolean isSquare(Component c) {
        return c instanceof JButton && "square".equals(((JButton)c).getClientProperty("JButton.buttonType"));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        final Border border = c.getBorder();
//        final GraphicsConfig config = GraphicsUtil.setupAAPainting(g);
        final boolean square = isSquare(c);
        if (c.isEnabled() && border != null) {
            final Insets ins = border.getBorderInsets(c);
            final int yOff = (ins.top + ins.bottom) / 4;
            if (!square) {
                if (c instanceof JButton && ((JButton)c).isDefaultButton()) {
                    ((Graphics2D)g).setPaint(UIUtil.getGradientPaint(0, 0, getSelectedButtonColor1(), 0, c.getHeight(), getSelectedButtonColor2()));
                }
                else {
                    ((Graphics2D)g).setPaint(UIUtil.getGradientPaint(0, 0, getButtonColor1(), 0, c.getHeight(), getButtonColor2()));
                }
            }
            g.fillRoundRect(square ? 2 : 2, yOff, c.getWidth() - 2 * 2, c.getHeight() - 2 * yOff, square ? 3 : 2, square ? 3 : 2);
        }
//        config.restore();
        super.paint(g, c);
    }

    @Override
    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        AbstractButton button = (AbstractButton)c;
        ButtonModel model = button.getModel();
        Color fg = button.getForeground();
        if (fg instanceof UIResource && button instanceof JButton && ((JButton)button).isDefaultButton()) {
            final Color selectedFg = UIManager.getColor("Button.darcula.selectedButtonForeground");
            if (selectedFg != null) {
                fg = selectedFg;
            }
        }
        g.setColor(fg);

        FontMetrics metrics = SwingUtilities2.getFontMetrics(c, g);
        int mnemonicIndex = button.getDisplayedMnemonicIndex();
        if (model.isEnabled()) {

            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex,
                    textRect.x + getTextShiftOffset(),
                    textRect.y + metrics.getAscent() + getTextShiftOffset());
        }
        else {
            g.setColor(UIManager.getColor("Button.darcula.disabledText.shadow"));
            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, -1,
                    textRect.x + getTextShiftOffset()+1,
                    textRect.y + metrics.getAscent() + getTextShiftOffset()+1);
            g.setColor(UIManager.getColor("Button.disabledText"));
            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, -1,
                    textRect.x + getTextShiftOffset(),
                    textRect.y + metrics.getAscent() + getTextShiftOffset());


        }
    }

    @Override
    public void update(Graphics g, JComponent c) {
        super.update(g, c);
        if (c instanceof JButton && ((JButton)c).isDefaultButton()) {
            if (!c.getFont().isBold()) {
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            }
        }
    }

    protected Color getButtonColor1() {
//        return UIManager.getColor("Button.darcula.color1");
        return ColorUtil.fromHex("555a5c");
    }

    protected Color getButtonColor2() {
//        return UIManager.getColor("Button.darcula.color2");
        return ColorUtil.fromHex("414648");
    }

    protected Color getSelectedButtonColor1() {
//        return UIManager.getColor("Button.darcula.selection.color1");
        return ColorUtil.fromHex("384f6b");
    }

    protected Color getSelectedButtonColor2() {
//        return UIManager.getColor("Button.darcula.selection.color2");
        return ColorUtil.fromHex("233143");
    }
}
