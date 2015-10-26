package org.omegat.gui.dictionaries;

import org.omegat.core.dictionaries.BaseDictionariesManager;
import org.omegat.core.dictionaries.DictionaryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DictionaryPopupModel implements SearchByKeyProvider {

    private BaseDictionariesManager dictionariesManager;

    public DictionaryPopupModel(BaseDictionariesManager dictionariesManager) {
        this.dictionariesManager = dictionariesManager;
    }

    @Override
    public List<DictionaryEntry> findWord(String str) {
        return dictionariesManager.findWord(str);
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
