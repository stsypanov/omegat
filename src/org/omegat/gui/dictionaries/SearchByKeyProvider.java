package org.omegat.gui.dictionaries;

import org.omegat.core.dictionaries.DictionaryEntry;

import java.util.List;

public interface SearchByKeyProvider {

    List<String> findByKey(String key);

    List<DictionaryEntry> findWord(String str);
}
