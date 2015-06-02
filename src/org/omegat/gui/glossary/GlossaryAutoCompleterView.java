/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2013 Zoltan Bartko, Aaron Madlon-Kay
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

package org.omegat.gui.glossary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.omegat.core.Core;
import org.omegat.gui.editor.autocompleter.AutoCompleterItem;
import org.omegat.gui.editor.autocompleter.AutoCompleterListView;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;

/**
 * The glossary auto-completer view.
 * 
 * @author Zoltan Bartko <bartkozoltan@bartkozoltan.com>
 * @author Aaron Madlon-Kay
 */
public class GlossaryAutoCompleterView extends AutoCompleterListView {

    public GlossaryAutoCompleterView() {
        super(OStrings.getString("AC_GLOSSARY_VIEW"));
    }

    @Override
    public List<AutoCompleterItem> computeListData(String prevText) {
        String wordChunk = getLastToken(prevText);
        
        List<AutoCompleterItem> result = new ArrayList<AutoCompleterItem>();
        boolean capitalize = (wordChunk.length() > 0) ?
                (Preferences.isPreference(Preferences.AC_GLOSSARY_CAPITALIZE))
                    && Character.isUpperCase(wordChunk.charAt(0)) 
                : false;
        
        List<GlossaryEntry> entries = Core.getGlossary().getDisplayedEntries();
        for (GlossaryEntry entry : entries) {
            for (String s : entry.getLocTerms(true)) {
                if (s.toLowerCase().startsWith(wordChunk.toLowerCase())) {
                   if (capitalize) {
                        s = s.substring(0,1).toUpperCase() + s.substring(1);
                    }
                    result.add(new AutoCompleterItem(s, new String[] { entry.getSrcText() }, wordChunk.length()));
                }
            }
        }
        
        if (!Core.getProject().getProjectProperties().getTargetLanguage().isSpaceDelimited()
                && result.size() == 0) {
            for (GlossaryEntry entry : entries) {
                for (String s : entry.getLocTerms(true)) {
                    result.add(new AutoCompleterItem(s, new String[] { entry.getSrcText() }, 0));
                }
            }
        }
        
        Collections.sort(result, new GlossaryComparator(entries));
        
        return result;
    }

    @Override
    public String itemToString(AutoCompleterItem item) {
        if (Preferences.isPreference(Preferences.AC_GLOSSARY_SHOW_SOURCE) && item.extras != null) {
            if (Preferences.isPreference(Preferences.AC_GLOSSARY_SHOW_TARGET_BEFORE_SOURCE)) {
                return item.payload + " \u2190 " + item.extras[0];
            } else {
                return item.extras[0] + " \u2192 " + item.payload;
            }
        } else {
            return item.payload;
        }
    }

    static class GlossaryComparator implements Comparator<AutoCompleterItem> {
        
        private boolean bySource = Preferences.isPreference(Preferences.AC_GLOSSARY_SORT_BY_SOURCE);
        private boolean byLength = Preferences.isPreference(Preferences.AC_GLOSSARY_SORT_BY_LENGTH);
        private boolean alphabetically = Preferences.isPreference(Preferences.AC_GLOSSARY_SORT_ALPHABETICALLY);
        
        private List<GlossaryEntry> entries;
        
        public GlossaryComparator(List<GlossaryEntry> entries) {
            this.entries = entries;
        }
        
        @Override
        public int compare(AutoCompleterItem o1, AutoCompleterItem o2) {
            
            // Sort alphabetically by source term
            if (bySource) {
                int result = o1.extras[0].compareTo(o2.extras[0]);
                if (result != 0) {
                    return result;
                }
            }
            
            // Sorting for same source with multiple targets
            if (o1.extras[0].equals(o2.extras[0])) {
                if (byLength) {
                    if (o1.payload.length() < o2.payload.length()) {
                        return 1;
                    } else if (o1.payload.length() > o2.payload.length()) {
                        return -1;
                    }
                }
                if (alphabetically) {
                    return o1.payload.compareTo(o2.payload);
                }
            }
            
            // If we make it here, we should ensure the sorting is the same
            // as in the original list of entries.
            int i1 = -1;
            int i2 = -1;
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getSrcText().equals(o1.extras[0])) {
                    i1 = i;
                }
                if (entries.get(i).getSrcText().equals(o2.extras[0])) {
                    i2 = i;
                }
                if (i1 != -1 && i2 != -1) {
                    break;
                }
            }
            if (i1 < i2) {
                return -1;
            } else if (i1 > i2) {
                return 1;
            }
            return 0;
        }
        
    }
}
