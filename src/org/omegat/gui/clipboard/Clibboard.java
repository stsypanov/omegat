package org.omegat.gui.clipboard;

import javax.swing.text.JTextComponent;
import java.util.List;

/**
 * Created by stsypanov on 20.04.2016.
 */
public interface Clibboard {
	void insertSelection(JTextComponent component, String string);
	List<String> getStoredStrings();
	void addString(String s);
}
