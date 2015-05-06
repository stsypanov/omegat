package org.omegat.gui.dictionaries;

import org.omegat.core.dictionaries.BaseDictionariesManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Сергей on 19.04.2015.
 */
public class DictionaryPopupModel implements SearchByKeyProvider {

    private List<String> keys;
    private BaseDictionariesManager dictionariesManager;

    public DictionaryPopupModel(BaseDictionariesManager dictionariesManager) {
        this.dictionariesManager = dictionariesManager;
        keys = new ArrayList<>();
    }

    public List<String> getKeys() {
        return keys;
    }

    public void clear() {
        keys.clear();
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
