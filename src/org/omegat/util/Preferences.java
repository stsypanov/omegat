/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey, Maxym Mykhalchuk, and Henry Pijffers
               2007 Zoltan Bartko
               2008-2009 Didier Briel
               2010 Wildrich Fourie, Antonio Vilei, Didier Briel
               2011 John Moran, Didier Briel
               2012 Martin Fleurke, Wildrich Fourie, Didier Briel, Thomas Cordonnier,
                    Aaron Madlon-Kay
               2013 Aaron Madlon-Kay, Zoltan Bartko
               2014 Piotr Kulik, Aaron Madlon-Kay
               2015 Aaron Madlon-Kay
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omegat.core.segmentation.SRX;
import org.omegat.filters2.TranslationException;
import org.omegat.util.xml.XMLBlock;
import org.omegat.util.xml.XMLStreamReader;

/**
 * Class to load & save OmegaT preferences. All methods are static here.
 * 
 * @author Keith Godfrey
 * @author Maxym Mykhalchuk
 * @author Henry Pijffers
 * @author Zoltan Bartko - bartkozoltan@bartkozoltan.com
 * @author Didier Briel
 * @author Wildrich Fourie
 * @author Antonio Vilei
 * @author Martin Fleurke
 * @author John Moran
 * @author Aaron Madlon-Kay
 */
public class Preferences {
    /** OmegaT-wide Preferences Filename */
    public static final String FILE_PREFERENCES = OStrings.BRANDING.isEmpty() ? "omegat.prefs"
            : "omegat-" + OStrings.BRANDING + ".prefs";

    // preference names
    public static final String SOURCE_LOCALE = "source_lang";
    public static final String TARGET_LOCALE = "target_lang";
    public static final String CURRENT_FOLDER = "current_folder";
    public static final String SOURCE_FOLDER = "source_folder";
    public static final String TARGET_FOLDER = "target_folder";
    public static final String TM_FOLDER = "tm_folder";
    public static final String DICT_FOLDER = "dict_folder";
    public static final String GLOSSARY_FOLDER = "glossary_folder";
    public static final String GLOSSARY_FILE = "glossary_file";
    public static final String GLOSSARY_TBX_DISPLAY_CONTEXT = "glossary_tbx_display_context";

    public static final String MAINWINDOW_WIDTH = "screen_width";
    public static final String MAINWINDOW_HEIGHT = "screen_height";
    public static final String MAINWINDOW_X = "screen_x";
    public static final String MAINWINDOW_Y = "screen_y";
    public static final String MAINWINDOW_LAYOUT = "docking_layout";

    // Project files window size and position
    public static final String PROJECT_FILES_WINDOW_WIDTH = "project_files_window_width";
    public static final String PROJECT_FILES_WINDOW_HEIGHT = "project_files_window_height";
    public static final String PROJECT_FILES_WINDOW_X = "project_files_window_x";
    public static final String PROJECT_FILES_WINDOW_Y = "project_files_window_y";
    // Using the main font for the Project Files window
    public static final String PROJECT_FILES_USE_FONT = "project_files_use_font";
    // Determines whether or not the Project Files window is shown on project load.
    // Currently not exposed in UI.
    public static final String PROJECT_FILES_SHOW_ON_LOAD = "project_files_show_on_load";

    // Search window size and position
    public static final String SEARCHWINDOW_WIDTH = "search_window_width";
    public static final String SEARCHWINDOW_HEIGHT = "search_window_height";
    public static final String SEARCHWINDOW_X = "search_window_x";
    public static final String SEARCHWINDOW_Y = "search_window_y";
    public static final String SEARCHWINDOW_SEARCH_TYPE = "search_window_search_type";
    public static final String SEARCHWINDOW_REPLACE_TYPE = "search_window_replace_type";
    public static final String SEARCHWINDOW_CASE_SENSITIVE = "search_window_case_sensitive";
    public static final String SEARCHWINDOW_SPACE_MATCH_NBSP = "search_window_space_match_nbsp";
    public static final String SEARCHWINDOW_CASE_SENSITIVE_REPLACE = "search_window_case_sensitive_replace";
    public static final String SEARCHWINDOW_SPACE_MATCH_NBSP_REPLACE = "search_window_space_match_nbsp_replace";
    public static final String SEARCHWINDOW_REPLACE_UNTRANSLATED = "search_window_replace_untranslated";
    public static final String SEARCHWINDOW_SEARCH_SOURCE = "search_window_search_source";
    public static final String SEARCHWINDOW_SEARCH_TRANSLATION = "search_window_search_translation";
    public static final String SEARCHWINDOW_SEARCH_STATE = "search_window_search_state";
    public static final String SEARCHWINDOW_SEARCH_NOTES = "search_window_search_notes";
    public static final String SEARCHWINDOW_SEARCH_COMMENTS = "search_window_search_comments";
    public static final String SEARCHWINDOW_REG_EXPRESSIONS = "search_window_reg_expressions";
    public static final String SEARCHWINDOW_GLOSSARY_SEARCH = "search_window_glossary_search";
    public static final String SEARCHWINDOW_MEMORY_SEARCH = "search_window_memory_search";
    public static final String SEARCHWINDOW_TM_SEARCH = "search_window_tm_search";
    public static final String SEARCHWINDOW_ALL_RESULTS = "search_window_all_results";
    public static final String SEARCHWINDOW_FILE_NAMES = "search_window_file_names";
    public static final String SEARCHWINDOW_ADVANCED_VISIBLE = "search_window_advanced_visible";
    public static final String SEARCHWINDOW_SEARCH_AUTHOR = "search_window_search_author";
    public static final String SEARCHWINDOW_AUTHOR_NAME = "search_window_author_name";
    public static final String SEARCHWINDOW_DATE_FROM = "search_window_date_from";
    public static final String SEARCHWINDOW_DATE_FROM_VALUE = "search_window_date_from_value";
    public static final String SEARCHWINDOW_DATE_TO = "search_window_date_to";
    public static final String SEARCHWINDOW_DATE_TO_VALUE = "search_window_date_to_value";
    public static final String SEARCHWINDOW_NUMBER_OF_RESULTS = "search_window_number_of_results";
    public static final String SEARCHWINDOW_DIR = "search_window_dir";
    public static final String SEARCHWINDOW_SEARCH_FILES = "search_window_search_files";
    public static final String SEARCHWINDOW_RECURSIVE = "search_window_search_recursive";
    
    // Search history
    public static final String SEARCHWINDOW_HISTORY_SIZE = "search_window_history_size";
    public static final String SEARCHWINDOW_SEARCH_HISTORY_ITEM_PREFIX = "search_window_search_history_item_";
    public static final String SEARCHWINDOW_REPLACE_HISTORY_ITEM_PREFIX = "search_window_replace_history_item_";

    // Tag validation window size and position
    public static final String TAGVWINDOW_WIDTH = "tagv_window_width";
    public static final String TAGVWINDOW_HEIGHT = "tagv_window_height";
    public static final String TAGVWINDOW_X = "tagv_window_x";
    public static final String TAGVWINDOW_Y = "tagv_window_y";

    // Help window size and position
    public static final String HELPWINDOW_WIDTH = "help_window_width";
    public static final String HELPWINDOW_HEIGHT = "help_window_height";
    public static final String HELPWINDOW_X = "help_window_x";
    public static final String HELPWINDOW_Y = "help_window_y";

    /** Use the TAB button to advance to the next segment */
    public static final String USE_TAB_TO_ADVANCE = "tab_advance";
    /** Always confirm Quit, even if the project is saved */
    public static final String ALWAYS_CONFIRM_QUIT = "always_confirm_quit";

    public static final String ALLOW_GOOGLE_TRANSLATE = "allow_google_translate";
    public static final String ALLOW_GOOGLE2_TRANSLATE = "allow_google2_translate";

    public static final String ALLOW_BELAZAR_TRANSLATE = "allow_belazar_translate";

    public static final String ALLOW_APERTIUM_TRANSLATE = "allow_apertium_translate";

    public static final String ALLOW_MICROSOFT_TRANSLATE = "allow_microsoft_translate";
    
    public static final String ALLOW_MYMEMORY_HUMAN_TRANSLATE = "allow_mymemory_human_translate";
    public static final String ALLOW_MYMEMORY_MACHINE_TRANSLATE = "allow_mymemory_machine_translate";
    
    public static final String ALLOW_YANDEX_TRANSLATE = "allow_yandex_translate";
    
    /** Enable TransTips */
    public static final String TRANSTIPS = "transtips";
    /** TransTips Option: Only match exact words */
    public static final String TRANSTIPS_EXACT_SEARCH = "transtips_exact_search";

    /** Mark the segments with a note with a different color */
    public static final String MARK_NOTED_SEGMENTS = "mark_noted_segments";
    
    /** Mark the non-breakable spaces with a different color */
    public static final String MARK_NBSP = "mark_nbsp";
    /** Mark whitespace as symbols */
    public static final String MARK_WHITESPACE = "mark_whitespace";
    /** Mark Bidi controls as symbols */
    public static final String MARK_BIDI = "mark_bidi";

    /** Mark the translated segments with a different color */
    public static final String MARK_TRANSLATED_SEGMENTS = "mark_translated_segments";

    public static final String MARK_AUTOPOPULATED = "mark_autopopulated";

    /** Mark the untranslated segments with a different color */
    public static final String MARK_UNTRANSLATED_SEGMENTS = "mark_untranslated_segments";

    /** Workflow Option: Don't Insert Source Text Into Translated Segment */
    public static final String DONT_INSERT_SOURCE_TEXT = "wf_noSourceText";
    /** Workflow Option: Allow translation to be equal to source */
    public static final String ALLOW_TRANS_EQUAL_TO_SRC = "wf_allowTransEqualToSrc";
    /** Workflow Option: Insert Best Match Into Translated Segment */
    public static final String BEST_MATCH_INSERT = "wf_insertBestMatch";
    /** Workflow Option: Minimal Similarity Of the Best Fuzzy Match to insert */
    public static final String BEST_MATCH_MINIMAL_SIMILARITY = "wf_minimalSimilarity";
    /**
     * Default Value of Workflow Option: Minimal Similarity Of the Best Fuzzy
     * Match to insert
     */
    public static final String BEST_MATCH_MINIMAL_SIMILARITY_DEFAULT = "80";
    /** Workflow Option: Insert Explanatory Text before the Best Fuzzy Match */
    public static final String BEST_MATCH_EXPLANATORY_TEXT = "wf_explanatoryText";
    /** Workflow Option: Export current segment */
    public static final String EXPORT_CURRENT_SEGMENT = "wf_exportCurrentSegment";
    /** Workflow Option: Go To Next Untranslated Segment stops when there is at least one
    alternative translation */
    public static final String STOP_ON_ALTERNATIVE_TRANSLATION="wf_stopOnAlternativeTranslation";
    /** Workflow Option: Attempt to convert numbers when inserting a fuzzy match */
    public static final String CONVERT_NUMBERS = "wf_convertNumbers";
    /** Workflow Option: Save auto-populated status */
    public static final String SAVE_AUTO_STATUS = "save_auto_status";
    
    /** Tag Validation Option: Don't check printf-tags */
    public static final String DONT_CHECK_PRINTF_TAGS = "tagValidation_noCheck";
    /** Tag Validation Option: check simple printf-tags */
    public static final String CHECK_SIMPLE_PRINTF_TAGS = "tagValidation_simpleCheck";
    /** Tag Validation Option: check all printf-tags */
    public static final String CHECK_ALL_PRINTF_TAGS = "tagValidation_elaborateCheck";
    /** Tag Validation Option: check simple java MessageFormat pattern tags */
    public static final String CHECK_JAVA_PATTERN_TAGS = "tagValidation_javaMessageFormatSimplePatternCheck";
    /** Tag Validation Option: check user defined tags according to regexp.*/
    public static final String CHECK_CUSTOM_PATTERN = "tagValidation_customPattern";
    /** Tag Validation Option: check target for text that should have been removed according to regexp.*/
    public static final String CHECK_REMOVE_PATTERN = "tagValidation_removePattern";

    /** Tag Validation Option: allow tag editing in editor. */
    public static final String ALLOW_TAG_EDITING = "allowTagEditing";

    /** Tag Validation Option: allow tag editing in editor. */
    public static final String TAG_VALIDATE_ON_LEAVE = "tagValidateOnLeave";

    /** Team option: author ID */
    public static final String TEAM_AUTHOR = "team_Author";

    /**
     * allow automatic spell checking or not
     */
    public static final String ALLOW_AUTO_SPELLCHECKING = "allow_auto_spellchecking";

    /**
     * The location of the spell checker dictionaries
     */
    public static final String SPELLCHECKER_DICTIONARY_DIRECTORY = "spellcheker_dir";

    /**
     * URL of the dictionary repository
     */
    public static final String SPELLCHECKER_DICTIONARY_URL = "dictionary_url";

    /**
     * The location of the scripts
     */
    public static final String SCRIPTS_DIRECTORY = "scripts_dir";
    
    /** Quick script names */
    public static final String SCRIPTS_QUICK_1 = "scripts_quick_1";
    public static final String SCRIPTS_QUICK_2 = "scripts_quick_2";
    public static final String SCRIPTS_QUICK_3 = "scripts_quick_3";
    public static final String SCRIPTS_QUICK_4 = "scripts_quick_4";
    public static final String SCRIPTS_QUICK_5 = "scripts_quick_5";
    public static final String SCRIPTS_QUICK_6 = "scripts_quick_6";
    public static final String SCRIPTS_QUICK_7 = "scripts_quick_7";
    public static final String SCRIPTS_QUICK_8 = "scripts_quick_8";
    public static final String SCRIPTS_QUICK_9 = "scripts_quick_9";
    public static final String SCRIPTS_QUICK_0 = "scripts_quick_0";

    /** Script window */
    public static final String SCRIPTWINDOW_WIDTH = "script_window_width";
    public static final String SCRIPTWINDOW_HEIGHT = "script_window_height";
    public static final String SCRIPTWINDOW_X = "script_window_x";
    public static final String SCRIPTWINDOW_Y = "script_window_y";
    
    /** Most recent projects list */
    public static final String MOST_RECENT_PROJECTS_SIZE = "most_recent_projects_size";
    public static final String MOST_RECENT_PROJECTS_PREFIX = "most_recent_projects_";

    /**
     * display the segment sources
     */
    public static final String DISPLAY_SEGMENT_SOURCES = "display_segment_sources";

    /**
     * mark unique segments
     */
    public static final String MARK_NON_UNIQUE_SEGMENTS = "mark_non_unique_segments";

    /**
     * display modification info (author and modification date)
     */
    public static final String DISPLAY_MODIFICATION_INFO = "display_modification_info";

    /** External TMX options: Display level 2 tags */
    public static final String EXT_TMX_SHOW_LEVEL2 = "ext_tmx_show_level2";
    /** External TMX options: Use / for stand-alone tags */
    public static final String EXT_TMX_USE_SLASH = "ext_tmx_use_slash";
    /** External TMX options: Display template **/
    public static final String EXT_TMX_MATCH_TEMPLATE = "ext_tmx_match_template";
    /** External TMX options: Fuzzy match sort key **/
    public static final String EXT_TMX_SORT_KEY = "ext_tmx_sort_key";

    /** View options: Show all sources in bold */
    public static final String VIEW_OPTION_SOURCE_ALL_BOLD = "view_option_source_all_bold";
    /** View options: Mark first non-unique */
    public static final String VIEW_OPTION_UNIQUE_FIRST = "view_option_unique_first";
    /** View options: Simplify protected parts tooltips */
    public static final String VIEW_OPTION_PPT_SIMPLIFY = "view_option_ppt_simplify";
    /** View options: Modification Info display templates **/
    public static final String VIEW_OPTION_TEMPLATE_ACTIVE = "view_option_template_active";
    public static final String VIEW_OPTION_MOD_INFO_TEMPLATE = "view_option_mod_info_template";
    public static final String VIEW_OPTION_MOD_INFO_TEMPLATE_WO_DATE = "view_option_mod_info_template_wo_date";

    /** Proxy options: User name for proxy access */
    public static final String PROXY_USER_NAME = "proxy_user_name";
    /** Proxy options: Password for proxy  access */
    public static final String PROXY_PASSWORD = "proxy_password";

    /** Automatic save interval in seconds */
    public static final String AUTO_SAVE_INTERVAL = "auto_save_interval";

    /** Default number of seconds to auto save project */
    public static final String AUTO_SAVE_DEFAULT = "180";
    
    /** Custom external command for post-processing */
    public static final String EXTERNAL_COMMAND = "external_command";
    
    /** Allow per-project external commands */
    public static final String ALLOW_PROJECT_EXTERN_CMD = "allow_project_extern_cmd";

    /**
     * Version of file filters. Unfortunately cannot put it into filters itself
     * for backwards compatibility reasons.
     */
    public static final String FILTERS_VERSION = "filters_version";

    public static final String LT_DISABLED = "lt_disabled";

    public static final String LOOSE_TAG_ORDERING = "loose_tag_ordering";

    public static final String TAGS_VALID_REQUIRED = "tags_valid_required";

    public static final String STAT_COUNTING_PROTECTED_TEXT = "stat_counting_protected_text";
    public static final String STAT_COUNTING_CUSTOM_TAGS = "stat_counting_custom_tags";

    /**
     * Prefix for keys used to record default tokenizer behavior settings.
     * Prepend to the full name of the tokenizer, e.g.
     * 
     * <code>TOK_BEHAVIOR_PREFIX + tokenizer.class.getName()</code> to obtain
     * <code>tokenizer_behavior_org.omegat.tokenizer.LuceneXXTokenizer</code>
     */
    public static final String TOK_BEHAVIOR_PREFIX = "tokenizer_behavior_";

    /** glossary auto-completion */
    public static final String AC_GLOSSARY_SHOW_SOURCE = "ac_glossary_show_source";
    public static final String AC_GLOSSARY_SHOW_TARGET_BEFORE_SOURCE = "ac_glossary_show_target_before_source";
    public static final String AC_GLOSSARY_SORT_BY_SOURCE = "ac_glossary_sort_by_source";
    public static final String AC_GLOSSARY_SORT_BY_LENGTH = "ac_glossary_sort_by_length";
    public static final String AC_GLOSSARY_SORT_ALPHABETICALLY = "ac_glossary_sort_alphabetically";
    public static final String AC_GLOSSARY_CAPITALIZE = "ac_glossary_capitalize";

    /** autotext auto-completion */
    public static final String AC_AUTOTEXT_FILE_NAME = "omegat.autotext";
    public static final String AC_AUTOTEXT_SORT_BY_LENGTH = "ac_autotext_sort_by_length";
    public static final String AC_AUTOTEXT_SORT_ALPHABETICALLY = "ac_autotext_sort_alphabetically";
    public static final String AC_AUTOTEXT_SORT_FULL_TEXT = "ac_autotext_sort_full_text";

    /** char table auto-completion */
    public static final String AC_CHARTABLE_USE_CUSTOM_CHARS = "ac_chartable_use_custom_chars";
    public static final String AC_CHARTABLE_CUSTOM_CHAR_STRING = "ac_chartable_custom_char_string";
    public static final String AC_CHARTABLE_UNIQUE_CUSTOM_CHARS = "ac_chartable_unique_custom_chars";

    /** status bar progress mode */
    public static final String SB_PROGRESS_MODE = "sb_progress_mode";

    /** TaaS glossary client.*/
    public static final String TAAS_LOOKUP = "taas_lookup";
    public static final String TAAS_DOMAIN = "taas_domain";

    /** Private constructor, because this file is singleton */
    static {
        m_loaded = false;
        m_preferenceMap = new HashMap<String, Integer>(64);
        m_nameList = new ArrayList<String>(32);
        m_valList = new ArrayList<String>(32);
        m_changed = false;
        doLoad();
    }

    /**
     * Returns the defaultValue of some preference out of OmegaT's preferences
     * file.
     * <p>
     * If the key is not found, returns the empty string.
     * 
     * @param key
     *            key of the key to look up, usually OConsts.PREF_...
     * @return preference defaultValue as a string
     */
    public static String getPreference(String key) {
        if (key == null || key.equals(""))
            return "";
        if (!m_loaded)
            doLoad();

        Integer i = m_preferenceMap.get(key);
        String v = "";
        if (i != null) {
            // mapping exists - recover defaultValue
            v = m_valList.get(i);
        }
        return v;
    }
    
	/**
	 * Returns true if the preference is in OmegaT's preferences
	 * file.
	 * <p>
	 * If the key is not found return false
	 * 
	 * @param key
	 *            key of the key to look up, usually OConsts.PREF_...
	 * @return true if preferences exists
	 */
	public static boolean existsPreference(String key) {
		boolean exists = false;
		if (key == null)
			exists = false;
		if (!m_loaded)
			doLoad();
		Integer i = m_preferenceMap.get(key);
		if (i != null) {
			exists = true;
		}		
		return exists;
	}
    

    /**
     * Returns the boolean defaultValue of some preference.
     * <p>
     * Returns true if the preference exists and is equal to "true", false
     * otherwise (no such preference, or it's equal to "false", etc).
     * 
     * @param key
     *            preference key, usually OConsts.PREF_...
     * @return preference defaultValue as a boolean
     */
    public static boolean isPreference(String key) {
        return "true".equals(getPreference(key));
    }

    /**
     * Returns the boolean value of some preference out of OmegaT's preferences
     * file, if it exists.
     * <p>
     * If the key is not found, returns the default value provided.
     * 
     * @param key
     *            name of the key to look up, usually OConsts.PREF_...
     * @param defaultValue
     *            default value for the key
     * @return preference value as an boolean
     */
    public static boolean isPreferenceDefault(String key, boolean defaultValue) {
        String val = getPreference(key);
        if (StringUtil.isEmpty(val)) {
            return defaultValue;
        }
        return "true".equals(val);
    }

    /**
     * Returns the value of some preference out of OmegaT's preferences file, if
     * it exists.
     * <p>
     * If the key is not found, returns the default value provided and sets the
     * preference to the default value.
     * 
     * @param key
     *            name of the key to look up, usually OConsts.PREF_...
     * @param defaultValue
     *            default value for the key
     * @return preference value as a string
     */
    public static String getPreferenceDefault(String key, String defaultValue) {
        String val = getPreference(key);
        if (val.equals("")) {
            val = defaultValue;
            setPreference(key, defaultValue);
        }
        return val;
    }

    /**
     * Returns the value of some preference out of OmegaT's preferences file, if it exists.
     * <p>
     * If the key is not found, returns the default value provided and sets the preference to the default
     * value.
     * 
     * @param key
     *            name of the key to look up, usually OConsts.PREF_...
     * @param defaultValue
     *            default value for the key
     * @return preference value as enum
     */
    public static <T extends Enum<T>> T getPreferenceEnumDefault(String key, T defaultValue) {
        String val = getPreference(key);
        T r;
        try {
            r = Enum.valueOf(defaultValue.getDeclaringClass(), val);
        } catch (IllegalArgumentException ex) {
            r = defaultValue;
            setPreference(key, defaultValue);
        }
        return r;
    }

    /**
     * Returns the value of some preference out of OmegaT's preferences file, if
     * it exists.
     * <p>
     * @param key
     *            name of the key to look up, usually OConsts.PREF_...
     * @return preference value as a string
     */
    public static String getPreferenceDefaultAllowEmptyString(String key) {
        String val = getPreference(key);
        return val;
    }

    /**
     * Returns the integer value of some preference out of OmegaT's preferences
     * file, if it exists.
     * <p>
     * If the key is not found, returns the default value provided and sets the
     * preference to the default value.
     * 
     * @param key
     *            name of the key to look up, usually OConsts.PREF_...
     * @param defaultValue
     *            default value for the key
     * @return preference value as an integer
     */
    public static int getPreferenceDefault(String key, int defaultValue) {
        String val = getPreferenceDefault(key, Integer.toString(defaultValue));
        int res = defaultValue;
        try {
            res = Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
        }
        return res;
    }

    /**
     * Sets the value of some preference.
     * 
     * @param name
     *            preference key name, usually Preferences.PREF_...
     * @param value
     *            preference value as a string
     */
    public static void setPreference(String name, String value) {
        m_changed = true;
        if (name != null && name.length() != 0 && value != null) {
            if (!m_loaded)
                doLoad();
            Integer i = m_preferenceMap.get(name);
            if (i == null) {
                // defaultValue doesn't exist - add it
                i = m_valList.size();
                m_preferenceMap.put(name, i);
                m_valList.add(value);
                m_nameList.add(name);
            } else {
                // mapping exists - reset defaultValue to new
                m_valList.set(i.intValue(), value);
            }
        }
    }

    /**
     * Sets the value of some preference.
     * 
     * @param name
     *            preference key name, usually Preferences.PREF_...
     * @param value
     *            preference value as enum
     */
    public static void setPreference(String name, Enum<?> value) {
        m_changed = true;
        if (name != null && name.length() != 0 && value != null) {
            if (!m_loaded)
                doLoad();
            Integer i = m_preferenceMap.get(name);
            if (i == null) {
                // defaultValue doesn't exist - add it
                i = m_valList.size();
                m_preferenceMap.put(name, i);
                m_valList.add(value.name());
                m_nameList.add(name);
            } else {
                // mapping exists - reset defaultValue to new
                m_valList.set(i.intValue(), value.name());
            }
        }
    }

    /**
     * Sets the boolean value of some preference.
     * 
     * @param name
     *            preference key name, usually Preferences.PREF_...
     * @param boolvalue
     *            preference defaultValue as a boolean
     */
    public static void setPreference(String name, boolean boolvalue) {
        setPreference(name, String.valueOf(boolvalue));
    }

    /**
     * Sets the int value of some preference.
     * 
     * @param name
     *            preference key name, usually Preferences.PREF_...
     * @param intvalue
     *            preference value as an integer
     */
    public static void setPreference(String name, int intvalue) {
        setPreference(name, String.valueOf(intvalue));
    }

    public static SRX getSRX() {
        return srx;
    }

    public static void setSRX(SRX newSrx) {
        srx = newSrx;

        File srxFile = new File(StaticUtils.getConfigDir() + SRX.CONF_SENTSEG);
        try {
            SRX.saveTo(srx, srxFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void save() {
        try {
            if (m_changed)
                doSave();
        } catch (IOException e) {
            Log.logErrorRB("PM_ERROR_SAVE");
            Log.log(e);
        }
    }

    private static void doLoad() {
        try {
            // mark as loaded - if the load fails, there's no use
            // trying again later
            m_loaded = true;

            XMLStreamReader xml = new XMLStreamReader();
            xml.killEmptyBlocks();
            
            File prefsFile = new File(StaticUtils.getConfigDir() + FILE_PREFERENCES);
            // If user prefs don't exist, fall back to defaults (possibly) bundled with OmegaT.
            if (!prefsFile.exists()) {
                prefsFile = new File(StaticUtils.installDir(), FILE_PREFERENCES);
            }
            // If no prefs are found so far, look inside JAR for defaults. Useful for e.g. Web Start.
            if (prefsFile.exists()) {
                xml.setStream(prefsFile);
            } else {
                InputStream is = Preferences.class.getResourceAsStream(FILE_PREFERENCES);
                if (is == null) {
                    throw new FileNotFoundException("No prefs found of any kind.");
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    xml.setStream(br);
                }
            }
            
            XMLBlock blk;
            List<XMLBlock> lst;

            m_preferenceMap.clear();
            String pref;
            String val;
            // advance to omegat tag
            if (xml.advanceToTag("omegat") == null)
                return;

            // advance to project tag
            if ((blk = xml.advanceToTag("preference")) == null)
                return;

            String ver = blk.getAttribute("version");
            if (ver != null && !ver.equals("1.0")) {
                // unsupported preference file version - abort read
                return;
            }

            lst = xml.closeBlock(blk);
            if (lst == null)
                return;

            for (int i = 0; i < lst.size(); i++) {
                blk = lst.get(i);
                if (blk.isClose())
                    continue;

                if (!blk.isTag())
                    continue;

                pref = blk.getTagName();
                blk = lst.get(++i);
                if (blk.isClose()) {
                //allow empty string as a preference value
                    val = "";
                } else {
                    val = blk.getText();
                }
                if (pref != null && val != null) {
                    // valid match - record these
                    m_preferenceMap.put(pref, m_valList.size());
                    m_nameList.add(pref);
                    m_valList.add(val);
                }
            }
        } catch (TranslationException te) {
            // error loading preference file - keep whatever was
            // loaded then return gracefully to calling function
            // print an error to the console as an FYI
            Log.logWarningRB("PM_WARNING_PARSEERROR_ON_READ");
            Log.log(te);
        } catch (IndexOutOfBoundsException e3) {
            // error loading preference file - keep whatever was
            // loaded then return gracefully to calling function
            // print an error to the console as an FYI
            Log.logWarningRB("PM_WARNING_PARSEERROR_ON_READ");
            Log.log(e3);
        } catch (UnsupportedEncodingException e3) {
            // unsupported encoding - forget about it
            Log.logErrorRB("PM_UNSUPPORTED_ENCODING");
            Log.log(e3);
        } catch (FileNotFoundException ex) {
            // there is no config file yet
        } catch (IOException e4) {
            // can't read file - forget about it and move on
            Log.logErrorRB("PM_ERROR_READING_FILE");
            Log.log(e4);
        }

        File srxFile = new File(StaticUtils.getConfigDir() + SRX.CONF_SENTSEG);
        srx = SRX.loadSRX(srxFile);
        if (srx == null) {
            srx = SRX.getDefault();
        }
    }

    private static void doSave() throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                StaticUtils.getConfigDir() + FILE_PREFERENCES), "UTF-8"));

        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        out.write("<omegat>\n");
        out.write("  <preference version=\"1.0\">\n");

        for (int i = 0; i < m_nameList.size(); i++) {
            String name = m_nameList.get(i);
            String val = StaticUtils.makeValidXML(m_valList.get(i));
            out.write("    <" + name + ">");
            out.write(val);
            out.write("</" + name + ">\n");
        }
        out.write("  </preference>\n");
        out.write("</omegat>\n");
        out.close();
        m_changed = false;
    }

    private static boolean m_loaded;
    private static boolean m_changed;

    // use a hash map for fast lookup of data
    // use array lists for orderly recovery of it for saving to disk
    private static List<String> m_nameList;
    private static List<String> m_valList;
    private static Map<String, Integer> m_preferenceMap;

    private static SRX srx;
}
