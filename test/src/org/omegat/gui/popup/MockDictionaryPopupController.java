package org.omegat.gui.popup;

import org.omegat.gui.dictionaries.DictionariesTextArea;
import org.omegat.gui.dictionaries.DictionaryPopupController;
import org.omegat.gui.dictionaries.SearchByKeyProvider;
import org.omegat.gui.popup.dictionary.DictionaryPopup;

/**
 * Created by stsypanov on 22.10.2015.
 */
public class MockDictionaryPopupController extends DictionaryPopupController {

	public MockDictionaryPopupController(DictionaryPopup dictionaryPopup, DictionariesTextArea dictionariesTextArea) {
		super(dictionaryPopup, dictionariesTextArea);
	}

	@Override
	protected SearchByKeyProvider getPopupModel() {
		return new MockDictionaryPopupModel();
	}
}
