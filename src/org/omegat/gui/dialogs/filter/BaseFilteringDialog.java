package org.omegat.gui.dialogs.filter;

import org.omegat.gui.common.PeroDialog;
import org.omegat.util.OStrings;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * Created by stsypanov on 12.05.2015.
 */
public class BaseFilteringDialog extends PeroDialog {

	private JTable table;

	public BaseFilteringDialog(Frame owner) {
		super(owner, true);
		init();
	}

	private void init() {
		JLabel label = new JLabel("<html>" + OStrings.getString("MW_OPTIONSMENU_BASE_FILTERING_LABEL") + "</html>");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setHorizontalTextPosition(JLabel.CENTER);

		table = new JTable();

		JPanel bottomContainer = new JPanel();

		JButton ok = new JButton("OK");
		bottomContainer.add(ok);

		JButton cancel = new JButton("Cancel");
		bottomContainer.add(cancel);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(5, 5));
		contentPane.add(label, BorderLayout.NORTH);
		contentPane.add(table, BorderLayout.CENTER);
		contentPane.add(bottomContainer, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(300, 400));
//		setResizable(false);
		pack();
		setVisible(true);
	}

	public void setTableModel(AbstractTableModel model){
		table.setModel(model);
	}

	public static void main(String[] args) {
		new BaseFilteringDialog(null).setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
