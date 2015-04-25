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
    private BaseDictionariesManager manager;

    public DictionaryPopupModel() {
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
        if (manager == null){
            manager = new BaseDictionariesManager();
        }
       if (key.length() >= 2){
           Set<String> keys = manager.getKeys(key);
           return new ArrayList<>(keys);
       } else {
           return Collections.emptyList();
       }
    }
}
