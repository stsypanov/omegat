/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey, Maxym Mykhalchuk, Henry Pijffers, 
                         Benjamin Siband, and Kim Bruning
               2007 Zoltan Bartko
               2008 Andrzej Sawula, Alex Buloichik
               2009 Didier Briel, Alex Buloichik
               2010 Wildrich Fourie, Didier Briel
               2011 Didier Briel
               2012 Wildrich Fourie, Guido Leenders, Martin Fleurke, Didier Briel
               2013 Zoltan Bartko, Didier Briel, Yu Tang
               2014 Aaron Madlon-Kay
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

package org.omegat.gui.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.events.IApplicationEventListener;
import org.omegat.core.events.IProjectEventListener;
import org.omegat.gui.editor.EditorSettings;
import org.omegat.util.Log;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.StaticUtils;
import org.omegat.util.gui.OSXIntegration;
import org.omegat.util.gui.Styles;
import org.openide.awt.Mnemonics;

/**
 * Class for create main menu and handle main menu events.
 * 
 * @author Keith Godfrey
 * @author Benjamin Siband
 * @author Maxym Mykhalchuk
 * @author Kim Bruning
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 * @author Zoltan Bartko - bartkozoltan@bartkozoltan.com
 * @author Andrzej Sawula
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Didier Briel
 * @author Wildrich Fourie
 * @author Martin Fleurke
 * @author Yu Tang
 * @author Aaron Madlon-Kay
 */
 
/**
 * Add newly created MenuItem items to
 * /src/org/omegat/gui/main/MainMenuShortcuts.properties and
 * /src/org/omegat/gui/main/MainMenuShortcuts.mac.properties
 * with the proper shortcuts if set.
 */
  
public class MainWindowMenu implements ActionListener, MenuListener, IMainMenu {
    private static final Logger LOGGER = Logger.getLogger(MainWindowMenu.class.getName());

    /** MainWindow instance. */
    protected final MainWindow mainWindow;

    /** MainWindow menu handler instance. */
    protected final MainWindowMenuHandler mainWindowMenuHandler;

    /**
     * Size of icons (both height and width) of menu entries.
     */
    private static final int ICON_SIZE=12;

    public MainWindowMenu(final MainWindow mainWindow, final MainWindowMenuHandler mainWindowMenuHandler) {
        this.mainWindow = mainWindow;
        this.mainWindowMenuHandler = mainWindowMenuHandler;
    }
    /**
     * Creates an icon to show color of background marking
     * @param color background color
     * @return
     */
    private Icon getViewMenuMarkBGIcon(final Color color) {
        Icon i = new Icon() {
            public void paintIcon(java.awt.Component cmpnt, java.awt.Graphics grphcs, int x, int y) {
                if (color!=null) {
                    grphcs.setColor(color);
                    grphcs.fillRect(x,y,ICON_SIZE,ICON_SIZE);
                }
            }
            public int getIconWidth() {
                return ICON_SIZE;
            }
            public int getIconHeight() {
                return ICON_SIZE;
            }
        };
        return i;
    }

    /**
     * Creates icon to show font marking
     * @param color color of font
     * @return 
     */
    private Icon getViewMenuMarkTextIcon(final Color color) {
        return new Icon() {
            public void paintIcon(java.awt.Component cmpnt, java.awt.Graphics grphcs, int x, int y) {
                if (color!=null && grphcs != null) { //Mac fix: test on grphcs != null needed. Weird...
                    grphcs.setColor(color);
                    char[] data = {'M'};
                    grphcs.drawChars(data, 0, 1, x, y+ICON_SIZE);
                }
            }
            public int getIconWidth() {
                return ICON_SIZE;
            }
            public int getIconHeight() {
                return ICON_SIZE;
            }
        };
    }

    /**
     * Code for dispatching events from components to event handlers.
     * 
     * @param evt
     *            event info
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        // Item what perform event.
        JMenuItem menuItem = (JMenuItem) evt.getSource();

        // Get item name from actionCommand.
        String action = menuItem.getActionCommand();

        Log.logInfoRB("LOG_MENU_CLICK", action);

        // Find method by item name.
        String methodName = action + "ActionPerformed";
        Method method = null;
        try {
            method = mainWindowMenuHandler.getClass().getMethod(methodName);
        } catch (NoSuchMethodException ex) {
            throw new IncompatibleClassChangeError(
                    "Error invoke method handler for main menu: there is no method " + methodName);
        }

        // Call ...MenuItemActionPerformed method.
        try {
            method.invoke(mainWindowMenuHandler);
        } catch (IllegalAccessException ex) {
            throw new IncompatibleClassChangeError("Error invoke method handler for main menu");
        } catch (InvocationTargetException ex) {
            LOGGER.log(Level.SEVERE, "Error execute method", ex);
            throw new IncompatibleClassChangeError("Error invoke method handler for main menu");
        }
    }

    /**
     * Code for dispatching events from components to event handlers.
     * 
     * @param evt
     *            event info
     */
    @Override
    public void menuSelected(MenuEvent evt) {
        // Item what perform event.
        JMenu menu = (JMenu) evt.getSource();

        // Get item name from actionCommand.
        String action = menu.getActionCommand();

        Log.logInfoRB("LOG_MENU_CLICK", action);

        // Find method by item name.
        String methodName = action + "MenuSelected";
        Method method = null;
        try {
            method = mainWindowMenuHandler.getClass().getMethod(methodName, JMenu.class);
        } catch (NoSuchMethodException ex) {
            // method not declared
            return;
        }

        // Call ...MenuMenuSelected method.
        try {
            method.invoke(mainWindowMenuHandler, menu);
        } catch (IllegalAccessException ex) {
            throw new IncompatibleClassChangeError("Error invoke method handler for main menu");
        } catch (InvocationTargetException ex) {
            LOGGER.log(Level.SEVERE, "Error execute method", ex);
            throw new IncompatibleClassChangeError("Error invoke method handler for main menu");
        }
    }

    public void menuCanceled(MenuEvent e) {
    }

    public void menuDeselected(MenuEvent e) {
    }

    /**
     * Initialize menu items.
     */
    JMenuBar initComponents() {
        mainMenu = new JMenuBar();
        mainMenu.add(projectMenu = createMenu("TF_MENU_FILE"));
        mainMenu.add(editMenu = createMenu("TF_MENU_EDIT"));
        mainMenu.add(gotoMenu = createMenu("MW_GOTOMENU"));
        mainMenu.add(viewMenu = createMenu("MW_VIEW_MENU"));
        mainMenu.add(toolsMenu = createMenu("TF_MENU_TOOLS"));
        mainMenu.add(optionsMenu = createMenu("MW_OPTIONSMENU"));
        mainMenu.add(helpMenu = createMenu("TF_MENU_HELP"));

        projectMenu.add(projectNewMenuItem = createMenuItem("TF_MENU_FILE_CREATE"));
        projectMenu.add(projectTeamNewMenuItem = createMenuItem("TF_MENU_FILE_TEAM_CREATE"));
        projectMenu.add(projectOpenMenuItem = createMenuItem("TF_MENU_FILE_OPEN"));
        projectMenu.add(projectOpenRecentMenuItem = createMenu("TF_MENU_FILE_OPEN_RECENT"));

        projectMenu.add(projectImportMenuItem = createMenuItem("TF_MENU_FILE_IMPORT"));
        projectMenu.add(projectWikiImportMenuItem = createMenuItem("TF_MENU_WIKI_IMPORT"));
        projectMenu.add(projectReloadMenuItem = createMenuItem("TF_MENU_PROJECT_RELOAD"));
        projectMenu.add(projectCloseMenuItem = createMenuItem("TF_MENU_FILE_CLOSE"));
        projectMenu.add(new JSeparator());
        projectMenu.add(projectSaveMenuItem = createMenuItem("TF_MENU_FILE_SAVE"));
        projectMenu.add(new JSeparator());
        projectMenu.add(projectCompileMenuItem = createMenuItem("TF_MENU_FILE_COMPILE"));
        projectMenu.add(projectSingleCompileMenuItem = createMenuItem("TF_MENU_FILE_SINGLE_COMPILE"));
        projectMenu.add(new JSeparator());
        projectMenu.add(projectEditMenuItem = createMenuItem("MW_PROJECTMENU_EDIT"));
        projectMenu.add(viewFileListMenuItem = createMenuItem("TF_MENU_FILE_PROJWIN"));
        projectExitMenuItem = createMenuItem("TF_MENU_FILE_QUIT");

        // all except MacOSX
        if (!StaticUtils.onMacOSX()) {
            projectMenu.add(new JSeparator());
            projectMenu.add(projectExitMenuItem);
        }

        editMenu.add(editUndoMenuItem = createMenuItem("TF_MENU_EDIT_UNDO"));
        editMenu.add(editRedoMenuItem = createMenuItem("TF_MENU_EDIT_REDO"));
        editMenu.add(new JSeparator());
        editMenu.add(editOverwriteTranslationMenuItem = createMenuItem("TF_MENU_EDIT_RECYCLE"));
        editMenu.add(editInsertTranslationMenuItem = createMenuItem("TF_MENU_EDIT_INSERT"));
        editMenu.add(new JSeparator());
        editMenu.add(editOverwriteMachineTranslationMenuItem = createMenuItem("TF_MENU_EDIT_OVERWRITE_MACHITE_TRANSLATION"));
        editMenu.add(new JSeparator());
        editMenu.add(editOverwriteSourceMenuItem = createMenuItem("TF_MENU_EDIT_SOURCE_OVERWRITE"));
        editMenu.add(editInsertSourceMenuItem = createMenuItem("TF_MENU_EDIT_SOURCE_INSERT"));
        editMenu.add(new JSeparator());
        editMenu.add(editTagPainterMenuItem = createMenuItem("TF_MENU_EDIT_TAGPAINT"));
        editMenu.add(editTagNextMissedMenuItem = createMenuItem("TF_MENU_EDIT_TAG_NEXT_MISSED"));
        editMenu.add(new JSeparator());
        editMenu.add(editExportSelectionMenuItem = createMenuItem("TF_MENU_EDIT_EXPORT_SELECTION"));
        editMenu.add(editCreateGlossaryEntryMenuItem = createMenuItem("TF_MENU_EDIT_CREATE_GLOSSARY_ENTRY"));
        editMenu.add(new JSeparator());
        editMenu.add(editFindInProjectMenuItem = createMenuItem("TF_MENU_EDIT_FIND"));
        editMenu.add(editReplaceInProjectMenuItem = createMenuItem("TF_MENU_EDIT_REPLACE"));
        editMenu.add(new JSeparator());
        editMenu.add(switchCaseSubMenu = createMenu("TF_EDIT_MENU_SWITCH_CASE"));
        editMenu.add(selectFuzzySubMenu = createMenu("TF_MENU_EDIT_COMPARE"));
        selectFuzzySubMenu.add(editSelectFuzzyPrevMenuItem = createMenuItem("TF_MENU_EDIT_COMPARE_PREV"));
        selectFuzzySubMenu.add(editSelectFuzzyNextMenuItem = createMenuItem("TF_MENU_EDIT_COMPARE_NEXT"));
        selectFuzzySubMenu.add(new JSeparator());
        selectFuzzySubMenu.add(editSelectFuzzy1MenuItem = createMenuItem("TF_MENU_EDIT_COMPARE_1"));
        selectFuzzySubMenu.add(editSelectFuzzy2MenuItem = createMenuItem("TF_MENU_EDIT_COMPARE_2"));
        selectFuzzySubMenu.add(editSelectFuzzy3MenuItem = createMenuItem("TF_MENU_EDIT_COMPARE_3"));
        selectFuzzySubMenu.add(editSelectFuzzy4MenuItem = createMenuItem("TF_MENU_EDIT_COMPARE_4"));
        selectFuzzySubMenu.add(editSelectFuzzy5MenuItem = createMenuItem("TF_MENU_EDIT_COMPARE_5"));
        editMenu.add(new JSeparator());
        editMenu.add(editMultipleDefault = createMenuItem("MULT_MENU_DEFAULT"));
        editMenu.add(editMultipleAlternate = createMenuItem("MULT_MENU_MULTIPLE"));
        editMenu.add(new JSeparator());
        editMenu.add(editRegisterUntranslatedMenuItem = createMenuItem("TF_MENU_EDIT_UNTRANSLATED_TRANSLATION"));
        editMenu.add(editRegisterEmptyMenuItem = createMenuItem("TF_MENU_EDIT_EMPTY_TRANSLATION"));
        editMenu.add(editRegisterIdenticalMenuItem = createMenuItem("TF_MENU_EDIT_IDENTICAL_TRANSLATION"));

        switchCaseSubMenu.add(lowerCaseMenuItem = createMenuItem("TF_EDIT_MENU_SWITCH_CASE_TO_LOWER"));
        switchCaseSubMenu.add(upperCaseMenuItem = createMenuItem("TF_EDIT_MENU_SWITCH_CASE_TO_UPPER"));
        switchCaseSubMenu.add(titleCaseMenuItem = createMenuItem("TF_EDIT_MENU_SWITCH_CASE_TO_TITLE"));
        switchCaseSubMenu.add(new JSeparator());
        switchCaseSubMenu.add(cycleSwitchCaseMenuItem = createMenuItem("TF_EDIT_MENU_SWITCH_CASE_CYCLE"));

        gotoMenu.add(gotoNextUntranslatedMenuItem = createMenuItem("TF_MENU_EDIT_UNTRANS"));
        gotoMenu.add(gotoNextTranslatedMenuItem = createMenuItem("TF_MENU_EDIT_TRANS"));
        gotoMenu.add(gotoNextSegmentMenuItem = createMenuItem("TF_MENU_EDIT_NEXT"));
        gotoMenu.add(gotoPreviousSegmentMenuItem = createMenuItem("TF_MENU_EDIT_PREV"));
        gotoMenu.add(gotoSegmentMenuItem = createMenuItem("TF_MENU_EDIT_GOTO"));
        gotoMenu.add(gotoNextNoteMenuItem = createMenuItem("TF_MENU_EDIT_NEXT_NOTE"));
        gotoMenu.add(gotoPreviousNoteMenuItem = createMenuItem("TF_MENU_EDIT_PREV_NOTE"));
        gotoMenu.add(gotoNextUniqueMenuItem = createMenuItem("TF_MENU_GOTO_NEXT_UNIQUE"));
        gotoMenu.add(gotoMatchSourceSegment = createMenuItem("TF_MENU_GOTO_SELECTED_MATCH_SOURCE"));
        gotoMenu.add(new JSeparator());
        gotoMenu.add(gotoHistoryForwardMenuItem = createMenuItem("TF_MENU_GOTO_FORWARD_IN_HISTORY"));
        gotoMenu.add(gotoHistoryBackMenuItem = createMenuItem("TF_MENU_GOTO_BACK_IN_HISTORY"));

        viewMenu.add(viewMarkTranslatedSegmentsCheckBoxMenuItem = createCheckboxMenuItem("TF_MENU_DISPLAY_MARK_TRANSLATED"));
        viewMenu.add(viewMarkUntranslatedSegmentsCheckBoxMenuItem = createCheckboxMenuItem("TF_MENU_DISPLAY_MARK_UNTRANSLATED"));
        viewMenu.add(viewDisplaySegmentSourceCheckBoxMenuItem = createCheckboxMenuItem("MW_VIEW_MENU_DISPLAY_SEGMENT_SOURCES"));
        viewMenu.add(viewMarkNonUniqueSegmentsCheckBoxMenuItem = createCheckboxMenuItem("MW_VIEW_MENU_MARK_NON_UNIQUE_SEGMENTS"));
        viewMenu.add(viewMarkNotedSegmentsCheckBoxMenuItem = createCheckboxMenuItem("MW_VIEW_MENU_MARK_NOTED_SEGMENTS"));
        viewMenu.add(viewMarkNBSPCheckBoxMenuItem = createCheckboxMenuItem("MW_VIEW_MENU_MARK_NBSP"));
        viewMenu.add(viewMarkWhitespaceCheckBoxMenuItem = createCheckboxMenuItem("MW_VIEW_MENU_MARK_WHITESPACE"));
        viewMenu.add(viewMarkBidiCheckBoxMenuItem = createCheckboxMenuItem("MW_VIEW_MENU_MARK_BIDI"));
        viewMenu.add(viewMarkAutoPopulatedCheckBoxMenuItem = createCheckboxMenuItem("MW_VIEW_MENU_MARK_AUTOPOPULATED"));
        viewMenu.add(viewModificationInfoMenu = createMenu("MW_VIEW_MENU_MODIFICATION_INFO"));
        ButtonGroup viewModificationInfoMenuBG = new ButtonGroup();
        viewModificationInfoMenu
                .add(viewDisplayModificationInfoNoneRadioButtonMenuItem = createRadioButtonMenuItem(
                        "MW_VIEW_MENU_MODIFICATION_INFO_NONE", viewModificationInfoMenuBG));
        viewModificationInfoMenu
                .add(viewDisplayModificationInfoSelectedRadioButtonMenuItem = createRadioButtonMenuItem(
                        "MW_VIEW_MENU_MODIFICATION_INFO_SELECTED", viewModificationInfoMenuBG));
        viewModificationInfoMenu
                .add(viewDisplayModificationInfoAllRadioButtonMenuItem = createRadioButtonMenuItem(
                        "MW_VIEW_MENU_MODIFICATION_INFO_ALL", viewModificationInfoMenuBG));
        
        viewMarkTranslatedSegmentsCheckBoxMenuItem.setIcon(getViewMenuMarkBGIcon(Styles.EditorColor.COLOR_TRANSLATED.getColor()));
        viewMarkUntranslatedSegmentsCheckBoxMenuItem.setIcon(getViewMenuMarkBGIcon(Styles.EditorColor.COLOR_UNTRANSLATED.getColor()));
        viewDisplaySegmentSourceCheckBoxMenuItem.setIcon(getViewMenuMarkBGIcon(Styles.EditorColor.COLOR_SOURCE.getColor()));
        viewMarkNonUniqueSegmentsCheckBoxMenuItem.setIcon(getViewMenuMarkTextIcon(Styles.EditorColor.COLOR_NON_UNIQUE.getColor()));
        viewMarkNotedSegmentsCheckBoxMenuItem.setIcon(getViewMenuMarkBGIcon(Styles.EditorColor.COLOR_NOTED.getColor()));
        viewMarkNBSPCheckBoxMenuItem.setIcon(getViewMenuMarkBGIcon(Styles.EditorColor.COLOR_NBSP.getColor()));
        viewMarkWhitespaceCheckBoxMenuItem.setIcon(getViewMenuMarkBGIcon(Styles.EditorColor.COLOR_WHITESPACE.getColor()));
        viewMarkBidiCheckBoxMenuItem.setIcon(getViewMenuMarkBGIcon(Styles.EditorColor.COLOR_BIDIMARKERS.getColor()));
        viewModificationInfoMenu.setIcon(getViewMenuMarkBGIcon(null));
        viewMarkAutoPopulatedCheckBoxMenuItem
                .setIcon(getViewMenuMarkBGIcon(Styles.EditorColor.COLOR_MARK_COMES_FROM_TM_XAUTO.getColor()));

        toolsMenu.add(toolsValidateTagsMenuItem = createMenuItem("TF_MENU_TOOLS_VALIDATE"));
        toolsMenu.add(toolsSingleValidateTagsMenuItem = createMenuItem("TF_MENU_TOOLS_SINGLE_VALIDATE"));
        toolsMenu
                .add(toolsShowStatisticsStandardMenuItem = createMenuItem("TF_MENU_TOOLS_STATISTICS_STANDARD"));
        toolsMenu
                .add(toolsShowStatisticsMatchesMenuItem = createMenuItem("TF_MENU_TOOLS_STATISTICS_MATCHES"));
        toolsMenu
                .add(toolsShowStatisticsMatchesPerFileMenuItem = createMenuItem("TF_MENU_TOOLS_STATISTICS_MATCHES_PER_FILE"));

        optionsMenu
                .add(optionsTabAdvanceCheckBoxMenuItem = createCheckboxMenuItem("TF_MENU_DISPLAY_ADVANCE"));
        optionsMenu
                .add(optionsAlwaysConfirmQuitCheckBoxMenuItem = createCheckboxMenuItem("MW_OPTIONSMENU_ALWAYS_CONFIRM_QUIT"));
        optionsMenu.add(optionsMachineTranslateMenu = createMenu("TF_OPTIONSMENU_MACHINETRANSLATE"));
        optionsMenu.add(optionsGlossaryMenu = createMenu("TF_OPTIONSMENU_GLOSSARY"));

        optionsGlossaryMenu
                .add(optionsGlossaryTBXDisplayContextCheckBoxMenuItem = createCheckboxMenuItem("TF_OPTIONSMENU_GLOSSARY_TBX_DISPLAY_CONTEXT"));

        optionsMenu.add(optionsTransTipsMenu = createMenu("TF_OPTIONSMENU_TRANSTIPS"));
        optionsTransTipsMenu
                .add(optionsTransTipsEnableMenuItem = createCheckboxMenuItem("TF_OPTIONSMENU_TRANSTIPS_ENABLE"));
        optionsTransTipsMenu
                .add(optionsTransTipsExactMatchMenuItem = createCheckboxMenuItem("TF_OPTIONSMENU_TRANSTIPS_EXACTMATCH"));

        optionsMenu.add(optionsAutoCompleteMenu = createMenu("MW_OPTIONSMENU_AUTOCOMPLETE"));
        // add any autocomplete view configuration menu items below
        optionsAutoCompleteMenu.add(optionsAutoCompleteGlossaryMenuItem = createMenuItem("MW_OPTIONSMENU_AUTOCOMPLETE_GLOSSARY"));
        optionsAutoCompleteMenu.add(optionsAutoCompleteAutoTextMenuItem = createMenuItem("MW_OPTIONSMENU_AUTOCOMPLETE_AUTOTEXT"));
        optionsAutoCompleteMenu.add(optionsAutoCompleteCharTableMenuItem = createMenuItem("MW_OPTIONSMENU_AUTOCOMPLETE_CHARTABLE"));

        optionsMenu.add(new JSeparator());
        optionsMenu.add(optionsFontSelectionMenuItem = createMenuItem("TF_MENU_DISPLAY_FONT"));
        optionsMenu.add(optionsColorsSelectionMenuItem = createMenuItem("TF_MENU_COLORS"));
        optionsMenu.add(optionsSetupFileFiltersMenuItem = createMenuItem("TF_MENU_DISPLAY_FILTERS"));
        optionsMenu.add(optionsSentsegMenuItem = createMenuItem("MW_OPTIONSMENU_SENTSEG"));
        optionsMenu.add(optionsSpellCheckMenuItem = createMenuItem("MW_OPTIONSMENU_SPELLCHECK"));
        optionsMenu.add(optionsWorkflowMenuItem = createMenuItem("MW_OPTIONSMENU_WORKFLOW"));
        optionsMenu.add(optionsTagValidationMenuItem = createMenuItem("MW_OPTIONSMENU_TAGVALIDATION"));
        optionsMenu.add(optionsTeamMenuItem = createMenuItem("MW_OPTIONSMENU_TEAM"));
        optionsMenu.add(optionsExtTMXMenuItem = createMenuItem("MW_OPTIONSMENU_EXT_TMX"));
        optionsMenu.add(optionsViewOptionsMenuItem = createMenuItem("MW_OPTIONSMENU_VIEW"));
        optionsMenu.add(optionsSaveOptionsMenuItem = createMenuItem("MW_OPTIONSMENU_SAVE"));
        optionsMenu.add(optionsViewOptionsMenuLoginItem = createMenuItem("MW_OPTIONSMENU_LOGIN"));
        optionsMenu.add(optionsRestoreGUIMenuItem = createMenuItem("MW_OPTIONSMENU_RESTORE_GUI"));

        optionsMenu.add(new JSeparator());

        helpMenu.add(helpContentsMenuItem = createMenuItem("TF_MENU_HELP_CONTENTS"));
        helpMenu.add(helpAboutMenuItem = createMenuItem("TF_MENU_HELP_ABOUT"));
        helpMenu.add(helpLastChangesMenuItem = createMenuItem("TF_MENU_HELP_LAST_CHANGES"));
        helpMenu.add(helpLogMenuItem = createMenuItem("TF_MENU_HELP_LOG"));
        
        setActionCommands();
        MainWindowMenuShortcuts.setShortcuts(mainMenu);

        if (StaticUtils.onMacOSX()) {
            initMacSpecific();
        }

        CoreEvents.registerApplicationEventListener(new IApplicationEventListener() {
            public void onApplicationStartup() {
                updateCheckboxesOnStart();
                onProjectStatusChanged(false);
            }

            public void onApplicationShutdown() {
            }
        });

        CoreEvents.registerProjectChangeListener(new IProjectEventListener() {
            public void onProjectChanged(PROJECT_CHANGE_TYPE eventType) {
                if (Core.getProject().isProjectLoaded()) {
                    onProjectStatusChanged(true);
                } else {
                    onProjectStatusChanged(false);
                }
            }
        });

        return mainMenu;
    }

    /** Updates menu checkboxes from preferences on start */
    private void updateCheckboxesOnStart() {
        optionsTabAdvanceCheckBoxMenuItem.setSelected(Core.getEditor().getSettings().isUseTabForAdvance());
        optionsAlwaysConfirmQuitCheckBoxMenuItem.setSelected(Preferences
                .isPreference(Preferences.ALWAYS_CONFIRM_QUIT));
        optionsTransTipsEnableMenuItem.setSelected(Preferences.isPreference(Preferences.TRANSTIPS));
        optionsTransTipsExactMatchMenuItem.setSelected(Preferences
                .isPreference(Preferences.TRANSTIPS_EXACT_SEARCH));

        viewMarkTranslatedSegmentsCheckBoxMenuItem.setSelected(Core.getEditor().getSettings()
                .isMarkTranslated());
        viewMarkUntranslatedSegmentsCheckBoxMenuItem.setSelected(Core.getEditor().getSettings()
                .isMarkUntranslated());

        viewDisplaySegmentSourceCheckBoxMenuItem.setSelected(Core.getEditor().getSettings()
                .isDisplaySegmentSources());
        viewMarkNonUniqueSegmentsCheckBoxMenuItem.setSelected(Core.getEditor().getSettings()
                .isMarkNonUniqueSegments());
        viewMarkNotedSegmentsCheckBoxMenuItem.setSelected(Core.getEditor().getSettings()
                .isMarkNotedSegments());
        viewMarkNBSPCheckBoxMenuItem.setSelected(Core.getEditor().getSettings()
                .isMarkNBSP());
        viewMarkWhitespaceCheckBoxMenuItem.setSelected(Core.getEditor().getSettings()
                .isMarkWhitespace());
        viewMarkBidiCheckBoxMenuItem.setSelected(Core.getEditor().getSettings()
                .isMarkBidi());
        viewMarkAutoPopulatedCheckBoxMenuItem.setSelected(Core.getEditor().getSettings()
                .isMarkAutoPopulated());

        viewDisplayModificationInfoNoneRadioButtonMenuItem
                .setSelected(EditorSettings.DISPLAY_MODIFICATION_INFO_NONE.equals(Core.getEditor()
                        .getSettings().getDisplayModificationInfo()));
        viewDisplayModificationInfoSelectedRadioButtonMenuItem
                .setSelected(EditorSettings.DISPLAY_MODIFICATION_INFO_SELECTED.equals(Core.getEditor()
                        .getSettings().getDisplayModificationInfo()));
        viewDisplayModificationInfoAllRadioButtonMenuItem
                .setSelected(EditorSettings.DISPLAY_MODIFICATION_INFO_ALL.equals(Core.getEditor()
                        .getSettings().getDisplayModificationInfo()));

        optionsGlossaryTBXDisplayContextCheckBoxMenuItem.setSelected(Preferences.isPreferenceDefault(
                Preferences.GLOSSARY_TBX_DISPLAY_CONTEXT, true));
    }

    /**
     * Initialize Mac-specific features.
     */
    private void initMacSpecific() {
        try {
            // MacOSX-specific
            OSXIntegration.setQuitHandler(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainWindowMenuHandler.projectExitMenuItemActionPerformed();
                }
            });
            OSXIntegration.setAboutHandler(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainWindowMenuHandler.helpAboutMenuItemActionPerformed();
                }
            });
        } catch (NoClassDefFoundError e) {
            Log.log(e);
        }
    }

    /**
     * Create menu instance and set title.
     * 
     * @param titleKey
     *            title name key in resource bundle
     * @return menu instance
     */
    private JMenu createMenu(final String titleKey) {
        JMenu result = new JMenu();
        Mnemonics.setLocalizedText(result, OStrings.getString(titleKey));
        result.addMenuListener(this);
        return result;
    }

    /**
     * Create menu item instance and set title.
     * 
     * @param titleKey
     *            title name key in resource bundle
     * @return menu item instance
     */
    private JMenuItem createMenuItem(final String titleKey) {
        JMenuItem result = new JMenuItem();
        Mnemonics.setLocalizedText(result, OStrings.getString(titleKey));
        result.addActionListener(this);
        return result;
    }

    /**
     * Create menu item instance and set title.
     * 
     * @param titleKey
     *            title name key in resource bundle
     * @return menu item instance
     */
    private JCheckBoxMenuItem createCheckboxMenuItem(final String titleKey) {
        JCheckBoxMenuItem result = new JCheckBoxMenuItem();
        Mnemonics.setLocalizedText(result, OStrings.getString(titleKey));
        result.addActionListener(this);
        return result;
    }

    /**
     * Create menu item instance and set title.
     * 
     * @param titleKey
     *            title name key in resource bundle
     * @return menu item instance
     */
    private JRadioButtonMenuItem createRadioButtonMenuItem(final String titleKey, ButtonGroup buttonGroup) {
        JRadioButtonMenuItem result = new JRadioButtonMenuItem();
        Mnemonics.setLocalizedText(result, OStrings.getString(titleKey));
        result.addActionListener(this);
        buttonGroup.add(result);
        return result;
    }

    /**
     * Set 'actionCommand' for all menu items. TODO: change to key from resource
     * bundle values
     */
    protected void setActionCommands() {
        try {
            for (Field f : this.getClass().getDeclaredFields()) {
                if (JMenuItem.class.isAssignableFrom(f.getType())) {
                    JMenuItem menuItem = (JMenuItem) f.get(this);
                    menuItem.setActionCommand(f.getName());
                }
            }
        } catch (IllegalAccessException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Enable or disable items depend of project open or close.
     * 
     * @param isProjectOpened
     *            project open status: true if opened, false if closed
     */
    private void onProjectStatusChanged(final boolean isProjectOpened) {
        JMenuItem[] itemsToSwitchOff = new JMenuItem[] { projectNewMenuItem, projectTeamNewMenuItem, projectOpenMenuItem };

        JMenuItem[] itemsToSwitchOn = new JMenuItem[] { projectImportMenuItem, projectWikiImportMenuItem,
                projectReloadMenuItem, projectCloseMenuItem, projectSaveMenuItem, projectEditMenuItem,
                projectCompileMenuItem, projectSingleCompileMenuItem,

                editMenu, editFindInProjectMenuItem, editReplaceInProjectMenuItem, editInsertSourceMenuItem,
                editInsertTranslationMenuItem, editTagPainterMenuItem, editOverwriteSourceMenuItem,
                editOverwriteTranslationMenuItem, editRedoMenuItem, editSelectFuzzy1MenuItem,
                editSelectFuzzy2MenuItem, editSelectFuzzy3MenuItem, editSelectFuzzy4MenuItem,
                editSelectFuzzy5MenuItem, editUndoMenuItem, switchCaseSubMenu,
                editOverwriteMachineTranslationMenuItem, editMultipleDefault, editMultipleAlternate,
                editRegisterUntranslatedMenuItem, editRegisterEmptyMenuItem, editRegisterIdenticalMenuItem,

                gotoMenu, gotoNextSegmentMenuItem, gotoNextUntranslatedMenuItem, gotoPreviousSegmentMenuItem,
                gotoSegmentMenuItem, gotoNextNoteMenuItem, gotoPreviousNoteMenuItem, gotoMatchSourceSegment,

                viewFileListMenuItem, toolsValidateTagsMenuItem, toolsSingleValidateTagsMenuItem,
                toolsShowStatisticsStandardMenuItem, toolsShowStatisticsMatchesMenuItem,
                toolsShowStatisticsMatchesPerFileMenuItem };

        for (JMenuItem item : itemsToSwitchOff) {
            item.setEnabled(!isProjectOpened);
        }
        for (JMenuItem item : itemsToSwitchOn) {
            item.setEnabled(isProjectOpened);
        }
        if (Core.getParams().containsKey("no-team")) {
        	projectTeamNewMenuItem.setEnabled(false);
        }
    }

    public JMenu getMachineTranslationMenu() {
        return optionsMachineTranslateMenu;
    }

    public JMenu getOptionsMenu() {
        return optionsMenu;
    }

    public JMenu getToolsMenu() {
        return toolsMenu;
    }

    public JMenu getGlossaryMenu() {
        return optionsGlossaryMenu;
    }

    public JMenu getProjectMenu() {
        return projectMenu;
    }
    
    public JMenuItem getProjectRecentMenuItem() {
        return projectOpenRecentMenuItem;
    }

    JMenuItem cycleSwitchCaseMenuItem;
    JMenuItem editFindInProjectMenuItem;
    JMenuItem editReplaceInProjectMenuItem;
    JMenuItem editInsertSourceMenuItem;
    JMenuItem editInsertTranslationMenuItem;
    JMenu editMenu;
    JMenuItem editOverwriteSourceMenuItem;
    JMenuItem editOverwriteTranslationMenuItem;
    JMenuItem editOverwriteMachineTranslationMenuItem;
    JMenuItem editRedoMenuItem;
    JMenu selectFuzzySubMenu;
    JMenuItem editSelectFuzzyPrevMenuItem;
    JMenuItem editSelectFuzzyNextMenuItem;
    JMenuItem editSelectFuzzy1MenuItem;
    JMenuItem editSelectFuzzy2MenuItem;
    JMenuItem editSelectFuzzy3MenuItem;
    JMenuItem editSelectFuzzy4MenuItem;
    JMenuItem editSelectFuzzy5MenuItem;
    public JMenuItem editMultipleDefault;
    public JMenuItem editMultipleAlternate;
    JMenuItem editUndoMenuItem;
    JMenuItem editTagPainterMenuItem;
    JMenuItem editTagNextMissedMenuItem;
    JMenuItem editExportSelectionMenuItem;
    JMenuItem editCreateGlossaryEntryMenuItem;
    JMenuItem editRegisterUntranslatedMenuItem;
    JMenuItem editRegisterEmptyMenuItem;
    JMenuItem editRegisterIdenticalMenuItem;
    public JMenuItem gotoHistoryBackMenuItem;
    public JMenuItem gotoHistoryForwardMenuItem;
    JMenu gotoMenu;
    JMenuItem gotoNextSegmentMenuItem;
    JMenuItem gotoNextUntranslatedMenuItem;
    JMenuItem gotoNextTranslatedMenuItem;
    JMenuItem gotoPreviousSegmentMenuItem;
    JMenuItem gotoSegmentMenuItem;
    JMenuItem gotoNextNoteMenuItem;
    JMenuItem gotoPreviousNoteMenuItem;
    JMenuItem gotoMatchSourceSegment;
    JMenuItem gotoNextUniqueMenuItem;
    JMenuItem helpAboutMenuItem;
    JMenuItem helpContentsMenuItem;
    JMenuItem helpLastChangesMenuItem;
    JMenuItem helpLogMenuItem;
    JMenu helpMenu;
    JMenuItem lowerCaseMenuItem;
    JMenuBar mainMenu;
    JCheckBoxMenuItem optionsAlwaysConfirmQuitCheckBoxMenuItem;
    JMenuItem optionsFontSelectionMenuItem;
    JMenuItem optionsColorsSelectionMenuItem;
    JMenu optionsMenu;
    JMenuItem optionsRestoreGUIMenuItem;
    JMenuItem optionsSentsegMenuItem;
    JMenuItem optionsSetupFileFiltersMenuItem;
    JMenuItem optionsSpellCheckMenuItem;
    JCheckBoxMenuItem optionsTabAdvanceCheckBoxMenuItem;
    JMenu optionsMachineTranslateMenu;
    JMenu optionsGlossaryMenu;
    JMenuItem optionsGlossaryTBXDisplayContextCheckBoxMenuItem;
    JMenu optionsTransTipsMenu;
    JCheckBoxMenuItem optionsTransTipsEnableMenuItem;
    JCheckBoxMenuItem optionsTransTipsExactMatchMenuItem;
    JMenu optionsAutoCompleteMenu;
    JMenuItem optionsAutoCompleteGlossaryMenuItem;
    JMenuItem optionsAutoCompleteAutoTextMenuItem;
    JMenuItem optionsAutoCompleteCharTableMenuItem;
    JMenuItem optionsWorkflowMenuItem;
    JMenuItem optionsTagValidationMenuItem;
    JMenuItem optionsTeamMenuItem;
    JMenuItem optionsExtTMXMenuItem;
    JMenuItem optionsViewOptionsMenuItem;
    JMenuItem optionsSaveOptionsMenuItem;
    JMenuItem optionsViewOptionsMenuLoginItem;
    JMenuItem projectCloseMenuItem;
    JMenuItem projectCompileMenuItem;
    JMenuItem projectSingleCompileMenuItem;
    JMenuItem projectEditMenuItem;
    JMenuItem projectExitMenuItem;
    JMenuItem projectImportMenuItem;
    JMenu projectMenu;
    JMenuItem projectNewMenuItem;
    JMenuItem projectTeamNewMenuItem;
    JMenuItem projectOpenMenuItem;
    JMenu projectOpenRecentMenuItem;
    JMenuItem projectReloadMenuItem;
    JMenuItem projectSaveMenuItem;
    JMenuItem projectWikiImportMenuItem;
    JMenu switchCaseSubMenu;
    JMenuItem titleCaseMenuItem;
    JMenu toolsMenu;
    JMenuItem toolsValidateTagsMenuItem;
    JMenuItem toolsSingleValidateTagsMenuItem;
    JMenuItem toolsShowStatisticsStandardMenuItem;
    JMenuItem toolsShowStatisticsMatchesMenuItem;
    JMenuItem toolsShowStatisticsMatchesPerFileMenuItem;
    JMenuItem upperCaseMenuItem;
    JCheckBoxMenuItem viewDisplaySegmentSourceCheckBoxMenuItem;
    JCheckBoxMenuItem viewMarkNonUniqueSegmentsCheckBoxMenuItem;
    JCheckBoxMenuItem viewMarkNotedSegmentsCheckBoxMenuItem;
    JCheckBoxMenuItem viewMarkNBSPCheckBoxMenuItem;
    JCheckBoxMenuItem viewMarkWhitespaceCheckBoxMenuItem;
    JCheckBoxMenuItem viewMarkBidiCheckBoxMenuItem;
    JCheckBoxMenuItem viewMarkAutoPopulatedCheckBoxMenuItem;
    JMenu viewModificationInfoMenu;
    JRadioButtonMenuItem viewDisplayModificationInfoNoneRadioButtonMenuItem;
    JRadioButtonMenuItem viewDisplayModificationInfoSelectedRadioButtonMenuItem;
    JRadioButtonMenuItem viewDisplayModificationInfoAllRadioButtonMenuItem;
    JMenuItem viewFileListMenuItem;
    JCheckBoxMenuItem viewMarkTranslatedSegmentsCheckBoxMenuItem;
    JCheckBoxMenuItem viewMarkUntranslatedSegmentsCheckBoxMenuItem;
    JMenu viewMenu;
}
