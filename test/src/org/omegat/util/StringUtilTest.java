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

package org.omegat.util;

import java.util.Locale;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * Tests for (some) static utility methods.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class StringUtilTest {

    @Test
    public void testIsSubstringAfter() {
        assertFalse(StringUtil.isSubstringAfter("123456", 5, "67"));
        assertTrue(StringUtil.isSubstringAfter("123456", 5, "6"));
        assertTrue(StringUtil.isSubstringAfter("123456", 4, "56"));
        assertTrue(StringUtil.isSubstringAfter("123456", 0, "12"));
        assertTrue(StringUtil.isSubstringAfter("123456", 1, "23"));
    }

    @Test
    public void testIsTitleCase() {
        assertFalse(StringUtil.isTitleCase("foobar"));
        assertFalse(StringUtil.isTitleCase("fooBar"));
        assertFalse(StringUtil.isTitleCase("f1obar"));
        assertFalse(StringUtil.isTitleCase("FooBar"));
        assertTrue(StringUtil.isTitleCase("Fo1bar"));
        assertTrue(StringUtil.isTitleCase("Foobar"));
        // LATIN CAPITAL LETTER L WITH SMALL LETTER J (U+01C8)
        assertTrue(StringUtil.isTitleCase("\u01C8bcd"));
        assertFalse(StringUtil.isTitleCase("a\u01C8bcd"));
        
        // LATIN CAPITAL LETTER L WITH SMALL LETTER J (U+01C8)
        assertTrue(StringUtil.isTitleCase("\u01c8"));
        // LATIN CAPITAL LETTER LJ (U+01C7)
        assertFalse(StringUtil.isTitleCase("\u01c7"));
        // LATIN SMALL LETTER LJ (U+01C9)
        assertFalse(StringUtil.isTitleCase("\u01c9"));
    }
    
    @Test
    public void testIsSubstringBefore() {
        assertFalse(StringUtil.isSubstringBefore("123456", 1, "01"));
        assertTrue(StringUtil.isSubstringBefore("123456", 1, "1"));
        assertTrue(StringUtil.isSubstringBefore("123456", 2, "12"));
        assertTrue(StringUtil.isSubstringBefore("123456", 6, "56"));
        assertTrue(StringUtil.isSubstringBefore("123456", 5, "45"));
    }
    
    @Test
    public void testUnicodeNonBMP() {
        // MATHEMATICAL BOLD CAPITAL A (U+1D400)
        String test = "\uD835\uDC00";
        assertTrue(StringUtil.isUpperCase(test));
        assertFalse(StringUtil.isLowerCase(test));
        assertTrue(StringUtil.isTitleCase(test));
        
        // MATHEMATICAL BOLD CAPITAL A (U+1D400) x2
        test = "\uD835\uDC00\uD835\uDC00";
        assertTrue(StringUtil.isUpperCase(test));
        assertFalse(StringUtil.isLowerCase(test));
        assertFalse(StringUtil.isTitleCase(test));
        
        // MATHEMATICAL BOLD SMALL A (U+1D41A)
        test = "\uD835\uDC1A";
        assertFalse(StringUtil.isUpperCase(test));
        assertTrue(StringUtil.isLowerCase(test));
        assertFalse(StringUtil.isTitleCase(test));
        
        // MATHEMATICAL BOLD CAPITAL A + MATHEMATICAL BOLD SMALL A
        test = "\uD835\uDC00\uD835\uDC1A";
        assertFalse(StringUtil.isUpperCase(test));
        assertFalse(StringUtil.isLowerCase(test));
        assertTrue(StringUtil.isTitleCase(test));
        
        // MATHEMATICAL BOLD SMALL A + MATHEMATICAL BOLD CAPITAL A
        test = "\uD835\uDC1A\uD835\uDC00";
        assertFalse(StringUtil.isUpperCase(test));
        assertFalse(StringUtil.isLowerCase(test));
        assertFalse(StringUtil.isTitleCase(test));
    }
    
    @Test
    public void testEmptyStringCase() {
        String test = null;
        try {
            assertFalse(StringUtil.isUpperCase(test));
            fail("Should throw an NPE");
        } catch (NullPointerException ex) {
            // OK
        }
        try {
            assertFalse(StringUtil.isLowerCase(test));
            fail("Should throw an NPE");
        } catch (NullPointerException ex) {
            // OK
        }
        try {
            assertFalse(StringUtil.isTitleCase(test));
            fail("Should throw an NPE");
        } catch (NullPointerException ex) {
            // OK
        }
        try {
            StringUtil.toTitleCase(test, Locale.ENGLISH);
            fail("Should throw an NPE");
        } catch (NullPointerException ex) {
            // OK
        }
        
        test = "";
        assertFalse(StringUtil.isUpperCase(test));
        assertFalse(StringUtil.isLowerCase(test));
        assertFalse(StringUtil.isTitleCase(test));
        assertEquals("", StringUtil.toTitleCase("", Locale.ENGLISH));
    }
    
    @Test
    public void testIsWhiteSpace() {
        try {
            assertFalse(StringUtil.isWhiteSpace(null));
            fail("Should throw an NPE");
        } catch (NullPointerException ex) {
            // OK
        }
        assertFalse(StringUtil.isWhiteSpace(""));
        assertTrue(StringUtil.isWhiteSpace(" "));
        assertFalse(StringUtil.isWhiteSpace(" a "));
        // SPACE (U+0020) + IDEOGRAPHIC SPACE (U+3000)
        assertTrue(StringUtil.isWhiteSpace(" \u3000"));
        // We consider whitespace but Character.isWhiteSpace(int) doesn't:
        // NO-BREAK SPACE (U+00A0) + FIGURE SPACE (U+2007) + NARROW NO-BREAK SPACE (U+202F)
        assertTrue(StringUtil.isWhiteSpace("\u00a0\u2007\u202f"));
    }
    
    @Test
    public void testIsMixedCase() {
        assertTrue(StringUtil.isMixedCase("ABc"));
        assertTrue(StringUtil.isMixedCase("aBc"));
        // This is title case, not mixed:
        assertFalse(StringUtil.isMixedCase("Abc"));
        // Non-letter characters should not affect the result:
        assertTrue(StringUtil.isMixedCase(" {ABc"));
    }
    
    @Test
    public void testNonWordCase() {
        String test = "{";
        assertFalse(StringUtil.isLowerCase(test));
        assertFalse(StringUtil.isUpperCase(test));
        assertFalse(StringUtil.isTitleCase(test));
        assertFalse(StringUtil.isMixedCase(test));
    }
    
    @Test
    public void testToTitleCase() {
        Locale locale = Locale.ENGLISH;
        assertEquals("Abc", StringUtil.toTitleCase("abc", locale));
        assertEquals("Abc", StringUtil.toTitleCase("ABC", locale));
        assertEquals("Abc", StringUtil.toTitleCase("Abc", locale));
        assertEquals("Abc", StringUtil.toTitleCase("abc", locale));
        assertEquals("Abc", StringUtil.toTitleCase("aBC", locale));
        assertEquals("A", StringUtil.toTitleCase("a", locale));
        // LATIN SMALL LETTER NJ (U+01CC) -> LATIN CAPITAL LETTER N WITH SMALL LETTER J (U+01CB)
        assertEquals("\u01CB", StringUtil.toTitleCase("\u01CC", locale));
        // LATIN SMALL LETTER I (U+0069) -> LATIN CAPITAL LETTER I WITH DOT ABOVE (U+0130) in Turkish
        assertEquals("\u0130jk", StringUtil.toTitleCase("ijk", new Locale("tr")));
    }
    
    public void testCompressSpace() {
        assertEquals("One Two Three Four Five", StringUtil.compressSpaces(" One Two\nThree   Four\r\nFive "));
        assertEquals("Six seven", StringUtil.compressSpaces("Six\tseven"));
    }
    
    public void testIsValidXMLChar() {
        assertFalse(StringUtil.isValidXMLChar(0x01));
        assertTrue(StringUtil.isValidXMLChar(0x09));
        assertTrue(StringUtil.isValidXMLChar(0x0A));
        assertTrue(StringUtil.isValidXMLChar(0x0D));
        
        assertTrue(StringUtil.isValidXMLChar(0x21));
        assertFalse(StringUtil.isValidXMLChar(0xD800));
        
        assertTrue(StringUtil.isValidXMLChar(0xE000));
        assertFalse(StringUtil.isValidXMLChar(0xFFFE));
        
        assertTrue(StringUtil.isValidXMLChar(0x10000));
        assertFalse(StringUtil.isValidXMLChar(0x110000));
    }

    public void testCapitalizeFirst() {
        Locale locale = Locale.ENGLISH;
        assertEquals("Abc", StringUtil.capitalizeFirst("abc", locale));
        assertEquals("ABC", StringUtil.capitalizeFirst("ABC", locale));
        assertEquals("Abc", StringUtil.capitalizeFirst("Abc", locale));
        assertEquals("Abc", StringUtil.capitalizeFirst("abc", locale));
        assertEquals("AbC", StringUtil.capitalizeFirst("abC", locale));
        assertEquals("A", StringUtil.capitalizeFirst("a", locale));
        // LATIN SMALL LETTER NJ (U+01CC) -> LATIN CAPITAL LETTER N WITH SMALL LETTER J (U+01CB)
        assertEquals("\u01CB", StringUtil.capitalizeFirst("\u01CC", locale));
        // LATIN SMALL LETTER I (U+0069) -> LATIN CAPITAL LETTER I WITH DOT ABOVE (U+0130) in Turkish
        assertEquals("\u0130jk", StringUtil.capitalizeFirst("ijk", new Locale("tr")));
    }
    
    public void testMatchCapitalization() {
        Locale locale = Locale.ENGLISH;
        String text = "foo";
        // matchTo is empty -> return original text
        assertEquals(text, StringUtil.matchCapitalization(text, null, locale));
        assertEquals(text, StringUtil.matchCapitalization(text, "", locale));
        // text starts with matchTo -> return original text
        assertEquals(text, StringUtil.matchCapitalization(text, text + "BAR", locale));
        // matchTo is title case
        assertEquals("Foo", StringUtil.matchCapitalization(text, "Abc", locale));
        assertEquals("Foo", StringUtil.matchCapitalization(text, "A", locale));
        // matchTo is lower case
        assertEquals("foo", StringUtil.matchCapitalization("FOO", "lower", locale));
        assertEquals("foo", StringUtil.matchCapitalization("fOo", "l", locale));
        // matchTo is upper case
        assertEquals("FOO", StringUtil.matchCapitalization(text, "UPPER", locale));
        assertEquals("FOO", StringUtil.matchCapitalization("fOo", "UP", locale));
        assertEquals("FOo", StringUtil.matchCapitalization("fOo", "U", locale)); // Interpreted as title case
        // matchTo is mixed or not cased
        assertEquals(text, StringUtil.matchCapitalization(text, "bAzZ", locale));
        assertEquals(text, StringUtil.matchCapitalization(text, ".", locale));
    }

    public void testFirstN() {
        // MATHEMATICAL BOLD CAPITAL A (U+1D400) x2
        String test = "\uD835\uDC00\uD835\uDC00";
        assertTrue(StringUtil.firstN(test, 0).isEmpty());
        assertEquals("\uD835\uDC00", StringUtil.firstN(test, 1));
        assertEquals(test, StringUtil.firstN(test, 2));
        assertEquals(test, StringUtil.firstN(test, 100));
    }

    public void testTruncateString() {
        // MATHEMATICAL BOLD CAPITAL A (U+1D400) x3
        String test = "\uD835\uDC00\uD835\uDC00\uD835\uDC00";
        try {
            StringUtil.truncate(test, 0);
            fail();
        } catch (IndexOutOfBoundsException ex) {
            // Ignore
        }
        assertEquals(String.valueOf(StringUtil.TRUNCATE_CHAR), StringUtil.truncate(test, 1));
        assertEquals("\uD835\uDC00" + StringUtil.TRUNCATE_CHAR, StringUtil.truncate(test, 2));
        assertEquals(test, StringUtil.truncate(test, 3));
        assertEquals(test, StringUtil.truncate(test, 100));
    }

    public void testNormalizeWidth() {
        String test = "Foo 123 " // ASCII
                + "\uFF26\uFF4F\uFF4F\u3000\uFF11\uFF12\uFF13 " // Full-width alphanumerics
                + "\uFF01\uFF1F\uFF08\uFF09 " // Full-width punctuation
                + "\u3371 " // Squared Latin Abbreviations
                + "\u2100 " // Letter-Like Symbols
                + "\u30AC\u30D1\u30AA " // Katakana
                + "\uD55C\uAD6D\uC5B4 " // Full-width Hangul
                + "\u314E\u314F\u3134"; // Full-width Jamo
        assertEquals("Foo 123 Foo 123 !?() hPa a/c \u30AC\u30D1\u30AA \uD55C\uAD6D\uC5B4 \u314E\u314F\u3134",
                StringUtil.normalizeWidth(test));
        test = "\uFF26\uFF4F\uFF4F\u3000\uFF11\uFF12\uFF13 " // Full-width alphanumerics
                + "Foo 123 !?() " // ASCII
                + "\uFF76\uFF9E\uFF8A\uFF9F\uFF75 " // Half-width Katakana
                + "\uFFBE\uFFC2\uFFA4"; // Half-width Jamo
        assertEquals("Foo 123 Foo 123 !?() \u30AC\u30D1\u30AA \u314E\uFFC2\u3134",
                StringUtil.normalizeWidth(test));
        test = "\uff21\uff22\uff23\uff0e\uff11\uff12\uff13\uff04\uff01";
        assertEquals("ABC.123$!", StringUtil.normalizeWidth(test));
        test = "\u30a2\uff71\u30ac\uff76\u3099\u3000";
        assertEquals("\u30a2\u30a2\u30ac\u30ac ", StringUtil.normalizeWidth(test));
    }
}
