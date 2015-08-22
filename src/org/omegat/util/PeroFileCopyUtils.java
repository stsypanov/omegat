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

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for copying untranslatable files.
 *
 * @author Keith Godfrey
 * @author Kim Bruning
 * @author Maxym Mykhalchuk
 */
public class PeroFileCopyUtils {

    /** Copies one file. Creates directories on the path to dest if necessary. */
    public static void copy(String src, String dest) throws IOException {
        File ifp = new File(src);
        File ofp = new File(dest);
        copy(ifp, ofp);
    }

    /** Copies one file. Creates directories on the path to dest if necessary. */
    public static void copy(File src, File dest) throws IOException {
        if (!src.exists()) {
            String message = StaticUtils.format(OStrings.getString("LFC_ERROR_FILE_DOESNT_EXIST"), src.getAbsolutePath());
            throw new IOException(message);
        }
        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /** Stores a file from input stream. Input stream is not closed. */
    public static void copy(InputStream src, File dest) throws IOException {
        dest.getParentFile().mkdirs();
        Files.copy(src, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Transfers all the input stream to the output stream. Input and output
     * streams are not closed.
     */
    public static void copy(InputStream src, OutputStream dest) throws IOException {
        ReadableByteChannel readableByteChannel = Channels.newChannel(src);
        WritableByteChannel writableByteChannel = Channels.newChannel(dest);
        fastCopy(readableByteChannel, writableByteChannel);
    }

    /**
     * Transfers all data from reader to writer. Reader and writer are not
     * closed.
     */
    public static void copy(Reader src, Writer dest) throws IOException {
        IOUtils.copy(src, dest);
    }

    /**
     * Loads contents of a file into output stream. Output stream is not closed.
     */
    public static void copy(File src, OutputStream dest) throws IOException {
        if (!src.exists()) {
            throw new IOException(StaticUtils.format(OStrings.getString("LFC_ERROR_FILE_DOESNT_EXIST"),
                    new Object[]{src.getAbsolutePath()}));
        }
        try (FileInputStream in = new FileInputStream(src)) {
            ReadableByteChannel readableByteChannel = Channels.newChannel(in);
            WritableByteChannel writableByteChannel = Channels.newChannel(dest);
            fastCopy(readableByteChannel, writableByteChannel);
        }
    }

    public static void fastCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

        while (src.read(buffer) != -1) {
            buffer.flip();
            dest.write(buffer);
            buffer.compact();
        }

        buffer.flip();

        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }

}
