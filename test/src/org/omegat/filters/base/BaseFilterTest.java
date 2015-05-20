package org.omegat.filters.base;

import gen.core.project.ProjectFileStorageTest;
import org.junit.Before;
import org.junit.Test;
import org.omegat.core.Core;
import org.omegat.core.data.EntryKey;
import org.omegat.core.data.ProjectProperties;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.editor.filter.BaseFilter;

import static org.junit.Assert.assertFalse;

/**
 * This rest checks if BaseFilter passes through the segments with dates and numbers
 * <p/>
 * Created by stsypanov on 05.05.2015.
 */
public class BaseFilterTest extends ProjectFileStorageTest {
	protected ProjectProperties projectProperties;
	protected  BaseFilter filter;

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

	public static final String[] emails = {
			"sieger_116@mail.ru",
			"sergei.tsypanov@yandex.ru"
	};

	@Before
	public void setUp() throws Exception {
		projectProperties = getProjectProperties();
		filter = new BaseFilter(projectProperties);
	}

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
		for (String number : numbers) {
			EntryKey entryKey = new EntryKey(number);
			SourceTextEntry entry = new SourceTextEntry(entryKey);
			assertFalse(filter.allowed(entry));
		}
	}

	@Test
	public void testEmails() throws Exception {
		for (String email : emails){
			EntryKey entryKey = new EntryKey(email);
			SourceTextEntry entry = new SourceTextEntry(entryKey);
			assertFalse(filter.allowed(entry));
		}
	}
}
