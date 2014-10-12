package org.omegat.gui.lf;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import java.awt.*;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaMetalTheme extends DefaultMetalTheme {

    public static final Font MENU_TEXT_FONT = new Font("Arial", Font.BOLD, 12);

    private final ColorUIResource myControlHighlightColor = new ColorUIResource(108, 111, 113);
    private final ColorUIResource myControlDarkShadowColor = new ColorUIResource(39, 42, 44);
    private final ColorUIResource myControlColor = new ColorUIResource(0x3c3f41);
    private static final ColorUIResource WHITE = new ColorUIResource(255, 255, 255);
    private static final ColorUIResource darkBlue = new ColorUIResource(82, 108, 164);
    private static final ColorUIResource lightGray = new ColorUIResource(214, 214, 214);
    private final ColorUIResource mySeparatorForeground = new ColorUIResource(53, 56, 58);

    public static final ColorUIResource primary1 = new ColorUIResource(53, 56, 58);
    private static final ColorUIResource primary2 = new ColorUIResource(91, 135, 206);
    private static final ColorUIResource primary3 = new ColorUIResource(166, 202, 240);


    @Override
    public String getName() {
        return "Darcula theme";
    }

    @Override
    public ColorUIResource getControl() {
        return myControlColor;
    }

    @Override
    public ColorUIResource getControlHighlight() {
        return myControlHighlightColor;
    }

    @Override
    public ColorUIResource getControlDarkShadow() {
        return myControlDarkShadowColor;
    }

    @Override
    public FontUIResource getUserTextFont() {
        return new FontUIResource(new Font("Verdana", Font.BOLD, 12));
    }

    @Override
    public ColorUIResource getUserTextColor() {
        return lightGray;
    }

    @Override
    public FontUIResource getMenuTextFont() {
        return new FontUIResource(MENU_TEXT_FONT);
    }

    @Override
    public FontUIResource getWindowTitleFont() {
        return super.getWindowTitleFont();
    }

    @Override
    public FontUIResource getSubTextFont() {
        return super.getSubTextFont();
    }

    @Override
    public ColorUIResource getControlTextColor() {
        return lightGray;
    }

    @Override
    public ColorUIResource getSystemTextColor() {
        return lightGray;
    }

    @Override
    public ColorUIResource getInactiveControlTextColor() {
        return lightGray;
    }

    @Override
    public ColorUIResource getWindowBackground() {
        return myControlColor;
    }

    @Override
    public ColorUIResource getMenuForeground() {
        return lightGray;
    }

    @Override
    public ColorUIResource getMenuBackground() {
        return myControlColor;
    }

    @Override
    public ColorUIResource getTextHighlightColor() {
        return darkBlue;
    }

    @Override
    public ColorUIResource getSeparatorBackground() {
        return getControl();
    }

    @Override
    public ColorUIResource getSeparatorForeground() {
        return mySeparatorForeground;
    }

    @Override
    public ColorUIResource getMenuSelectedBackground() {
        return darkBlue;
    }

    @Override
    public ColorUIResource getMenuSelectedForeground() {
        return WHITE;
    }

    @Override
    public ColorUIResource getAcceleratorSelectedForeground() {
        return WHITE;
    }

    @Override
    public ColorUIResource getAcceleratorForeground() {
        return WHITE;
    }

    @Override
    public ColorUIResource getPrimaryControl() {
        return super.getPrimaryControl();
    }

    @Override
    public ColorUIResource getFocusColor() {
        return new ColorUIResource(Color.black);
    }

    @Override
    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    @Override
    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    @Override
    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    public static Font getAcceleratorFont(){
        return MENU_TEXT_FONT;
    }



}
