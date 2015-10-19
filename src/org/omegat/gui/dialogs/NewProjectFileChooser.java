/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2008 Alex Buloichik
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

import java.io.File;

import javax.swing.JOptionPane;

import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.gui.OmegaTFileChooser;

/**
 * A chooser for project's directory for a newly created project.
 * 
 * @author Keith Godfrey
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
@SuppressWarnings("serial")
public class NewProjectFileChooser extends OmegaTFileChooser {
    public NewProjectFileChooser() {
        setMultiSelectionEnabled(false);
        setFileHidingEnabled(true);
        setFileSelectionMode(DIRECTORIES_ONLY);
        setDialogTitle(OStrings.getString("PP_SAVE_PROJECT_FILE"));

        String curDir = Preferences.getPreference(Preferences.CURRENT_FOLDER);
        if (curDir != null) {
            File dir = new File(curDir);
            if (dir.isDirectory()) {
                setCurrentDirectory(dir);
            }
        }
    }

    public void approveSelection() {
        // user hit 'open' button - redirect command to open project or
        // recurse into lower directory
        File dir = getSelectedFile();
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files.length > 0) {
                // must select non-existing name or empty dir for project
                JOptionPane.showMessageDialog(this, OStrings.getString("NDC_SELECT_NEW_OR_EMPTY"),
                    OStrings.getString("NDC_SELECT_UNIQUE_TITLE"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // this is OK - continue
        super.approveSelection();
    }
}