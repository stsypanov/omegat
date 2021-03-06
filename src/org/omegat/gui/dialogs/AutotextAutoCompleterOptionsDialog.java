/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2013 Zoltan Bartko, Aaron Madlon-Kay
               2014 Aaron Madlon-Kay
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/
package org.omegat.gui.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.omegat.gui.common.PeroDialog;
import org.omegat.gui.editor.autotext.Autotext;
import org.omegat.gui.editor.autotext.Autotext.AutotextItem;
import org.omegat.gui.editor.autotext.AutotextTableModel;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.gui.StaticUIUtils;

/**
 *
 * @author bartkoz
 */
@SuppressWarnings("serial")
public class AutotextAutoCompleterOptionsDialog extends PeroDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    
    public int returnStatus = RET_CANCEL;
    
    private final JFileChooser fc = new JFileChooser();
    
    /**
     * Creates new form AutotextAutoCompleterOptionsDialog
     */
    public AutotextAutoCompleterOptionsDialog(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        
        StaticUIUtils.setEscapeClosable(this);
        getRootPane().setDefaultButton(okButton);
        
        sortByLengthCheckBox.setSelected(Preferences.isPreference(Preferences.AC_AUTOTEXT_SORT_BY_LENGTH));
        sortAlphabeticallyCheckBox.setSelected(Preferences.isPreference(Preferences.AC_AUTOTEXT_SORT_ALPHABETICALLY));
        sortFullTextCheckBox.setSelected(Preferences.isPreference(Preferences.AC_AUTOTEXT_SORT_FULL_TEXT));
        sortFullTextCheckBox.setEnabled(sortAlphabeticallyCheckBox.isSelected());
        enabledCheckBox.setSelected(Preferences.isPreferenceDefault(Preferences.AC_AUTOTEXT_ENABLED, true));

        enabledCheckBoxActionPerformed(null);

        fc.setDialogType(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter(OStrings.getString("AC_AUTOTEXT_FILE"), "autotext");
        fc.addChoosableFileFilter(filter);
        
        //entryTextArea.setFont(this.getFont());
        entryTable.setModel(new AutotextTableModel(Autotext.getItems()));
        
        setPreferredSize(new Dimension(500, 500));
        pack();
        
        setLocationRelativeTo(parent);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        enabledCheckBox = new javax.swing.JCheckBox();
        displayPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        sortByLengthCheckBox = new javax.swing.JCheckBox();
        sortAlphabeticallyCheckBox = new javax.swing.JCheckBox();
        sortFullTextCheckBox = new javax.swing.JCheckBox();
        entriesPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        entryTable = new JTable() {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(getValueAt(row, column).toString());
                }
                return c;
            }
        };
        jPanel10 = new javax.swing.JPanel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanel7 = new javax.swing.JPanel();
        loadButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        addNewRowButton = new javax.swing.JButton();
        removeEntryButton = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(OStrings.getString("AC_AUTOTEXT_OPTIONS_TITLE")); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        jPanel11.setLayout(new java.awt.BorderLayout());

        enabledCheckBox.setText(OStrings.getString("AC_AUTOTEXT_ENABLED")); // NOI18N
        enabledCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enabledCheckBoxActionPerformed(evt);
            }
        });
        jPanel11.add(enabledCheckBox, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel11);

        displayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(OStrings.getString("AC_AUTOTEXT_DISPLAY_PANEL"))); // NOI18N
        displayPanel.setLayout(new java.awt.BorderLayout());

        jPanel9.setLayout(new java.awt.GridBagLayout());

        sortByLengthCheckBox.setText(OStrings.getString("AC_AUTOTEXT_SORT_BY_LENGTH")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(sortByLengthCheckBox, gridBagConstraints);

        sortAlphabeticallyCheckBox.setText(OStrings.getString("AC_AUTOTEXT_ALPHABETICALLY")); // NOI18N
        sortAlphabeticallyCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortAlphabeticallyCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(sortAlphabeticallyCheckBox, gridBagConstraints);

        sortFullTextCheckBox.setText(OStrings.getString("AC_AUTOTEXT_SORT_FULL_TEXT")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 5, 0);
        jPanel9.add(sortFullTextCheckBox, gridBagConstraints);

        displayPanel.add(jPanel9, java.awt.BorderLayout.WEST);

        jPanel6.add(displayPanel);

        entriesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(OStrings.getString("AC_AUTOTEXT_ENTRIES_PANEL"))); // NOI18N
        entriesPanel.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 0));
        jPanel8.setLayout(new java.awt.BorderLayout());

        entryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Shortcut", "Full text", "Comment"
            }
        ));
        entryTable.setFillsViewportHeight(true);
        jScrollPane2.setViewportView(entryTable);
        if (entryTable.getColumnModel().getColumnCount() > 0) {
            entryTable.getColumnModel().getColumn(0).setHeaderValue(OStrings.getString("AC_AUTOTEXT_ABBREVIATION")); // NOI18N
            entryTable.getColumnModel().getColumn(1).setHeaderValue(OStrings.getString("AC_AUTOTEXT_TEXT")); // NOI18N
            entryTable.getColumnModel().getColumn(2).setHeaderValue(OStrings.getString("AC_AUTOTEXT_COMMENT")); // NOI18N
        }

        jPanel8.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));
        jPanel10.add(filler3);

        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(loadButton, OStrings.getString("AC_AUTOTEXT_BUTTON_LOAD")); // NOI18N
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(loadButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(saveButton, OStrings.getString("AC_AUTOTEXT_BUTTON_SAVE")); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(saveButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel7.add(filler1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addNewRowButton, OStrings.getString("BUTTON_ADD_NODOTS")); // NOI18N
        addNewRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewRowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(addNewRowButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(removeEntryButton, OStrings.getString("BUTTON_REMOVE")); // NOI18N
        removeEntryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeEntryButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(removeEntryButton, gridBagConstraints);

        jPanel10.add(jPanel7);
        jPanel10.add(filler2);

        jPanel8.add(jPanel10, java.awt.BorderLayout.EAST);

        entriesPanel.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel6.add(entriesPanel);

        getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(okButton, OStrings.getString("BUTTON_OK")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jPanel5.add(okButton);

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, OStrings.getString("BUTTON_CANCEL")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel5.add(cancelButton);

        jPanel4.add(jPanel5, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
            try {
                List<AutotextItem> data = Autotext.load(fc.getSelectedFile());
                entryTable.setModel(new AutotextTableModel(data));
            } catch (IOException ex) {
                Logger.getLogger(AutotextAutoCompleterOptionsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_loadButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
        if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(this)) {
            try {
                List<AutotextItem> data = ((AutotextTableModel) entryTable.getModel()).getData();
                Autotext.save(data, fc.getSelectedFile());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, OStrings.getString("AC_AUTOTEXT_UNABLE_TO_SAVE"));
            }
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        TableCellEditor editor = entryTable.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        
        Autotext.setList(((AutotextTableModel) entryTable.getModel()).getData());

        try {
            Autotext.save();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, OStrings.getString("AC_AUTOTEXT_UNABLE_TO_SAVE"));
        }
            
        Preferences.setPreference(Preferences.AC_AUTOTEXT_SORT_BY_LENGTH, sortByLengthCheckBox.isSelected());
        Preferences.setPreference(Preferences.AC_AUTOTEXT_SORT_ALPHABETICALLY, sortAlphabeticallyCheckBox.isSelected());
        Preferences.setPreference(Preferences.AC_AUTOTEXT_SORT_FULL_TEXT, sortFullTextCheckBox.isSelected());
        Preferences.setPreference(Preferences.AC_AUTOTEXT_ENABLED, enabledCheckBox.isSelected());
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void sortAlphabeticallyCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortAlphabeticallyCheckBoxActionPerformed
        sortFullTextCheckBox.setEnabled(sortAlphabeticallyCheckBox.isSelected());
    }//GEN-LAST:event_sortAlphabeticallyCheckBoxActionPerformed

    private void addNewRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewRowButtonActionPerformed
        int newRow = ((AutotextTableModel) entryTable.getModel()).addRow(new AutotextItem(),
                entryTable.getSelectedRow());
        entryTable.changeSelection(newRow, 0, false, false);
        entryTable.changeSelection(newRow, entryTable.getColumnCount() - 1, false, true);
        entryTable.editCellAt(newRow, 0);
        entryTable.transferFocus();
    }//GEN-LAST:event_addNewRowButtonActionPerformed

    private void removeEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeEntryButtonActionPerformed
        if (entryTable.getSelectedRow() != -1) {
            ((AutotextTableModel) entryTable.getModel()).removeRow(entryTable.getSelectedRow());
        }
    }//GEN-LAST:event_removeEntryButtonActionPerformed

    private void enabledCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enabledCheckBoxActionPerformed
        StaticUIUtils.setHierarchyEnabled(displayPanel, enabledCheckBox.isSelected());
        StaticUIUtils.setHierarchyEnabled(entriesPanel, enabledCheckBox.isSelected());
    }//GEN-LAST:event_enabledCheckBoxActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewRowButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JCheckBox enabledCheckBox;
    private javax.swing.JPanel entriesPanel;
    private javax.swing.JTable entryTable;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton loadButton;
    private javax.swing.JButton okButton;
    private javax.swing.JButton removeEntryButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JCheckBox sortAlphabeticallyCheckBox;
    private javax.swing.JCheckBox sortByLengthCheckBox;
    private javax.swing.JCheckBox sortFullTextCheckBox;
    // End of variables declaration//GEN-END:variables
}
