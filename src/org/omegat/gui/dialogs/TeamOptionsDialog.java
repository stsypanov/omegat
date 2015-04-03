/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2009 Didier Briel, Martin Fleurke
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
import javax.swing.JLabel;

import org.omegat.gui.common.PeroDialog;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.gui.DockingUI;
import org.omegat.util.gui.StaticUIUtils;

/**
 * 
 * @author Martin Fleurke
 */
@SuppressWarnings("serial")
public class TeamOptionsDialog extends PeroDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form WorkflowOptionsDialog */
    public TeamOptionsDialog(Frame parent) {
        super(parent, true);

        StaticUIUtils.setEscapeClosable(this);

        initComponents();

        getRootPane().setDefaultButton(okButton);

        authorText.setText(Preferences.getPreferenceDefault(Preferences.TEAM_AUTHOR,
                System.getProperty("user.name")));

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

        descriptionLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        authorLabel = new javax.swing.JLabel();
        authorText = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle(OStrings.getString("GUI_TITLE_Team_Options")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(descriptionLabel, OStrings.getString("GUI_TEAM_DESCRIPTION")); // NOI18N
        descriptionLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(descriptionLabel, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 30, 10, 30));
        jPanel2.setLayout(new java.awt.BorderLayout());

        authorLabel.setLabelFor(authorText);
        org.openide.awt.Mnemonics.setLocalizedText(authorLabel, OStrings.getString("WF_OPTION_INSERT_AUTHOR_PREFIX")); // NOI18N
        jPanel2.add(authorLabel, java.awt.BorderLayout.WEST);
        jPanel2.add(authorText, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(okButton, OStrings.getString("BUTTON_OK")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jPanel1.add(okButton);

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, OStrings.getString("BUTTON_CANCEL")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel1.add(cancelButton);

        jPanel3.add(jPanel1, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_okButtonActionPerformed
    {
        Preferences.setPreference(Preferences.TEAM_AUTHOR, authorText.getText());

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
    private javax.swing.JLabel authorLabel;
    private javax.swing.JTextField authorText;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}
