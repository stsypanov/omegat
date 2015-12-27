/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.
 
 Copyright (C) 2013 Aaron Madlon-Kay
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

import java.io.StringReader;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ga.IrishAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * @author Aaron Madlon-Kay
 */
@Tokenizer(languages = { "ga" })
public class LuceneIrishTokenizer extends BaseTokenizer {
    @SuppressWarnings("resource")
    @Override
    protected TokenStream getTokenStream(final String strOrig,
            final boolean stemsAllowed, final boolean stopWordsAllowed) {
        if (stemsAllowed) {
            CharArraySet stopWords = stopWordsAllowed ? IrishAnalyzer.getDefaultStopSet()
                    : new CharArraySet(getBehavior(), 0, false);
            return new IrishAnalyzer(getBehavior(), stopWords).tokenStream("", new StringReader(
                    strOrig));
        } else {
            return new StandardTokenizer(getBehavior(),
                    new StringReader(strOrig));
        }
    }
}
