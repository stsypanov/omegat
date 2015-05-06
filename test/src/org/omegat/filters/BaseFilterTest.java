package org.omegat.filters;

import org.junit.Test;
import org.omegat.core.data.EntryKey;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.editor.filter.BaseFilter;

import static org.junit.Assert.assertFalse;

/**
 * This rest checks if BaseFilter passes through the segments with dates and numbers
 *
 * Created by stsypanov on 05.05.2015.
 */
public class BaseFilterTest {
	public static final BaseFilter filter = new BaseFilter();

	public static final String[] dates = {
			"19.02.1990",
			"19.2.1990",
			"19/02/1990",
			"19/2/1990",
			"19-02-1990",
			"19-2-1990"};

	public static final String[] numbers = {
			"1990",
			"2.1990",
			"19,1990",
			"0",
			"0.0"};

	@Test
	public void testDates() throws Exception {
		for (String date : dates) {
			EntryKey entryKey = new EntryKey(date);
			SourceTextEntry entry = new SourceTextEntry(entryKey);
			assertFalse(filter.allowed(entry));
		}
	}

	@Test
	public void testNumbers() throws Exception {
		for (String number : numbers){
			EntryKey entryKey = new EntryKey(number);
			SourceTextEntry entry = new SourceTextEntry(entryKey);
			assertFalse(filter.allowed(entry));
		}
	}
}
