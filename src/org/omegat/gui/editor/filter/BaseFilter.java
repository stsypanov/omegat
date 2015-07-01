package org.omegat.gui.editor.filter;

import org.omegat.core.data.EntryKey;
import org.omegat.core.data.IProject;
import org.omegat.core.data.ProjectProperties;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.gui.dialogs.filter.BaseFilteringItem;
import org.omegat.gui.dialogs.filter.BaseFilteringItems;
import org.omegat.gui.dialogs.filter.BaseFilteringParser;
import org.omegat.gui.editor.IEditorFilter;
import org.omegat.util.Log;
import org.omegat.util.OConsts;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
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
	private static final Map<Pattern, Boolean> PATTERNS = new HashMap<>();

	private static final Pattern NUMBER_PATTERN = Pattern.compile("^(?:(\\d+(?:[\\.,]\\d+)?)(?:\\|)?)+");
	private static final Pattern DATE_PATTERN = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.]([1-9]|[1-9]|0[1-9]|1[012])[- /.](19|20)\\d\\d");
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$");
	//todo check if this is correctly compiled when reading from XML
	private static final Pattern URL_PATTERN = Pattern.compile("((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[\\-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9\\.\\-]+|(?:www\\.|[\\-;:&=\\+\\$,\\w]+@)[A-Za-z0-9\\.\\-]+)((?:\\/[\\+~%\\/\\.\\w\\-_]*)?\\??(?:[\\-\\+=&;%@\\.\\w_]*)#?(?:[\\.\\!\\/\\\\\\w]*))?)");

	//сделать шаблон для номеров вида №1

	private final boolean filteringItemsLoaded;


	public BaseFilter(IProject project) {
		this(project.getProjectProperties());
	}

	public BaseFilter(ProjectProperties projectProperties) {
		boolean fileLoaded;
		String baseFilteringItems = projectProperties.getBaseFilteringItems();
		try {
			load(baseFilteringItems);
			fileLoaded = true;
		} catch (IOException | JAXBException e) {
			fileLoaded = false;
			Log.log(Level.SEVERE, e.getMessage(), e);
		}

		filteringItemsLoaded = fileLoaded;
	}

	//todo consider case when there is no file with filters
    private void load(String baseFilteringItems) throws IOException, JAXBException {
		File filteringItems = new File(baseFilteringItems);
		if (filteringItems.exists()) {
			BaseFilteringParser<BaseFilteringItems> parser = new BaseFilteringParser<>();
			BaseFilteringItems items = parser.getObject(filteringItems, BaseFilteringItems.class);
			for (BaseFilteringItem item : items.getFilteringItems()) {
				String pattern = item.getPattern().replaceAll("\\\\+", "\\\\");
				Pattern p = Pattern.compile(pattern);
				PATTERNS.put(p, item.isApply());
			}
		} else {
			String message = "cannot load " + OConsts.FILTERING_ITEMS_FILE_NAME + " no base filtering provided";
			throw new IOException(message);
		}
	}

	@Override
	public boolean allowed(SourceTextEntry ste) {
		boolean allowed = true;
		if (filteringItemsLoaded){
			for (Map.Entry<Pattern, Boolean> entry : PATTERNS.entrySet()){
				Boolean applied = entry.getValue();
				if (applied){
					EntryKey key = ste.getKey();
					String sourceText = key.sourceText;
					Pattern pattern = entry.getKey();
					allowed = !pattern.matcher(sourceText).matches();
                    if (!allowed){
                        break;
                    }
				}
			}
		}
		return allowed;
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
