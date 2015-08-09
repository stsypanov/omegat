package org.omegat.gui.dictionaries;

import org.omegat.core.dictionaries.BaseDictionariesManager;
import org.omegat.core.dictionaries.DictionaryEntry;
import org.omegat.gui.editor.autocompleter.DictionaryAutoCompleterView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

/**
 * Created by stsypanov on 19.04.2015.
 */
public class DictionaryPopupController {
    private DictionaryAutoCompleterView popup;
    private DictionaryPopupModel popupModel;
    private DictionariesTextArea dictionariesTextArea;
    private BaseDictionariesManager dictionariesManager;

    public DictionaryPopupController(DictionaryAutoCompleterView popup) {
        this.popup = popup;
        init();
    }

    public DictionaryPopupController(DictionaryAutoCompleterView dictionaryPopup, DictionariesTextArea dictionariesTextArea) {
        this(dictionaryPopup);
        this.dictionariesTextArea = dictionariesTextArea;
    }

    private void init() {
        dictionariesManager = new BaseDictionariesManager();
        popupModel = new DictionaryPopupModel(dictionariesManager);
        popup.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                processKeyEvent(e);
            }
        });
        popup.setCallback(new Callback<String>() {
            @Override
            public void execute(String str) {
                List<DictionaryEntry> dictionaryEntries = dictionariesManager.findWord(str);
                dictionariesTextArea.setFoundResult(dictionaryEntries);
            }
        });
    }

    private void processKeyEvent(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            popupModel.clear();
            popup.hide();
        } else {
            updatePopup();
        }
    }

    public void updatePopup() {
        List<String> byKey = popupModel.findByKey(popup.getText());
        if (!byKey.isEmpty()){
            popup.setModel(byKey);
            popup.redraw();
        } else {
            popup.setModel(Collections.<String>emptyList());
            popup.redraw();
        }
    }
}
