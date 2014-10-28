/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2008 Alex Buloichik
               2009 Didier Briel
               2012 Alex Buloichik, Didier Briel
               2014 Alex Buloichik, Aaron Madlon-Kay
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.omegat.gui.help.HelpFrame;

/**
 * Files processing utilities.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 * @author Didier Briel
 * @author Aaron Madlon-Kay
 */
public class FileUtil {
    public static String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Removes old backups so that only 10 last are there.
     */
    public static void removeOldBackups(final File originalFile, int maxBackups) {
        try {
            File[] bakFiles = originalFile.getParentFile().listFiles(new FileFilter() {
                public boolean accept(File f) {
                    return !f.isDirectory() && f.getName().startsWith(originalFile.getName())
                            && f.getName().endsWith(".bak");
                }
            });

            if (bakFiles != null && bakFiles.length > maxBackups) {
                Arrays.sort(bakFiles, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        if (f2.lastModified() < f1.lastModified()) {
                            return -1;
                        } else if (f2.lastModified() > f1.lastModified()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                for (int i = maxBackups; i < bakFiles.length; i++) {
                    bakFiles[i].delete();
                }
            }
        } catch (Exception e) {
            // we don't care
        }
    }

    /**
     * Create file backup with datetime suffix.
     */
    public static void backupFile(File f) throws IOException {
        long fileMillis = f.lastModified();
        String str = new SimpleDateFormat("yyyyMMddHHmm").format(new Date(fileMillis));
        LFileCopy.copy(f, new File(f.getPath() + "." + str + ".bak"));
    }

    /**
     * Writes a text into a UTF-8 text file in the script directory.
     * 
     * @param textToWrite
     *            The text to write in the file
     * @param fileName
     *            The file name without path
     */
    public static File writeScriptFile(String textToWrite, String fileName) {

        File outFile = new File(StaticUtils.getScriptDir(), fileName);
        File outFileTemp = new File(StaticUtils.getScriptDir(), fileName + ".temp");
        outFile.delete();
        BufferedWriter bw = null;
        try {
            textToWrite = textToWrite.replaceAll("\n", System.getProperty("line.separator"));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileTemp), OConsts.UTF8));
            bw.write(textToWrite);
        } catch (Exception ex) {
            // Eat exception silently
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException ex) {
                Log.log(ex);
            }
        }
        outFileTemp.renameTo(outFile);
        return outFile;
    }

    public static String readScriptFile(File file) {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(new FileInputStream(file), OConsts.UTF8));

            try {
                StringWriter out = new StringWriter();
                LFileCopy.copy(rd, out);
                return out.toString().replace(System.getProperty("line.separator"), "\n");
            } finally {
                rd.close();
            }
        } catch (Exception ex) {
            Log.log(ex);
            return null;
        }
    }

    /**
     * Read file as UTF-8 text.
     */
    public static String readTextFile(File file) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(new FileInputStream(file), OConsts.UTF8));

        try {
            StringWriter out = new StringWriter();
            LFileCopy.copy(rd, out);
            return out.toString();
        } finally {
            rd.close();
        }
    }

    /**
     * Write text in file using UTF-8.
     */
    public static void writeTextFile(File file, String text) throws IOException {
        Writer wr = new OutputStreamWriter(new FileOutputStream(file), OConsts.UTF8);
        try {
            wr.write(text);
        } finally {
            wr.close();
        }
    }

    /**
     * Find files in subdirectories.
     * 
     * @param dir
     *            directory to start find
     * @param filter
     *            filter for found files
     * @return list of filtered found files
     */
    public static List<File> findFiles(final File dir, final FileFilter filter) {
        final List<File> result = new ArrayList<>();
        Set<String> knownDirs = new HashSet<>();
        findFiles(dir, filter, result, knownDirs);
        return result;
    }

    /**
     * Internal find method, which calls himself recursively.
     * 
     * @param dir
     *            directory to start find
     * @param filter
     *            filter for found files
     * @param result
     *            list of filtered found files
     */
    private static void findFiles(final File dir, final FileFilter filter, final List<File> result,
            final Set<String> knownDirs) {
        String curr_dir;
        try {
            // check for recursive
            curr_dir = dir.getCanonicalPath();
            if (!knownDirs.add(curr_dir)) {
                return;
            }
        } catch (IOException ex) {
            Log.log(ex);
            return;
        }
        File[] list = dir.listFiles();
        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) {
                    findFiles(f, filter, result, knownDirs);
                } else {
                    if (filter.accept(f)) {
                        result.add(f);
                    }
                }
            }
        }
    }

    /**
     * Compute relative path of file.
     * 
     * @param rootDir
     *            root directory
     * @param filePath
     *            file path
     * @return
     */
    public static String computeRelativePath(File rootDir, File file) throws IOException {
        String rootAbs = rootDir.getAbsolutePath().replace('\\', '/') + '/';
        String fileAbs = file.getAbsolutePath().replace('\\', '/');

        switch (Platform.getOsType()) {
        case WIN32:
        case WIN64:
            if (!fileAbs.toUpperCase().startsWith(rootAbs.toUpperCase())) {
                throw new IOException("File '" + file + "' is not under dir '" + rootDir + "'");
            }
            break;
        default:
            if (!fileAbs.startsWith(rootAbs)) {
                throw new IOException("File '" + file + "' is not under dir '" + rootDir + "'");
            }
            break;
        }
        return fileAbs.substring(rootAbs.length());
    }
    
    /**
     * Load a text file from the root of help.
     * @param The name of the text file
     * @return The content of the text file
     */
    public static String loadTextFileFromDoc(String textFile) {

        // Get the license
        URL url = HelpFrame.getHelpFileURL(null, textFile);
        if (url == null) {
            return HelpFrame.errorHaiku();
        }

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(url.openStream(), OConsts.UTF8));
            try {
                StringWriter out = new StringWriter();
                LFileCopy.copy(rd, out);
                return out.toString();
            } finally {
                rd.close();
            }
        } catch (IOException ex) {
            return HelpFrame.errorHaiku();
        }

    }

    /**
     * Recursively delete a directory and all of its contents.
     * @param dir The directory to delete
     */
    public static void deleteTree(File dir) {
        if (!dir.exists()) {
            return;
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Argument must be a directory.");
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                deleteTree(file);
            }
        }
        dir.delete();
    }

    /**
     * This method is taken from
     * <a href="https://code.google.com/p/guava-libraries/">Google Guava</a>,
     * which is licenced under the Apache License 2.0.
     * 
     * <p>Atomically creates a new directory somewhere beneath the system's
     * temporary directory (as defined by the {@code java.io.tmpdir} system
     * property), and returns its name.
     *
     * <p>Use this method instead of {@link File#createTempFile(String, String)}
     * when you wish to create a directory, not a regular file.  A common pitfall
     * is to call {@code createTempFile}, delete the file and create a
     * directory in its place, but this leads a race condition which can be
     * exploited to create security vulnerabilities, especially when executable
     * files are to be written into the directory.
     *
     * <p>This method assumes that the temporary volume is writable, has free
     * inodes and free blocks, and that it will not be called thousands of times
     * per second.
     *
     * @return the newly-created directory
     * @throws IllegalStateException if the directory could not be created
     */
    public static File createTempDir() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";

        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException("Failed to create directory within "
                + TEMP_DIR_ATTEMPTS + " attempts (tried "
                + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
    }

    private static int TEMP_DIR_ATTEMPTS = 10000;
}
