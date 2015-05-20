package org.omegat.gui.dialogs.filter;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by stsypanov on 12.05.2015.
 */
public class BaseFilteringParserTest {
	private static final Logger logger = Logger.getLogger(BaseFilteringParserTest.class.getName());

	private BaseFilteringParser<BaseFilteringItems> parser = new BaseFilteringParser<>();
	private File file = new File("filtering.xml");
	private List<BaseFilteringItem> items;

	@Test
	public void testSaveObject() throws Exception {


		BaseFilteringItems baseFilteringItems = new BaseFilteringItems();
		baseFilteringItems.setFilteringItems(items);

		parser.saveObject(file, baseFilteringItems);
	}

	@Test
	public void testGetObject() throws Exception {
		BaseFilteringItems filteringItems = parser.getObject(file, BaseFilteringItems.class);
		List<BaseFilteringItem> actual = filteringItems.getFilteringItems();
		assertArrayEquals(items.toArray(), actual.toArray());
	}

	@Before
	public void setUp() throws Exception {
		if (!file.exists() && file.createNewFile()) {
			logger.info("File filtering.xml has been created");
		}
		BaseFilteringItem item = new BaseFilteringItem(true, "pattern1", "example1");
		BaseFilteringItem item1 = new BaseFilteringItem(false, "pattern2", "example2");
		BaseFilteringItem item3 = new BaseFilteringItem(true, "pattern3", "example3");
		BaseFilteringItem item4 = new BaseFilteringItem(false, "pattern4", "example4");

		items = new ArrayList<>(4);
		items.add(item);
		items.add(item1);
		items.add(item3);
		items.add(item4);
	}
}