package org.omegat.gui.glossary.editor;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * Created by stsypanov on 26.10.2015.
 */
public abstract class KeyValueTable<K, V> {

	protected JTable table;
	protected JScrollPane scrollPane;
	protected KeyValueTableModel<K, V> model;

	public KeyValueTable() {
		table = new JTable();
		scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

	public TableModel getModel() {
		return model;
	}

	public void setModel(KeyValueTableModel<K, V> model) {
		this.model = model;
		table.setModel(model);
	}

	public JComponent asJComponent(){
		return scrollPane;
	}
}
