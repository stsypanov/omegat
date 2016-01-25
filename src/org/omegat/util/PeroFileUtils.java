package org.omegat.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * Created by stsypanov on 02.06.2015.
 */
public class PeroFileUtils {
    private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\n");
    public static String LINE_SEPARATOR = System.getProperty("line.separator");
    public static long RENAME_RETRY_TIMEOUT = 3000;

    /**
     * Removes old backups so that only 10 last are there.
     */
    public static void removeOldBackups(final File originalFile, int maxBackups) {
        try {
            File[] bakFiles = originalFile.getParentFile().listFiles(new FileFilter() {
                public boolean accept(File f) {
                    return !f.isDirectory() && f.getName().startsWith(originalFile.getName())
                            && f.getName().endsWith(OConsts.BACKUP_EXTENSION);
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
     * Renames file, with checking errors and 3 seconds retry against external programs (like antivirus or
     * TortoiseSVN) locking.
     */
    public static void rename(File from, File to) throws IOException {
        if (!from.exists()) {
            throw new IOException("Source file to rename (" + from + ") doesn't exist");
        }
        if (to.exists()) {
            throw new IOException("Target file to rename (" + to + ") already exists");
        }
        long b = System.currentTimeMillis();
        while (!from.renameTo(to)) {
            long e = System.currentTimeMillis();
            if (e - b > RENAME_RETRY_TIMEOUT) {
                throw new IOException("Error renaming " + from + " to " + to);
            }
        }
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

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileTemp), OConsts.UTF8))){
            textToWrite = NEW_LINE_PATTERN.matcher(textToWrite).replaceAll(System.getProperty("line.separator"));

            bw.write(textToWrite);
        } catch (Exception ex) {
            Log.log(Level.SEVERE, "writeScriptFile", ex);
        }
        outFileTemp.renameTo(outFile);
        return outFile;
    }


    /**
     * Read file as UTF-8 text.
     */
    public static String readTextFile(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            return IOUtils.toString(is, OConsts.UTF8);
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
     * Recursively delete a directory and all of its contents.
     * @param dir The directory to delete
     */
    public static void deleteTree(File dir) {
        if (!dir.exists()) {
            return;
        }
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    deleteTree(file);
                }
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
