/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2009 Alex Buloichik
               2011 Didier Briel
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

package org.omegat.core.dictionaries;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.omegat.gui.dictionaries.DictionariesTextArea;
import org.omegat.util.DirectoryMonitor;
import org.omegat.util.FileUtil;
import org.omegat.util.Log;
import org.omegat.util.OConsts;

/**
 * Class for load dictionaries.
 * 
 * @author Alex Buloichik <alex73mail@gmail.com>
 * @author Didier Briel
 */
public class DictionariesManager extends BaseDictionariesManager implements DirectoryMonitor.Callback {
    private final DictionariesTextArea pane;

    public DictionariesManager(final DictionariesTextArea pane) {
        super();
        this.pane = pane;
    }

    public void start(final String dictDir) {
        File dir = new File(dictDir);
        monitor = new DirectoryMonitor(dir, this);
        monitor.start();
    }

    public void stop() {
        monitor.fin();
        synchronized (this) {
            infos.clear();
        }
    }

    /**
     * Executed on file changed.
     */
    public void fileChanged(File file) {
        String fn = file.getPath();
        synchronized (this) {
            infos.remove(fn);
        }
        if (file.exists()) {
            try {
                long st = System.currentTimeMillis();

                if (file.getName().equals("ignore.txt")) {
                    loadIgnoreWords(file);
                } else if (fn.endsWith(".ifo")) {
                    IDictionary dict = new StarDict(file);
                    Map<String, Object> header = dict.readHeader();
                    synchronized (this) {
                        infos.put(fn, new DictionaryInfo(dict, header));
                    }
                } else if (fn.endsWith(".dsl")) {
                    IDictionary dict = new LingvoDSL(file);
                    Map<String, Object> header = dict.readHeader();
                    synchronized (this) {
                        infos.put(fn, new DictionaryInfo(dict, header));
                    }
                } else {
                    fn = null;
                }

                if (fn != null) {
                    long en = System.currentTimeMillis();
                    Log.log("Loaded dictionary from '" + fn + "': " + (en - st) + "ms");
                }
            } catch (Exception ex) {
                Log.log("Error load dictionary from '" + fn + "': " + ex.getMessage());
            }
        }
        pane.refresh();
    }

    /**
     * Load ignored words from 'ignore.txt' file.
     */
    protected void loadIgnoreWords(final File f) throws IOException {
        //todo use utils to read file
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(new FileInputStream(f), OConsts.UTF8))) {
            synchronized (ignoreWords) {
                ignoreWords.clear();
                String line;
                while ((line = rd.readLine()) != null) {
                    ignoreWords.add(line.trim());
                }
            }
        }
    }

    /**
     * Add new ignore word.
     */
    public void addIgnoreWord(final String word) {
        try {
            File outFile = new File(monitor.getDir(), "ignore.txt");
            File outFileTmp = new File(monitor.getDir(), "ignore.txt.new");
            try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileTmp),
                    OConsts.UTF8))) {
                synchronized (ignoreWords) {
                    ignoreWords.add(word);
                    for (String w : ignoreWords) {
                        wr.write(w + System.getProperty("line.separator"));
                    }
                }
                wr.flush();
            }
            outFile.delete();
            FileUtil.rename(outFileTmp, outFile);
        } catch (Exception ex) {
            Log.log("Error save ignore words:" + ex.getMessage());
        }
    }

}
