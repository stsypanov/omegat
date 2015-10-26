package org.omegat.gui.popup;

import org.omegat.core.dictionaries.DictionaryEntry;
import org.omegat.gui.dictionaries.SearchByKeyProvider;

import java.util.*;

/**
 * Created by stsypanov on 22.10.2015.
 */
public class MockDictionaryPopupModel implements SearchByKeyProvider {
	protected Set<String> loadedKeys;

	@Override
	public List<String> findByKey(String key) {
		if (key.length() >= 2){
			Set<String> keys = getKeys(key);
			return new ArrayList<>(keys);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<DictionaryEntry> findWord(String str) {
		return null;
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
		loadedKeys = new HashSet<>(
				Arrays.asList(
						"kova", "koska", "koirva", "kivi", "kilo", "kana", "kala", "kissa",
						"kaura", "kamala", "kasakka", "kossu", "kokis", "kiva", "kukka"
				));
	}
}
