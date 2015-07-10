package org.omegat.gui.dialogs.filter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by stsypanov on 13.05.2015.
 */
@XmlRootElement(name = "filteringItems")
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseFilteringItems {

	@XmlElement(name = "filteringItem")
	private List<BaseFilteringItem> filteringItems;

	public List<BaseFilteringItem> getFilteringItems() {
		return filteringItems;
	}

	public void setFilteringItems(List<BaseFilteringItem> filteringItems) {
		this.filteringItems = filteringItems;
	}
}
