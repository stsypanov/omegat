/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2014 Briac Pilpre
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

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;

import org.omegat.gui.common.PeroDialog;
import org.omegat.gui.editor.UnderlineFactory;
import org.omegat.gui.editor.UnderlineFactory.WaveUnderline;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.gui.Styles;
import org.omegat.util.gui.Styles.EditorColor;

/**
 * Dialog for configuring custom colors.
 * 
 * @author Briac Pilpre
 */
@SuppressWarnings("serial")
public class CustomColorSelectionDialog extends PeroDialog {

    /**
     * Creates new form CustomColorSelectionDialog
     */
    public CustomColorSelectionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sampleEditorPane = new javax.swing.JTextArea();
        colorStylesLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        colorStylesList = new javax.swing.JList(Styles.EditorColor.values());
        colorChooser = new javax.swing.JColorChooser();
        applyColorChangesButton = new javax.swing.JButton();
        defaultColorButton = new javax.swing.JButton();
        setColorButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        sampleEditorPane.setEditable(false);
        sampleEditorPane.setText("Sample translation text");
        sampleEditorPane.setMinimumSize(new java.awt.Dimension(400, 100));
        sampleEditorPane.setName(""); // NOI18N
        sampleEditorPane.setPreferredSize(new java.awt.Dimension(400, 100));
        sampleEditorPane.setRequestFocusEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OmegaT Custom Color");
        setIconImage(null);

        colorStylesLabel.setText(OStrings.getString("GUI_COLORS_COLOR"));

        colorStylesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        colorStylesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                colorStylesListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(colorStylesList);

        //final JComponent samplePanel = sampleEditorPane;
        final CustomColorPreview samplePanel = new CustomColorPreview(colorChooser, colorStylesList.getSelectedValue());

        //final JComponent samplePanel = sampleText;

        colorChooser.setPreviewPanel(samplePanel);
        ColorSelectionModel model = colorChooser.getSelectionModel();
        model.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                ColorSelectionModel model = (ColorSelectionModel) evt.getSource();

                samplePanel.curColor = model.getSelectedColor();
                samplePanel.editorColor = (Styles.EditorColor) colorStylesList.getSelectedValue();
            }
        });

        // Remove sample Swatches
        javax.swing.colorchooser.AbstractColorChooserPanel[] oldPanels = colorChooser.getChooserPanels();
        for (AbstractColorChooserPanel oldPanel : oldPanels) {
            String clsName = oldPanel.getClass().getName();
            if (clsName.equals("javax.swing.colorchooser.DefaultSwatchChooserPanel")) {
                colorChooser.removeChooserPanel(oldPanel);
            }
        }

        applyColorChangesButton.setText(OStrings.getString("GUI_COLORS_APPLY"));
        applyColorChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyColorChangesButtonActionPerformed(evt);
            }
        });

        defaultColorButton.setText(OStrings.getString("GUI_COLORS_DEFAULT_COLOR"));
        defaultColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultColorButtonActionPerformed(evt);
            }
        });

        setColorButton.setText(OStrings.getString("GUI_COLORS_SET_COLOR"));
        setColorButton.setToolTipText("");
        setColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setColorButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(OStrings.getString("BUTTON_CANCEL"));
        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, OStrings.getString("BUTTON_CANCEL"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(colorStylesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(setColorButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(defaultColorButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(colorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(applyColorChangesButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setColorButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(defaultColorButton))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(colorStylesLabel)
                            .addGap(320, 320, 320))
                        .addComponent(colorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(applyColorChangesButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void colorStylesListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_colorStylesListValueChanged
        Color selectedColor = ((Styles.EditorColor) colorStylesList.getSelectedValue()).getColor();
        colorChooser.setColor(selectedColor);
    }//GEN-LAST:event_colorStylesListValueChanged

    private void defaultColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultColorButtonActionPerformed
    	((Styles.EditorColor) colorStylesList.getSelectedValue()).setColor(null);
    }//GEN-LAST:event_defaultColorButtonActionPerformed

    private void setColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setColorButtonActionPerformed
    	((Styles.EditorColor) colorStylesList.getSelectedValue()).setColor(colorChooser.getColor());
    }//GEN-LAST:event_setColorButtonActionPerformed

    private void applyColorChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyColorChangesButtonActionPerformed
        Preferences.save();
        closeDialog();
    }//GEN-LAST:event_applyColorChangesButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        closeDialog();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void closeDialog() {
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CustomColorSelectionDialog dialog = new CustomColorSelectionDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    private class CustomColorPreview extends JTextArea
    {
        EditorColor editorColor;
		Color curColor;

        CustomColorPreview(JColorChooser colorChooser, Object object)
        {
            curColor = colorChooser.getColor();
            this.editorColor = (Styles.EditorColor) object;
            init();
        }
        
        public void init()
        {
            setText(
    "Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.");
            setBackground(Styles.EditorColor.COLOR_BACKGROUND.getColor());
            setForeground(Styles.EditorColor.COLOR_FOREGROUND.getColor());
            Highlighter highlighter = getHighlighter();

            WaveUnderline hlSpellcheck = new UnderlineFactory.WaveUnderline(Styles.EditorColor.COLOR_SPELLCHECK.getColor());
            try {
    			highlighter.addHighlight(45, 55, hlSpellcheck);
    		} catch (BadLocationException e) {
    			/* ignore */
    		}
        }
        
        public void paint(Graphics g)
        {
        	//super.paint(g);
            g.setColor(curColor);
            g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyColorChangesButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JColorChooser colorChooser;
    private javax.swing.JLabel colorStylesLabel;
    private javax.swing.JList colorStylesList;
    private javax.swing.JButton defaultColorButton;
    private javax.swing.JScrollPane jScrollPane1;
    private JTextArea sampleEditorPane;
    private javax.swing.JButton setColorButton;
    // End of variables declaration//GEN-END:variables
}
