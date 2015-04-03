/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2010 Didier Briel
               2014-2015 Aaron Madlon-Kay
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
import javax.swing.JDialog;

import org.omegat.core.matching.NearString.SORT_KEY;
import org.omegat.gui.common.PeroDialog;
import org.omegat.gui.matches.MatchesVarExpansion;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.gui.DelegatingComboBoxRenderer;
import org.omegat.util.gui.DockingUI;
import org.omegat.util.gui.StaticUIUtils;

/**
 * 
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class ExternalTMXMatchesDialog extends PeroDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form WorkflowOptionsDialog */
    public ExternalTMXMatchesDialog(Frame parent) {
        super(parent, true);

        StaticUIUtils.setEscapeClosable(this);

        initComponents();
        
        sortMatchesList.setModel(new DefaultComboBoxModel(
                new SORT_KEY[] {SORT_KEY.SCORE, SORT_KEY.SCORE_NO_STEM, SORT_KEY.ADJUSTED_SCORE}));
        sortMatchesList.setRenderer(new DelegatingComboBoxRenderer<SORT_KEY>() {
            @Override
            protected Object getDisplayText(SORT_KEY value) {
                return OStrings.getString("EXT_TMX_SORT_KEY_" + value.toString());
            }
        });

        getRootPane().setDefaultButton(okButton);

        // initializing options
        sortMatchesList.setSelectedItem(Preferences.getPreferenceEnumDefault(Preferences.EXT_TMX_SORT_KEY, SORT_KEY.SCORE));
        displayLevel2Tags.setSelected(Preferences.isPreference(Preferences.EXT_TMX_SHOW_LEVEL2));
        useSlash.setSelected(Preferences.isPreference(Preferences.EXT_TMX_USE_SLASH));
        matchesTemplate.setText(Preferences.getPreferenceDefault(Preferences.EXT_TMX_MATCH_TEMPLATE,
                MatchesVarExpansion.DEFAULT_TEMPLATE));
        matchesTemplate.setCaretPosition(0);

        invalidate();
        pack();
        DockingUI.displayCentered(this);
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
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        sortMatchesLabel = new javax.swing.JLabel();
        sortMatchesList = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        tagHandlingLabel = new javax.swing.JLabel();
        displayLevel2Tags = new javax.swing.JCheckBox();
        useSlash = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        templateLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        matchesTemplate = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        variablesLabel = new javax.swing.JLabel();
        variablesList = new javax.swing.JComboBox();
        insertButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle(OStrings.getString("EXT_TMX_TITLE")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(sortMatchesLabel, OStrings.getString("EXT_TMX_SORT_KEY")); // NOI18N
        jPanel1.add(sortMatchesLabel);
        jPanel1.add(sortMatchesList);

        jPanel2.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(tagHandlingLabel, OStrings.getString("EXT_TMX_DESCRIPTION")); // NOI18N
        jPanel7.add(tagHandlingLabel);

        displayLevel2Tags.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(displayLevel2Tags, OStrings.getString("EXT_TMX_SHOW_LEVEL2")); // NOI18N
        jPanel7.add(displayLevel2Tags);

        useSlash.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(useSlash, OStrings.getString("EXT_TMX_USE_XML")); // NOI18N
        jPanel7.add(useSlash);

        jPanel2.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel3.setLayout(new java.awt.BorderLayout());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/omegat/Bundle"); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(templateLabel, bundle.getString("EXT_TMX_MATCHES_TEMPLATE")); // NOI18N
        jPanel3.add(templateLabel, java.awt.BorderLayout.NORTH);

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(525, 25));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(446, 96));

        matchesTemplate.setColumns(30);
        matchesTemplate.setRows(5);
        jScrollPane1.setViewportView(matchesTemplate);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(variablesLabel, bundle.getString("EXT_TMX_MATCHES_TEMPLATE_VARIABLES")); // NOI18N
        jPanel4.add(variablesLabel, java.awt.BorderLayout.WEST);

        variablesList.setModel(new DefaultComboBoxModel(org.omegat.gui.matches.MatchesVarExpansion.MATCHES_VARIABLES));
        jPanel4.add(variablesList, java.awt.BorderLayout.CENTER);

        org.openide.awt.Mnemonics.setLocalizedText(insertButton, bundle.getString("BUTTON_INSERT")); // NOI18N
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });
        jPanel4.add(insertButton, java.awt.BorderLayout.EAST);

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(okButton, OStrings.getString("BUTTON_OK")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jPanel6.add(okButton);

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, OStrings.getString("BUTTON_CANCEL")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel6.add(cancelButton);

        jPanel5.add(jPanel6, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel5, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
        matchesTemplate.replaceSelection(variablesList.getSelectedItem().toString());
    }//GEN-LAST:event_insertButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_okButtonActionPerformed
    {
        Preferences.setPreference(Preferences.EXT_TMX_SORT_KEY, (SORT_KEY)sortMatchesList.getSelectedItem());
        Preferences.setPreference(Preferences.EXT_TMX_SHOW_LEVEL2, displayLevel2Tags.isSelected());
        Preferences.setPreference(Preferences.EXT_TMX_USE_SLASH, useSlash.isSelected());
        Preferences.setPreference(Preferences.EXT_TMX_MATCH_TEMPLATE, matchesTemplate.getText());

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
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox displayLevel2Tags;
    private javax.swing.JButton insertButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea matchesTemplate;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel sortMatchesLabel;
    private javax.swing.JComboBox sortMatchesList;
    private javax.swing.JLabel tagHandlingLabel;
    private javax.swing.JLabel templateLabel;
    private javax.swing.JCheckBox useSlash;
    private javax.swing.JLabel variablesLabel;
    private javax.swing.JComboBox variablesList;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}
