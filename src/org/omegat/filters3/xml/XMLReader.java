/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2008 Didier Briel
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.filters3.xml;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;

import org.omegat.filters2.AbstractReader;
import org.omegat.util.OConsts;
import org.omegat.util.PatternConsts;

/**
 * This class automatically detects encoding of an inner XML file and constructs
 * a Reader with appropriate encoding.
 * <p>
 * Detecting of encoding is done first by reading a possible BOM, to detect
 * UTF-16 or UTF-8 then by reading a value from the XML header
 * <code>&lt;?xml version="1.0" encoding="..."?&gt;</code>
 * <p>
 * If encoding isn't specified, or it is not supported by the Java platform, the
 * file is opened in UTF-8, in compliance with the XML specifications
 * 
 * 
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 */
public class XMLReader extends AbstractReader {

    /**
     * Creates a new instance of XMLReader. If encoding cannot be detected,
     * falls back to default UTF-8.
     * 
     * @param fileName
     *            - the file to read
     */
    public XMLReader(File file) throws IOException {
        this(file, null);
    }

    /**
     * Creates a new instance of XMLReader. If encoding cannot be detected,
     * falls back to supplied <code>encoding</code>, or (if supplied null, or
     * supplied encoding is not supported by JVM) falls back to UTF-8.
     * 
     * @param fileName
     *            The file to read.
     * @param encoding
     *            The encoding to use if we can't autodetect.
     */
    public XMLReader(File file, String encoding) throws IOException {
        reader = createReader(file, encoding);
    }

    /**
     * Returns the reader of the underlying file in the correct encoding.
     * 
     * <p>
     * We can detect the following:
     * <ul>
     * <li>UTF-16 with BOM (byte order mark)
     * <li>UTF-8 with BOM (byte order mark)
     * <li>Any other encoding with 8-bit Latin symbols (e.g. Windows-1251, UTF-8
     * etc), if it is specified using XML/HTML-style encoding declarations.
     * </ul>
     * 
     * <p>
     * Note that we cannot detect UTF-16 encoding, if there's no BOM!
     */
    private BufferedReader createReader(File file, String defaultEncoding) throws IOException {
        // BOM detection
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));

        is.mark(OConsts.READ_AHEAD_LIMIT);

        int char1 = is.read();
        int char2 = is.read();
        int char3 = is.read();
        encoding = null;
        if (char1 == 0xFE && char2 == 0xFF)
            encoding = "UTF-16BE";
        if (char1 == 0xFF && char2 == 0xFE)
            encoding = "UTF-16LE";
        if (char1 == 0xEF && char2 == 0xBB && char3 == 0xBF)
            encoding = "UTF-8";

        is.reset();
        if (encoding != null) {
            return createReaderAndDetectEOL(is, encoding);
        }

        is.mark(OConsts.READ_AHEAD_LIMIT);
        byte[] buf = new byte[OConsts.READ_AHEAD_LIMIT];
        int len = is.read(buf);
        if (len > 0) {
            String buffer = new String(buf, 0, len);

            Matcher matcher_xml = PatternConsts.XML_ENCODING.matcher(buffer);
            if (matcher_xml.find())
                encoding = matcher_xml.group(1);
        }

        is.reset();
        if (encoding != null) {
            return createReaderAndDetectEOL(is, encoding);
        }

        // UTF-8 if we couldn't detect it ourselves
        try {
            return createReaderAndDetectEOL(is, OConsts.UTF8);
        } catch (Exception e) {
            return createReaderAndDetectEOL(is, null);
        }
    }

    private BufferedReader createReaderAndDetectEOL(InputStream is, String encoding) throws IOException {
        BufferedReader rd = new BufferedReader(encoding != null ? new InputStreamReader(is, encoding)
                : new InputStreamReader(is), OConsts.READ_AHEAD_LIMIT);
        rd.mark(OConsts.READ_AHEAD_LIMIT);

        for (int i = 0; i < OConsts.READ_AHEAD_LIMIT; i++) {
            char ch = (char) rd.read();
            if (ch == '\r' || ch == '\n') {
                if (eol == null) {
                    eol = "";
                } else if (eol.codePointAt(0) == ch) {
                    // duplicate char - this is second line
                    rd.reset();
                    return rd;
                }
                eol += ch;
                if (eol.codePointCount(0, eol.length()) == 2) {
                    // second char - latest
                    rd.reset();
                    return rd;
                }
            } else {
                if (eol != null) {
                    rd.reset();
                    return rd;
                }
            }
        }

        // no eols found - assume '\n'
        eol = "\n";
        rd.reset();
        return rd;
    }
}
