/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2010 Alex Buloichik
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

package org.omegat.core.dictionaries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Dictionary implementation for Lingvo DSL format.
 * 
 * Lingvo DSL format described in Lingvo help. See also http://www.dsleditor.narod.ru/art_03.htm(russian).
 * 
 * @author Alex Buloichik <alex73mail@gmail.com>
 */
public class LingvoDSL implements IDictionary {
    protected static final String CHARSET = "UTF-16";
    protected static final Pattern RE_SKIP = Pattern.compile("\\[.+?\\]");

    protected final File file;

    public LingvoDSL(File file) {
        this.file = file;
    }

    public Map<String, Object> readHeader() throws Exception {
        Map<String, Object> result = new HashMap<>();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(new FileInputStream(file), CHARSET))) {
            String s;
            StringBuilder word = new StringBuilder();
            StringBuilder trans = new StringBuilder();
            while ((s = rd.readLine()) != null) {
                if (s.isEmpty()) {
                    continue;
                }
                if (s.charAt(0) == '#') {
                    continue;
                }
                s = RE_SKIP.matcher(s).replaceAll("");
                if (Character.isWhitespace(s.charAt(0))) {
                    trans.append(s).append('\n');
                } else {
                    if (word.length() > 0) {
                        result.put(word.toString(), trans.toString());
                        word.setLength(0);
                        trans.setLength(0);
                    }
                    word.append(s);
                }
            }
            if (word.length() > 0) {
                result.put(word.toString(), trans.toString());
            }
            return result;
        }
    }

    public String readArticle(String word, Object articleData) throws Exception {
        return (String) articleData;
    }
}
