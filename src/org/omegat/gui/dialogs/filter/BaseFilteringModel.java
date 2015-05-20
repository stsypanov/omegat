package org.omegat.gui.dialogs.filter;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * Created by stsypanov on 12.05.2015.
 */
public class BaseFilteringModel extends AbstractTableModel {

	private List<BaseFilteringItem> items;

	public BaseFilteringModel(List<BaseFilteringItem> items) {
		this.items = items;
	}

	@Override
	public int getRowCount() {
		return items.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0: {
				return "Apply";
			}
			case 1: {
				return "Pattern";
			}
			case 2: {
				return "Example";
			}
			default: {
				return "";
			}
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0: {
				return JCheckBox.class;
			}
			case 1: {
				return String.class;
			}
			case 2: {
				return String.class;
			}
			default: {
				return String.class;
			}
		}
	}


	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (rowIndex) {
			case 0: {
				return false;
			}
			case 1: {
				return true;
			}
			case 2: {
				return true;
			}
			default: {
				return false;
			}
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		BaseFilteringItem item = items.get(rowIndex);
		switch (rowIndex) {
			case 0: {
				return item.isApply();
			}
			case 1: {
				return item.getPattern();
			}
			case 2: {
				return item.getExample();
			}
			default: {
				return "";
			}
		}
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		BaseFilteringItem item = items.get(rowIndex);
		switch (rowIndex) {
			case 0: {
				item.setApply(((JCheckBox) value).isSelected());
			}
			case 1: {
				item.setPattern((String) value);
			}
			case 2: {
				item.setExample((String) value);
			}
		}
	}

	@Override
	public void addTableModelListener(TableModelListener l) {

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {

	}
}
