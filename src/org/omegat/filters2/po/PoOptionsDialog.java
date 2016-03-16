/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2011 Didier Briel
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

package org.omegat.filters2.po;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.AbstractAction;


import org.omegat.gui.common.PeroDialog;
import org.omegat.util.OStrings;
import org.omegat.util.gui.StaticUIUtils;

/**
 * Modal dialog to edit the PO filter options.
 * 
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 */
@SuppressWarnings("serial")
public class PoOptionsDialog extends PeroDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    private final Map<String, String> options;

    /**
     * Creates new form PoOptionsDialog
     */
    public PoOptionsDialog(Dialog parent, Map<String, String> options) {
        super(parent, true);
        this.options = new TreeMap<>(options);
        initComponents();

        String allowBlank = options.get(PoFilter.OPTION_ALLOW_BLANK);
        allowBlankCB.setSelected("true".equalsIgnoreCase(allowBlank));
        String allowEditingBlankSegment = options.get(PoFilter.OPTION_ALLOW_EDITING_BLANK_SEGMENT);
        allowEditingBlankSegmentCB.setSelected("true".equalsIgnoreCase(allowEditingBlankSegment));
        String skipHeader = options.get(PoFilter.OPTION_SKIP_HEADER);
        skipHeaderCB.setSelected("true".equalsIgnoreCase(skipHeader));
        String autoFillInPluralStatement = options.get(PoFilter.OPTION_AUTO_FILL_IN_PLURAL_STATEMENT);
        autoFillInPluralStatementCB.setSelected("true".equalsIgnoreCase(autoFillInPluralStatement));
        if ("true".equalsIgnoreCase(options.get(PoFilter.OPTION_FORMAT_MONOLINGUAL))) {
            formatMonolingualRB.setSelected(true);
        } else {
            formatStandardRB.setSelected(true);
        }

        StaticUIUtils.setEscapeAction(this, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
        setLocationRelativeTo(parent);
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    /** Returns updated options. */
    public Map<String, String> getOptions() {
        return options;
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        allowBlankCB = new javax.swing.JCheckBox();
        skipHeaderCB = new javax.swing.JCheckBox();
        autoFillInPluralStatementCB = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        formatStandardRB = new javax.swing.JRadioButton();
        formatMonolingualRB = new javax.swing.JRadioButton();
        allowEditingBlankSegmentCB = new javax.swing.JCheckBox();

        setTitle(OStrings.getString("POFILTER_OPTIONS_TITLE")); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

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

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(allowBlankCB, OStrings.getString("POFILTER_ALLOW_BLANK")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(allowBlankCB, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(skipHeaderCB, OStrings.getString("POFILTER_SKIP_HEADER")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(skipHeaderCB, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(autoFillInPluralStatementCB, OStrings.getString("POFILTER_AUTO_FILL_IN_PLURAL_STATEMENT")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(autoFillInPluralStatementCB, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, OStrings.getString("POFILTER_FORMAT_LABEL")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jLabel1, gridBagConstraints);

        buttonGroup1.add(formatStandardRB);
        org.openide.awt.Mnemonics.setLocalizedText(formatStandardRB, OStrings.getString("POFILTER_FORMAT_STANDARD")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(formatStandardRB, gridBagConstraints);

        buttonGroup1.add(formatMonolingualRB);
        org.openide.awt.Mnemonics.setLocalizedText(formatMonolingualRB, OStrings.getString("POFILTER_FORMAT_MONOLINGUAL")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(formatMonolingualRB, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(allowEditingBlankSegmentCB, OStrings.getString("POFILTER_ALLOW_BLANK_SEGMENT")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(allowEditingBlankSegmentCB, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_okButtonActionPerformed
    {
        options.put(PoFilter.OPTION_ALLOW_BLANK, Boolean.toString(allowBlankCB.isSelected()));
        options.put(PoFilter.OPTION_ALLOW_EDITING_BLANK_SEGMENT, Boolean.toString(allowEditingBlankSegmentCB.isSelected()));
        options.put(PoFilter.OPTION_SKIP_HEADER, Boolean.toString(skipHeaderCB.isSelected()));
        options.put(PoFilter.OPTION_AUTO_FILL_IN_PLURAL_STATEMENT, Boolean.toString(autoFillInPluralStatementCB.isSelected()));
        options.put(PoFilter.OPTION_FORMAT_MONOLINGUAL, Boolean.toString(formatMonolingualRB.isSelected()));

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
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox allowBlankCB;
    private javax.swing.JCheckBox allowEditingBlankSegmentCB;
    private javax.swing.JCheckBox autoFillInPluralStatementCB;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton formatMonolingualRB;
    private javax.swing.JRadioButton formatStandardRB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton okButton;
    private javax.swing.JCheckBox skipHeaderCB;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}
