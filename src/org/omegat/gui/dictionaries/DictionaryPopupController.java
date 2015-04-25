package org.omegat.gui.dictionaries;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Created by Сергей on 19.04.2015.
 */
public class DictionaryPopupController {
    private DictionaryPopup popup;
    private DictionaryPopupModel popupModel = new DictionaryPopupModel();
    private DictionariesTextArea dictionariesTextArea;

    public DictionaryPopupController(DictionaryPopup popup) {
        this.popup = popup;
        init();
    }

    public DictionaryPopupController(DictionaryPopup dictionaryPopup, DictionariesTextArea dictionariesTextArea) {
        this(dictionaryPopup);
        this.dictionariesTextArea = dictionariesTextArea;
    }

    private void init() {
        popup.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                processKeyEvent(e);
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
        }
    }

    public static void main(String[] args) {
        new DictionaryPopupController(new DictionaryPopup("Find in dictionary"));
    }
}
