/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2009-2010 Alex Buloichik
               2011 Martin Fleurke
               2012 Jean-Christophe Helary
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

package org.omegat.gui.exttrans;

import java.awt.Dimension;

import java.util.ArrayList;
import java.util.List;

import org.omegat.core.Core;
import org.omegat.core.data.ProjectProperties;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.filters2.master.PluginUtils;
import org.omegat.gui.common.EntryInfoSearchThread;
import org.omegat.gui.common.EntryInfoThreadPane;
import org.omegat.gui.main.DockableScrollPane;
import org.omegat.util.Language;
import org.omegat.util.Log;
import org.omegat.util.OStrings;
import org.omegat.util.StringUtil;
import org.omegat.util.gui.AlwaysVisibleCaret;
import org.omegat.util.gui.UIThreadsUtil;

/**
 * Pane for display machine translations.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Martin Fleurke
 * @author Jean-Christophe Helary
 */
@SuppressWarnings("serial")
public class MachineTranslateTextArea extends EntryInfoThreadPane<MachineTranslationInfo> {

    private static final String EXPLANATION = OStrings.getString("GUI_MACHINETRANSLATESWINDOW_explanation");

    protected final IMachineTranslation[] translators;

    protected String displayed;

    public MachineTranslateTextArea() {
        super(true);

        setEditable(false);
        AlwaysVisibleCaret.apply(this);
        this.setText(EXPLANATION);
        setMinimumSize(new Dimension(100, 50));

        String title = OStrings.getString("GUI_MATCHWINDOW_SUBWINDOWTITLE_MachineTranslate");
        Core.getMainWindow().addDockable(new DockableScrollPane("MACHINE_TRANSLATE", title, this, true));

        List<Class<?>> classes = PluginUtils.getMachineTranslationClasses();
        List<IMachineTranslation> tr = new ArrayList<>(classes.size());
        for (Class<?> mtc : classes) {
            try {
                tr.add((IMachineTranslation) mtc.newInstance());
            } catch (Exception ex) {
                Log.log(ex);
            }
        }
        translators = tr.toArray(new IMachineTranslation[tr.size()]);
    }

    public String getDisplayedTranslation() {
        return displayed;
    }

    @Override
    protected void onProjectClose() {
        UIThreadsUtil.mustBeSwingThread();
        this.setText(EXPLANATION);
    }

    @Override
    protected void startSearchThread(final SourceTextEntry newEntry) {
        UIThreadsUtil.mustBeSwingThread();

        setText("");
        displayed = null;
        for (IMachineTranslation mt : translators) {
            new FindThread(mt, newEntry).start();
        }
    }

    @Override
    protected void setFoundResult(final SourceTextEntry se, final MachineTranslationInfo data) {
        UIThreadsUtil.mustBeSwingThread();

        if (data != null && StringUtil.notEmpty(data.result)) {
            if (displayed == null) {
                displayed = data.result;
            }
            setText(getText() + data.result + "\n<" + data.translatorName + ">\n\n");
        }
    }

    protected class FindThread extends EntryInfoSearchThread<MachineTranslationInfo> {
        private final IMachineTranslation translator;
        private final String src;

        public FindThread(final IMachineTranslation translator, final SourceTextEntry newEntry) {
            super(MachineTranslateTextArea.this, newEntry);
            this.translator = translator;
            src = newEntry.getSrcText();
        }

        @Override
        protected MachineTranslationInfo search() throws Exception {
            Language source=null;
            Language target=null;
            ProjectProperties pp = Core.getProject().getProjectProperties();
            if (pp != null){
                 source = pp.getSourceLanguage();
                 target = pp.getTargetLanguage();
             }
            if (source == null || target == null) {
                return null;
            }

            String translation = translator.getTranslation(source, target, src);
            if (StringUtil.isEmpty(translation)) {
                return null;
            }
            MachineTranslationInfo info = new MachineTranslationInfo();
            info.translatorName = translator.getName();
            info.result = StringUtil.normalizeUnicode(translation);
            return info;
        }
    }
}
