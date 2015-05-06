package org.omegat.gui.dictionaries;

import org.omegat.core.dictionaries.BaseDictionariesManager;
import org.omegat.core.dictionaries.DictionaryEntry;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

/**
 * This is a controller for DictionaryPopup ui class and provides
 * search functional for popup
 *
 * Created by stsypanov on 19.04.2015.
 */
public class DictionaryPopupController {
    private DictionaryPopup popup;
    private DictionaryPopupModel popupModel;
    private DictionariesTextArea dictionariesTextArea;
    private BaseDictionariesManager dictionariesManager;

    public DictionaryPopupController(DictionaryPopup popup) {
        this.popup = popup;
        init();
    }

    public DictionaryPopupController(DictionaryPopup dictionaryPopup, DictionariesTextArea dictionariesTextArea) {
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
                popup.hidePopup();
            }
        });
    }

    private void processKeyEvent(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            popup.hidePopup();
            popup.setVisible(false);
            popup.dispose();
            popupModel.clear();
        } else {
            updatePopup();
        }
    }

    public void updatePopup() {
        List<String> byKey = popupModel.findByKey(popup.getText());
        if (byKey.size() != 0){
            popup.setModel(byKey);
            popup.redraw();
            popup.showPopup();
        } else {
            popup.setModel(Collections.<String>emptyList());
            popup.redraw();
        }
    }

    public void showPopup() {
        popup.invoke();
    }
}
