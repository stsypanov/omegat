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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.omegat.gui.dictionaries.IDictionaries;
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
    private static final String IGNORE_FILE = "ignore.txt";
    private final IDictionaries pane;

    public DictionariesManager(final IDictionaries pane) {
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
        infos.clear();
    }

    /**
     * Executed if file is changed.
     */
    public synchronized void fileChanged(File file) {
        String fn = file.getPath();
        infos.remove(fn);
        if (file.exists()) {
            try {
                long st = System.currentTimeMillis();

                if (file.getName().equals(IGNORE_FILE)) {
                    loadIgnoredWords(file);
                } else if (fn.endsWith(".ifo")) {
                    IDictionary dict = new StarDict(file);
                    Map<String, Object> header = dict.readHeader();
                    infos.put(fn, new DictionaryInfo(dict, header));
                } else if (fn.endsWith(".dsl")) {
                    IDictionary dict = new LingvoDSL(file);
                    Map<String, Object> header = dict.readHeader();
                    infos.put(fn, new DictionaryInfo(dict, header));
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
    protected void loadIgnoredWords(final File f) throws IOException {
        //todo use utils to read file
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(new FileInputStream(f), OConsts.UTF8))) {
            synchronized (ignoredWords) {
                ignoredWords.clear();
                String line;
                while ((line = rd.readLine()) != null) {
                    ignoredWords.add(line.trim());
                }
            }
        }
    }

    /**
     * Add new ignored word.
     */
    public void addIgnoredWord(final String word) {
        ignoredWords.add(word);
        saveIgnoredWords(ignoredWords);
    }

    private synchronized void saveIgnoreWords(Collection<String> words) {
        if (monitor == null) {
            Log.log("Could not save ignored words because no dictionary dir has been set.");
            return;
        }
        try {
            File outFile = new File(monitor.getDir(), IGNORE_FILE);
            File outFileTmp = new File(monitor.getDir(), IGNORE_FILE + ".new");
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileTmp),
                    OConsts.UTF8));
            try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileTmp), OConsts.UTF8))) {
                ignoredWords.add(word);
                for (String w : ignoredWords) {
                    wr.write(w + System.getProperty("line.separator"));
                }
                wr.flush();
            }
            outFile.delete();
            FileUtil.rename(outFileTmp, outFile);
        } catch (Exception ex) {
            Log.log("Error saving ignored words: " + ex.getMessage());
        }
    }

    /**
     * Find words list in all dictionaries.
     * 
     * @param words
     *            words list
     * @return articles list
     */
    public List<DictionaryEntry> findWords(Collection<String> words) {
        List<DictionaryInfo> dicts;
        synchronized (this) {
            dicts = new ArrayList<DictionaryInfo>(infos.values());
        }
        List<DictionaryEntry> result = new ArrayList<DictionaryEntry>();
        for (String word : words) {
            for (DictionaryInfo di : dicts) {
                try {
                    synchronized (ignoredWords) {
                        if (ignoredWords.contains(word)) {
                            continue;
                        }
                    }
                    Object data = di.info.get(word);
                    if (data == null) {
                        String lowerCaseWord = word.toLowerCase();
                        synchronized (ignoredWords) {
                            if (ignoredWords.contains(lowerCaseWord)) {
                                continue;
                            }
                        }
                        data = di.info.get(lowerCaseWord);
                    }
                    if (data != null) {
                        if (data.getClass().isArray()) {
                            for (Object d : (Object[]) data) {
                                String a = di.dict.readArticle(word, d);
                                result.add(new DictionaryEntry(word, a));
                            }
                        } else {
                            String a = di.dict.readArticle(word, data);
                            result.add(new DictionaryEntry(word, a));
                        }
                    }
                } catch (Exception ex) {
                    Log.log(ex);
                }
            }
        }
        return result;
    }
}
