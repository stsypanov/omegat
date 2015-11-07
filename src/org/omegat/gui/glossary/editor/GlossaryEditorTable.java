package org.omegat.gui.glossary.editor;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by stsypanov on 28.10.2015.
 */
public class GlossaryEditorTable extends KeyValueTable<String, GlossaryEditorModel.GlossaryValue> {

	public static final int ROW_HEIGHT = 50;

	public GlossaryEditorTable() {
		super();
		table.setDefaultRenderer(GlossaryEditorModel.GlossaryValue.class, new GlossaryValueCellRenderer());
		table.setDefaultEditor(GlossaryEditorModel.GlossaryValue.class, new GlossaryValueCellEditor());
		table.setRowHeight(ROW_HEIGHT);
	}

	protected static class GlossaryValueCellRenderer implements TableCellRenderer {
		protected JLabel srcLabel;
		protected JLabel commentsLabel;
		protected JTextField srcTextArea;
		protected JTextField commentsTextArea;

		public GlossaryValueCellRenderer() {
			srcLabel = new JLabel("Перевод");
			commentsLabel = new JLabel("Примечания");

			srcTextArea = new JTextField();
			commentsTextArea = new JTextField();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			switch (column) {
				case 0: {
					table.setValueAt(value, row, column);
					break;
				}
				case 1: {
					GlossaryEditorModel.GlossaryValue glossaryValue = (GlossaryEditorModel.GlossaryValue) value;
					String loc = StringUtils.join(glossaryValue.getLoc(), "; ");
					String comments = StringUtils.join(glossaryValue.getComments(), "; ");
					srcTextArea.setText(loc);
					commentsTextArea.setText(comments);
					break;
				}
			}

			JPanel root = new JPanel(new GridBagLayout());
			root.add(srcLabel, new GridBagConstraints(0, 0, 1, 1, 0, 1, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 5), 0, 0));
			root.add(srcTextArea, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			root.add(commentsLabel, new GridBagConstraints(0, 1, 1, 1, 0, 1, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 5), 0, 0));
			root.add(commentsTextArea, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			return root;
		}
	}

	private static class GlossaryValueCellEditor extends AbstractCellEditor implements TableCellEditor {
		protected JLabel srcLabel;
		protected JLabel commentsLabel;
		protected JTextField locTextField;
		protected JTextField commentsTextField;

		public GlossaryValueCellEditor() {
			srcLabel = new JLabel("Перевод");
			srcLabel.setMinimumSize(new Dimension(100, 0));
			commentsLabel = new JLabel("Примечания");
			commentsLabel.setMinimumSize(new Dimension(100, 0));

			locTextField = new JTextField();
			commentsTextField = new JTextField();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			switch (column) {
				case 0: {
					table.setValueAt(value, row, column);
					break;
				}
				case 1: {
					GlossaryEditorModel.GlossaryValue glossaryValue = (GlossaryEditorModel.GlossaryValue) value;
					String[] loc = glossaryValue.getLoc();
					String[] comments = glossaryValue.getComments();
					String locToString = StringUtils.join(loc, "; ");
					locTextField.setText(locToString);
					String commentsToString = StringUtils.join(comments, "; ");
					commentsTextField.setText(commentsToString);
					break;
				}
			}

			JPanel root = new JPanel(new GridBagLayout());
			root.add(srcLabel, new GridBagConstraints(0, 0, 1, 1, 0, 1, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 5), 0, 0));
			root.add(locTextField, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			root.add(commentsLabel, new GridBagConstraints(0, 1, 1, 1, 0, 1, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 5), 0, 0));
			root.add(commentsTextField, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			return root;
		}


		@Override
		public Object getCellEditorValue() {
			GlossaryEditorModel.GlossaryValue value = new GlossaryEditorModel.GlossaryValue();

			String locString = locTextField.getText();
			String[] loc = locString.split(";");
			value.setLoc(loc);

			String comment = commentsTextField.getText();
			String[] comments = comment.split(";");
			value.setComments(comments);
			return value;
		}
	}
}
