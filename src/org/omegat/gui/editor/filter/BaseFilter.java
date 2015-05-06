package org.omegat.gui.editor.filter;

import org.omegat.core.data.EntryKey;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.editor.IEditorFilter;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Provides base source text filtering for project.
 * It helps to get rid of showing segments with numbers, dates, emails, URI's etc
 * which are usually remain the same for all languages.
 * <p/>
 * Created by stsypanov on 22.04.2015.
 */
public class BaseFilter implements IEditorFilter {

	private static final JPanel controlComponentStub = new JPanel();

	private static final Pattern NUMBER_PATTERN = Pattern.compile("^(?:(\\d+(?:[\\.,]\\d+)?)(?:\\|)?)+");
	private static final Pattern DATE_PATTERN = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.]([1-9]|[1-9]|0[1-9]|1[012])[- /.](19|20)\\d\\d");
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$");
	private static final Pattern URL_PATTERN = Pattern.compile("((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[\\-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9\\.\\-]+|(?:www\\.|[\\-;:&=\\+\\$,\\w]+@)[A-Za-z0-9\\.\\-]+)((?:\\/[\\+~%\\/\\.\\w\\-_]*)?\\??(?:[\\-\\+=&;%@\\.\\w_]*)#?(?:[\\.\\!\\/\\\\\\w]*))?)");


	@Override
	public boolean allowed(SourceTextEntry ste) {
		EntryKey key = ste.getKey();
		String sourceText = key.sourceText;
		return !(
				NUMBER_PATTERN.matcher(sourceText).matches() ||
						DATE_PATTERN.matcher(sourceText).matches() ||
						EMAIL_PATTERN.matcher(sourceText).matches() ||
						URL_PATTERN.matcher(sourceText).matches()
		);
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
