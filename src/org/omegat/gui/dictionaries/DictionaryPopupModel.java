package org.omegat.gui.dictionaries;

import org.omegat.core.dictionaries.DictionariesManager;
import org.omegat.core.dictionaries.DictionaryEntry;

import java.util.*;

public class DictionaryPopupModel implements SearchByKeyProvider {

    private DictionariesManager dictionariesManager;

    public DictionaryPopupModel(DictionariesManager dictionariesManager) {
        this.dictionariesManager = dictionariesManager;
    }

    @Override
    public List<DictionaryEntry> findWord(String str) {
        return dictionariesManager.findWords(Collections.singletonList(str));
    }

    @Override
    public List<String> findByKey(String key) {
       if (key.length() >= 2){
           Set<String> keys = dictionariesManager.getKeys(key);
           return new ArrayList<>(keys);
       } else {
           return Collections.emptyList();
       }
    }
}
