/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey, Maxym Mykhalchuk, and Kim Bruning
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

package org.omegat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Utility class for copying untranslatable files.
 * 
 * @author Keith Godfrey
 * @author Kim Bruning
 * @author Maxym Mykhalchuk
 * @deprecated use {@link org.omegat.util.PeroFileCopyUtils} instead
 */
public class LFileCopy {
    private static int BUFSIZE = 1024;

    /** Copies one file. Creates directories on the path to dest if necessary. */
    public static void copy(File src, File dest) throws IOException {
        if (!src.exists()) {
            throw new IOException(StringUtil.format(OStrings.getString("LFC_ERROR_FILE_DOESNT_EXIST"),
                    src.getAbsolutePath()));
        }
        if (src.getCanonicalFile().equals(dest.getCanonicalFile())) {
            // Source and destination are literally the same file.
            // It doesn't make sense to do anything but return.
            return;
        }
        FileInputStream fis = new FileInputStream(src);
        dest.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] b = new byte[BUFSIZE];
        int readBytes;
        while ((readBytes = fis.read(b)) > 0) {
            fos.write(b, 0, readBytes);
        }
        fis.close();
        fos.close();
    }

    /** Stores a file from input stream. Input stream is not closed. */
    public static void copy(InputStream src, File dest) throws IOException {
        dest.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] b = new byte[BUFSIZE];
        int readBytes;
        while ((readBytes = src.read(b)) > 0) {
            fos.write(b, 0, readBytes);
        }
        fos.close();
    }

    /**
     * Transfers all the input stream to the output stream. Input and output
     * streams are not closed.
     */
    public static void copy(InputStream src, OutputStream dest) throws IOException {
        byte[] b = new byte[BUFSIZE];
        int readBytes;
        while ((readBytes = src.read(b)) > 0) {
            dest.write(b, 0, readBytes);
        }
    }

    /**
     * Transfers all data from reader to writer. Reader and writer are not
     * closed.
     */
    public static void copy(Reader src, Writer dest) throws IOException {
        char[] b = new char[BUFSIZE];
        int readChars;
        while ((readChars = src.read(b)) > 0) {
            dest.write(b, 0, readChars);
        }
    }

    /**
     * Loads contents of a file into output stream. Output stream is not closed.
     */
    public static void copy(File src, OutputStream dest) throws IOException {
        if (!src.exists()) {
            throw new IOException(StringUtil.format(OStrings.getString("LFC_ERROR_FILE_DOESNT_EXIST"),
                    src.getAbsolutePath()));
        }
        FileInputStream fis = new FileInputStream(src);
        byte[] b = new byte[BUFSIZE];
        int readBytes;
        while ((readBytes = fis.read(b)) > 0) {
            dest.write(b, 0, readBytes);
        }
        fis.close();
    }

}
