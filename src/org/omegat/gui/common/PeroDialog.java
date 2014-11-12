package org.omegat.gui.common;

import org.omegat.util.gui.GuiUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: rad1kal
 * Date: 28.07.2014
 * Time: 15:25
 */
public class PeroDialog extends JDialog {

    PeroDialog(){
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Frame owner) {
        super(owner);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Frame owner, boolean modal) {
        super(owner, modal);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Frame owner, String title) {
        super(owner, title);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Dialog owner) {
        super(owner);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Dialog owner, boolean modal) {
        super(owner, modal);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Dialog owner, String title) {
        super(owner, title);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Window owner) {
        super(owner);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Window owner, ModalityType modalityType) {
        super(owner, modalityType);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Window owner, String title) {
        super(owner, title);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);
        GuiUtils.setOmegatIcons(this);
    }

    public PeroDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
        GuiUtils.setOmegatIcons(this);
    }
}
