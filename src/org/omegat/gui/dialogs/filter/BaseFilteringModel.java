package org.omegat.gui.dialogs.filter;

import javax.swing.table.AbstractTableModel;

/**
 * Created by stsypanov on 12.05.2015.
 */
public class BaseFilteringModel extends AbstractTableModel {

    private BaseFilteringItems items;

    public BaseFilteringModel(BaseFilteringItems items) {
        this.items = items;
    }

    @Override
    public int getRowCount() {
        return items.getFilteringItems().size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return "Is applied";
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
                return Boolean.class;
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
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BaseFilteringItem item = items.getFilteringItems().get(rowIndex);
        switch (columnIndex) {
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
        BaseFilteringItem item = items.getFilteringItems().get(rowIndex);
        switch (columnIndex) {
            case 0: {
                item.setApply((Boolean) value);
                break;
            }
            case 1: {
                item.setPattern((String) value);
                break;
            }
            case 2: {
                item.setExample((String) value);
                break;
            }
        }
    }

    public void addItem() {
        items.getFilteringItems().add(new BaseFilteringItem(false, "", ""));
        fireTableDataChanged();
    }

    public void removeItem(int row) {
        items.getFilteringItems().remove(row);
        fireTableRowsDeleted(row, row);
    }

    public BaseFilteringItems getItems() {
        return items;
    }
}
