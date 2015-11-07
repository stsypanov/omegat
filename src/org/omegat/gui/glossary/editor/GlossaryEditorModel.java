package org.omegat.gui.glossary.editor;

import org.omegat.gui.glossary.GlossaryEntry;

import javax.swing.event.TableModelListener;
import java.util.*;

/**
 * Created by stsypanov on 29.10.2015.
 */
public class GlossaryEditorModel extends KeyValueTableModel<String, GlossaryEditorModel.GlossaryValue> {

	protected List<GlossaryEntry> entries;
	protected List<TableModelListener> listeners;

	public GlossaryEditorModel(List<GlossaryEntry> entries) {
		this.entries = entries;
	}

	@Override
	public int getRowCount() {
		return entries.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0 : {
				return "Key";
			}
			case 1 : {
				return "Value";
			}
			default: {
				return "";
			}
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex){
			case 0:{
				return String.class;
			}
			case 1:{
				return GlossaryValue.class;
			}
			default:{
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
		GlossaryEntry entry = entries.get(rowIndex);
		switch (columnIndex) {
			case 0: {
				return entry.getSrcText();
			}
			case 1 : {
				return new GlossaryValue(entry);
			}
			default: {
				return null;
			}
		}
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		GlossaryEntry entry = entries.get(rowIndex);
		switch (columnIndex) {
			case 0: {
				entry.setSrcText((String) value);
				break;
			}
			case 1:{
				GlossaryValue glossaryValue = (GlossaryValue) value;
				entry.setLocText(glossaryValue.getLoc());
				entry.setComments(glossaryValue.getComments());
			}
			default:{
				break;
			}
		}
	}

	@Override
	public void addTableModelListener(TableModelListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<>(2);
		}
		listeners.add(listener);
	}

	@Override
	public void removeTableModelListener(TableModelListener listener) {
		listeners.remove(listener);
	}

	public List<GlossaryEntry> getEntries() {
		return entries;
	}

	protected static class GlossaryValue {
		protected String[] loc;
		protected String[] comments;

		public GlossaryValue() {
		}

		public GlossaryValue(String[] loc, String[] comments) {
			this.loc = loc;
			this.comments = comments;
		}

		public GlossaryValue(GlossaryEntry entry) {
			loc = entry.getLocTerms(false);
			comments = entry.getComments();

		}

		public String[] getLoc() {
			return loc;
		}

		public void setLoc(String[] loc) {
			this.loc = loc;
		}

		public String[] getComments() {
			return comments;
		}

		public void setComments(String[] comments) {
			this.comments = comments;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			GlossaryValue that = (GlossaryValue) o;

			// Probably incorrect - comparing Object[] arrays with Arrays.equals
			if (!Arrays.equals(loc, that.loc)) return false;
			// Probably incorrect - comparing Object[] arrays with Arrays.equals
			return Arrays.equals(comments, that.comments);

		}

		@Override
		public int hashCode() {
			int result = Arrays.hashCode(loc);
			result = 31 * result + Arrays.hashCode(comments);
			return result;
		}
	}


}
