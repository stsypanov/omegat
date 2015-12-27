/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2008 Alex Buloichik
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

package org.omegat.filters;

import org.omegat.core.data.IProject;
import org.omegat.filters2.text.ini.INIFilter;

public class INIFilterTest extends TestFilterBase {
    public void testParse() throws Exception {
        parse(new INIFilter(), "test/data/filters/ini/file-INIFilter.ini");
    }

    public void testTranslate() throws Exception {
        translateText(new INIFilter(), "test/data/filters/ini/file-INIFilter.ini");
    }

    public void testLoad() throws Exception {
        String f = "test/data/filters/ini/file-INIFilter.ini";
        IProject.FileInfo fi = loadSourceFiles(new INIFilter(), f);

        checkMultiStart(fi, f);
        checkMulti("Value", "nsID", null, null, null, null);
        checkMulti("Value", "Section/ID", null, null, null, null);
        checkMulti("Value2", "Section/ID2", null, null, null, null);
        checkMultiEnd();
    }
}
