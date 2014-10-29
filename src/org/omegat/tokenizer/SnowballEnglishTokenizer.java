/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.
 
 Copyright (C) 2008 Alex Buloichik (alex73mail@gmail.com)
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
package org.omegat.tokenizer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Aaron Madlon-Kay
 */
@Tokenizer(languages = { "en" })
public class SnowballEnglishTokenizer extends BaseTokenizer {
    public static final String[] STOP_WORDS;

    static {
        // Load stopwords
        try {
            try (InputStream in = SnowballEnglishTokenizer.class.getResourceAsStream("StopList_en.txt");
                 BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {

                String s;
                List<String> words = new ArrayList<>();
                while ((s = rd.readLine()) != null) {
                    s = s.trim();
                    if (s.length() == 0 || s.startsWith("#")) {
                        continue;
                    }
                    words.add(s);
                }
                STOP_WORDS = words.toArray(new String[words.size()]);
            }
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(
                    "Error load stopwords in SnowballEnglishTokenizer: "
                            + ex.getMessage());
        }
    }

    @Override
    protected TokenStream getTokenStream(final String strOrig,
            final boolean stemsAllowed, final boolean stopWordsAllowed) {
        if (stemsAllowed) {
            return new SnowballAnalyzer(getBehavior(),
                    "English",
                    stopWordsAllowed ? STOP_WORDS : new String[0]).tokenStream(
                    null, new StringReader(strOrig));
        } else {
            return new StandardTokenizer(getBehavior(),
                    new StringReader(strOrig.toLowerCase()));
        }
    }
}
