/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2010 Didier Briel
               2015 Aaron Madlon-Kay
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

import java.awt.Frame;
import javax.swing.DefaultComboBoxModel;

import org.omegat.gui.common.PeroDialog;
import org.omegat.gui.editor.ModificationInfoManager;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.gui.StaticUIUtils;

/**
 * 
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class ViewOptionsDialog extends PeroDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form ViewOptionsDialog */
    public ViewOptionsDialog(Frame parent) {
        super(parent, true);

        StaticUIUtils.setEscapeClosable(this);

        initComponents();

        getRootPane().setDefaultButton(okButton);

        // Initializing options
        viewSourceAllBold.setSelected(Preferences.isPreferenceDefault(Preferences.VIEW_OPTION_SOURCE_ALL_BOLD, true));
        markFirstNonUnique.setSelected(Preferences.isPreference(Preferences.VIEW_OPTION_UNIQUE_FIRST));
        
        simplifyPPTooltips.setSelected(Preferences.isPreferenceDefault(Preferences.VIEW_OPTION_PPT_SIMPLIFY, true));
        
        templateActivator.setSelected(Preferences.isPreferenceDefault(Preferences.VIEW_OPTION_TEMPLATE_ACTIVE, false));
        templatesSetEnabled(templateActivator.isSelected());

        modInfoTemplate.setText(Preferences.getPreferenceDefault(
                Preferences.VIEW_OPTION_MOD_INFO_TEMPLATE, ModificationInfoManager.DEFAULT_TEMPLATE));
        modInfoTemplate.setCaretPosition(0);

        modInfoTemplateND.setText(Preferences.getPreferenceDefault(
                Preferences.VIEW_OPTION_MOD_INFO_TEMPLATE_WO_DATE, ModificationInfoManager.DEFAULT_TEMPLATE_NO_DATE));
        modInfoTemplateND.setCaretPosition(0);

        invalidate();
        pack();
        setLocationRelativeTo(parent);
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        viewSourceAllBold = new javax.swing.JCheckBox();
        markFirstNonUnique = new javax.swing.JCheckBox();
        templateLabel = new javax.swing.JLabel();
        modInfoTemplate = new javax.swing.JTextField();
        variablesLabel = new javax.swing.JLabel();
        variablesList = new javax.swing.JComboBox<String>();
        insertButton = new javax.swing.JButton();
        templateLabelND = new javax.swing.JLabel();
        modInfoTemplateND = new javax.swing.JTextField();
        variablesLabelND = new javax.swing.JLabel();
        variablesListND = new javax.swing.JComboBox<String>();
        insertButtonND = new javax.swing.JButton();
        simplifyPPTooltips = new javax.swing.JCheckBox();
        templateActivator = new javax.swing.JCheckBox();

        setTitle(OStrings.getString("VIEW_OPTION_TITLE")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(okButton, OStrings.getString("BUTTON_OK")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(14, 4, 4, 4);
        getContentPane().add(okButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, OStrings.getString("BUTTON_CANCEL")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(14, 4, 4, 4);
        getContentPane().add(cancelButton, gridBagConstraints);

        viewSourceAllBold.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(viewSourceAllBold, OStrings.getString("VIEW_OPTION_SOURCE")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(viewSourceAllBold, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(markFirstNonUnique, OStrings.getString("VIEW_OPTION_UNIQUE")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(markFirstNonUnique, gridBagConstraints);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/omegat/Bundle"); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(templateLabel, bundle.getString("MOD_INFO_TEMPLATE")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        getContentPane().add(templateLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        getContentPane().add(modInfoTemplate, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(variablesLabel, bundle.getString("MOD_INFO_TEMPLATE_VARIABLES")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        getContentPane().add(variablesLabel, gridBagConstraints);

        variablesList.setModel(new DefaultComboBoxModel<>(ModificationInfoManager.MOD_INFO_VARIABLES));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        getContentPane().add(variablesList, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(insertButton, bundle.getString("BUTTON_INSERT")); // NOI18N
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        getContentPane().add(insertButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(templateLabelND, bundle.getString("MOD_INFO_TEMPLATE_NO_DATE")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        getContentPane().add(templateLabelND, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        getContentPane().add(modInfoTemplateND, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(variablesLabelND, bundle.getString("MOD_INFO_TEMPLATE_VARIABLES")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        getContentPane().add(variablesLabelND, gridBagConstraints);

        variablesListND.setModel(new DefaultComboBoxModel<>(ModificationInfoManager.MOD_INFO_VARIABLES_NO_DATE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        getContentPane().add(variablesListND, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(insertButtonND, bundle.getString("BUTTON_INSERT")); // NOI18N
        insertButtonND.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonNDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        getContentPane().add(insertButtonND, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(simplifyPPTooltips, OStrings.getString("VIEW_OPTION_PPT_SIMPLIFY")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(simplifyPPTooltips, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(templateActivator, OStrings.getString("MOD_INFO_TEMPLATE_ACTIVATOR")); // NOI18N
        templateActivator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateActivatorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(templateActivator, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
        modInfoTemplate.replaceSelection(variablesList.getSelectedItem().toString());
    }//GEN-LAST:event_insertButtonActionPerformed

    private void insertButtonNDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonNDActionPerformed
        modInfoTemplateND.replaceSelection(variablesListND.getSelectedItem().toString());
    }//GEN-LAST:event_insertButtonNDActionPerformed

    private void templateActivatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateActivatorActionPerformed
        templatesSetEnabled(templateActivator.isSelected());
    }//GEN-LAST:event_templateActivatorActionPerformed

    private void templatesSetEnabled(boolean isEnabled) {
        modInfoTemplate.setEnabled(isEnabled);
        templateLabel.setEnabled(isEnabled);
        variablesLabel.setEnabled(isEnabled);
        variablesList.setEnabled(isEnabled);
        insertButton.setEnabled(isEnabled);
        modInfoTemplateND.setEnabled(isEnabled);
        templateLabelND.setEnabled(isEnabled);
        variablesLabelND.setEnabled(isEnabled);
        variablesListND.setEnabled(isEnabled);
        insertButtonND.setEnabled(isEnabled);
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        Preferences.setPreference(Preferences.VIEW_OPTION_SOURCE_ALL_BOLD, viewSourceAllBold.isSelected());
        Preferences.setPreference(Preferences.VIEW_OPTION_UNIQUE_FIRST, markFirstNonUnique.isSelected());
        Preferences.setPreference(Preferences.VIEW_OPTION_PPT_SIMPLIFY, simplifyPPTooltips.isSelected());
        Preferences.setPreference(Preferences.VIEW_OPTION_TEMPLATE_ACTIVE, templateActivator.isSelected());
        Preferences.setPreference(Preferences.VIEW_OPTION_MOD_INFO_TEMPLATE, modInfoTemplate.getText());
        Preferences.setPreference(Preferences.VIEW_OPTION_MOD_INFO_TEMPLATE_WO_DATE, modInfoTemplateND.getText());
        ModificationInfoManager.reset();

        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton insertButton;
    private javax.swing.JButton insertButtonND;
    private javax.swing.JCheckBox markFirstNonUnique;
    private javax.swing.JTextField modInfoTemplate;
    private javax.swing.JTextField modInfoTemplateND;
    private javax.swing.JButton okButton;
    private javax.swing.JCheckBox simplifyPPTooltips;
    private javax.swing.JCheckBox templateActivator;
    private javax.swing.JLabel templateLabel;
    private javax.swing.JLabel templateLabelND;
    private javax.swing.JLabel variablesLabel;
    private javax.swing.JLabel variablesLabelND;
    private javax.swing.JComboBox<String> variablesList;
    private javax.swing.JComboBox<String> variablesListND;
    private javax.swing.JCheckBox viewSourceAllBold;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}
