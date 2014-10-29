package org.omegat.gui.lf.components;

import java.awt.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 16.07.2014
 * Time: 13:17
 */
public class GraphicsConfig {

    private final Graphics2D myG;
    private final Map myHints;

    public GraphicsConfig(Graphics g) {
        myG = (Graphics2D)g;
        myHints = (Map)myG.getRenderingHints().clone();
    }

    public GraphicsConfig setAntialiasing(boolean on) {
        myG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, on ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        return this;
    }

    public Graphics2D getG() {
        return myG;
    }

    public void restore() {
        myG.setRenderingHints(myHints);
    }
}
