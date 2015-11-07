package org.omegat.gui.glossary.editor;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stsypanov on 26.10.2015.
 */
public abstract class KeyValueTableModel<K, V> implements TableModel {

	protected static final int COLUMN_COUNT = 2;

	private List<KeyValue<K, V>> data;

	public KeyValueTableModel() {
		data = new ArrayList<>();
	}

	public KeyValueTableModel(List<KeyValue<K, V>> data) {
		this.data = data;
	}

	@Override
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

}
