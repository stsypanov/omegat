/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2010 Alex Buloichik
               2012 Thomas CORDONNIER
               2013 Aaron Madlon-Kay
               2014 Alex Buloichik
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
package org.omegat.core.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.omegat.core.Core;
import org.omegat.util.FileUtil;
import org.omegat.util.StringUtil;
import org.omegat.util.TMXReader2;

/**
 * Class for store data from TMX from /tm/ folder. They are used only for fuzzy matches.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Thomas CORDONNIER
 * @author Aaron Madlon-Kay
 */
public class ExternalTMX {

    private final String name;

    private final List<PrepareTMXEntry> entries;

    public ExternalTMX(final ProjectProperties props, final File file, final boolean extTmxLevel2,
            final boolean useSlash) throws Exception {
        this.name = file.getName();
        entries = new ArrayList<>();

        TMXReader2.LoadCallback loader = new TMXReader2.LoadCallback() {
            public boolean onEntry(TMXReader2.ParsedTu tu, TMXReader2.ParsedTuv tuvSource,
                    TMXReader2.ParsedTuv tuvTarget, boolean isParagraphSegtype) {
                if (tuvSource == null) {
                    return false;
                }

                if (tuvTarget != null) {
                    // add only target Tuv
                    addTuv(tu, tuvSource, tuvTarget, isParagraphSegtype);
                } else {
                    // add all non-source Tuv
                    for (int i = 0; i < tu.tuvs.size(); i++) {
                        if (tu.tuvs.get(i) != tuvSource) {
                            addTuv(tu, tuvSource, tu.tuvs.get(i), isParagraphSegtype);
                        }
                    }
                }
                return true;
            }

            private void addTuv(TMXReader2.ParsedTu tu, TMXReader2.ParsedTuv tuvSource,
                    TMXReader2.ParsedTuv tuvTarget, boolean isParagraphSegtype) {
                String changer = StringUtil.nvl(tuvTarget.changeid, tuvTarget.creationid, tu.changeid,
                        tu.creationid);
                String creator = StringUtil.nvl(tuvTarget.creationid, tu.creationid);
                long changed = StringUtil.nvlLong(tuvTarget.changedate, tuvTarget.creationdate, tu.changedate,
                        tu.creationdate);
                long created = StringUtil.nvlLong(tuvTarget.creationdate, tu.creationdate);

                List<String> sources = new ArrayList<String>();
                List<String> targets = new ArrayList<String>();
                Core.getSegmenter().segmentEntries(props.isSentenceSegmentingEnabled() && isParagraphSegtype,
                        props.getSourceLanguage(), tuvSource.text, props.getTargetLanguage(), tuvTarget.text,
                        sources, targets);

                for (int i = 0; i < sources.size(); i++) {
                    PrepareTMXEntry te = new PrepareTMXEntry();
                    te.source = sources.get(i);
                    te.translation = targets.get(i);
                    te.changer = changer;
                    te.changeDate = changed;
                    te.creator = creator;
                    te.creationDate = created;
                    te.note = tu.note;
                    te.otherProperties = tu.props;
                    entries.add(te);
                }
            }
        };

        TMXReader2 reader = new TMXReader2();
        reader.readTMX(file, props.getSourceLanguage(), props.getTargetLanguage(),
                props.isSentenceSegmentingEnabled(), false, extTmxLevel2, useSlash, loader);
    }

    public String getName() {
        return name;
    }

    public List<PrepareTMXEntry> getEntries() {
        return entries;
    }

    /**
     * Check if TMX is in specified path.
     */
    public static boolean isInPath(File path, File tmxFile) {
        try {
            FileUtil.computeRelativePath(path, tmxFile);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
