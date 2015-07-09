package org.omegat.gui.dialogs.filter;

import org.omegat.core.Core;
import org.omegat.gui.common.PeroDialog;
import org.omegat.util.Log;
import org.omegat.util.OStrings;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Created by stsypanov on 12.05.2015.
 */
public class BaseFilteringDialog extends PeroDialog {
    private static BaseFilteringDialog instance;

    private JTable table;
    private JLabel errorLabel = new JLabel();

    private BaseFilteringDialog(Frame owner) {
        super(owner, true);
        setTitle("Configure base items filtering");
        init();
    }

    public static BaseFilteringDialog getInstance() {
        if (instance == null) {
            instance = new BaseFilteringDialog(Core.getMainWindow().getApplicationFrame());
        }
        return instance;
    }

    private void init() {
        JLabel label = new JLabel("<html>" + OStrings.getString("MW_OPTIONSMENU_BASE_FILTERING_LABEL") + "</html>");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.CENTER);


        table = new JTable(new DefaultTableModel());
        table.setDragEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.addMouseListener(new MouseAdapter() {


            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row == -1 ){
                        getModel().addItem();
                        table.changeSelection(table.getRowCount() - 1, 0, false, false);
                        table.changeSelection(table.getRowCount() - 1, table.getColumnCount() - 1, false, true);
                    }
                }
            }
        });
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        errorLabel.setVisible(false);

        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        JPanel bottomContainer = new JPanel();
        bottomContainer.add(ok);
        bottomContainer.add(cancel);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.add(label, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(bottomContainer, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(600, 300));
        pack();
    }

    private BaseFilteringModel getModel() {
        return (BaseFilteringModel) table.getModel();
    }

    private void onOk() {
        BaseFilteringItems items = getModel().getItems();
        File target = new File(Core.getProject().getProjectProperties().getBaseFilteringItems());
        try {
            BaseFilteringParser.saveObject(target, items);
        } catch (JAXBException e) {
            Log.severe("failed to save base filtering items", e);
        }
    }

    private void onCancel() {
        dispose();
    }


    public void setTableModel(AbstractTableModel model) {
        table.setModel(model);
    }

    public void showErrorMessage() {
        errorLabel.setText("Failed to load items");
        errorLabel.setVisible(true);
    }

    public void hideErrorMessage() {
        errorLabel.setVisible(false);
    }

    public void adjustColumns(){
        table.getColumnModel().getColumn(0).setMinWidth(40);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(1).setMinWidth(400);
        table.getColumnModel().getColumn(1).setMaxWidth(400);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        hideErrorMessage();
    }

    public static void main(String[] args) {
        BaseFilteringDialog baseFilteringDialog = new BaseFilteringDialog(null);
        baseFilteringDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        baseFilteringDialog.setVisible(true);
    }
}
