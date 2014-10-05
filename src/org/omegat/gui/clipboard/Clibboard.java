package org.omegat.gui.clipboard;

import javax.swing.text.JTextComponent;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 23.07.2014
 * Time: 11:20
 */
public interface Clibboard {

    void insertSelection(JTextComponent component, String string);

    List<String> getStoredStrings();

    void addString(String s);
}
