/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2010-2013 Alex Buloichik
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

package org.omegat.languagetools;

import java.util.List;

import org.junit.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.Belarusian;
import org.languagetool.language.French;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.UppercaseSentenceStartRule;
import org.languagetool.rules.patterns.PatternRule;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class LanguageToolTest {

    @Test
    public void testExecute() throws Exception {
        JLanguageTool lt = new JLanguageTool(new Belarusian());

        // The test string is Russian(?); originally it was actual UTF-8,
        // but that causes the test to fail when environment encodings aren't set
        // correctly, so we are now using Unicode literals.
        List<RuleMatch> matches = lt.check("\u0441\u043F\u0440\u0430\u0443\u0434\u0437\u0456\u043C.");
        assertEquals(2, matches.size());
        assertTrue(matches.get(0).getRule() instanceof UppercaseSentenceStartRule);
        assertTrue(matches.get(1).getRule() instanceof PatternRule);
    }

    @Test
    public void testFrench() throws Exception {
        JLanguageTool lt = new JLanguageTool(new French());

        List<RuleMatch> matches = lt.check("Directeur production du groupe");
        assertEquals(1, matches.size());
        assertTrue(matches.get(0).getRule() instanceof PatternRule);
    }

    @Test
    public void testEnglish() throws Exception {
        JLanguageTool lt = new JLanguageTool(new AmericanEnglish());

        List<RuleMatch> matches = lt.check("Check test");
        assertEquals(0, matches.size());
    }
}
