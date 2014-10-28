/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
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

package org.omegat.convert.v20to21.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Wrapper around all the file filter classes. Is a JavaBean, so that it's easy
 * to write/read it to/from XML file and provides a table model.
 * 
 * @author Maxym Mykhalchuk
 */
public class Filters {
    /** Holds the list of available filters. */
    private List<OneFilter> filters = new ArrayList<>();

    /**
     * Returns all the filters as an array.
     */
    public OneFilter[] getFilter() {
        return filters.toArray(new OneFilter[filters.size()]);
    }

    /**
     * Sets all filters from the array.
     */
    public void setFilter(OneFilter[] filter) {
        filters = new ArrayList<>(Arrays.asList(filter));
    }
}
