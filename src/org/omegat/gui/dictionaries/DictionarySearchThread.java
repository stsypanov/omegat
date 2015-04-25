package org.omegat.gui.dictionaries;

import org.omegat.core.dictionaries.BaseDictionariesManager;

/**
 * Created by Сергей on 25.04.2015.
 */
public class DictionarySearchThread implements Runnable {
    protected BaseDictionariesManager dictionariesManager;

    public DictionarySearchThread(BaseDictionariesManager dictionariesManager) {
        this.dictionariesManager = dictionariesManager;
    }

    @Override
    public void run() {
//        dictionariesManager.findWord()
    }
}
