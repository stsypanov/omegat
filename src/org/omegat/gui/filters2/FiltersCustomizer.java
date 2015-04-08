/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2013 Alex Buloichik, Yu Tang
               2014 Aaron Madlon-Kay
               2015 Yu Tang, Aaron Madlon-Kay
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

package org.omegat.gui.filters2;

import gen.core.filters.Filter;
import gen.core.filters.Filters;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.omegat.core.Core;
import org.omegat.core.data.IProject;
import org.omegat.filters2.IFilter;
import org.omegat.filters2.master.FilterMaster;
import org.omegat.filters2.master.FiltersTableModel;
import org.omegat.util.OStrings;
import org.omegat.util.gui.DockingUI;
import org.omegat.util.gui.StaticUIUtils;
import org.omegat.util.gui.TableColumnSizer;

/**
 * Main dialog for for setting up filters. Filter is a class that allows for
 * reading and writing a single file format. OmegaT has different filters for
 * different supported file formats. E.g. HTML, OpenOffice etc.
 * 
 * @author Maxym Mykhalchuk
 * @author Alex Buloichik
 * @author Yu Tang
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class FiltersCustomizer extends JDialog implements ListSelectionListener {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    public Filters result;
    /** Filters from OmegaT. */
    private final Filters defaultFilters;
    /** Filters which editable now. */
    private Filters editableFilters;

    /**
     * Flag if this customizer shows project specific filters or not
     */
    private final boolean isProjectSpecific;

    /** Creates new form FilterCustomizer */
    public FiltersCustomizer(Frame parent, boolean projectSpecific, Filters defaultFilters,
            Filters userFilters, Filters projectFilters) {
        super(parent, true);
        isProjectSpecific = projectSpecific;

        this.defaultFilters = defaultFilters;
        if (userFilters == null) {
            userFilters = defaultFilters;
        }
        this.editableFilters = isProjectSpecific && projectFilters != null ? FilterMaster.cloneConfig(projectFilters)
                : FilterMaster.cloneConfig(userFilters);

        StaticUIUtils.setEscapeClosable(this);

        initComponents();

        getRootPane().setDefaultButton(okButton);
        filtersTable.setModel(new FiltersTableModel(editableFilters));
        filtersTable.getSelectionModel().addListSelectionListener(this);
        filtersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (isLeftDoubleClick(me) && isClickOnFileFormatColumn(me)) {
                    editButtonActionPerformed(null);
                }
            }

            private boolean isLeftDoubleClick(MouseEvent me) {
                return me.getClickCount() == 2
                        && me.getButton() == MouseEvent.BUTTON1;
            }

            private boolean isClickOnFileFormatColumn(MouseEvent me) {
                int viewColumnIndex = filtersTable.columnAtPoint(me.getPoint());
                int modelColumnIndex = filtersTable.convertColumnIndexToModel(viewColumnIndex);
                return modelColumnIndex == FiltersTableModel.COLUMN.FILTERS_FILE_FORMAT.index;
            }
        });
        String columnName = FiltersTableModel.COLUMN.FILTERS_FILE_FORMAT.getColumnName();
        TableColumn column = filtersTable.getColumn(columnName);
        column.setCellRenderer(new FilterFormatCellRenderer(getInUseFormatNames()));
        
        TableColumnSizer.autoSize(filtersTable, 0, true);

        if (projectSpecific) {
            setTitle(OStrings.getString("FILTERSCUSTOMIZER_TITLE_PROJECTSPECIFIC"));
        } else {
            projectSpecificCB.setVisible(false);
        }

        if (projectSpecific) {
            projectSpecificCB.setSelected(projectFilters != null);
        }
        projectSpecificCBActionPerformed(null);

        cbRemoveTags.setSelected(editableFilters.isRemoveTags());
        cbRemoveSpacesNonseg.setSelected(editableFilters.isRemoveSpacesNonseg());
        cbPreserveSpaces.setSelected(editableFilters.isPreserveSpaces());
        cbIgnoreFileContext.setSelected(editableFilters.isIgnoreFileContext());

        // hack for "autoresizing" the dialog
        // accomodating table dimensions
        Dimension tableSize = filtersTable.getPreferredSize();
        tableSize.height = tableSize.height + 70;
        filtersScrollPane.setPreferredSize(tableSize);
        pack();
        Toolkit kit = getToolkit();
        Dimension screenSize = kit.getScreenSize();
        Dimension dialogSize = getSize();
        GraphicsConfiguration config = getGraphicsConfiguration();
        Insets insets = kit.getScreenInsets(config);
        screenSize.height -= (insets.top + insets.bottom);  // excluding the Windows taskbar
        if (dialogSize.height > screenSize.height) {
            dialogSize.height = screenSize.height;
            setSize(dialogSize);
        }
        DockingUI.displayCentered(this);
     }    

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (lsm.isSelectionEmpty()) {
            editButton.setEnabled(false);
            optionsButton.setEnabled(false);
        } else {
            editButton.setEnabled(true);
            int fIdx = filtersTable.getSelectedRow();
            Filter currFilter = editableFilters.getFilters().get(fIdx);
            IFilter f = FilterMaster.getFilterInstance(currFilter.getClassName());
            optionsButton.setEnabled(f.hasOptions());
        }
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public Filters getResult() {
        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonPanel = new javax.swing.JPanel();
        toDefaultsButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        filtersScrollPane = new javax.swing.JScrollPane();
        filtersTable = new javax.swing.JTable();
        description = new javax.swing.JTextArea();
        editButton = new javax.swing.JButton();
        optionsButton = new javax.swing.JButton();
        projectSpecificCB = new javax.swing.JCheckBox();
        cbRemoveTags = new javax.swing.JCheckBox();
        cbRemoveSpacesNonseg = new javax.swing.JCheckBox();
        cbPreserveSpaces = new javax.swing.JCheckBox();
        cbIgnoreFileContext = new javax.swing.JCheckBox();

        setTitle(OStrings.getString("FILTERSCUSTOMIZER_TITLE")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        org.openide.awt.Mnemonics.setLocalizedText(toDefaultsButton, OStrings.getString("BUTTON_TO_DEFAULTS")); // NOI18N
        toDefaultsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toDefaultsButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(toDefaultsButton);

        jLabel1.setPreferredSize(new java.awt.Dimension(20, 0));
        buttonPanel.add(jLabel1);

        org.openide.awt.Mnemonics.setLocalizedText(okButton, OStrings.getString("BUTTON_OK")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(okButton);

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, OStrings.getString("BUTTON_CANCEL")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(buttonPanel, gridBagConstraints);

        filtersScrollPane.setFocusable(false);
        filtersScrollPane.setViewportView(filtersTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(filtersScrollPane, gridBagConstraints);

        description.setEditable(false);
        description.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        description.setFont(new JLabel().getFont());
        description.setLineWrap(true);
        description.setText(OStrings.getString("FILTERSCUSTOMIZER_DESCRIPTION")); // NOI18N
        description.setWrapStyleWord(true);
        description.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(description, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(editButton, OStrings.getString("BUTTON_EDIT")); // NOI18N
        editButton.setToolTipText(OStrings.getString("FILTERSCUSTOMIZER_BUTTON_EDIT_HINT")); // NOI18N
        editButton.setEnabled(false);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(editButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(optionsButton, OStrings.getString("FILTERSCUSTOMIZER_BUTTON_OPTIONS")); // NOI18N
        optionsButton.setEnabled(false);
        optionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(optionsButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(projectSpecificCB, OStrings.getString("FILTERSCUSTOMIZER_CHECKBOX_PROJECTSPECIFIC")); // NOI18N
        projectSpecificCB.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        projectSpecificCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                projectSpecificCBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(projectSpecificCB, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cbRemoveTags, OStrings.getString("FILTERSCUSTOMIZER_OPTION_GLOBAL_REMOVE_TAGS")); // NOI18N
        cbRemoveTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRemoveTagsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(cbRemoveTags, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cbRemoveSpacesNonseg, OStrings.getString("FILTERSCUSTOMIZER_OPTION_GLOBAL_REMOVE_SPACES_NONSEG")); // NOI18N
        cbRemoveSpacesNonseg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRemoveSpacesNonsegActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(cbRemoveSpacesNonseg, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cbPreserveSpaces, OStrings.getString("FILTERSCUSTOMIZER_OPTION_GLOBAL_PRESERVE_SPACES")); // NOI18N
        cbPreserveSpaces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPreserveSpacesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(cbPreserveSpaces, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cbIgnoreFileContext, OStrings.getString("FILTERSCUSTOMIZER_OPTION_GLOBAL_IGNORE_FILE")); // NOI18N
        cbIgnoreFileContext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbIgnoreFileContextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(cbIgnoreFileContext, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void projectSpecificCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_projectSpecificCBActionPerformed
        if (projectSpecificCB.isSelected() || !isProjectSpecific) {
            filtersTable.setEnabled(true);
            filtersTable.setFocusable(true);
            cbRemoveTags.setEnabled(true);
            cbRemoveSpacesNonseg.setEnabled(true);
            cbPreserveSpaces.setEnabled(true);
            cbIgnoreFileContext.setEnabled(true);
            toDefaultsButton.setEnabled(true);
        } else {
            filtersTable.setEnabled(false);
            filtersTable.setFocusable(false);
            filtersTable.getSelectionModel().clearSelection();
            cbRemoveTags.setEnabled(false);
            cbRemoveSpacesNonseg.setEnabled(false);
            cbPreserveSpaces.setEnabled(false);
            cbIgnoreFileContext.setEnabled(false);
            toDefaultsButton.setEnabled(false);
        }
    }//GEN-LAST:event_projectSpecificCBActionPerformed

    private void cbRemoveTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRemoveTagsActionPerformed
        editableFilters.setRemoveTags(cbRemoveTags.isSelected());
    }//GEN-LAST:event_cbRemoveTagsActionPerformed

    private void cbRemoveSpacesNonsegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRemoveSpacesNonsegActionPerformed
        editableFilters.setRemoveSpacesNonseg(cbRemoveSpacesNonseg.isSelected());
    }//GEN-LAST:event_cbRemoveSpacesNonsegActionPerformed

    private void cbPreserveSpacesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPreserveSpacesActionPerformed
        editableFilters.setPreserveSpaces(cbPreserveSpaces.isSelected());
    }//GEN-LAST:event_cbPreserveSpacesActionPerformed

    private void cbIgnoreFileContextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbIgnoreFileContextActionPerformed
        editableFilters.setIgnoreFileContext(cbIgnoreFileContext.isSelected());
    }//GEN-LAST:event_cbIgnoreFileContextActionPerformed

    private void optionsButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_optionsButtonActionPerformed
    {// GEN-HEADEREND:event_optionsButtonActionPerformed
        int fIdx = filtersTable.getSelectedRow();
        Filter currFilter = editableFilters.getFilters().get(fIdx);
        IFilter f = FilterMaster.getFilterInstance(currFilter.getClassName());

        // new options handling
        Map<String, String> newConfig = f.changeOptions(this, FilterMaster.forFilter(currFilter.getOption()));
        if (newConfig != null) {
            FilterMaster.setOptions(currFilter, newConfig);
        }
    }// GEN-LAST:event_optionsButtonActionPerformed

    private void toDefaultsButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_toDefaultsButtonActionPerformed
    {// GEN-HEADEREND:event_toDefaultsButtonActionPerformed
        editableFilters = FilterMaster.cloneConfig(defaultFilters);
        filtersTable.setModel(new FiltersTableModel(editableFilters));
        cbRemoveTags.setSelected(editableFilters.isRemoveTags());
        cbRemoveSpacesNonseg.setSelected(editableFilters.isRemoveSpacesNonseg());
        cbPreserveSpaces.setSelected(editableFilters.isPreserveSpaces());
        cbIgnoreFileContext.setSelected(editableFilters.isIgnoreFileContext());
    }// GEN-LAST:event_toDefaultsButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_editButtonActionPerformed
        int row = filtersTable.getSelectedRow();
        FilterEditor editor = new FilterEditor(this, editableFilters.getFilters().get(row));
        editor.setVisible(true);
        if (editor.result != null) {
            editableFilters.getFilters().set(row, editor.result);
        }
    }// GEN-LAST:event_editButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_okButtonActionPerformed
    {
        doClose(RET_OK);
    }// GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_cancelButtonActionPerformed
    {
        doClose(RET_CANCEL);
    }// GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)// GEN-FIRST:event_closeDialog
    {
        doClose(RET_CANCEL);
    }// GEN-LAST:event_closeDialog

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        if (isProjectSpecific && projectSpecificCB.isSelected() == false) {
            result = null;
        } else {
            result = editableFilters;
        }
        setVisible(false);
        dispose();
    }

    private Set<String> getInUseFormatNames() {
        Set<String> inUseFormatNames = new HashSet<String>();
        IProject project = Core.getProject();
        if (project.isProjectLoaded()) {
            Filters projectSpecificFilters = Core.getProject().getProjectProperties().getProjectFilters();
            boolean noProjectSpecificFiltersAvailable = (projectSpecificFilters == null);
            if (isProjectSpecific || noProjectSpecificFiltersAvailable) {
                for (IProject.FileInfo fi : project.getProjectFiles()) {
                    inUseFormatNames.add(fi.filterFileFormatName);
                }
            }
        }
        return inUseFormatNames;
    }

    private class FilterFormatCellRenderer extends DefaultTableCellRenderer {

        private final Set<String> highlightFormatNames;

        private FilterFormatCellRenderer(Set<String> highlightFormatNames) {
            this.highlightFormatNames = highlightFormatNames;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (highlightFormatNames.contains(value.toString())) {
                component.setFont(component.getFont().deriveFont(Font.BOLD));
            }
            return component;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox cbIgnoreFileContext;
    private javax.swing.JCheckBox cbPreserveSpaces;
    private javax.swing.JCheckBox cbRemoveSpacesNonseg;
    private javax.swing.JCheckBox cbRemoveTags;
    private javax.swing.JTextArea description;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane filtersScrollPane;
    private javax.swing.JTable filtersTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton okButton;
    private javax.swing.JButton optionsButton;
    private javax.swing.JCheckBox projectSpecificCB;
    private javax.swing.JButton toDefaultsButton;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}
