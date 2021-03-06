/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2009 Alex Buloichik
               2011 Didier Briel
               2015 Aaron Madlon-Kay
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

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import org.omegat.core.Core;
import org.omegat.gui.dictionaries.IDictionaries;
import org.omegat.util.DirectoryMonitor;
import org.omegat.util.FileUtil;
import org.omegat.util.Log;

/**
 * Class for load dictionaries.
 *
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Didier Briel
 * @author Aaron Madlon-Kay
 */
public class DictionariesManager implements DirectoryMonitor.Callback {
    public static final String IGNORE_FILE = "ignore.txt";
    public static final String DICTIONARY_SUBDIR = "dictionary";

    private final IDictionaries pane;
    protected DirectoryMonitor monitor;
    protected final List<IDictionaryFactory> factories = new ArrayList<IDictionaryFactory>();
    protected final Map<String, IDictionary> dictionaries = new TreeMap<String, IDictionary>();
    protected final Set<String> ignoreWords = new TreeSet<String>();
    protected Set<String> loadedKeys;

    public DictionariesManager(final IDictionaries pane) {
        this.pane = pane;
        factories.add(new LingvoDSL());
        factories.add(new StarDict());
    }

    public void addDictionaryFactory(IDictionaryFactory dict) {
        synchronized (factories) {
            factories.add(dict);
        }
        if (monitor != null) {
            monitor.fin();
            start(monitor.getDir());
        }
    }

    public void removeDictionaryFactory(IDictionaryFactory factory) {
        synchronized (factories) {
            factories.remove(factory);
        }
    }

    public void start(File dictDir) {
        monitor = new DirectoryMonitor(dictDir, this);
        monitor.start();
    }

    public void stop() {
        monitor.fin();
        synchronized (this) {
            dictionaries.clear();
        }
    }

    /**
     * Executed on file changed.
     */
    public void fileChanged(File file) {
        synchronized (dictionaries) {
            dictionaries.remove(file.getPath());
        }
        if (!file.exists()) {
            return;
        }
        try {
            long st = System.currentTimeMillis();
            if (file.getName().equals(IGNORE_FILE)) {
                loadIgnoreWords(file);
            } else if (loadDictionary(file)) {
                long en = System.currentTimeMillis();
                Log.log("Loaded dictionary from '" + file.getPath() + "': " + (en - st) + "ms");
            }
        } catch (Exception ex) {
            Log.log("Error load dictionary from '" + file.getPath() + "': " + ex.getMessage());
        }
        pane.refresh();
    }

    /**
     * Check all known dictionary factories to see if they support this file.
     * Will stop at the first supporting factory and attempt to load the
     * dictionary.
     *
     * @param file
     *            Dictionary file to be loaded
     * @return Whether or not the file was loaded
     * @throws Exception
     *             Even when a file appears to be supported, exceptions can
     *             still occur while loading.
     */
    private boolean loadDictionary(File file) throws Exception {
        if (!file.isFile()) {
            return false;
        }
        List<IDictionaryFactory> currFactories;
        synchronized (factories) {
            currFactories = new ArrayList<IDictionaryFactory>(factories);
        }
        for (IDictionaryFactory factory : currFactories) {
            if (factory.isSupportedFile(file)) {
                synchronized (this) {
                    dictionaries.put(file.getPath(), factory.loadDict(file));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Load ignored words from 'ignore.txt' file.
     */
    protected void loadIgnoreWords(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        synchronized (ignoreWords) {
            ignoreWords.clear();
            lines.stream().map(String::trim).forEach((line) -> ignoreWords.add(line));
        }
    }

    /**
     * Add new ignore word.
     */
    public void addIgnoredWord(final String word) {
        Collection<String> words = Collections.emptyList();
        synchronized (ignoreWords) {
            ignoreWords.add(word);
            words = new ArrayList<String>(ignoreWords);
        }
        if (monitor != null) {
            saveIgnoreWords(words, new File(monitor.getDir(), IGNORE_FILE));
        }
    }

    private static void saveIgnoreWords(Collection<String> words, File outFile) {
        try {
            File outFileTmp = new File(outFile.getPath() + ".new");
            Files.write(outFileTmp.toPath(), words);
            FileUtil.rename(outFileTmp, outFile);
        } catch (IOException ex) {
            Log.log("Error saving ignore words");
            Log.log(ex);
        }
    }

    private boolean isIgnoreWord(String word) {
        synchronized (ignoreWords) {
            return ignoreWords.contains(word);
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
        List<IDictionary> dicts;
        synchronized (this) {
            dicts = new ArrayList<IDictionary>(dictionaries.values());
        }
        List<DictionaryEntry> result = new ArrayList<DictionaryEntry>();
        for (String word : words) {
            if (isIgnoreWord(word)) {
                continue;
            }
            for (IDictionary di : dicts) {
                try {
                    List<DictionaryEntry> entries = di.readArticles(word);
                    if (entries.isEmpty()) {
                        Locale loc = Core.getProject().getProjectProperties().getSourceLanguage().getLocale();
                        String lowerCaseWord = word.toLowerCase(loc);
                        if (isIgnoreWord(lowerCaseWord)) {
                            continue;
                        }
                        entries = di.readArticles(lowerCaseWord);
                    }
                    result.addAll(entries);
                } catch (Exception ex) {
                    Log.log(ex);
                }
            }
        }
        return result;
    }

    public Set<String> getKeys(String key) {
        if (loadedKeys == null) {
            loadKeys();
        }
        Set<String> possibleKeys = new TreeSet<>();
        for (String loadedKey : loadedKeys) {
            if (loadedKey.startsWith(key)) {
                possibleKeys.add(loadedKey);
            }
        }
        return possibleKeys;
    }

    private void loadKeys() {
        loadedKeys = new HashSet<>();
        for (IDictionary dictionary : dictionaries.values()) {
            Set<String> strings = dictionary.getKeys();
            loadedKeys.addAll(strings);
        }
    }
}
