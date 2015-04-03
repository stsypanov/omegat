/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2009 Martin Fleurke
               2013 Aaron Madlon-Kay
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
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.omegat.gui.common.PeroDialog;
import org.omegat.util.OStrings;
import org.omegat.util.PatternConsts;
import org.omegat.util.Preferences;
import org.omegat.util.gui.DockingUI;
import org.omegat.util.gui.StaticUIUtils;

/**
 * 
 * @author Maxym Mykhalchuk
 * @author Martin Fleurke
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class TagValidationOptionsDialog extends PeroDialog {
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form WorkflowOptionsDialog */
    public TagValidationOptionsDialog(Frame parent) {
        super(parent, true);

        StaticUIUtils.setEscapeClosable(this);

        initComponents();

        getRootPane().setDefaultButton(okButton);

        // initializing options
        noCheckRadio.setSelected(Preferences.isPreference(Preferences.DONT_CHECK_PRINTF_TAGS));
        simpleCheckRadio.setSelected(Preferences.isPreference(Preferences.CHECK_SIMPLE_PRINTF_TAGS));
        fullCheckRadio.setSelected(Preferences.isPreference(Preferences.CHECK_ALL_PRINTF_TAGS));
        javaPatternCheckBox.setSelected(Preferences.isPreference(Preferences.CHECK_JAVA_PATTERN_TAGS));
        customPatternRegExpTF.setText(Preferences.getPreferenceDefaultAllowEmptyString(Preferences.CHECK_CUSTOM_PATTERN));
        removePatternRegExpTF.setText(Preferences.getPreferenceDefaultAllowEmptyString(Preferences.CHECK_REMOVE_PATTERN));
        looseTagOrderCheckBox.setSelected(Preferences.isPreference(Preferences.LOOSE_TAG_ORDERING));
        cbTagsValidRequired.setSelected(Preferences.isPreference(Preferences.TAGS_VALID_REQUIRED));

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
        java.awt.GridBagConstraints gridBagConstraints;

        ourButtonGroup = new javax.swing.ButtonGroup();
        descriptionTextArea = new javax.swing.JTextArea();
        noCheckRadio = new javax.swing.JRadioButton();
        simpleCheckRadio = new javax.swing.JRadioButton();
        fullCheckRadio = new javax.swing.JRadioButton();
        javaPatternCheckBox = new javax.swing.JCheckBox();
        looseTagOrderCheckBox = new javax.swing.JCheckBox();
        looseTagOrderWarningTextArea = new javax.swing.JTextArea();
        cbTagsValidRequired = new javax.swing.JCheckBox();
        jLabelCustomPattern = new javax.swing.JLabel();
        customPatternRegExpTF = new javax.swing.JTextField();
        jLabelRemovePattern = new javax.swing.JLabel();
        removePatternRegExpTF = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle(OStrings.getString("GUI_TITLE_TagValidation_Options")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        descriptionTextArea.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setFont(new JLabel().getFont());
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setText(OStrings.getString("GUI_TAGVALIDATION_DESCRIPTION")); // NOI18N
        descriptionTextArea.setWrapStyleWord(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(descriptionTextArea, gridBagConstraints);

        ourButtonGroup.add(noCheckRadio);
        noCheckRadio.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(noCheckRadio, OStrings.getString("TV_OPTION_NO_CHECK")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 4);
        getContentPane().add(noCheckRadio, gridBagConstraints);

        ourButtonGroup.add(simpleCheckRadio);
        org.openide.awt.Mnemonics.setLocalizedText(simpleCheckRadio, OStrings.getString("TV_OPTION_SIMPLE_CHECK")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 4);
        getContentPane().add(simpleCheckRadio, gridBagConstraints);

        ourButtonGroup.add(fullCheckRadio);
        org.openide.awt.Mnemonics.setLocalizedText(fullCheckRadio, OStrings.getString("TV_OPTION_FULL_CHECK")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 4);
        getContentPane().add(fullCheckRadio, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(javaPatternCheckBox, OStrings.getString("TV_OPTION_JAVA_PATTERN")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        getContentPane().add(javaPatternCheckBox, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(looseTagOrderCheckBox, OStrings.getString("TV_OPTION_LOOSE_TAG_ORDER")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 4);
        getContentPane().add(looseTagOrderCheckBox, gridBagConstraints);

        looseTagOrderWarningTextArea.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        looseTagOrderWarningTextArea.setEditable(false);
        looseTagOrderWarningTextArea.setFont(new JLabel().getFont());
        looseTagOrderWarningTextArea.setLineWrap(true);
        looseTagOrderWarningTextArea.setText(OStrings.getString("TV_OPTION_LOOSE_TAG_WARNING")); // NOI18N
        looseTagOrderWarningTextArea.setWrapStyleWord(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 4, 24);
        getContentPane().add(looseTagOrderWarningTextArea, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cbTagsValidRequired, OStrings.getString("TV_OPTION_TAGS_VALID_REQUIRED")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 4);
        getContentPane().add(cbTagsValidRequired, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelCustomPattern, OStrings.getString("TV_OPTION_CUSTOMPATTERN")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 4);
        getContentPane().add(jLabelCustomPattern, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        getContentPane().add(customPatternRegExpTF, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelRemovePattern, OStrings.getString("TV_OPTION_REMOVEPATTERN")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 4);
        getContentPane().add(jLabelRemovePattern, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        getContentPane().add(removePatternRegExpTF, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(okButton, OStrings.getString("BUTTON_OK")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
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
        gridBagConstraints.gridx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(14, 4, 4, 4);
        getContentPane().add(cancelButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Checks text value of JTextField if it is a valid regular expression. If not, focus is set to the text field and an alert is shown.
     * @param textfield the textfield with the regular expression
     * @return true if regular expression is valid, false otherwise
     */
    private boolean checkRegExp(JTextField textfield) {
        try {
            Pattern.compile(textfield.getText());
        } catch (PatternSyntaxException e) {
            textfield.setCaretPosition(e.getIndex());
            JOptionPane.showMessageDialog(this,
                    e.getLocalizedMessage(), OStrings.getString("TV_OPTION_ERROR_CUSTOMREGEXP_TITLE"),
                    JOptionPane.ERROR_MESSAGE);
            textfield.grabFocus();
            return false;
        }
        return true;
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_okButtonActionPerformed
    {
        if (checkRegExp(customPatternRegExpTF) && checkRegExp(removePatternRegExpTF)) {
            Preferences.setPreference(Preferences.DONT_CHECK_PRINTF_TAGS, noCheckRadio.isSelected());
            Preferences.setPreference(Preferences.CHECK_SIMPLE_PRINTF_TAGS, simpleCheckRadio.isSelected());
            Preferences.setPreference(Preferences.CHECK_ALL_PRINTF_TAGS, fullCheckRadio.isSelected());
            Preferences.setPreference(Preferences.CHECK_JAVA_PATTERN_TAGS, javaPatternCheckBox.isSelected());
            Preferences.setPreference(Preferences.CHECK_CUSTOM_PATTERN, customPatternRegExpTF.getText());
            Preferences.setPreference(Preferences.CHECK_REMOVE_PATTERN, removePatternRegExpTF.getText());
            Preferences.setPreference(Preferences.LOOSE_TAG_ORDERING, looseTagOrderCheckBox.isSelected());
            Preferences.setPreference(Preferences.TAGS_VALID_REQUIRED, cbTagsValidRequired.isSelected());
            PatternConsts.updatePlaceholderPattern();
            PatternConsts.updateRemovePattern();
            PatternConsts.updateCustomTagPattern();
            doClose(RET_OK);
        }
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
    private javax.swing.JCheckBox cbTagsValidRequired;
    private javax.swing.JTextField customPatternRegExpTF;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JRadioButton fullCheckRadio;
    private javax.swing.JLabel jLabelCustomPattern;
    private javax.swing.JLabel jLabelRemovePattern;
    private javax.swing.JCheckBox javaPatternCheckBox;
    private javax.swing.JCheckBox looseTagOrderCheckBox;
    private javax.swing.JTextArea looseTagOrderWarningTextArea;
    private javax.swing.JRadioButton noCheckRadio;
    private javax.swing.JButton okButton;
    private javax.swing.ButtonGroup ourButtonGroup;
    private javax.swing.JTextField removePatternRegExpTF;
    private javax.swing.JRadioButton simpleCheckRadio;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
}
