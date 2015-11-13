package org.omegat.gui.clipboard;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stsypanov on 12.11.2015.
 */
public class ClipboadrListModel extends AbstractListModel<String> {

	protected List<String> items;

	@SuppressWarnings("CollectionWithoutInitialCapacity")
	public ClipboadrListModel() {
		items = new ArrayList<>();
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public String getElementAt(int index) {
		return items.get(index);
	}

	public void insert(String item){
		if (!items.contains(item)) {
			items.add(item);
			fireContentsChanged(this,  items.size(), items.size());
		}
	}
}
