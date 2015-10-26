package org.omegat.gui.dictionaries;

import org.omegat.core.dictionaries.DictionaryEntry;

import java.util.List;

/**
 * Created by ������ on 19.04.2015.
 */
public interface SearchByKeyProvider {

    List<String> findByKey(String key);

    List<DictionaryEntry> findWord(String str);
}
