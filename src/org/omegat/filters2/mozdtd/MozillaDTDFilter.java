/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2010 Alex Buloichik, Didier Briel
               2011-2012 Didier Briel
               2014 Enrique Est�vez Fern�ndez
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

package org.omegat.filters2.mozdtd;

import org.omegat.filters2.AbstractAlignmentFilter;
import org.omegat.filters2.FilterContext;
import org.omegat.filters2.Instance;
import org.omegat.filters2.TranslationException;
import org.omegat.util.Log;
import org.omegat.util.OConsts;
import org.omegat.util.OStrings;

import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter for support Mozilla DTD files.
 * 
 * Format described on
 * http://msdn.microsoft.com/en-us/library/aa380599(VS.85).aspx
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Didier Briel
 *
 * Option to remove untranslated segments in the target files
 * Code adapted from the file: PoFilter.java
 *
 * @author Enrique Est�vez (keko.gl@gmail.com)
 */
public class MozillaDTDFilter extends AbstractAlignmentFilter {

    public static final String OPTION_REMOVE_STRINGS_UNTRANSLATED = "unremoveStringsUntranslated";

    protected static Pattern RE_ENTITY =  Pattern.compile("<\\!ENTITY\\s+(\\S+)\\s+([\"'])(.+)\\2\\s*>", Pattern.DOTALL);
    
    /**
     * If true, will remove non-translated segments in the target files
     */
    public static boolean removeStringsUntranslated = false;


    @Override
    public Instance[] getDefaultInstances() {
        return new Instance[] { new Instance("*.dtd") };
    }

    @Override
    public String getFileFormatName() {
        return OStrings.getString("MOZDTD_FILTER_NAME");
    }

    @Override
    public boolean isSourceEncodingVariable() {
        return false;
    }

    @Override
    public boolean isTargetEncodingVariable() {
        return false;
    }

    @Override
    protected BufferedReader createReader(File inFile, String inEncoding)
            throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(inFile), OConsts.UTF8));
    }

    @Override
    protected BufferedWriter createWriter(File outFile, String outEncoding)
            throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), OConsts.UTF8));
    }

    @Override
    protected void processFile(BufferedReader inFile, BufferedWriter outFile, FilterContext fc) throws IOException,
            TranslationException {

        String removeStringsUntranslatedStr = processOptions.get(OPTION_REMOVE_STRINGS_UNTRANSLATED);
        // If the value is null the default is false
        if ((removeStringsUntranslatedStr != null) && (removeStringsUntranslatedStr.equalsIgnoreCase("true"))) {
            removeStringsUntranslated = true;
        } else {
            removeStringsUntranslated = false;
        }

        StringBuilder block = new StringBuilder();
        boolean isInBlock = false;
        boolean foundQuotes = false;
        int quotes = '"';
        int previousChar = 0;
        int c;
        while ((c = inFile.read()) != -1) {
            if (c == '<' && !isInBlock) {
                isInBlock = true;
            }
            if (isInBlock) {
                block.append((char) c);
            } else {
                outFile.write(c);
            }
            // Find out the character used as begin and end of the segment
            if (!foundQuotes && (c == '"' || c == 39)) { // 39 is single quote
                foundQuotes = true;
                quotes = c;
            }
            if (c == '>' && isInBlock && previousChar == quotes) {
                isInBlock = false;
                foundQuotes = false;
                processBlock(block.toString(), outFile);
                block.setLength(0);
            } else if ((c == '>' && isInBlock && previousChar == '-')) { // This was a comment
                isInBlock = false;
                outFile.write(block.toString());
                block.setLength(0);
            }
            if (!Character.isWhitespace(c) ) { // In the regexp, there could be whitespace between " and >
                previousChar = c;
            }
        }
    }

    protected void processBlock(String block, BufferedWriter out) throws IOException {
        Matcher m = RE_ENTITY.matcher(block);
        if (!m.matches()) {
            // not ENTITY declaration
            out.write(block);
            return;
        }
        String id = m.group(1);
        String text = m.group(3);
        if (entryParseCallback != null) {
            entryParseCallback.addEntry(id, text, null, false, null, null, this, null);
        } else if (entryTranslateCallback != null) {
            // replace translation
            String trans = entryTranslateCallback.getTranslation(id, text, null);
            if (trans != null || removeStringsUntranslated == false) {
                out.write(block.substring(0, m.start(2)));
                out.write(trans != null ? trans : text);
                out.write(block.substring(m.end(3)));
            }    
        } else if (entryAlignCallback != null && id != null) {
            align.put(id, text);
        }
    }

    @Override
    public String getInEncodingLastParsedFile() {
        return OConsts.UTF8;
    }


    @Override
    public Map<String, String> changeOptions(Dialog parent, Map<String, String> config) {
        try {
            MozillaDTDOptionsDialog dialog = new MozillaDTDOptionsDialog(parent, config);
            dialog.setVisible(true);
            if (MozillaDTDOptionsDialog.RET_OK == dialog.getReturnStatus())
                return dialog.getOptions();
            else
                return null;
        } catch (Exception e) {
            Log.log(OStrings.getString("MOZDTD_FILTER_EXCEPTION"));
            Log.log(e);
            return null;
        }
    }

    /**
     * Returns true to indicate that Mozilla DTD filter has options.
     * 
     */
    @Override
    public boolean hasOptions() {
        return true;
    }

}
