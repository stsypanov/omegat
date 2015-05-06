package org.omegat.gui.editor.filter;

import org.omegat.core.data.EntryKey;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.editor.IEditorFilter;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Provides base source text filtering for project.
 * It helps to get rid of showing segments with numbers, dates, etc
 * which are usually remain the same for all languages.
 *
 * Created by stsypanov on 22.04.2015.
 */
public class BaseFilter implements IEditorFilter {

	private static final Pattern NUMBER_PATTERN = Pattern.compile("^(?:(\\d+(?:[\\.,]\\d+)?)(?:\\|)?)+");
	private static final Pattern DATE_PATTERN = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.]([1-9]|[1-9]|0[1-9]|1[012])[- /.](19|20)\\d\\d");
//	private static final Pattern DATE_PATTERN = Pattern.compile("[A-Za-z\\s]+");
	private static final JPanel controlComponentStub = new JPanel();

	@Override
	public boolean allowed(SourceTextEntry ste) {
		EntryKey key = ste.getKey();
		String sourceText = key.sourceText;
		return !(NUMBER_PATTERN.matcher(sourceText).matches() || DATE_PATTERN.matcher(sourceText).matches());
	}

	@Override
	public Component getControlComponent() {
		return controlComponentStub;
	}

	@Override
	public boolean isSourceAsEmptyTranslation() {
		return false;
	}
}
