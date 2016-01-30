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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("deprecation")
@Tokenizer(languages = { "en" })
public class SnowballEnglishTokenizer extends BaseTokenizer {
    public static final Set<String> STOP_WORDS;

    static {
        // Load stopwords
        try {
            InputStream in = SnowballEnglishTokenizer.class
                    .getResourceAsStream("StopList_en.txt");
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        in, "UTF-8"));
                String s;
                STOP_WORDS = new HashSet<String>();
                while ((s = rd.readLine()) != null) {
                    s = s.trim();
                    if (s.isEmpty() || s.startsWith("#")) {
                        continue;
                    }
                    STOP_WORDS.add(s);
                }
            } finally {
                in.close();
            }
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(
                    "Error load stopwords in SnowballEnglishTokenizer: "
                            + ex.getMessage());
        }
    }

    @SuppressWarnings("resource")
    @Override
    protected TokenStream getTokenStream(final String strOrig,
            final boolean stemsAllowed, final boolean stopWordsAllowed) {
        if (stemsAllowed) {
            return new SnowballAnalyzer(getBehavior(),
                    "English",
                    stopWordsAllowed ? STOP_WORDS : Collections.emptySet()).tokenStream(
                    null, new StringReader(strOrig));
        } else {
            return new StandardTokenizer(getBehavior(),
                    new StringReader(strOrig));
        }
    }
}
