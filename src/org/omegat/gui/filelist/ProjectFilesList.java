/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2014 Alex Buloichik
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

package org.omegat.gui.filelist;

import javax.swing.JLabel;

import org.omegat.gui.common.PeroFrame;
import org.omegat.util.OStrings;

/**
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Aaron Madlon-Kay
 */
public class ProjectFilesList extends PeroFrame {

    /**
     * Creates new form ProjectFilesList
     */
    public ProjectFilesList() {
        initComponents();
    }

    @Override
    public String getPreferenceBaseName() {
        return "project_files_window";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel3 = new javax.swing.JPanel();
        tablesOuterPanel = new javax.swing.JPanel();
        tablesInnerPanel = new javax.swing.JPanel();
        scrollFiles = new javax.swing.JScrollPane();
        tableFiles = new javax.swing.JTable();
        tableTotal = new javax.swing.JTable();
        statLabel = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanel2 = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        btnDown = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jPanel1 = new javax.swing.JPanel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        m_addNewFileButton = new javax.swing.JButton();
        m_wikiImportButton = new javax.swing.JButton();
        m_closeButton = new javax.swing.JButton();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 10));
        jPanel3.setLayout(new java.awt.BorderLayout());

        tablesOuterPanel.setLayout(new java.awt.BorderLayout());

        tablesInnerPanel.setLayout(new java.awt.BorderLayout());

        scrollFiles.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollFiles.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tableFiles.setFillsViewportHeight(true);
        scrollFiles.setViewportView(tableFiles);

        tablesInnerPanel.add(scrollFiles, java.awt.BorderLayout.CENTER);

        tableTotal.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tablesInnerPanel.add(tableTotal, java.awt.BorderLayout.SOUTH);

        tablesOuterPanel.add(tablesInnerPanel, java.awt.BorderLayout.CENTER);

        jPanel3.add(tablesOuterPanel, java.awt.BorderLayout.CENTER);

        statLabel.setEditable(false);
        statLabel.setFont(new JLabel().getFont());
        statLabel.setLineWrap(true);
        statLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 0, 10));
        statLabel.setOpaque(false);
        jPanel3.add(statLabel, java.awt.BorderLayout.SOUTH);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 0, 0));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.PAGE_AXIS));
        jPanel4.add(filler2);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(btnFirst, OStrings.getString("PF_MOVE_FIRST")); // NOI18N
        btnFirst.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(btnFirst, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(btnUp, OStrings.getString("PF_MOVE_UP")); // NOI18N
        btnUp.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(btnUp, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel2.add(filler3, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(btnDown, OStrings.getString("PF_MOVE_DOWN")); // NOI18N
        btnDown.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(btnDown, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(btnLast, OStrings.getString("PF_MOVE_LAST")); // NOI18N
        btnLast.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(btnLast, gridBagConstraints);

        jPanel4.add(jPanel2);
        jPanel4.add(filler1);

        jPanel3.add(jPanel4, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));
        jPanel1.add(filler5);

        org.openide.awt.Mnemonics.setLocalizedText(m_addNewFileButton, OStrings.getString("TF_MENU_FILE_IMPORT")); // NOI18N
        jPanel1.add(m_addNewFileButton);

        org.openide.awt.Mnemonics.setLocalizedText(m_wikiImportButton, OStrings.getString("TF_MENU_WIKI_IMPORT")); // NOI18N
        jPanel1.add(m_wikiImportButton);

        org.openide.awt.Mnemonics.setLocalizedText(m_closeButton, OStrings.getString("BUTTON_CLOSE")); // NOI18N
        jPanel1.add(m_closeButton);
        jPanel1.add(filler6);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnDown;
    public javax.swing.JButton btnFirst;
    public javax.swing.JButton btnLast;
    public javax.swing.JButton btnUp;
    public javax.swing.Box.Filler filler1;
    public javax.swing.Box.Filler filler2;
    public javax.swing.Box.Filler filler3;
    public javax.swing.Box.Filler filler5;
    public javax.swing.Box.Filler filler6;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    public javax.swing.JButton m_addNewFileButton;
    public javax.swing.JButton m_closeButton;
    public javax.swing.JButton m_wikiImportButton;
    public javax.swing.JScrollPane scrollFiles;
    public javax.swing.JTextArea statLabel;
    public javax.swing.JTable tableFiles;
    public javax.swing.JTable tableTotal;
    public javax.swing.JPanel tablesInnerPanel;
    public javax.swing.JPanel tablesOuterPanel;
    // End of variables declaration//GEN-END:variables
}
