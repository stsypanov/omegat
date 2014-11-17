package org.omegat.filters2;

import org.omegat.util.NullBufferedWriter;
import org.omegat.util.StringUtil;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rad1kal on 14.11.2014.
 */
public abstract class AbstractAlignmentFilter extends AbstractFilter {
	protected Map<String, String> align;

	@Override
	protected void alignFile(BufferedReader sourceFile, BufferedReader translatedFile, FilterContext fc) throws Exception {
		Map<String, String> source = new HashMap<>();
		Map<String, String> translated = new HashMap<>();

		align = source;
		processFile(sourceFile, new NullBufferedWriter(), fc);
		align = translated;
		processFile(translatedFile, new NullBufferedWriter(), fc);
		for (Map.Entry<String, String> en : source.entrySet()) {
			String tr = translated.get(en.getKey());
			if (!StringUtil.isEmpty(tr)) {
				entryAlignCallback.addTranslation(en.getKey(), en.getValue(), tr, false, null, this);
			}
		}
	}
}
