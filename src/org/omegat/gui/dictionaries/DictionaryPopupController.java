package org.omegat.gui.dictionaries;

import org.omegat.core.dictionaries.BaseDictionariesManager;
import org.omegat.core.dictionaries.DictionaryEntry;
import org.omegat.gui.popup.dictionary.DictionaryPopup;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

/**
 * Created by stsypanov on 19.04.2015.
 */
public class DictionaryPopupController {
    protected DictionaryPopup popup;
    protected SearchByKeyProvider popupModel;
    protected DictionariesTextArea dictionariesTextArea;

    private DictionaryPopupController(DictionaryPopup popup) {
        this.popup = popup;
        init();
    }

    public DictionaryPopupController(DictionaryPopup dictionaryPopup, DictionariesTextArea dictionariesTextArea) {
        this(dictionaryPopup);
        this.dictionariesTextArea = dictionariesTextArea;
    }

    protected void init() {
        popupModel = getPopupModel();
        popup.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                processKeyEvent(e);
            }
        });
        popup.setCallback(new Callback<String>() {
            @Override
            public void execute(String str) {
                List<DictionaryEntry> dictionaryEntries = popupModel.findWord(str);
                dictionariesTextArea.setFoundResult(dictionaryEntries);
            }
        });
    }

    protected SearchByKeyProvider getPopupModel() {
        return new DictionaryPopupModel(new BaseDictionariesManager());
    }

    protected void processKeyEvent(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            popup.hide();
        } else if (e.getKeyChar() != KeyEvent.VK_ENTER) {
            updatePopup();
        }
    }

    protected void updatePopup() {
        List<String> byKey = popupModel.findByKey(popup.getText());
        if (!byKey.isEmpty()){
            popup.setModel(byKey);
            popup.redraw();
            if (popup.notVisible()){
                popup.showPopup();
            }
        } else {
            popup.setModel(Collections.<String>emptyList());
            popup.redraw();
        }
    }
}
