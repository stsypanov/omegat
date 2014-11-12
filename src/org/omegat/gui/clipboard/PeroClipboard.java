package org.omegat.gui.clipboard;

import org.omegat.gui.common.PeroFrame;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rad1kal
 * Date: 23.07.2014
 * Time: 11:28
 */
public class PeroClipboard extends PeroFrame implements Clibboard {


    private List<String> storedStrings = new ArrayList<>();

    public PeroClipboard(String title) throws HeadlessException {
        super(title);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setPreferredSize(new Dimension(600, 400));
    }

    @Override
    public void insertSelection(JTextComponent component, String string) {
        int position = component.getCaretPosition();
//        if (component instanceof JTextArea){
            ((JTextArea)component).insert(string, position);
//        }
//        if (component instanceof JEditorPane){
//            ((JEditorPane)component).insert(string, position);
//        }

    }

    @Override
    public List<String> getStoredStrings() {
        return new ArrayList<>(storedStrings);
    }

    @Override
    public void addString(String s) {
        storedStrings.add(s);
    }
}
