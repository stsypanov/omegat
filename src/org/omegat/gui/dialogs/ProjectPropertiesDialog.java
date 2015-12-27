/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2008-2009 Alex Buloichik
               2011 Martin Fleurke
               2012 Didier Briel, Aaron Madlon-Kay
               2013 Aaron Madlon-Kay, Yu Tang
               2014-2015 Aaron Madlon-Kay
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

package org.omegat.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.omegat.core.Core;
import org.omegat.core.data.CommandVarExpansion;
import org.omegat.core.data.ProjectProperties;
import org.omegat.core.segmentation.SRX;
import org.omegat.filters2.master.FilterMaster;
import org.omegat.filters2.master.PluginUtils;
import org.omegat.gui.common.PeroDialog;
import org.omegat.gui.filters2.FiltersCustomizer;
import org.omegat.gui.segmentation.SegmentationCustomizer;
import org.omegat.tokenizer.DefaultTokenizer;
import org.omegat.tokenizer.ITokenizer;
import org.omegat.util.Language;
import org.omegat.util.OConsts;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.StaticUtils;
import org.omegat.util.StringUtil;
import org.omegat.util.gui.DockingUI;
import org.omegat.util.gui.LanguageComboBoxRenderer;
import org.omegat.util.gui.OmegaTFileChooser;
import org.omegat.util.gui.StaticUIUtils;
import org.omegat.util.gui.TokenizerBehaviorComboBoxRenderer;
import org.omegat.util.gui.TokenizerComboBoxRenderer;
import org.openide.awt.Mnemonics;

import gen.core.filters.Filters;

/**
 * The dialog for customizing the OmegaT project (where project properties are
 * entered and/or modified).
 * <p>
 * It is used:
 * <ul>
 * <li>During creation of a new project.
 * <li>If some directories are missing while opening a project.
 * <li>For editing project properties.
 * </ul>
 * 
 * @author Keith Godfrey
 * @author Maxym Mykhalchuk
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Martin Fleurke
 * @author Didier Briel
 * @author Aaron Madlon-Kay
 * @author Yu Tang
 */
@SuppressWarnings("serial")
public class ProjectPropertiesDialog extends PeroDialog {
    private ProjectProperties projectProperties;

    public enum Mode {
        /** This dialog is used to create a new project. */
        NEW_PROJECT,
        /**
         * This dialog is used to resolve missing directories of existing project
         * (upon opening the project).
         */
        RESOLVE_DIRS,
        /**
         * This dialog is used to edit project's properties: where directories
         * reside, languages, etc.
         */
        EDIT_PROJECT,
        /**
         * Project properties stored in omegat.project are not editable for team
         * projects, but access is available through this dialog to the project-specific
         * filter settings and segmentation settings.
         */
        EDIT_TEAM_PROJECT
    }

    /**
     * The type of the dialog:
     * <ul>
     * <li>Creating project == {@link Mode#NEW_PROJECT}
     * <li>Resolving the project's directories (existing project with some dirs
     * missing) == {@link Mode#RESOLVE_DIRS}
     * <li>Editing project properties == {@link Mode#EDIT_PROJECT}
     * <li>Editing filter or segmentation settings for team project == {@link Mode#EDIT_TEAM_PROJECT}
     * </ul>
     */
    private Mode dialogType;

    /** Project SRX. */
    private SRX srx;

    /** Project filters. */
    private Filters filters;

    private List<String> srcExcludes = new ArrayList<>();

    /**
     * Creates a dialog to create a new project / edit folders of existing one.
     * 
     * @param projectProperties
     *            properties of the project
     * @param projFileName
     *            project file name
     * @param dialogTypeValue
     *            type of the dialog ({@link #NEW_PROJECT},
     *            {@link #RESOLVE_DIRS} or {@link #EDIT_PROJECT}).
     */
    public ProjectPropertiesDialog(final ProjectProperties projectProperties, String projFileName,
            Mode dialogTypeValue) {
        super(Core.getMainWindow().getApplicationFrame(), true);
        this.projectProperties = projectProperties;
        this.srx = projectProperties.getProjectSRX();
        this.dialogType = dialogTypeValue;
        filters = FilterMaster.loadConfig(projectProperties.getProjectInternal());
        srcExcludes.addAll(projectProperties.getSourceRootExcludes());

        Border emptyBorder = new EmptyBorder(2, 0, 2, 0);
        Box centerBox = new ScrollableBox(BoxLayout.Y_AXIS);
        // Have to set background and opacity on OS X or else entire dialog is white.
        centerBox.setBackground(getBackground());
        centerBox.setOpaque(true);
        centerBox.setBorder(new EmptyBorder(5, 5, 5, 5));

        // hinting message
        JTextArea m_messageArea = new JTextArea();
        m_messageArea.setEditable(false);
        m_messageArea.setOpaque(false);
        m_messageArea.setFont(new Label().getFont());
        Box bMes = Box.createHorizontalBox();
        bMes.setBorder(emptyBorder);
        bMes.add(m_messageArea);
        bMes.add(Box.createHorizontalGlue());
        centerBox.add(bMes);

        // Source and target languages and tokenizers
        Box localesBox = Box.createHorizontalBox();
        localesBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), OStrings.getString("PP_LANGUAGES") ));

        // Languages box
        Box bL = Box.createVerticalBox();
        localesBox.add(bL);

        // Source language label
        JLabel m_sourceLocaleLabel = new JLabel();
        Mnemonics.setLocalizedText(m_sourceLocaleLabel, OStrings.getString("PP_SRC_LANG"));
        Box bSL = Box.createHorizontalBox();
        bSL.setBorder(emptyBorder);
        bSL.add(m_sourceLocaleLabel);
        bSL.add(Box.createHorizontalGlue());
        bL.add(bSL);

        // Source language field
        final JComboBox m_sourceLocaleField = new JComboBox(Language.LANGUAGES);
        if (m_sourceLocaleField.getMaximumRowCount() < 20) 
            m_sourceLocaleField.setMaximumRowCount(20);
        m_sourceLocaleField.setEditable(true);
        m_sourceLocaleField.setRenderer(new LanguageComboBoxRenderer());
        m_sourceLocaleField.setSelectedItem(projectProperties.getSourceLanguage());
        bL.add(m_sourceLocaleField);

        // Target language label
        JLabel m_targetLocaleLabel = new JLabel();
        Mnemonics.setLocalizedText(m_targetLocaleLabel, OStrings.getString("PP_LOC_LANG"));
        Box bLL = Box.createHorizontalBox();
        bLL.setBorder(emptyBorder);
        bLL.add(m_targetLocaleLabel);
        bLL.add(Box.createHorizontalGlue());
        bL.add(bLL);

        // Target language field
        final JComboBox m_targetLocaleField = new JComboBox(Language.LANGUAGES);
        if (m_targetLocaleField.getMaximumRowCount() < 20)
            m_targetLocaleField.setMaximumRowCount(20);
        m_targetLocaleField.setEditable(true);
        m_targetLocaleField.setRenderer(new LanguageComboBoxRenderer());
        m_targetLocaleField.setSelectedItem(projectProperties.getTargetLanguage());
        bL.add(m_targetLocaleField);

        // Tokenizers box
        Box bT = Box.createVerticalBox();
        localesBox.add(bT);
        Object[] tokenizers = PluginUtils.getTokenizerClasses().toArray();

        // Source tokenizer label
        JLabel m_sourceTokenizerLabel = new JLabel();
        Mnemonics.setLocalizedText(m_sourceTokenizerLabel, OStrings.getString("PP_SRC_TOK"));
        Box bST = Box.createHorizontalBox();
        bST.setBorder(emptyBorder);
        bST.add(m_sourceTokenizerLabel);
        bST.add(Box.createHorizontalGlue());
        bT.add(bST);

        // Source tokenizer field
        final JComboBox m_sourceTokenizerField = new JComboBox(tokenizers);
        if (m_sourceTokenizerField.getMaximumRowCount() < 20)
            m_sourceTokenizerField.setMaximumRowCount(20);
        m_sourceTokenizerField.setEditable(false);
        m_sourceTokenizerField.setRenderer(new TokenizerComboBoxRenderer());
        m_sourceTokenizerField.setSelectedItem(projectProperties.getSourceTokenizer());
        bT.add(m_sourceTokenizerField);

        String cliTokSrc = Core.getParams().get(ITokenizer.CLI_PARAM_SOURCE);
        if (cliTokSrc != null) {
            m_sourceTokenizerField.setEnabled(false);
            m_sourceTokenizerField.addItem(cliTokSrc);
            m_sourceTokenizerField.setSelectedItem(cliTokSrc);
        }

        m_sourceLocaleField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!m_sourceLocaleField.isEnabled()) return;
                Object newLang = m_sourceLocaleField.getSelectedItem();
                if (newLang instanceof String) {
                    newLang = new Language((String)newLang);
                }
                Class<?> newTok = PluginUtils.getTokenizerClassForLanguage((Language)newLang);
                m_sourceTokenizerField.setSelectedItem(newTok);
            }});

        // Target tokenizer label
        JLabel m_targetTokenizerLabel = new JLabel();
        Mnemonics.setLocalizedText(m_targetTokenizerLabel, OStrings.getString("PP_LOC_TOK"));
        Box bTT = Box.createHorizontalBox();
        bTT.setBorder(emptyBorder);
        bTT.add(m_targetTokenizerLabel);
        bTT.add(Box.createHorizontalGlue());
        bT.add(bTT);

        // Target tokenizer field
        final JComboBox m_targetTokenizerField = new JComboBox(tokenizers);
        if (m_targetTokenizerField.getMaximumRowCount() < 20)
            m_targetTokenizerField.setMaximumRowCount(20);
        m_targetTokenizerField.setEditable(false);
        m_targetTokenizerField.setRenderer(new TokenizerComboBoxRenderer());
        m_targetTokenizerField.setSelectedItem(projectProperties.getTargetTokenizer());
        bT.add(m_targetTokenizerField);

        String cliTokTrg = Core.getParams().get(ITokenizer.CLI_PARAM_TARGET);
        if (cliTokTrg != null) {
            m_targetTokenizerField.setEnabled(false);
            m_targetTokenizerField.addItem(cliTokTrg);
            m_targetTokenizerField.setSelectedItem(cliTokTrg);
        }

        m_targetLocaleField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!m_targetLocaleField.isEnabled()) return;
                Object newLang = m_targetLocaleField.getSelectedItem();
                if (newLang instanceof String) {
                    newLang = new Language((String)newLang);
                }
                Class<?> newTok = PluginUtils.getTokenizerClassForLanguage((Language)newLang);
                m_targetTokenizerField.setSelectedItem(newTok);
            }});

        // Tokenizer behavior box
        Box bB = Box.createVerticalBox();
        localesBox.add(bB);

        // Source tokenizer behavior label
        JLabel m_sourceTokenizerBehaviorLabel = new JLabel();
        Mnemonics.setLocalizedText(m_sourceTokenizerBehaviorLabel, OStrings.getString("PP_SRC_TOK_BEHAVIOR"));
        Box bSTB = Box.createHorizontalBox();
        bSTB.setBorder(emptyBorder);
        bSTB.add(m_sourceTokenizerBehaviorLabel);
        bSTB.add(Box.createHorizontalGlue());
        bB.add(bSTB);

        // Source tokenizer behavior field
        Class<?> srcTokClass = projectProperties.getSourceTokenizer();
        ITokenizer srcTok;
        try {
            srcTok = (ITokenizer) srcTokClass.newInstance();
        } catch (Exception e) {
            srcTok = new DefaultTokenizer();
        }
        final JComboBox m_sourceTokenizerBehaviorField = new JComboBox(
                srcTok.getSupportedBehaviors().keySet().toArray());
        m_sourceTokenizerBehaviorField.setEnabled(!srcTok.getSupportedBehaviors().isEmpty());
        if (m_sourceTokenizerBehaviorField.getMaximumRowCount() < 20)
            m_sourceTokenizerBehaviorField.setMaximumRowCount(20);
        m_sourceTokenizerBehaviorField.setEditable(false);
        m_sourceTokenizerBehaviorField.setRenderer(
                new TokenizerBehaviorComboBoxRenderer(srcTok.getSupportedBehaviors(),
                        srcTok.getDefaultBehavior()));
        m_sourceTokenizerBehaviorField.setSelectedItem(srcTok.getBehavior());
        bB.add(m_sourceTokenizerBehaviorField);

        if (!m_sourceTokenizerField.isEnabled()) {
            m_sourceTokenizerBehaviorField.setEnabled(false);
        }

        final String cliTokSrcBehavior = Core.getParams().get(ITokenizer.CLI_PARAM_SOURCE_BEHAVIOR);
        if (cliTokSrcBehavior != null) {
            m_sourceTokenizerBehaviorField.setEnabled(false);
            m_sourceTokenizerBehaviorField.addItem(cliTokSrcBehavior);
            m_sourceTokenizerBehaviorField.setSelectedItem(cliTokSrcBehavior);
        }

        m_sourceTokenizerField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!m_sourceTokenizerField.isEnabled()) {
                    return;
                }
                if (cliTokSrcBehavior != null) {
                    return;
                }
                Object cls = m_sourceTokenizerField.getSelectedItem();
                if (!(cls instanceof Class<?>)) {
                    return;
                }
                ITokenizer newTok;
                try {
                    newTok = (ITokenizer)((Class<?>) cls).newInstance();
                    m_sourceTokenizerBehaviorField.setRenderer(
                            new TokenizerBehaviorComboBoxRenderer(newTok.getSupportedBehaviors(),
                                    newTok.getDefaultBehavior()));
                    m_sourceTokenizerBehaviorField.setModel(new DefaultComboBoxModel(
                            newTok.getSupportedBehaviors().keySet().toArray()));
                    if (m_sourceTokenizerBehaviorField.getModel().getSize() > 0) {
                        m_sourceTokenizerBehaviorField.setEnabled(true);
                        m_sourceTokenizerBehaviorField.setSelectedItem(newTok.getBehavior());
                    } else {
                        m_sourceTokenizerBehaviorField.setEnabled(false);
                    }
                } catch (Exception ex) {
                }
            }});

        // Target tokenizer behavior label
        JLabel m_targetTokenizerBehaviorLabel = new JLabel();
        Mnemonics.setLocalizedText(m_targetTokenizerBehaviorLabel, OStrings.getString("PP_LOC_TOK_BEHAVIOR"));
        Box bTTB = Box.createHorizontalBox();
        bTTB.setBorder(emptyBorder);
        bTTB.add(m_targetTokenizerBehaviorLabel);
        bTTB.add(Box.createHorizontalGlue());
        bB.add(bTTB);

        // Target tokenizer behavior field
        Class<?> trgTokClass = projectProperties.getTargetTokenizer();
        ITokenizer trgTok;
        try {
            trgTok = (ITokenizer) trgTokClass.newInstance();
        } catch (Exception e) {
            trgTok = new DefaultTokenizer();
        }
        final JComboBox m_targetTokenizerBehaviorField = new JComboBox(
                trgTok.getSupportedBehaviors().keySet().toArray());
        m_targetTokenizerBehaviorField.setEnabled(!trgTok.getSupportedBehaviors().isEmpty());
        if (m_targetTokenizerBehaviorField.getMaximumRowCount() < 20)
            m_targetTokenizerBehaviorField.setMaximumRowCount(20);
        m_targetTokenizerBehaviorField.setEditable(false);
        m_targetTokenizerBehaviorField.setRenderer(
                new TokenizerBehaviorComboBoxRenderer(trgTok.getSupportedBehaviors(),
                        trgTok.getDefaultBehavior()));
        m_targetTokenizerBehaviorField.setSelectedItem(trgTok.getBehavior());
        bB.add(m_targetTokenizerBehaviorField);

        if (!m_targetTokenizerField.isEnabled()) {
            m_targetTokenizerBehaviorField.setEnabled(false);
        }

        final String cliTokTrgBehavior = Core.getParams().get(ITokenizer.CLI_PARAM_TARGET_BEHAVIOR);
        if (cliTokTrgBehavior != null) {
            m_targetTokenizerBehaviorField.setEnabled(false);
            m_targetTokenizerBehaviorField.addItem(cliTokTrgBehavior);
            m_targetTokenizerBehaviorField.setSelectedItem(cliTokTrgBehavior);
        }

        m_targetTokenizerField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!m_targetTokenizerField.isEnabled()) {
                    return;
                }
                if (cliTokTrgBehavior != null) {
                    return;
                }
                Object cls = m_targetTokenizerField.getSelectedItem();
                if (!(cls instanceof Class<?>)) {
                    return;
                }
                ITokenizer newTok;
                try {
                    newTok = (ITokenizer)((Class<?>) cls).newInstance();
                    m_targetTokenizerBehaviorField.setRenderer(
                            new TokenizerBehaviorComboBoxRenderer(newTok.getSupportedBehaviors(),
                                    newTok.getDefaultBehavior()));
                    m_targetTokenizerBehaviorField.setModel(new DefaultComboBoxModel(
                            newTok.getSupportedBehaviors().keySet().toArray()));
                    if (m_targetTokenizerBehaviorField.getModel().getSize() > 0) {
                        m_targetTokenizerBehaviorField.setEnabled(true);
                        m_targetTokenizerBehaviorField.setSelectedItem(newTok.getBehavior());
                    } else {
                        m_targetTokenizerBehaviorField.setEnabled(false);
                    }
                } catch (Exception ex) {
                }
            }});

        centerBox.add(localesBox);

        // options
        centerBox.add(Box.createVerticalStrut(5));
        Box optionsBox = Box.createVerticalBox();
        optionsBox.setBorder(new EtchedBorder());
        optionsBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), OStrings.getString("PP_OPTIONS") ));
        
        // sentence-segmenting
        final JCheckBox m_sentenceSegmentingCheckBox = new JCheckBox();
        Mnemonics
                .setLocalizedText(m_sentenceSegmentingCheckBox, OStrings.getString("PP_SENTENCE_SEGMENTING"));

        JButton m_sentenceSegmentingButton = new JButton();
        Mnemonics.setLocalizedText(m_sentenceSegmentingButton, OStrings.getString("MW_OPTIONSMENU_SENTSEG"));

        Box bSent = Box.createHorizontalBox();
        bSent.add(m_sentenceSegmentingCheckBox);
        bSent.add(Box.createHorizontalGlue());
        bSent.add(m_sentenceSegmentingButton);
        optionsBox.add(bSent);
        
        //File Filters
        JButton m_fileFiltersButton = new JButton();
        Mnemonics.setLocalizedText(m_fileFiltersButton, OStrings.getString("WM_PROJECTMENU_FILEFILTERS"));

        Box bFF = Box.createHorizontalBox();
        bFF.add(Box.createHorizontalGlue());
        bFF.add(m_fileFiltersButton);
        optionsBox.add(bFF);

        //multiple translations
        final JCheckBox m_allowDefaultsCheckBox = new JCheckBox();
        Mnemonics.setLocalizedText(m_allowDefaultsCheckBox, OStrings.getString("PP_ALLOW_DEFAULTS"));
        Box bMT = Box.createHorizontalBox();
        bMT.setBorder(emptyBorder);
        bMT.add(m_allowDefaultsCheckBox);
        bMT.add(Box.createHorizontalGlue());
        optionsBox.add(bMT);

        //Remove Tags
        final JCheckBox m_removeTagsCheckBox = new JCheckBox();
        Mnemonics.setLocalizedText(m_removeTagsCheckBox, OStrings.getString("PP_REMOVE_TAGS"));
        Box bRT = Box.createHorizontalBox();
        bRT.setBorder(emptyBorder);
        bRT.add(m_removeTagsCheckBox);
        bRT.add(Box.createHorizontalGlue());
        optionsBox.add(bRT);
        
        //Post-processing
        JLabel m_externalCommandLabel = new JLabel();
        Box bEC = Box.createHorizontalBox();
        bEC.setBorder(emptyBorder);
        bEC.add(m_externalCommandLabel);
        bEC.add(Box.createHorizontalGlue());
        optionsBox.add(bEC);
        final JTextArea m_externalCommandTextArea = new JTextArea();
        m_externalCommandTextArea.setRows(2);
        m_externalCommandTextArea.setLineWrap(true);
        m_externalCommandTextArea.setText(projectProperties.getExternalCommand());
        if (Preferences.isPreference(Preferences.ALLOW_PROJECT_EXTERN_CMD)) {
        	Mnemonics.setLocalizedText(m_externalCommandLabel, OStrings.getString("PP_EXTERNAL_COMMAND"));
        } else {
        	Mnemonics.setLocalizedText(m_externalCommandLabel, OStrings.getString("PP_EXTERN_CMD_DISABLED"));
            m_externalCommandTextArea.setEditable(false);
            m_externalCommandTextArea.setToolTipText(OStrings.getString("PP_EXTERN_CMD_DISABLED_TOOLTIP"));
            m_externalCommandLabel.setToolTipText(OStrings.getString("PP_EXTERN_CMD_DISABLED_TOOLTIP"));
            m_externalCommandTextArea.setBackground(getBackground());
        }
        final JScrollPane m_externalCommandScrollPane = new JScrollPane();
        m_externalCommandScrollPane.setViewportView(m_externalCommandTextArea);
        optionsBox.add(m_externalCommandScrollPane);
        final JLabel m_variablesLabel = new javax.swing.JLabel();
        final JComboBox m_variablesList = new javax.swing.JComboBox(CommandVarExpansion.COMMAND_VARIABLES);
        final JButton m_insertButton = new javax.swing.JButton();
        // Add variable insertion controls only if project external commands are enabled.
        if (Preferences.isPreference(Preferences.ALLOW_PROJECT_EXTERN_CMD)) {
            Box bIC = Box.createHorizontalBox();
            bIC.setBorder(emptyBorder);
            Mnemonics.setLocalizedText(m_variablesLabel, OStrings.getString("EXT_TMX_MATCHES_TEMPLATE_VARIABLES"));
            bIC.add(m_variablesLabel);
            bIC.add(m_variablesList);
            Mnemonics.setLocalizedText(m_insertButton, OStrings.getString("BUTTON_INSERT"));
            m_insertButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    insertButtonActionPerformed(m_externalCommandTextArea, m_variablesList);
                }
            });
            bIC.add(m_insertButton);
            bIC.add(Box.createHorizontalGlue());
            optionsBox.add(bIC);
        }

        centerBox.add(optionsBox, BorderLayout.WEST);

        // directories
        centerBox.add(Box.createVerticalStrut(5));

        Box dirsBox = Box.createVerticalBox();
        dirsBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), OStrings.getString("PP_DIRECTORIES") ));

        JLabel m_srcRootLabel = new JLabel();
        Mnemonics.setLocalizedText(m_srcRootLabel, OStrings.getString("PP_SRC_ROOT"));
        Box bSrc = Box.createHorizontalBox();
        bSrc.setBorder(emptyBorder);
        bSrc.add(m_srcRootLabel);
        bSrc.add(Box.createHorizontalGlue());
        JButton m_srcExcludes = new JButton();
        Mnemonics.setLocalizedText(m_srcExcludes, OStrings.getString("PP_BUTTON_BROWSE_SRC_EXCLUDES"));
        bSrc.add(m_srcExcludes);
        JButton m_srcBrowse = new JButton();
        Mnemonics.setLocalizedText(m_srcBrowse, OStrings.getString("PP_BUTTON_BROWSE_SRC"));
        bSrc.add(m_srcBrowse);
        final JTextField m_srcRootField = new JTextField();
        dirsBox.add(bSrc);
        dirsBox.add(m_srcRootField);

        JLabel m_tmRootLabel = new JLabel();
        Mnemonics.setLocalizedText(m_tmRootLabel, OStrings.getString("PP_TM_ROOT"));
        Box bTM = Box.createHorizontalBox();
        bTM.setBorder(emptyBorder);
        bTM.add(m_tmRootLabel);
        bTM.add(Box.createHorizontalGlue());
        JButton m_tmBrowse = new JButton();
        Mnemonics.setLocalizedText(m_tmBrowse, OStrings.getString("PP_BUTTON_BROWSE_TM"));
        bTM.add(m_tmBrowse);
        final JTextField m_tmRootField = new JTextField();
        dirsBox.add(bTM);
        dirsBox.add(m_tmRootField);

        JLabel m_glosRootLabel = new JLabel();
        Mnemonics.setLocalizedText(m_glosRootLabel, OStrings.getString("PP_GLOS_ROOT"));
        Box bGlos = Box.createHorizontalBox();
        bGlos.setBorder(emptyBorder);
        bGlos.add(m_glosRootLabel);
        bGlos.add(Box.createHorizontalGlue());
        JButton m_glosBrowse = new JButton();
        Mnemonics.setLocalizedText(m_glosBrowse, OStrings.getString("PP_BUTTON_BROWSE_GL"));
        bGlos.add(m_glosBrowse);
        final JTextField m_glosRootField = new JTextField();
        dirsBox.add(bGlos);
        dirsBox.add(m_glosRootField);

        JLabel m_writeableGlosLabel = new JLabel();
        Mnemonics.setLocalizedText(m_writeableGlosLabel, OStrings.getString("PP_WRITEABLE_GLOS"));
        Box bwGlos = Box.createHorizontalBox();
        bwGlos.setBorder(emptyBorder);
        bwGlos.add(m_writeableGlosLabel);
        bwGlos.add(Box.createHorizontalGlue());
        JButton m_wGlosBrowse = new JButton();
        Mnemonics.setLocalizedText(m_wGlosBrowse, OStrings.getString("PP_BUTTON_BROWSE_WG"));
        bwGlos.add(m_wGlosBrowse);
        final JTextField m_writeableGlosField = new JTextField();
        dirsBox.add(bwGlos);
        dirsBox.add(m_writeableGlosField);

        JLabel m_locDictLabel = new JLabel();
        Mnemonics.setLocalizedText(m_locDictLabel, OStrings.getString("PP_DICT_ROOT"));
        Box bDict = Box.createHorizontalBox();
        bDict.setBorder(emptyBorder);
        bDict.add(m_locDictLabel);
        bDict.add(Box.createHorizontalGlue());
        JButton m_dictBrowse = new JButton();
        Mnemonics.setLocalizedText(m_dictBrowse, OStrings.getString("PP_BUTTON_BROWSE_DICT"));
        bDict.add(m_dictBrowse);
        final JTextField m_dictRootField = new JTextField();
        dirsBox.add(bDict);
        dirsBox.add(m_dictRootField);

        JLabel m_locRootLabel = new JLabel();
        Mnemonics.setLocalizedText(m_locRootLabel, OStrings.getString("PP_LOC_ROOT"));
        Box bLoc = Box.createHorizontalBox();
        bLoc.setBorder(emptyBorder);
        bLoc.add(m_locRootLabel);
        bLoc.add(Box.createHorizontalGlue());
        JButton m_locBrowse = new JButton();
        Mnemonics.setLocalizedText(m_locBrowse, OStrings.getString("PP_BUTTON_BROWSE_TAR"));
        bLoc.add(m_locBrowse);
        final JTextField m_locRootField = new JTextField();
        dirsBox.add(bLoc);
        dirsBox.add(m_locRootField);

        centerBox.add(dirsBox);

        JScrollPane scrollPane = new JScrollPane(centerBox);
        scrollPane.getViewport().setBackground(getBackground());
        getContentPane().add(scrollPane, "Center");

        JButton m_okButton = new JButton();
        Mnemonics.setLocalizedText(m_okButton, OStrings.getString("BUTTON_OK"));
        getRootPane().setDefaultButton(m_okButton);
        JButton m_cancelButton = new JButton();
        Mnemonics.setLocalizedText(m_cancelButton, OStrings.getString("BUTTON_CANCEL"));

        Box southBox = Box.createHorizontalBox();
        southBox.setBorder(new EmptyBorder(5, 5, 5, 5));
        southBox.add(Box.createHorizontalGlue());
        southBox.add(m_okButton);
        southBox.add(Box.createHorizontalStrut(5));
        southBox.add(m_cancelButton);
        getContentPane().add(southBox, "South");

        setResizable(false);

        m_okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doOK(m_sourceLocaleField, m_targetLocaleField, m_sourceTokenizerField, m_targetTokenizerField,
                        m_sourceTokenizerBehaviorField, m_targetTokenizerBehaviorField,
                        m_sentenceSegmentingCheckBox, m_srcRootField, m_locRootField, m_glosRootField,
                        m_writeableGlosField, m_tmRootField, m_dictRootField, m_allowDefaultsCheckBox,
                        m_removeTagsCheckBox, m_externalCommandTextArea);
            }
        });

        m_cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        });

        m_srcBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doBrowseDirectoy(1, m_srcRootField);
            }
        });
        m_srcExcludes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> result = FilenamePatternsEditorController.show(srcExcludes);
                if (result != null) {
                    srcExcludes.clear();
                    srcExcludes.addAll(result);
                }
            }
        });

        m_locBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doBrowseDirectoy(2, m_locRootField);
            }
        });

        m_glosBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Test now, because result may change after doBrowseDirectory().
                boolean isDefaultGlossaryFile = projectProperties.isDefaultWriteableGlossaryFile();
                doBrowseDirectoy(3, m_glosRootField);
                // If file started as default, automatically use new default.
                if (isDefaultGlossaryFile) {
                    String newDefault = projectProperties.computeDefaultWriteableGlossaryFile();
                    projectProperties.setWriteableGlossary(newDefault);
                    m_writeableGlosField.setText(newDefault);
                }
            }
        });

        m_wGlosBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doBrowseDirectoy(6, m_writeableGlosField);
            }
        });

        m_tmBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doBrowseDirectoy(4, m_tmRootField);
            }
        });

        m_dictBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doBrowseDirectoy(5, m_dictRootField);
            }
        });

        final JDialog self = this;
        m_sentenceSegmentingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SegmentationCustomizer segmentationCustomizer = new SegmentationCustomizer(self, true,
                        SRX.getDefault(), Preferences.getSRX(), srx);
                segmentationCustomizer.setVisible(true);
                if (segmentationCustomizer.getReturnStatus() == SegmentationCustomizer.RET_OK) {
                    if (segmentationCustomizer.isProjectSpecific()) {
                        srx = segmentationCustomizer.getSRX();
                    } else {
                        srx = null;
                    }
                }
            }
        });
        
        m_fileFiltersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame mainWindow = Core.getMainWindow().getApplicationFrame();
                FiltersCustomizer dlg = new FiltersCustomizer(mainWindow, true, FilterMaster
                        .createDefaultFiltersConfig(), FilterMaster.loadConfig(StaticUtils.getConfigDir()),
                        filters);
                dlg.setVisible(true);
                if (dlg.getReturnStatus() == FiltersCustomizer.RET_OK) {
                    // saving config
                    filters = dlg.result;
                }
            }
        });

        StaticUIUtils.setEscapeAction(this, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        });

        m_srcRootField.setText(projectProperties.getSourceRoot());
        m_locRootField.setText(projectProperties.getTargetRoot());
        m_glosRootField.setText(projectProperties.getGlossaryRoot());
        m_writeableGlosField.setText(projectProperties.getWriteableGlossary());
        m_tmRootField.setText(projectProperties.getTMRoot());
        m_dictRootField.setText(projectProperties.getDictRoot());
        m_sentenceSegmentingCheckBox.setSelected(projectProperties.isSentenceSegmentingEnabled());
        m_allowDefaultsCheckBox.setSelected(projectProperties.isSupportDefaultTranslations());
        m_removeTagsCheckBox.setSelected(projectProperties.isRemoveTags());

        switch (dialogType) {
        case RESOLVE_DIRS:
            // disabling some of the controls
            m_sourceLocaleField.setEnabled(false);
            m_targetLocaleField.setEnabled(false);
            m_sourceTokenizerField.setEnabled(false);
            m_targetTokenizerField.setEnabled(false);
            m_sourceTokenizerBehaviorField.setEnabled(false);
            m_targetTokenizerBehaviorField.setEnabled(false);
            m_sentenceSegmentingCheckBox.setEnabled(false);
            m_allowDefaultsCheckBox.setEnabled(false);
            m_removeTagsCheckBox.setEnabled(false);
            m_externalCommandTextArea.setEnabled(false);
            m_insertButton.setEnabled(false);
            m_variablesList.setEnabled(false);

            // marking missing folder RED
            File f = new File(m_srcRootField.getText());
            if (!f.exists() || !f.isDirectory())
                m_srcRootField.setForeground(Color.RED);

            f = new File(m_locRootField.getText());
            if (!f.exists() || !f.isDirectory())
                m_locRootField.setForeground(Color.RED);

            f = new File(m_glosRootField.getText());
            if (!f.exists() || !f.isDirectory())
                m_glosRootField.setForeground(Color.RED);

            f = new File(m_writeableGlosField.getText());
            String wGlos = f.getParent(); // Remove the file name
            if (!wGlos.endsWith(File.separator)) {
                wGlos += File.separator;
            }
            f = new File(wGlos);
            // The writeable glossary must be in in the /glossary folder
            if (!f.exists() || !f.isDirectory() || !wGlos.contains(m_glosRootField.getText()))
                m_writeableGlosField.setForeground(Color.RED);  

            f = new File(m_tmRootField.getText());
            if (!f.exists() || !f.isDirectory())
                m_tmRootField.setForeground(Color.RED);

            break;
        case EDIT_TEAM_PROJECT:
            m_sourceLocaleField.setEnabled(false);
            m_targetLocaleField.setEnabled(false);
            m_sourceTokenizerField.setEnabled(false);
            m_targetTokenizerField.setEnabled(false);
            m_sourceTokenizerBehaviorField.setEnabled(false);
            m_targetTokenizerBehaviorField.setEnabled(false);
            m_sentenceSegmentingCheckBox.setEnabled(false);
            m_allowDefaultsCheckBox.setEnabled(false);
            m_removeTagsCheckBox.setEnabled(false);
            m_externalCommandTextArea.setEnabled(false);
            m_insertButton.setEnabled(false);
            m_variablesList.setEnabled(false);
            m_srcBrowse.setEnabled(false);
            m_srcRootField.setEnabled(false);
            m_tmBrowse.setEnabled(false);
            m_tmRootField.setEnabled(false);
            m_glosBrowse.setEnabled(false);
            m_glosRootField.setEnabled(false);
            m_wGlosBrowse.setEnabled(false);
            m_writeableGlosField.setEnabled(false);
            m_dictBrowse.setEnabled(false);
            m_dictRootField.setEnabled(false);
            m_locBrowse.setEnabled(false);
            m_locRootField.setEnabled(false);
            m_srcExcludes.setEnabled(false);
        default:
            // Nothing
        }

        updateUIText(m_messageArea);

        pack();

        setSize(9 * getWidth() / 8, getHeight() + 10);
        setResizable(true);
        StaticUIUtils.fitInScreen(this);
        DockingUI.displayCentered(this);
    }

    /**
     * Browses for the directory.
     * 
     * @param browseTarget
     *            customizes the messages depending on what is browsed for
     * @param field
     *            text field to write browsed folder to
     */
    private void doBrowseDirectoy(int browseTarget, JTextField field) {
        if (field == null) {
            return;
        }
        String title;
        boolean fileMode = false;
        boolean glossaryFile = false;

        if (browseTarget == 6) {
            fileMode = true;
            glossaryFile = true;
        }

        switch (browseTarget) {
        case 1:
            title = OStrings.getString("PP_BROWSE_TITLE_SOURCE");
            break;

        case 2:
            title = OStrings.getString("PP_BROWSE_TITLE_TARGET");
            break;

        case 3:
            title = OStrings.getString("PP_BROWSE_TITLE_GLOS");
            break;

        case 4:
            title = OStrings.getString("PP_BROWSE_TITLE_TM");
            break;

        case 5:
            title = OStrings.getString("PP_BROWSE_TITLE_DICT");
            break;

        case 6:
            title = OStrings.getString("PP_BROWSE_W_GLOS");
            break;

        default:
            return;
        }

        OmegaTFileChooser browser = new OmegaTFileChooser();
        // String str = OStrings.getString("BUTTON_SELECT_NO_MNEMONIC");
        // browser.setApproveButtonText(str);
        browser.setDialogTitle(title);
        if (fileMode) {
            browser.setFileSelectionMode(OmegaTFileChooser.FILES_ONLY);
        } else {
            browser.setFileSelectionMode(OmegaTFileChooser.DIRECTORIES_ONLY);
        }

        // check if the current directory as specified by the field exists
        String curDir = field.getText();
        File curDirCheck = new File(curDir);
        if (fileMode && !StringUtil.isEmpty(curDirCheck.getName())) {
            String dirOnly = curDirCheck.getParent();
            dirOnly = (dirOnly != null) ? dirOnly : "";
            curDirCheck = new File(dirOnly);
        }

        // if the dir doesn't exist, use project dir and check if that exists
        if (!curDirCheck.exists() || !curDirCheck.isDirectory()) {
            curDir = projectProperties.getProjectRoot();
            curDirCheck = new File(curDir);
        }

        // if all fails, get last used dir from preferences
        if (!curDirCheck.exists() || !curDirCheck.isDirectory()) {
            switch (browseTarget) {
            case 1:
                curDir = Preferences.getPreference(Preferences.SOURCE_FOLDER);
                break;

            case 2:
                curDir = Preferences.getPreference(Preferences.TARGET_FOLDER);
                break;

            case 3:
                curDir = Preferences.getPreference(Preferences.GLOSSARY_FOLDER);
                break;

            case 4:
                curDir = Preferences.getPreference(Preferences.TM_FOLDER);
                break;

            case 5:
                curDir = Preferences.getPreference(Preferences.DICT_FOLDER);
                break;

            case 6:
                curDir = Preferences.getPreference(Preferences.GLOSSARY_FILE);
                break;

            }
        }

        if (fileMode) {
            File dirFile = new File(curDir);
            curDir = dirFile.getParent();
        }

        if (curDir.isEmpty())
            curDir = Preferences.getPreference(Preferences.CURRENT_FOLDER);

        if (!curDir.isEmpty()) {
            File dir = new File(curDir);
            if (dir.exists() && dir.isDirectory()) {
                browser.setCurrentDirectory(dir);
            }
        }

        // show the browser
        int action = browser.showOpenDialog(this);

        // check if the selection has been approved
        if (action != javax.swing.JFileChooser.APPROVE_OPTION)
            return;

        // get the selected folder
        File dir = browser.getSelectedFile();
        if (dir == null)
            return;

        String str = dir.getAbsolutePath();
        if (!fileMode) {
            str+= File.separator; // Add file separator for directories
        }

        // The writeable glossary file must end with .txt or utf8. Not .tab, because it not necessarily is .utf8
        if (glossaryFile && !str.endsWith(OConsts.EXT_TSV_TXT) &&!str.endsWith(OConsts.EXT_TSV_UTF8)) {
           str += OConsts.EXT_TSV_TXT; // Defaults to .txt
        }

        // reset appropriate path - store preferred directory
        switch (browseTarget) {
        case 1:
            Preferences.setPreference(Preferences.SOURCE_FOLDER, browser.getSelectedFile().getParent());
            projectProperties.setSourceRoot(str);
            field.setText(projectProperties.getSourceRoot());
            if (new File(projectProperties.getSourceRoot()).exists()
                    && new File(projectProperties.getSourceRoot()).isDirectory())
                field.setForeground(java.awt.SystemColor.textText);
            break;

        case 2:
            Preferences.setPreference(Preferences.TARGET_FOLDER, browser.getSelectedFile().getParent());
            projectProperties.setTargetRoot(str);
            field.setText(projectProperties.getTargetRoot());
            if (new File(projectProperties.getTargetRoot()).exists()
                    && new File(projectProperties.getTargetRoot()).isDirectory())
                field.setForeground(java.awt.SystemColor.textText);
            break;

        case 3:
            Preferences.setPreference(Preferences.GLOSSARY_FOLDER, browser.getSelectedFile().getParent());
            projectProperties.setGlossaryRoot(str);
            field.setText(projectProperties.getGlossaryRoot());
            if (new File(projectProperties.getGlossaryRoot()).exists()
                    && new File(projectProperties.getGlossaryRoot()).isDirectory())
                field.setForeground(java.awt.SystemColor.textText);
            break;

        case 4:
            Preferences.setPreference(Preferences.TM_FOLDER, browser.getSelectedFile().getParent());
            projectProperties.setTMRoot(str);
            field.setText(projectProperties.getTMRoot());
            if (new File(projectProperties.getTMRoot()).exists()
                    && new File(projectProperties.getTMRoot()).isDirectory())
                field.setForeground(java.awt.SystemColor.textText);
            break;

        case 5:
            Preferences.setPreference(Preferences.DICT_FOLDER, browser.getSelectedFile().getParent());
            projectProperties.setDictRoot(str);
            field.setText(projectProperties.getDictRoot());
            if (new File(projectProperties.getDictRoot()).exists()
                    && new File(projectProperties.getDictRoot()).isDirectory())
                field.setForeground(java.awt.SystemColor.textText);
            break;

        case 6:
            Preferences.setPreference(Preferences.GLOSSARY_FILE, browser.getSelectedFile().getPath());
            projectProperties.setWriteableGlossary(str);
            field.setText(projectProperties.getWriteableGlossary());
            // The writable glosssary file must be inside the glossary dir
            if (new File(projectProperties.getWriteableGlossaryDir()).exists()
                    && new File(projectProperties.getWriteableGlossaryDir()).isDirectory()
                    && projectProperties.getWriteableGlossaryDir().contains(projectProperties.getGlossaryRoot()))
                field.setForeground(java.awt.SystemColor.textText);
            break;

        }
    }

    private void doOK(JComboBox m_sourceLocaleField, JComboBox m_targetLocaleField,
            JComboBox m_sourceTokenizerField, JComboBox m_targetTokenizerField,
            JComboBox m_sourceTokenizerBehaviorField, JComboBox m_targetTokenizerBehaviorField,
            JCheckBox m_sentenceSegmentingCheckBox, JTextField m_srcRootField, JTextField m_locRootField,
            JTextField m_glosRootField, JTextField m_writeableGlosField, JTextField m_tmRootField, JTextField m_dictRootField,
            JCheckBox m_allowDefaultsCheckBox, JCheckBox m_removeTagsCheckBox, JTextArea m_customCommandTextArea) {
        if (!ProjectProperties.verifySingleLangCode(m_sourceLocaleField.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(
                    this,
                    OStrings.getString("NP_INVALID_SOURCE_LOCALE")
                            + OStrings.getString("NP_LOCALE_SUGGESTION"), OStrings.getString("TF_ERROR"),
                    JOptionPane.ERROR_MESSAGE);
            m_sourceLocaleField.requestFocusInWindow();
            return;
        }
        projectProperties.setSourceLanguage(m_sourceLocaleField.getSelectedItem().toString());

        if (!ProjectProperties.verifySingleLangCode(m_targetLocaleField.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(
                    this,
                    OStrings.getString("NP_INVALID_TARGET_LOCALE")
                            + OStrings.getString("NP_LOCALE_SUGGESTION"), OStrings.getString("TF_ERROR"),
                    JOptionPane.ERROR_MESSAGE);
            m_targetLocaleField.requestFocusInWindow();
            return;
        }
        projectProperties.setTargetLanguage(m_targetLocaleField.getSelectedItem().toString());

        if (m_sourceTokenizerField.isEnabled()) {
            projectProperties.setSourceTokenizer((Class<?>)m_sourceTokenizerField.getSelectedItem());
        }

        if (m_targetTokenizerField.isEnabled()) {
            projectProperties.setTargetTokenizer((Class<?>)m_targetTokenizerField.getSelectedItem());
        }

        if (m_sourceTokenizerBehaviorField.isEnabled()) {
            Class<?> srcTok = (Class<?>)m_sourceTokenizerField.getSelectedItem();
            Preferences.setPreference(Preferences.TOK_BEHAVIOR_PREFIX + srcTok.getName(),
                    m_sourceTokenizerBehaviorField.getSelectedItem().toString());
        }

        if (m_targetTokenizerBehaviorField.isEnabled()) {
            Class<?> trgTok = (Class<?>)m_targetTokenizerField.getSelectedItem();
            Preferences.setPreference(Preferences.TOK_BEHAVIOR_PREFIX + trgTok.getName(),
                    m_targetTokenizerBehaviorField.getSelectedItem().toString());
        }

        projectProperties.setSentenceSegmentingEnabled(m_sentenceSegmentingCheckBox.isSelected());

        projectProperties.setSupportDefaultTranslations(m_allowDefaultsCheckBox.isSelected());

        projectProperties.setRemoveTags(m_removeTagsCheckBox.isSelected());
        
        projectProperties.setExternalCommand(m_customCommandTextArea.getText());

        projectProperties.setSourceRoot(m_srcRootField.getText());
        if (!projectProperties.getSourceRoot().endsWith(File.separator))
            projectProperties.setSourceRoot(projectProperties.getSourceRoot() + File.separator);

        if (dialogType != Mode.NEW_PROJECT && !new File(projectProperties.getSourceRoot()).exists()) {
            JOptionPane.showMessageDialog(this, OStrings.getString("NP_SOURCEDIR_DOESNT_EXIST"),
                    OStrings.getString("TF_ERROR"), JOptionPane.ERROR_MESSAGE);
            m_srcRootField.requestFocusInWindow();
            return;
        }

        projectProperties.setTargetRoot(m_locRootField.getText());
        if (!projectProperties.getTargetRoot().endsWith(File.separator))
            projectProperties.setTargetRoot(projectProperties.getTargetRoot() + File.separator);
        if (dialogType != Mode.NEW_PROJECT && !new File(projectProperties.getTargetRoot()).exists()) {
            JOptionPane.showMessageDialog(this, OStrings.getString("NP_TRANSDIR_DOESNT_EXIST"),
                    OStrings.getString("TF_ERROR"), JOptionPane.ERROR_MESSAGE);
            m_locRootField.requestFocusInWindow();
            return;
        }

        projectProperties.setGlossaryRoot(m_glosRootField.getText());
        if (!projectProperties.getGlossaryRoot().endsWith(File.separator))
            projectProperties.setGlossaryRoot(projectProperties.getGlossaryRoot() + File.separator);
        if (dialogType != Mode.NEW_PROJECT && !new File(projectProperties.getGlossaryRoot()).exists()) {
            JOptionPane.showMessageDialog(this, OStrings.getString("NP_GLOSSDIR_DOESNT_EXIST"),
                    OStrings.getString("TF_ERROR"), JOptionPane.ERROR_MESSAGE);
            m_glosRootField.requestFocusInWindow();
            return;
        }

        projectProperties.setWriteableGlossary(m_writeableGlosField.getText());
        if (dialogType != Mode.NEW_PROJECT && !new File(projectProperties.getWriteableGlossaryDir()).exists()) {
            JOptionPane.showMessageDialog(this, OStrings.getString("NP_W_GLOSSDIR_DOESNT_EXIST"),
                    OStrings.getString("TF_ERROR"), JOptionPane.ERROR_MESSAGE);
            m_writeableGlosField.requestFocusInWindow();
            return;
        }

        String glossaryDir = projectProperties.getWriteableGlossaryDir();
        if (!glossaryDir.endsWith(File.separator)) {
            glossaryDir += File.separator;
        }
        if (!glossaryDir.contains(projectProperties.getGlossaryRoot())) {
            JOptionPane.showMessageDialog(this, OStrings.getString("NP_W_GLOSDIR_NOT_INSIDE_GLOS"),
                    OStrings.getString("TF_ERROR"), JOptionPane.ERROR_MESSAGE);
            m_writeableGlosField.requestFocusInWindow();
            return;
        }

        projectProperties.setTMRoot(m_tmRootField.getText());
        if (!projectProperties.getTMRoot().endsWith(File.separator))
            projectProperties.setTMRoot(projectProperties.getTMRoot() + File.separator);
        if (dialogType != Mode.NEW_PROJECT && !new File(projectProperties.getTMRoot()).exists()) {
            JOptionPane.showMessageDialog(this, OStrings.getString("NP_TMDIR_DOESNT_EXIST"),
                    OStrings.getString("TF_ERROR"), JOptionPane.ERROR_MESSAGE);
            m_tmRootField.requestFocusInWindow();
            return;
        }

        projectProperties.setDictRoot(m_dictRootField.getText());
        if (!projectProperties.getDictRoot().endsWith(File.separator))
            projectProperties.setDictRoot(projectProperties.getDictRoot() + File.separator);
        if (dialogType != Mode.NEW_PROJECT && !new File(projectProperties.getDictRoot()).exists()) {
            JOptionPane.showMessageDialog(this, OStrings.getString("NP_DICTDIR_DOESNT_EXIST"),
                    OStrings.getString("TF_ERROR"), JOptionPane.ERROR_MESSAGE);
            m_dictRootField.requestFocusInWindow();
            return;
        }

        projectProperties.setProjectSRX(srx);
        projectProperties.setProjectFilters(filters);
        projectProperties.getSourceRootExcludes().clear();
        projectProperties.getSourceRootExcludes().addAll(srcExcludes);

        m_dialogCancelled = false;
        setVisible(false);
    }

    private void doCancel() {
        // delete project dir in case of a new project
        // to fix bug 1476591 the project root is created before everything else
        // and if the new project is cancelled, the project root still exists,
        // so it must be deleted
        if (dialogType == Mode.NEW_PROJECT)
            new File(projectProperties.getProjectRoot()).delete();

        m_dialogCancelled = true;
        setVisible(false);
    }
    
    private void insertButtonActionPerformed(JTextArea area, JComboBox box) {
        area.replaceSelection(box.getSelectedItem().toString());
    }

    private void updateUIText(JTextArea m_messageArea) {
        switch (dialogType) {
        case NEW_PROJECT:
            setTitle(OStrings.getString("PP_CREATE_PROJ"));
            m_messageArea.setText(OStrings.getString("PP_MESSAGE_CONFIGPROJ"));
            break;
        case RESOLVE_DIRS:
            setTitle(OStrings.getString("PP_OPEN_PROJ"));
            m_messageArea.setText(OStrings.getString("PP_MESSAGE_BADPROJ"));
            break;
        case EDIT_PROJECT:
        case EDIT_TEAM_PROJECT:
            setTitle(OStrings.getString("PP_EDIT_PROJECT"));
            m_messageArea.setText(OStrings.getString("PP_MESSAGE_EDITPROJ"));
            break;
        }
    }

    private static class ScrollableBox extends Box implements Scrollable {

        public ScrollableBox(int axis) {
            super(axis);
        }
        
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return getFont().getSize();
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return getFont().getSize();
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
        
    }
    
    /**
     * Whether the user cancelled the dialog.
     */
    private boolean m_dialogCancelled;

    /**
     * Return new properties or null if dialog cancelled.
     */
    public ProjectProperties getResult() {
        return m_dialogCancelled ? null : projectProperties;
    }
}
