
package gen.core.preference;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="preference">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="docking_layout" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="sb_progress_mode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="source_font" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="source_font_size" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="most_recent_projects_size" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="display_modification_info" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="auto_save_interval" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="scripts_dir" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="tokenizer_behavior_org.omegat.tokenizer.LuceneEnglishTokenizer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="tokenizer_behavior_org.omegat.tokenizer.LuceneRussianTokenizer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="source_lang" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="target_lang" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ext_tmx_sort_key" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ext_tmx_match_template" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="help_window_width" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="help_window_height" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="help_window_x" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="help_window_y" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="project_files_window_width" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="project_files_window_height" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="project_files_window_x" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="project_files_window_y" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="script_window_width" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="script_window_height" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="script_window_x" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="script_window_y" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="screen_x" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="screen_y" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="screen_width" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="screen_height" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                   &lt;element name="current_folder" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="most_recent_projects_0" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}float" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"preference"
})
@XmlRootElement(name = "omegat")
public class Omegat {

	@XmlElement(required = true)
	protected Omegat.Preference preference;

	/**
	 * Gets the value of the preference property.
	 *
	 * @return possible object is
	 * {@link Omegat.Preference }
	 */
	public Omegat.Preference getPreference() {
		return preference;
	}

	/**
	 * Sets the value of the preference property.
	 *
	 * @param value allowed object is
	 *              {@link Omegat.Preference }
	 */
	public void setPreference(Omegat.Preference value) {
		this.preference = value;
	}


	/**
	 * <p>Java class for anonymous complex type.
	 * <p/>
	 * <p>The following schema fragment specifies the expected content contained within this class.
	 * <p/>
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="docking_layout" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="sb_progress_mode" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="source_font" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="source_font_size" type="{http://www.w3.org/2001/XMLSchema}byte"/>
	 *         &lt;element name="most_recent_projects_size" type="{http://www.w3.org/2001/XMLSchema}byte"/>
	 *         &lt;element name="display_modification_info" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="auto_save_interval" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="scripts_dir" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="tokenizer_behavior_org.omegat.tokenizer.LuceneEnglishTokenizer" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="tokenizer_behavior_org.omegat.tokenizer.LuceneRussianTokenizer" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="source_lang" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="target_lang" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="ext_tmx_sort_key" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="ext_tmx_match_template" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="help_window_width" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="help_window_height" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="help_window_x" type="{http://www.w3.org/2001/XMLSchema}byte"/>
	 *         &lt;element name="help_window_y" type="{http://www.w3.org/2001/XMLSchema}byte"/>
	 *         &lt;element name="project_files_window_width" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="project_files_window_height" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="project_files_window_x" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="project_files_window_y" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="script_window_width" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="script_window_height" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="script_window_x" type="{http://www.w3.org/2001/XMLSchema}byte"/>
	 *         &lt;element name="script_window_y" type="{http://www.w3.org/2001/XMLSchema}byte"/>
	 *         &lt;element name="screen_x" type="{http://www.w3.org/2001/XMLSchema}byte"/>
	 *         &lt;element name="screen_y" type="{http://www.w3.org/2001/XMLSchema}byte"/>
	 *         &lt;element name="screen_width" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="screen_height" type="{http://www.w3.org/2001/XMLSchema}short"/>
	 *         &lt;element name="current_folder" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="most_recent_projects_0" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *       &lt;/sequence>
	 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}float" />
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {
			"dockingLayout",
			"sbProgressMode",
			"sourceFont",
			"sourceFontSize",
			"mostRecentProjectsSize",
			"displayModificationInfo",
			"autoSaveInterval",
			"scriptsDir",
			"tokenizerBehaviorOrgOmegatTokenizerLuceneEnglishTokenizer",
			"tokenizerBehaviorOrgOmegatTokenizerLuceneRussianTokenizer",
			"sourceLang",
			"targetLang",
			"extTmxSortKey",
			"extTmxMatchTemplate",
			"helpWindowWidth",
			"helpWindowHeight",
			"helpWindowX",
			"helpWindowY",
			"projectFilesWindowWidth",
			"projectFilesWindowHeight",
			"projectFilesWindowX",
			"projectFilesWindowY",
			"scriptWindowWidth",
			"scriptWindowHeight",
			"scriptWindowX",
			"scriptWindowY",
			"screenX",
			"screenY",
			"screenWidth",
			"screenHeight",
			"currentFolder",
			"mostRecentProjects0"
	})
	public static class Preference {

		@XmlElement(name = "docking_layout", required = true)
		protected String dockingLayout;
		@XmlElement(name = "sb_progress_mode", required = true)
		protected String sbProgressMode;
		@XmlElement(name = "source_font", required = true)
		protected String sourceFont;
		@XmlElement(name = "source_font_size")
		protected byte sourceFontSize;
		@XmlElement(name = "most_recent_projects_size")
		protected byte mostRecentProjectsSize;
		@XmlElement(name = "display_modification_info", required = true)
		protected String displayModificationInfo;
		@XmlElement(name = "auto_save_interval")
		protected short autoSaveInterval;
		@XmlElement(name = "scripts_dir", required = true)
		protected String scriptsDir;
		@XmlElement(name = "tokenizer_behavior_org.omegat.tokenizer.LuceneEnglishTokenizer", required = true)
		protected String tokenizerBehaviorOrgOmegatTokenizerLuceneEnglishTokenizer;
		@XmlElement(name = "tokenizer_behavior_org.omegat.tokenizer.LuceneRussianTokenizer", required = true)
		protected String tokenizerBehaviorOrgOmegatTokenizerLuceneRussianTokenizer;
		@XmlElement(name = "source_lang", required = true)
		protected String sourceLang;
		@XmlElement(name = "target_lang", required = true)
		protected String targetLang;
		@XmlElement(name = "ext_tmx_sort_key", required = true)
		protected String extTmxSortKey;
		@XmlElement(name = "ext_tmx_match_template", required = true)
		protected String extTmxMatchTemplate;
		@XmlElement(name = "help_window_width")
		protected short helpWindowWidth;
		@XmlElement(name = "help_window_height")
		protected short helpWindowHeight;
		@XmlElement(name = "help_window_x")
		protected byte helpWindowX;
		@XmlElement(name = "help_window_y")
		protected byte helpWindowY;
		@XmlElement(name = "project_files_window_width")
		protected short projectFilesWindowWidth;
		@XmlElement(name = "project_files_window_height")
		protected short projectFilesWindowHeight;
		@XmlElement(name = "project_files_window_x")
		protected short projectFilesWindowX;
		@XmlElement(name = "project_files_window_y")
		protected short projectFilesWindowY;
		@XmlElement(name = "script_window_width")
		protected short scriptWindowWidth;
		@XmlElement(name = "script_window_height")
		protected short scriptWindowHeight;
		@XmlElement(name = "script_window_x")
		protected byte scriptWindowX;
		@XmlElement(name = "script_window_y")
		protected byte scriptWindowY;
		@XmlElement(name = "screen_x")
		protected byte screenX;
		@XmlElement(name = "screen_y")
		protected byte screenY;
		@XmlElement(name = "screen_width")
		protected short screenWidth;
		@XmlElement(name = "screen_height")
		protected short screenHeight;
		@XmlElement(name = "current_folder", required = true)
		protected String currentFolder;
		@XmlElement(name = "most_recent_projects_0", required = true)
		protected String mostRecentProjects0;
		@XmlAttribute(name = "version")
		protected Float version;

		/**
		 * Gets the value of the dockingLayout property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getDockingLayout() {
			return dockingLayout;
		}

		/**
		 * Sets the value of the dockingLayout property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setDockingLayout(String value) {
			this.dockingLayout = value;
		}

		/**
		 * Gets the value of the sbProgressMode property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getSbProgressMode() {
			return sbProgressMode;
		}

		/**
		 * Sets the value of the sbProgressMode property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setSbProgressMode(String value) {
			this.sbProgressMode = value;
		}

		/**
		 * Gets the value of the sourceFont property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getSourceFont() {
			return sourceFont;
		}

		/**
		 * Sets the value of the sourceFont property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setSourceFont(String value) {
			this.sourceFont = value;
		}

		/**
		 * Gets the value of the sourceFontSize property.
		 */
		public byte getSourceFontSize() {
			return sourceFontSize;
		}

		/**
		 * Sets the value of the sourceFontSize property.
		 */
		public void setSourceFontSize(byte value) {
			this.sourceFontSize = value;
		}

		/**
		 * Gets the value of the mostRecentProjectsSize property.
		 */
		public byte getMostRecentProjectsSize() {
			return mostRecentProjectsSize;
		}

		/**
		 * Sets the value of the mostRecentProjectsSize property.
		 */
		public void setMostRecentProjectsSize(byte value) {
			this.mostRecentProjectsSize = value;
		}

		/**
		 * Gets the value of the displayModificationInfo property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getDisplayModificationInfo() {
			return displayModificationInfo;
		}

		/**
		 * Sets the value of the displayModificationInfo property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setDisplayModificationInfo(String value) {
			this.displayModificationInfo = value;
		}

		/**
		 * Gets the value of the autoSaveInterval property.
		 */
		public short getAutoSaveInterval() {
			return autoSaveInterval;
		}

		/**
		 * Sets the value of the autoSaveInterval property.
		 */
		public void setAutoSaveInterval(short value) {
			this.autoSaveInterval = value;
		}

		/**
		 * Gets the value of the scriptsDir property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getScriptsDir() {
			return scriptsDir;
		}

		/**
		 * Sets the value of the scriptsDir property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setScriptsDir(String value) {
			this.scriptsDir = value;
		}

		/**
		 * Gets the value of the tokenizerBehaviorOrgOmegatTokenizerLuceneEnglishTokenizer property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getTokenizerBehaviorOrgOmegatTokenizerLuceneEnglishTokenizer() {
			return tokenizerBehaviorOrgOmegatTokenizerLuceneEnglishTokenizer;
		}

		/**
		 * Sets the value of the tokenizerBehaviorOrgOmegatTokenizerLuceneEnglishTokenizer property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setTokenizerBehaviorOrgOmegatTokenizerLuceneEnglishTokenizer(String value) {
			this.tokenizerBehaviorOrgOmegatTokenizerLuceneEnglishTokenizer = value;
		}

		/**
		 * Gets the value of the tokenizerBehaviorOrgOmegatTokenizerLuceneRussianTokenizer property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getTokenizerBehaviorOrgOmegatTokenizerLuceneRussianTokenizer() {
			return tokenizerBehaviorOrgOmegatTokenizerLuceneRussianTokenizer;
		}

		/**
		 * Sets the value of the tokenizerBehaviorOrgOmegatTokenizerLuceneRussianTokenizer property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setTokenizerBehaviorOrgOmegatTokenizerLuceneRussianTokenizer(String value) {
			this.tokenizerBehaviorOrgOmegatTokenizerLuceneRussianTokenizer = value;
		}

		/**
		 * Gets the value of the sourceLang property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getSourceLang() {
			return sourceLang;
		}

		/**
		 * Sets the value of the sourceLang property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setSourceLang(String value) {
			this.sourceLang = value;
		}

		/**
		 * Gets the value of the targetLang property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getTargetLang() {
			return targetLang;
		}

		/**
		 * Sets the value of the targetLang property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setTargetLang(String value) {
			this.targetLang = value;
		}

		/**
		 * Gets the value of the extTmxSortKey property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getExtTmxSortKey() {
			return extTmxSortKey;
		}

		/**
		 * Sets the value of the extTmxSortKey property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setExtTmxSortKey(String value) {
			this.extTmxSortKey = value;
		}

		/**
		 * Gets the value of the extTmxMatchTemplate property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getExtTmxMatchTemplate() {
			return extTmxMatchTemplate;
		}

		/**
		 * Sets the value of the extTmxMatchTemplate property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setExtTmxMatchTemplate(String value) {
			this.extTmxMatchTemplate = value;
		}

		/**
		 * Gets the value of the helpWindowWidth property.
		 */
		public short getHelpWindowWidth() {
			return helpWindowWidth;
		}

		/**
		 * Sets the value of the helpWindowWidth property.
		 */
		public void setHelpWindowWidth(short value) {
			this.helpWindowWidth = value;
		}

		/**
		 * Gets the value of the helpWindowHeight property.
		 */
		public short getHelpWindowHeight() {
			return helpWindowHeight;
		}

		/**
		 * Sets the value of the helpWindowHeight property.
		 */
		public void setHelpWindowHeight(short value) {
			this.helpWindowHeight = value;
		}

		/**
		 * Gets the value of the helpWindowX property.
		 */
		public byte getHelpWindowX() {
			return helpWindowX;
		}

		/**
		 * Sets the value of the helpWindowX property.
		 */
		public void setHelpWindowX(byte value) {
			this.helpWindowX = value;
		}

		/**
		 * Gets the value of the helpWindowY property.
		 */
		public byte getHelpWindowY() {
			return helpWindowY;
		}

		/**
		 * Sets the value of the helpWindowY property.
		 */
		public void setHelpWindowY(byte value) {
			this.helpWindowY = value;
		}

		/**
		 * Gets the value of the projectFilesWindowWidth property.
		 */
		public short getProjectFilesWindowWidth() {
			return projectFilesWindowWidth;
		}

		/**
		 * Sets the value of the projectFilesWindowWidth property.
		 */
		public void setProjectFilesWindowWidth(short value) {
			this.projectFilesWindowWidth = value;
		}

		/**
		 * Gets the value of the projectFilesWindowHeight property.
		 */
		public short getProjectFilesWindowHeight() {
			return projectFilesWindowHeight;
		}

		/**
		 * Sets the value of the projectFilesWindowHeight property.
		 */
		public void setProjectFilesWindowHeight(short value) {
			this.projectFilesWindowHeight = value;
		}

		/**
		 * Gets the value of the projectFilesWindowX property.
		 */
		public short getProjectFilesWindowX() {
			return projectFilesWindowX;
		}

		/**
		 * Sets the value of the projectFilesWindowX property.
		 */
		public void setProjectFilesWindowX(short value) {
			this.projectFilesWindowX = value;
		}

		/**
		 * Gets the value of the projectFilesWindowY property.
		 */
		public short getProjectFilesWindowY() {
			return projectFilesWindowY;
		}

		/**
		 * Sets the value of the projectFilesWindowY property.
		 */
		public void setProjectFilesWindowY(short value) {
			this.projectFilesWindowY = value;
		}

		/**
		 * Gets the value of the scriptWindowWidth property.
		 */
		public short getScriptWindowWidth() {
			return scriptWindowWidth;
		}

		/**
		 * Sets the value of the scriptWindowWidth property.
		 */
		public void setScriptWindowWidth(short value) {
			this.scriptWindowWidth = value;
		}

		/**
		 * Gets the value of the scriptWindowHeight property.
		 */
		public short getScriptWindowHeight() {
			return scriptWindowHeight;
		}

		/**
		 * Sets the value of the scriptWindowHeight property.
		 */
		public void setScriptWindowHeight(short value) {
			this.scriptWindowHeight = value;
		}

		/**
		 * Gets the value of the scriptWindowX property.
		 */
		public byte getScriptWindowX() {
			return scriptWindowX;
		}

		/**
		 * Sets the value of the scriptWindowX property.
		 */
		public void setScriptWindowX(byte value) {
			this.scriptWindowX = value;
		}

		/**
		 * Gets the value of the scriptWindowY property.
		 */
		public byte getScriptWindowY() {
			return scriptWindowY;
		}

		/**
		 * Sets the value of the scriptWindowY property.
		 */
		public void setScriptWindowY(byte value) {
			this.scriptWindowY = value;
		}

		/**
		 * Gets the value of the screenX property.
		 */
		public byte getScreenX() {
			return screenX;
		}

		/**
		 * Sets the value of the screenX property.
		 */
		public void setScreenX(byte value) {
			this.screenX = value;
		}

		/**
		 * Gets the value of the screenY property.
		 */
		public byte getScreenY() {
			return screenY;
		}

		/**
		 * Sets the value of the screenY property.
		 */
		public void setScreenY(byte value) {
			this.screenY = value;
		}

		/**
		 * Gets the value of the screenWidth property.
		 */
		public short getScreenWidth() {
			return screenWidth;
		}

		/**
		 * Sets the value of the screenWidth property.
		 */
		public void setScreenWidth(short value) {
			this.screenWidth = value;
		}

		/**
		 * Gets the value of the screenHeight property.
		 */
		public short getScreenHeight() {
			return screenHeight;
		}

		/**
		 * Sets the value of the screenHeight property.
		 */
		public void setScreenHeight(short value) {
			this.screenHeight = value;
		}

		/**
		 * Gets the value of the currentFolder property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getCurrentFolder() {
			return currentFolder;
		}

		/**
		 * Sets the value of the currentFolder property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setCurrentFolder(String value) {
			this.currentFolder = value;
		}

		/**
		 * Gets the value of the mostRecentProjects0 property.
		 *
		 * @return possible object is
		 * {@link String }
		 */
		public String getMostRecentProjects0() {
			return mostRecentProjects0;
		}

		/**
		 * Sets the value of the mostRecentProjects0 property.
		 *
		 * @param value allowed object is
		 *              {@link String }
		 */
		public void setMostRecentProjects0(String value) {
			this.mostRecentProjects0 = value;
		}

		/**
		 * Gets the value of the version property.
		 *
		 * @return possible object is
		 * {@link Float }
		 */
		public Float getVersion() {
			return version;
		}

		/**
		 * Sets the value of the version property.
		 *
		 * @param value allowed object is
		 *              {@link Float }
		 */
		public void setVersion(Float value) {
			this.version = value;
		}

		@Override
		public String toString() {
			return "Preference{" +
					"\ndockingLayout='" + dockingLayout + '\'' +
					",\n sbProgressMode='" + sbProgressMode + '\'' +
					",\n sourceFont='" + sourceFont + '\'' +
					",\n sourceFontSize=" + sourceFontSize +
					",\n mostRecentProjectsSize=" + mostRecentProjectsSize +
					",\n displayModificationInfo='" + displayModificationInfo + '\'' +
					",\n autoSaveInterval=" + autoSaveInterval +
					",\n scriptsDir='" + scriptsDir + '\'' +
					",\n tokenizerBehaviorOrgOmegatTokenizerLuceneEnglishTokenizer='" + tokenizerBehaviorOrgOmegatTokenizerLuceneEnglishTokenizer + '\'' +
					",\n tokenizerBehaviorOrgOmegatTokenizerLuceneRussianTokenizer='" + tokenizerBehaviorOrgOmegatTokenizerLuceneRussianTokenizer + '\'' +
					",\n sourceLang='" + sourceLang + '\'' +
					",\n targetLang='" + targetLang + '\'' +
					",\n extTmxSortKey='" + extTmxSortKey + '\'' +
					",\n extTmxMatchTemplate='" + extTmxMatchTemplate + '\'' +
					",\n helpWindowWidth=" + helpWindowWidth +
					",\n helpWindowHeight=" + helpWindowHeight +
					",\n helpWindowX=" + helpWindowX +
					",\n helpWindowY=" + helpWindowY +
					",\n projectFilesWindowWidth=" + projectFilesWindowWidth +
					",\n projectFilesWindowHeight=" + projectFilesWindowHeight +
					",\n projectFilesWindowX=" + projectFilesWindowX +
					",\n projectFilesWindowY=" + projectFilesWindowY +
					",\n scriptWindowWidth=" + scriptWindowWidth +
					",\n scriptWindowHeight=" + scriptWindowHeight +
					",\n scriptWindowX=" + scriptWindowX +
					",\n scriptWindowY=" + scriptWindowY +
					",\n screenX=" + screenX +
					",\n screenY=" + screenY +
					",\n screenWidth=" + screenWidth +
					",\n screenHeight=" + screenHeight +
					",\n currentFolder='" + currentFolder + '\'' +
					",\n mostRecentProjects0='" + mostRecentProjects0 + '\'' +
					",\n version=" + version +
					'}';
		}
	}

}
