/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2014 Briac Pilpre, Didier Briel
               2015 Yu Tang, Aaron Madlon-Kay
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
package org.omegat.gui.scripting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.omegat.util.LinebreakPreservingReader;
import org.omegat.util.OConsts;
import org.jetbrains.annotations.NotNull;
import org.omegat.util.*;

/**
 * A script file in the script list is represented as ScriptListFile to allow
 * for localization, description and reordering.
 * 
 * @author Briac Pilpre
 * @author Didier Briel
 * @author Aaron Madlon-Kay
 */
public class ScriptItem implements Comparable<ScriptItem> {

    private static final String PROPERTIES = "properties/";
    private static final Pattern PATTERN = Pattern.compile("\n");

    public ScriptItem(File scriptFile) {
        m_file = scriptFile;
        try {
            ClassLoader loader = new URLClassLoader(new URL[]{scriptFile.getParentFile().toURI().toURL()});
            String shortName = FilenameUtils.removeExtension(scriptFile.getName());
            try { // Try first at the root of the script dir, for compatibility
                m_res = ResourceBundle.getBundle(shortName, Locale.getDefault(), loader); 
            } catch (MissingResourceException e) {
                try { // Then inside the /properties dir
                    m_res = ResourceBundle.getBundle(PROPERTIES  + shortName, Locale.getDefault(), loader);
                } catch (MissingResourceException ex) {
                    scanFileForDescription(scriptFile);
                }
            }
        } catch (IOException e) {
            Log.log(e);
        }
    }

    private void scanFileForDescription(File file) {

        try (Scanner scan = new Scanner(file)) {

            scan.findInLine(":name\\s*=\\s*(.*)\\s+:description\\s*=\\s*(.*)");
            MatchResult results = scan.match();
            m_scriptName = results.group(1).trim();
            m_description = results.group(2).trim();
        } catch (IllegalStateException | FileNotFoundException e) {
            Log.severe("No match result available for file " + file.getAbsolutePath(), e);
        }
    }

    public ResourceBundle getResourceBundle() {
        if (m_res != null) {
            return m_res;
        }

        // Create empty resource for confirmation
        return new ResourceBundle() {
            private static final String MISSING_BUNDLE_MESSAGE = "ResourceBundle (.properties file for localization) is missing.";

            @Override
            protected Object handleGetObject(@NotNull String key) {
                throw new MissingResourceException(MISSING_BUNDLE_MESSAGE, null, key);
            }

            @NotNull
            @Override
            public Enumeration<String> getKeys() {
                throw new MissingResourceException(MISSING_BUNDLE_MESSAGE, null, null);
            }
        };
    }

    public String getScriptName() {
        if (m_scriptName == null) {
            String name = m_file.getName();
            if (m_res != null) {
                try {
                    name = m_res.getString("name");
                } catch (MissingResourceException ignore) {
                }
            }
            m_scriptName = name;
        }
        return m_scriptName;
    }

    public File getFile() {
        return m_file;
    }

    public String getDescription() {
        if (m_description != null) {
            return m_description;
        }

        try {
            m_description = m_res == null ? "" : m_res.getString("description");
        } catch (MissingResourceException e) {
            m_description = "";
        }

        return m_description;
    }

    public String getToolTip() {
        String name = getScriptName();
        String description = getDescription();
        return description != null && description.isEmpty() ? name : name + " - " + description;
    }

    public String getText() throws IOException {
        String ret;

        try (LinebreakPreservingReader lpin = getUTF8LinebreakPreservingReader(m_file)) {
            StringBuilder sb = new StringBuilder();
            String s = lpin.readLine();
            startsWithBOM = s.startsWith(BOM);
            if (startsWithBOM) {
                s = s.substring(1);  // eat BOM
            }
            while (s != null) {
                sb.append(s);
                String br = lpin.getLinebreak();
                if (!br.isEmpty()) {
                    lineBreak = br;
                    sb.append('\n');
                }
                s = lpin.readLine();
            }
            ret = sb.toString();
        }
        return ret;
    }

    private LinebreakPreservingReader getUTF8LinebreakPreservingReader(File file) throws FileNotFoundException, UnsupportedEncodingException {
        InputStream is = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(is, OConsts.UTF8);
        BufferedReader in = new BufferedReader(isr);
        return new LinebreakPreservingReader(in);
    }

    public void setText(String text) throws IOException {
        text = PATTERN.matcher(text).replaceAll(lineBreak);
        if (startsWithBOM) {
            text = BOM + text;
        }

        FileUtils.writeStringToFile(m_file, text, OConsts.UTF8);
    }

    @Override
    public String toString() {
        return getScriptName();
    }

    @Override
    public int compareTo(ScriptItem o) {
        return getScriptName().compareTo(o.getScriptName());
    }

    private static final String BOM = "\uFEFF";
    private boolean startsWithBOM = false;
    private String lineBreak = System.getProperty("line.separator");

    private File m_file = null;
    private String m_scriptName = null;
    private String m_description = null;
    private ResourceBundle m_res = null;
}
