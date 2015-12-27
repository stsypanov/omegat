/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2015 Aaron Madlon-Kay
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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.UIManager;

import org.omegat.util.OStrings;
import org.omegat.util.StringUtil;
import org.omegat.util.gui.DockingUI;
import org.omegat.util.gui.StaticUIUtils;

/**
 *
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class FileCollisionDialog extends javax.swing.JDialog {

    private JButton userClicked;
    
    public static boolean promptToReplace(javax.swing.JDialog parent, String filename) {
        FileCollisionDialog dialog = new FileCollisionDialog(parent);
        dialog.setFilename(filename);
        dialog.enableApplyToAll(false);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.shouldReplace();
    }
    
    /**
     * Creates new form FileCollisionDialog
     */
    public FileCollisionDialog(java.awt.Frame parent) {
        super(parent, true);
        grandInit();
    }
    
    public FileCollisionDialog(javax.swing.JDialog parent) {
        super(parent, true);
        grandInit();
    }
    
    private void grandInit() {
        initComponents();
        icon.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        DockingUI.displayCentered(this);
        StaticUIUtils.setEscapeAction(this, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed(null);
            }
        });
    }
    
    public void setFilename(String name) {
        message.setText(StringUtil.format(OStrings.getString("DND_FILE_COLLISION_MESSAGE"), name));
    }
    
    public void enableApplyToAll(boolean enabled) {
        batchCheckbox.setVisible(enabled);
    }

    public boolean isApplyToAll() {
        return batchCheckbox.isSelected();
    }
    
    public boolean userDidCancel() {
        return userClicked == cancelButton;
    }
    
    public boolean shouldReplace() {
        return userClicked == replaceButton;
    }
    
    private void close() {
        setVisible(false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        icon = new javax.swing.JLabel();
        message = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        batchCheckbox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        replaceButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel3.setLayout(new java.awt.BorderLayout(10, 0));
        jPanel3.add(icon, java.awt.BorderLayout.WEST);

        message.setEditable(false);
        message.setLineWrap(true);
        message.setText(OStrings.getString("DND_FILE_COLLISION_MESSAGE")); // NOI18N
        message.setWrapStyleWord(true);
        message.setOpaque(false);
        jPanel3.add(message, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel2.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(batchCheckbox, OStrings.getString("DND_APPLY_TO_ALL_BUTTON")); // NOI18N
        jPanel2.add(batchCheckbox, java.awt.BorderLayout.WEST);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, OStrings.getString("BUTTON_CANCEL")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel1.add(cancelButton);

        org.openide.awt.Mnemonics.setLocalizedText(replaceButton, OStrings.getString("DND_REPLACE_BUTTON")); // NOI18N
        replaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replaceButtonActionPerformed(evt);
            }
        });
        jPanel1.add(replaceButton);

        jPanel2.add(jPanel1, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        userClicked = cancelButton;
        close();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void replaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceButtonActionPerformed
        userClicked = replaceButton;
        close();
    }//GEN-LAST:event_replaceButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox batchCheckbox;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel icon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextArea message;
    private javax.swing.JButton replaceButton;
    // End of variables declaration//GEN-END:variables
}
