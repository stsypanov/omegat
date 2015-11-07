package org.omegat.gui.glossary;

import org.omegat.util.EncodingDetector;
import org.omegat.util.OConsts;
import org.omegat.util.StringUtil;

import java.io.*;
import java.util.Collection;

/**
 * Created by stsypanov on 07.11.2015.
 */
public class GlossaryTSVWriter {

	public static void writeIntoFile(File file, Collection<GlossaryEntry> entries) throws IOException {
		String encoding = OConsts.UTF8;
		if (!file.exists()) {
			File parentFile = file.getParentFile();
			if (parentFile != null) {
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
			}
			file.createNewFile();
		} else {
			encoding = EncodingDetector.detectEncodingDefault(file, OConsts.UTF8);
		}
		try (Writer wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding))) {
			for (GlossaryEntry newEntry : entries) {
				wr.append(newEntry.getSrcText()).append('\t').append(newEntry.getLocText());
				if (!StringUtil.isEmpty(newEntry.getCommentText())) {
					wr.append('\t').append(newEntry.getCommentText());
				}
				wr.append(System.getProperty("line.separator"));
			}
		}
	}
}
