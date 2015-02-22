package org.omegat.core.segmentation.datamodels;

import org.omegat.gui.editor.autotext.AutotextTableModel;

import java.beans.ExceptionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Сергей on 22.02.2015.
 */
public abstract class AbstractModel extends AutotextTableModel {

	/**
	 * List of listeners
	 */
	protected List<ExceptionListener> listeners = new ArrayList<>();

	public void fireException(Exception e) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			ExceptionListener l = listeners.get(i);
			l.exceptionThrown(e);
		}
	}

	public void addExceptionListener(ExceptionListener l) {
		listeners.add(l);
	}

	public void removeTableModelListener(ExceptionListener l) {
		listeners.remove(l);
	}
}
