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

package org.omegat.core.segmentation;

import java.util.ArrayList;
import java.util.List;

import org.omegat.util.Language;

import junit.framework.TestCase;

/**
 * Tests for OmegaT segmentation.
 *
 * @author Maxym Mykhalchuk
 */
public class SegmenterTest extends TestCase {

    private Segmenter segmenter = new Segmenter(SRX.getDefault());

    /**
     * Test of segment method, of class org.omegat.core.segmentation.Segmenter.
     */
    public void testSegment()
    {
        List<StringBuilder> spaces = new ArrayList<StringBuilder>();
        List<String> segments = segmenter.segment(new Language("en"), "<br7>\n\n<br5>\n\nother", spaces,
                new ArrayList<Rule>());
        assertEquals(3, segments.size());
        assertEquals("<br7>", segments.get(0));
        assertEquals("<br5>", segments.get(1));
        assertEquals("other", segments.get(2));
    }
    
    /**
     * Test of glue method, of class org.omegat.core.segmentation.Segmenter.
     */
    public void testGlue()
    {
        List<StringBuilder> spaces = new ArrayList<StringBuilder>();
        List<Rule> brules = new ArrayList<Rule>();
        String oldString = "<br7>\n\n<br5>\n\nother";
        List<String> segments = segmenter.segment(new Language("en"), oldString, spaces, brules);
        String newString = segmenter.glue(new Language("en"), new Language("fr"), segments, spaces, brules);
        assertEquals(oldString, newString);
    }
    
    /**
     * Test of glue method for CJK, of class org.omegat.core.segmentation.Segmenter.
     */
    public void testGlueCJK()
    {
        final String EN_FULLSTOP = ".";
        final String JA_FULLSTOP = "\\u3002"; // Unicode escaped

        // basic combination
        final String SOURCE = "Foo. Bar.\nHere.\n\nThere.\r\nThis.\tThat.\n\tOther.";
        final String TRANSLATED = SOURCE.replace(" ", "").replace(EN_FULLSTOP, JA_FULLSTOP);
        String translated = getPseudoTranslationFromEnToJa(SOURCE);
        assertEquals(TRANSLATED, translated);

        // spaces after/before \n
        final String SOURCE2 = "Foo. \n Bar.";
        final String TRANSLATED2 = "Foo\\u3002\n Bar\\u3002";
        translated = getPseudoTranslationFromEnToJa(SOURCE2);
        assertEquals(TRANSLATED2, translated);

        // spaces after/before \t
        final String SOURCE3 = "Foo. \t Bar.";
        final String TRANSLATED3 = "Foo\\u3002\t Bar\\u3002";
        translated = getPseudoTranslationFromEnToJa(SOURCE3);
        assertEquals(TRANSLATED3, translated);
    }
    
    private String getPseudoTranslationFromEnToJa(final String source) {
        final String EN_FULLSTOP = ".";
        final String JA_FULLSTOP = "\\u3002";
        List<StringBuilder> spaces = new ArrayList<StringBuilder>();
        List<Rule> brules = new ArrayList<Rule>();
        List<String> segments = segmenter.segment(new Language("en"), source, spaces, brules);

        // pseudo-translation (just replace full-stop char)
        for (int i = 0; i < segments.size(); i++) {
            segments.set(i, segments.get(i).replace(EN_FULLSTOP, JA_FULLSTOP));
        }
        return segmenter.glue(new Language("en"), new Language("ja"), segments, spaces, brules);
    }
}
