/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2015 Aaron Madlon-Kay
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

package org.omegat;

import java.util.Locale;

/**
 * A class to hold all command-line arguments understood by OmegaT.
 * <p>
 * See also: COMMAND_LINE_HELP in Bundle.properties
 * 
 * @author Aaron Madlon-Kay
 */
public class CLIParameters {

    // Help
    public static final String HELP_SHORT = "-h";
    public static final String HELP = "--help";

    // All modes
    public static final String MODE = "mode";
    public static final String CONFIG_FILE = "config-file";
    public static final String RESOURCE_BUNDLE = "resource-bundle";
    public static final String CONFIG_DIR = "config-dir";
    public static final String DISABLE_PROJECT_LOCKING = "disable-project-locking";
    public static final String DISABLE_LOCATION_SAVE = "disable-location-save";
    /** CLI parameter to disable team functionality (treat as local project) */
    public static final String NO_TEAM = "no-team";
    /** CLI parameter to specify source tokenizer */
    public static final String TOKENIZER_SOURCE = "ITokenizer";
    /** CLI parameter to specify target tokenizer */
    public static final String TOKENIZER_TARGET = "ITokenizerTarget";
    // TODO: Document this; see RealProject.patchFileNameForEntryKey()
    public static final String ALTERNATE_FILENAME_FROM = "alternate-filename-from";
    // TODO: Document this; see RealProject.patchFileNameForEntryKey()
    public static final String ALTERNATE_FILENAME_TO = "alternate-filename-to";

    // Non-GUI modes only
    public static final String QUIET = "quiet";
    public static final String SCRIPT = "script";
    public static final String TAG_VALIDATION = "tag-validation";

    // CONSOLE_TRANSLATE mode
    public static final String SOURCE_PATTERN = "source-pattern";

    // CONSOLE_CREATEPSEUDOTRANSLATETMX mode
    public static final String PSEUDOTRANSLATETMX = "pseudotranslatetmx";
    public static final String PSEUDOTRANSLATETYPE = "pseudotranslatetype";

    // CONSOLE_ALIGN mode
    public static final String ALIGNDIR = "alignDir";

    /**
     * Application execution mode. Value of {@link #MODE}.
     */
    enum RUN_MODE {
        GUI, CONSOLE_TRANSLATE, CONSOLE_CREATEPSEUDOTRANSLATETMX, CONSOLE_ALIGN;
        public static RUN_MODE parse(String s) {
            try {
                return valueOf(normalize(s));
            } catch (Exception ex) {
                // default mode
                return GUI;
            }
        }
    }

    /**
     * Choice of types of translation for all segments in the optional, special
     * TMX file that contains all segments of the project. Value of
     * {@link #PSEUDOTRANSLATETYPE}.
     */
    public enum PSEUDO_TRANSLATE_TYPE {
        EQUAL, EMPTY;
        public static PSEUDO_TRANSLATE_TYPE parse(String s) {
            try {
                return valueOf(normalize(s));
            } catch (Exception ex) {
                // default mode
                return EQUAL;
            }
        }
    }

    /**
     * Behavior when validating tags. Value of {@link #TAG_VALIDATION}.
     */
    public enum TAG_VALIDATION_MODE {
        IGNORE, WARN, ABORT;
        public static TAG_VALIDATION_MODE parse(String s) {
            try {
                return valueOf(normalize(s));
            } catch (Exception ex) {
                // default mode
                return IGNORE;
            }
        }
    }

    private static String normalize(String s) {
        return s.toUpperCase(Locale.ENGLISH).replace('-', '_');
    }
}
