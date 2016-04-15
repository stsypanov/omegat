/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2009 Alex Buloichik
               2012 Thomas Cordonnier
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

package org.omegat.gui.stat;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import org.omegat.core.Core;
import org.omegat.core.statistics.CalcMatchStatistics;
import org.omegat.core.statistics.CalcStandardStatistics;
import org.omegat.core.threads.LongProcessThread;
import org.omegat.gui.common.PeroDialog;
import org.omegat.util.OStrings;
import org.omegat.util.gui.StaticUIUtils;

/**
 * Display match statistics window and save data to file.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Thomas Cordonnier
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class StatisticsWindow extends PeroDialog {

    private String textData;

    public static enum STAT_TYPE {
        STANDARD, MATCHES, MATCHES_PER_FILE
    };

    private LongProcessThread thread;

    /**
     * Creates new form StatisticsWindow
     */
    public StatisticsWindow(STAT_TYPE statType) {
        super(Core.getMainWindow().getApplicationFrame(), true);
        initComponents();
        copyDataButton.setVisible(false);

        JComponent output = null;

        switch (statType) {
        case STANDARD:
            setTitle(OStrings.getString("CT_STATSSTANDARD_WindowHeader"));
            StatisticsPanel panel = new StatisticsPanel(this);
            thread = new CalcStandardStatistics(panel);
            output = panel;
            break;
        case MATCHES:
            setTitle(OStrings.getString("CT_STATSMATCH_WindowHeader"));
            MatchStatisticsPanel panel1 = new MatchStatisticsPanel(this);
            thread = new CalcMatchStatistics(panel1, false);
            output = panel1;
            break;
        case MATCHES_PER_FILE:
            setTitle(OStrings.getString("CT_STATSMATCH_PER_FILE_WindowHeader"));
            PerFileMatchStatisticsPanel panel2 = new PerFileMatchStatisticsPanel(this);
            thread = new CalcMatchStatistics(panel2, true);
            output = panel2;
            break;
        }

        // Run calculation
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        displayPanel.add(output);

        StaticUIUtils.setEscapeClosable(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thread.fin();
            }
        });

        setSize(800, 400);
        setLocationRelativeTo(Core.getMainWindow().getApplicationFrame());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        progressBar = new javax.swing.JProgressBar();
        copyDataButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        displayPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        displayPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(displayPanel, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));
        jPanel2.add(filler1);

        progressBar.setStringPainted(true);
        jPanel2.add(progressBar);

        org.openide.awt.Mnemonics.setLocalizedText(copyDataButton, OStrings.getString("CT_STATS_CopyToClipboard")); // NOI18N
        copyDataButton.setEnabled(false);
        copyDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyDataButtonActionPerformed(evt);
            }
        });
        jPanel2.add(copyDataButton);

        org.openide.awt.Mnemonics.setLocalizedText(closeButton, OStrings.getString("BUTTON_CLOSE")); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        jPanel2.add(closeButton);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void copyDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyDataButtonActionPerformed
        if (textData != null) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(textData), null);
        }
    }//GEN-LAST:event_copyDataButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        // Apparently calling dispose() does not invoke
        // WindowListener.windowClosing() so we have to be sure to end the
        // thread here too.
        // See https://sourceforge.net/p/omegat/bugs/789/
        thread.fin();
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    public void setTextData(final String textData) {
        this.textData = textData;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                copyDataButton.setEnabled(textData != null && !textData.isEmpty());
            }
        });
    }

    public void showProgress(final int percent) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setValue(percent);
                progressBar.setString(percent + "%");
            }
        });
    }

    public void finishData() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setValue(100);
                progressBar.setString("");
                progressBar.setVisible(false);
                copyDataButton.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton copyDataButton;
    javax.swing.JPanel displayPanel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JPanel jPanel2;
    javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
