package org.omegat.core.dictionaries;

import org.omegat.util.DirectoryMonitor;
import org.omegat.util.Log;

import java.util.*;

/**
 * Created by stsypanov on 19.04.2015.
 */
public class BaseDictionariesManager {
    protected static final Map<String, DictionaryInfo> infos = new TreeMap<>();
    protected final Set<String> ignoreWords = new TreeSet<>();
    protected DirectoryMonitor monitor;
    protected List<DictionaryInfo> dictionaries;
    protected Set<String> loadedKeys;

    public BaseDictionariesManager(){
        synchronized (this) {
            dictionaries = new ArrayList<>(infos.values());
        }
    }

    /**
     * Find words list in all dictionaries.
     *
     * @param words
     *            words list
     * @return articles list
     */
    public List<DictionaryEntry> findWords(Set<String> words) {
        List<DictionaryEntry> result = new ArrayList<>();
        for (String word : words) {
            findWord(dictionaries, result, word);
        }
        return result;
    }

    public List<DictionaryEntry> findWord(String word){
        List<DictionaryEntry> result = new ArrayList<>();
        findWord(dictionaries, result, word);
        return result;
    }

    public Set<String> getKeys(String key){
        if (loadedKeys == null){
            loadKeys();
        }
        Set<String> possibleKeys = new TreeSet<>();
        for (String loadedKey : loadedKeys){
            if (loadedKey.startsWith(key)){
                possibleKeys.add(loadedKey);
            }
        }
        return possibleKeys;
    }

    private void loadKeys() {
        loadedKeys = new HashSet<>();
        for (DictionaryInfo dictionary : dictionaries){
            Set<String> strings = dictionary.info.keySet();
            loadedKeys.addAll(strings);
        }
    }

    protected void findWord(List<DictionaryInfo> dictionaries, List<DictionaryEntry> result, String word) {
        for (DictionaryInfo dictionary : dictionaries) {
            try {
                synchronized (ignoreWords) {
                    if (ignoreWords.contains(word)) {
                        continue;
                    }
                }
                Object data = dictionary.info.get(word);
                if (data == null) {
                    String lowerCaseWord = word.toLowerCase();
                    synchronized (ignoreWords) {
                        if (ignoreWords.contains(lowerCaseWord)) {
                            continue;
                        }
                    }
                    data = dictionary.info.get(lowerCaseWord);
                }
                if (data != null) {
                    if (data.getClass().isArray()) {
                        for (Object d : (Object[]) data) {
                            String a = dictionary.dict.readArticle(word, d);
                            result.add(new DictionaryEntry(word, a));
                        }
                    } else {
                        String a = dictionary.dict.readArticle(word, data);
                        result.add(new DictionaryEntry(word, a));
                    }
                }
            } catch (Exception ex) {
                Log.log(ex);
            }
        }
    }

    protected static class DictionaryInfo {
        public final IDictionary dict;
        public final Map<String, Object> info;

        public DictionaryInfo(final IDictionary dict, final Map<String, Object> info) {
            this.dict = dict;
            this.info = info;
        }
    }
}
