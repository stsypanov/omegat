package org.omegat.gui.lf.utils;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 13:56
 */
public class GraphicsUtil {
    @SuppressWarnings("UndesirableClassUsage")
    private static final Graphics2D ourGraphics = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB).createGraphics();
    static {
        setupFractionalMetrics(ourGraphics);
        setupAntialiasing(ourGraphics, true, true);
    }

    public static void setupFractionalMetrics(Graphics g) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    public static void setupAntialiasing(@NotNull Graphics g2) {
        setupAntialiasing(g2, true, false);
    }

    public static int stringWidth(String text, Font font) {
        setupAntialiasing(ourGraphics, true, true);
        return ourGraphics.getFontMetrics(font).stringWidth(text);
    }

    public static int charsWidth(char[] data, int off, int len, Font font) {
        return ourGraphics.getFontMetrics(font).charsWidth(data, off, len);
    }

    public static int charWidth(char ch,Font font) {
        return ourGraphics.getFontMetrics(font).charWidth(ch);
    }

    public static int charWidth(int ch,Font font) {
        return ourGraphics.getFontMetrics(font).charWidth(ch);
    }

    public static void setupAntialiasing(Graphics g2, boolean enableAA, boolean ignoreSystemSettings) {
        if (g2 instanceof Graphics2D) {
            Graphics2D g = (Graphics2D)g2;
            Toolkit tk = Toolkit.getDefaultToolkit();
            //noinspection HardCodedStringLiteral
            Map map = (Map)tk.getDesktopProperty("awt.font.desktophints");

            if (map != null && !ignoreSystemSettings) {
                g.addRenderingHints(map);
            } else {
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        enableAA ? RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            }
        }
    }

    public static GraphicsConfig setupAAPainting(Graphics g) {
        final GraphicsConfig config = new GraphicsConfig(g);
        final Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        return config;
    }

    public static GraphicsConfig paintWithAlpha(Graphics g, float alpha) {
        assert 0.0f <= alpha && alpha <= 1.0f : "alpha should be in range 0.0f .. 1.0f";
        final GraphicsConfig config = new GraphicsConfig(g);
        final Graphics2D g2 = (Graphics2D)g;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        return config;
    }
}
