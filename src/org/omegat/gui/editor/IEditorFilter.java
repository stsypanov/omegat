/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2013 Alex Buloichik
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

package org.omegat.gui.editor;

import java.awt.Component;

import org.omegat.core.data.SourceTextEntry;


/**
 * Interface for editor's filter.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public interface IEditorFilter {

    /**
     * Returns true if the source text entry will be shown in
     * the editor. If false the user cannot create translation
     * for the item
     *
     * @param ste source text entry to be checked
     * @return should the value be shown to user
     */
    boolean allowed(SourceTextEntry ste);

    /**
     * Ui component controlling the behaviour of a filter.
     *
     * @return component with controls
     */

    Component getControlComponent();

    /**
     * Checks if source should be treated as empty
     *
     * @return if source should be treated as empty
     */
    boolean isSourceAsEmptyTranslation();
}
