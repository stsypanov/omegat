/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
 with fuzzy matching, translation memory, keyword search,
 glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2007 Didier Briel and Tiago Saboga
               2007 Zoltan Bartko - bartkozoltan@bartkozoltan.com
               2008 Andrzej Sawula
               2010-2013 Alex Buloichik
               2015 Zoltan Bartko, Aaron Madlon-Kay
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

import java.text.Normalizer;
import java.util.Locale;

/**
 * Utilities for string processing.
 *
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 * @author Tiago Saboga
 * @author Zoltan Bartko
 * @author Andrzej Sawula
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class StringUtil {

    /**
     * Check if string is empty, i.e. null or length==0
     */
    public static boolean isEmpty(final String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Check if string is not empty
     */
    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Returns true if the input has at least one letter and
     * all letters are lower case.
     */
    public static boolean isLowerCase(final String input) {
        if (input.isEmpty()) {
            return false;
        }
        boolean hasLetters = false;
        for (int i = 0, cp; i < input.length(); i += Character.charCount(cp)) {
            cp = input.codePointAt(i);
            if (Character.isLetter(cp)) {
                hasLetters = true;
                if (!Character.isLowerCase(cp)) {
                    return false;
                }
            }
        }
        return hasLetters;
    }

    /**
     * Returns true if the input is upper case.
     */
    public static boolean isUpperCase(final String input) {
        if (input.isEmpty()) {
            return false;
        }
        boolean hasLetters = false;
        for (int i = 0, cp; i < input.length(); i += Character.charCount(cp)) {
            cp = input.codePointAt(i);
            if (Character.isLetter(cp)) {
                hasLetters = true;
                if (!Character.isUpperCase(cp)) {
                    return false;
                }
            }
        }
        return hasLetters;
    }

    /**
     * Returns true if the input has both upper case and lower case letters, but
     * is not title case.
     */
    public static boolean isMixedCase(final String input) {
        if (input.isEmpty() || input.codePointCount(0, input.length()) < 2) {
            return false;
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        for (int i = 0, cp; i < input.length(); i += Character.charCount(cp)) {
            cp = input.codePointAt(i);
            if (Character.isLetter(cp)) {
                // Don't count the first cp as upper to allow for title case
                if (Character.isUpperCase(cp) && i > 0) {
                    hasUpper = true;
                } else if (Character.isLowerCase(cp)) {
                    hasLower = true;
                }
                if (hasUpper && hasLower) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the input is title case, meaning the first character is UpperCase or
     * TitleCase* and the rest of the string (if present) is LowerCase.
     * <p>
     * *There are exotic characters that are neither UpperCase nor LowerCase, but are TitleCase:
     * e.g. LATIN CAPITAL LETTER L WITH SMALL LETTER J (U+01C8)<br>
     * These are handled correctly.
     */
    public static boolean isTitleCase(final String input) {
        if (input.length() > 1)
            return Character.isTitleCase(input.charAt(0)) && isLowerCase(input.substring(1));
        else
            return notEmpty(input) && Character.isTitleCase(input.charAt(0));
    }

    public static boolean isTitleCase(int codePoint) {
        // True if is actual title case, or if is upper case and has no separate title case variant.
        return Character.isTitleCase(codePoint) ||
                (Character.isUpperCase(codePoint) && Character.toTitleCase(codePoint) == codePoint);
    }

    /**
     * Returns true if the input consists only of whitespace characters
     * (including non-breaking characters that are false according to
     * {@link Character#isWhitespace(int)}).
     */
    public static boolean isWhiteSpace(final String input) {
        if (input.isEmpty()) {
            return false;
        }
        for (int i = 0, cp; i < input.length(); i += Character.charCount(cp)) {
            cp = input.codePointAt(i);
            if (!isWhiteSpace(cp)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the input is a whitespace character
     * (including non-breaking characters that are false according to
     * {@link Character#isWhitespace(int)}).
     */
    public static boolean isWhiteSpace(int codePoint) {
        return Character.isWhitespace(codePoint)
                || codePoint == '\u00A0'
                || codePoint == '\u2007'
                || codePoint == '\u202F';
    }

    public static boolean isCJK(String input) {
        if (input.isEmpty()) {
            return false;
        }
        for (int i = 0, cp; i < input.length(); i += Character.charCount(cp)) {
            cp = input.codePointAt(i);
            // Anything less than CJK Radicals Supplement is "not CJK". Everything else is.
            // TODO: Make this smarter?
            if (cp < '\u2E80') {
                return false;
            }
        }
        return true;
    }

    /**
     * Convert text to title case according to the supplied locale.
     */
    public static String toTitleCase(String text, Locale locale) {
        if (text.isEmpty()) {
            return text;
        }
        int firstTitleCase = Character.toTitleCase(text.codePointAt(0));
        int remainderOffset = text.offsetByCodePoints(0, 1);
        // If the first codepoint has an actual title case variant (rare), use that.
        // Otherwise convert first codepoint to upper case according to locale.
        String first = Character.isTitleCase(firstTitleCase)
                    ? String.valueOf(Character.toChars(firstTitleCase))
                    : text.substring(0, remainderOffset).toUpperCase(locale);
        return first + text.substring(remainderOffset).toLowerCase(locale);
    }

	/**
	 * Returns first not null object from list, or null if all values is null.
	 */
	public static <T> T nvl(T... values) {
		for (T value : values) {
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/**
	 * Returns first non-zero object from list, or zero if all values is null.
	 */
	public static long nvlLong(long... values) {
		for (long value : values) {
			if (value != 0) {
				return value;
			}
		}
		return 0;
	}

	/**
	 * Compare two values, which could be null.
	 */
	public static <T> boolean equalsWithNulls(T v1, T v2) {
        return v1 == null && v2 == null || v1 != null && v2 != null && v1.equals(v2);
    }

	/**
	 * Compare two values, which could be null.
	 */
	public static <T extends Comparable<T>> int compareToWithNulls(T v1, T v2) {
		if (v1 == null && v2 == null) {
			return 0;
		} else if (v1 == null) {
			return -1;
		} else if (v2 == null) {
			return 1;
		} else {
			return v1.compareTo(v2);
		}
	}

    /**
     * Extracts first N chars from string.
     */
    public static String firstN(String str, int len) {
        if (str.length() < len) {
            return str;
        } else {
            return str.substring(0, len) + "...";
        }
    }

    /**
     * Returns first letter in lowercase. Usually used for create tag shortcuts.
     * Does not support non-BMP Unicode characters.
     */
   	public static char getFirstLetterLowercase(CharSequence s) {
		if (s == null) {
			return 0;
		}

		char f = 0;

		for (int i = 0; i < s.length(); i++) {
			if (Character.isLetter(s.charAt(i))) {
				f = Character.toLowerCase(s.charAt(i));
				break;
			}
		}

		return f;
	}

	/**
	 * Checks if text contains substring after specified position.
	 */
	public static boolean isSubstringAfter(String text, int pos, String substring) {
		if (pos + substring.length() > text.length()) {
			return false;
		}
		return substring.equals(text.substring(pos, pos + substring.length()));
	}

    /**
     * Checks if text contains substring before specified position.
     */
    public static boolean isSubstringBefore(String text, int pos, String substring) {
        if (pos - substring.length() < 0) {
            return false;
        }
        return substring.equals(text.substring(pos - substring.length(), pos));
    }

    public static String stripFromEnd(String string, String... toStrip) {
        if (string == null) {
            return null;
        }
        if (toStrip == null) {
            return string;
        }
        for (String s : toStrip) {
            if (string.endsWith(s)) {
                string = string.substring(0, string.length() - s.length());
            }
        }
        return string;
    }

    /**
     * Apply Unicode NFC normalization to a string.
     */
    public static String normalizeUnicode(CharSequence text) {
        return Normalizer.isNormalized(text, Normalizer.Form.NFC) ? text.toString() :
            Normalizer.normalize(text, Normalizer.Form.NFC);
    }
}
