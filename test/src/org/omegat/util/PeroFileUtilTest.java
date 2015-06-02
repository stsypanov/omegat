package org.omegat.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by stsypanov on 25.05.2015.
 */
public class PeroFileUtilTest extends LFileCopyTest {
    public static final Logger logger = Logger.getLogger(PeroFileUtilTest.class.getName());

    public static final String PARENT = "test/data";
    public static final String UTIL_PARENT = "test/data" + "/util";
    public static final String TEXT_FILE_NAME = "textFile.txt";

    public static final File PARENT_DIR = new File(PARENT);
    public static final File UTIL_PARENT_DIR = new File(UTIL_PARENT);
    public static final File TEXT_FILE = new File(UTIL_PARENT_DIR, TEXT_FILE_NAME);

    @Test
    public void testRemoveOldBackups() throws Exception {

    }

    @Test
    public void testBackupFile() throws Exception {

    }

    @Test
    public void testRename() throws Exception {
        File renameFrom= new File(PARENT, "renameFrom.delete_on_exit");
        FileUtils.forceDeleteOnExit(renameFrom);

        assertTrue(renameFrom.createNewFile());

        File renameTo = new File(PARENT, "renameTo.delete_on_exit");
        FileUtils.forceDeleteOnExit(renameTo);

        assertFalse(renameTo.exists());

        FileUtil.rename(renameFrom, renameTo);

        assertEquals(renameTo.getAbsolutePath(), renameTo.getAbsolutePath());

        FileUtils.forceDelete(renameTo);

        assertFalse(renameFrom.exists());
        assertFalse(renameTo.exists());
    }

    @Test
    public void testWriteScriptFile() throws Exception {
        String script = "javascript";
        String scriptName = "script.js";
        File file = FileUtil.writeScriptFile(script, scriptName);
        String fromFile = FileUtil.readTextFile(file);
        assertEquals(script, fromFile);

    }

    @Test
    public void testReadScriptFile() throws Exception {

    }

    @Test
    public void testReadTextFile() throws Exception {
        String s = FileUtil.readTextFile(new File(UTIL_PARENT, TEXT_FILE_NAME));
        assertEquals("text file", s);
    }

    @Test
    public void testFindFiles() throws Exception {
        assertTrue(PARENT_DIR.exists());
        final String suffix = ".tmx";
        List<File> files = FileUtil.findFiles(PARENT_DIR, new FileFilter() {
            @Override
            public boolean accept(File pathname) {

                return pathname.getName().endsWith(suffix);
            }
        });
        for (File file : files){
            String name = file.getName();
            String extension = name.substring(name.length() - 4);
            logger.info("name " + name + "\textension " + extension);
            assertEquals(suffix, extension);
        }
    }

    @Test
    public void testComputeRelativePath() throws Exception {

        String relativePath = FileUtil.computeRelativePath(UTIL_PARENT_DIR, TEXT_FILE);
        assertEquals(TEXT_FILE_NAME, relativePath);

        relativePath = FilenameUtils.getName(TEXT_FILE.getPath());
        assertEquals(TEXT_FILE_NAME, relativePath);
    }

    @Test
    public void testLoadTextFileFromDoc() throws Exception {

    }

    @Test
    public void testDeleteTree() throws Exception {

    }

    @Test
    public void testCopyFilesTo() throws Exception {

    }

    @Test
    public void testCreateTempDir() throws Exception {

    }
}