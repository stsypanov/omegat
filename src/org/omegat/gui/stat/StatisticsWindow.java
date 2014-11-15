/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2009 Alex Buloichik
               2012 Thomas Cordonnier
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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import org.omegat.core.Core;
import org.omegat.core.statistics.CalcMatchStatistics;
import org.omegat.core.statistics.CalcStandardStatistics;
import org.omegat.core.threads.LongProcessThread;
import org.omegat.gui.common.PeroFrame;
import org.omegat.util.OStrings;
import org.omegat.util.gui.DockingUI;
import org.omegat.util.gui.StaticUIUtils;

/**
 * Display match statistics window and save data to file.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Thomas Cordonnier
 */
@SuppressWarnings("serial")
public class StatisticsWindow extends PeroFrame {

    public static enum STAT_TYPE {
        STANDARD, MATCHES, MATCHES_PER_FILE
    }

    private JProgressBar progressBar;
    private JTextArea output;
    private LongProcessThread thread;

    public StatisticsWindow(STAT_TYPE statType) {
        super();
        resolveStatTypeAndStartCalculation(statType);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 400);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                Core.getMainWindow().getApplicationFrame().setEnabled(true);
                Core.getMainWindow().getApplicationFrame().requestFocus();
                Core.getEditor().requestFocus();
            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                thread.fin();
            }
        });

        Core.getMainWindow().getApplicationFrame().setEnabled(false);

        // Prepare UI
        setLayout(new BorderLayout());
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(p);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        p.add(progressBar, BorderLayout.SOUTH);

        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, Core.getMainWindow().getApplicationFont().getSize()));
        p.add(new JScrollPane(output), BorderLayout.CENTER);

        StaticUIUtils.setEscapeAction(this, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread.fin();
                dispose();
            }
        });
        DockingUI.displayCentered(this);
    }

    private void resolveStatTypeAndStartCalculation(STAT_TYPE statType) {
        switch (statType) {
            case STANDARD:
                setTitle(OStrings.getString("CT_STATSSTANDARD_WindowHeader"));
                thread = new CalcStandardStatistics(this);
                break;
            case MATCHES:
                setTitle(OStrings.getString("CT_STATSMATCH_WindowHeader"));
                thread = new CalcMatchStatistics(this, false);
                break;
            case MATCHES_PER_FILE:
                setTitle(OStrings.getString("CT_STATSMATCH_PER_FILE_WindowHeader"));
                thread = new CalcMatchStatistics(this, true);
                break;
        }

        // Run calculation
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
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

    public void displayData(final String result) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                output.setText(result);
            }
        });
    }

    public void appendData(final String result) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                output.append(result);
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
                output.setCaretPosition(0);
            }
        });
    }
}
