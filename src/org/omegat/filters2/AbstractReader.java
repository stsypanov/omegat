package org.omegat.filters2;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by stsypanov on 20.02.2015.
 */
public abstract class AbstractReader extends Reader {
	protected boolean readFirstTime = true;
	protected BufferedReader reader;
	/**
	 * Inner encoding.
	 */
	protected String encoding;
	/**
	 * End of line chars used in source file.
	 */
	protected String eol;

	@Override
	public int read(@NotNull char[] cbuf, int off, int len) throws IOException {
		// BOM (byte order mark) bugfix
		if (readFirstTime) {
			readFirstTime = false;
			reader.mark(1);
			int ch = reader.read();
			if (ch != 0xFEFF)
				reader.reset();
		}
		return reader.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	/**
	 * Returns detected encoding.
	 */
	public String getEncoding() {
		return encoding;
	}


	/**
	 * Returns detected EOL chars.
	 */
	public String getEol() {
		return eol;
	}
}
