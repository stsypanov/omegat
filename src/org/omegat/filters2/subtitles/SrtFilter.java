/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2008 Alex Buloichik
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

package org.omegat.filters2.subtitles;

import org.omegat.filters2.AbstractAlignmentFilter;
import org.omegat.filters2.FilterContext;
import org.omegat.filters2.Instance;
import org.omegat.filters2.TranslationException;
import org.omegat.util.OStrings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Filter for subtitles files. Format described on
 * http://en.wikipedia.org/wiki/SubRip.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class SrtFilter extends AbstractAlignmentFilter {
    protected static final Pattern PATTERN_TIME_INTERVAL = Pattern
            .compile("([0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3})\\s+-->\\s+([0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3})");
    protected static final String EOL = "\r\n";
    private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\n", Pattern.LITERAL);

    enum READ_STATE {
        WAIT_TIME, WAIT_TEXT
    }

    protected String key;
    protected StringBuilder text = new StringBuilder();
    protected BufferedWriter out;

    @Override
    public Instance[] getDefaultInstances() {
        return new Instance[] { new Instance("*.srt") };
    }

    @Override
    public String getFileFormatName() {
        return OStrings.getString("SRTFILTER_FILTER_NAME");
    }

    @Override
    public boolean isSourceEncodingVariable() {
        return true;
    }

    @Override
    public boolean isTargetEncodingVariable() {
        return true;
    }

    @Override
    protected void processFile(BufferedReader inFile, BufferedWriter outFile, FilterContext fc) throws IOException,
            TranslationException {
        out = outFile;
        READ_STATE state = READ_STATE.WAIT_TIME;
        key = null;
        text.setLength(0);

        String s;
        while ((s = inFile.readLine()) != null) {
            switch (state) {
            case WAIT_TIME:
                if (PATTERN_TIME_INTERVAL.matcher(s).matches()) {
                    state = READ_STATE.WAIT_TEXT;
                }
                key = s;
                text.setLength(0);
                outFile.write(s);
                outFile.write(EOL);
                break;
            case WAIT_TEXT:
                if (s.trim().length() == 0) {
                    flush();
                    outFile.write(EOL);
                    state = READ_STATE.WAIT_TIME;
                }
                if (text.length() > 0) {
                    text.append('\n');
                }
                text.append(s);
                break;
            }
        }
        flush();
    }

    private void flush() throws IOException {
        if (text.length() == 0) {
            return;
        }

        if (align != null) {
            align.put(key, text.toString());
        }
        
        if (entryParseCallback != null) {
            entryParseCallback.addEntry(key, text.toString(), null, false, null, null, this, null);
        } else {
            String tr = entryTranslateCallback.getTranslation(key, text.toString(), null);
            if (tr == null) {
                tr = text.toString();
            }
            out.write(NEW_LINE_PATTERN.matcher(tr).replaceAll(EOL));
            out.write(EOL);
        }
        
        key = null;
        text.setLength(0);
    }

}
