package org.omegat.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by stsypanov on 25.05.2015.
 */
@SuppressWarnings("deprecation")
public class LFileCopyTest {
    protected static final Charset CS = Charset.forName(OConsts.UTF8);
    protected File tempFileSrc;
    protected File tempFileDestination;

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {
        tempFileSrc = File.createTempFile("tempFileSrc", ".tmp");
        List<String> lines = Arrays.asList("Line1", "Line2");
        Files.write(tempFileSrc.toPath(), lines, CS, StandardOpenOption.WRITE);

        tempFileDestination = File.createTempFile("tempFileDestination", ".tmp");
    }

    @After
    public void tearDown() throws Exception {
        Files.delete(tempFileSrc.toPath());
        Files.delete(tempFileDestination.toPath());
    }

    @Test
    public void testCopy() throws Exception {
        String from = tempFileSrc.getAbsolutePath();
        String to = tempFileDestination.getAbsolutePath();
        LFileCopy.copy(new File(from), new File(to));

        testResult();
    }

    /**
     * For LFileCopy.copy(File src, File dest)
     *
     * @throws Exception
     */
    @Test
    public void testCopy1() throws Exception {
        LFileCopy.copy(tempFileSrc, tempFileDestination);

        testResult();
    }

    /**
     * For LFileCopy.copy(InputStream src, File dest)
     *
     * @throws Exception
     */
    @Test
    public void testCopy2() throws Exception {
        try (InputStream is = new FileInputStream(tempFileSrc)) {
            LFileCopy.copy(is, tempFileDestination);

        }
        testResult();
    }

    /**
     * For LFileCopy.copy(InputStream src, OutputStream dest)
     *
     * @throws Exception
     */
    @Test
    public void testCopy3() throws Exception {
        try (InputStream is = new FileInputStream(tempFileSrc);
             OutputStream os = new FileOutputStream(tempFileDestination)) {

            LFileCopy.copy(is, os);
        }
        testResult();
    }

    /**
     * For LFileCopy.copy(Reader src, Writer dest)
     *
     * @throws Exception
     */
    @Test
    public void testCopy4() throws Exception {
        try (Reader reader = new InputStreamReader(new FileInputStream(tempFileSrc), CS);
             Writer writer = new OutputStreamWriter(new FileOutputStream(tempFileDestination), CS)) {

            LFileCopy.copy(reader, writer);
        }
        testResult();
    }

    /**
     * For LFileCopy.copy(File src, OutputStream dest)
     *
     * @throws Exception
     */
    @Test
    public void testCopy5() throws Exception {
        try (OutputStream os = new FileOutputStream(tempFileDestination)) {

            LFileCopy.copy(tempFileSrc, os);
        }
        testResult();
    }

    private void testResult() throws IOException {
        List<String> strings1 = Files.readAllLines(tempFileSrc.toPath(), CS);
        List<String> strings2 = Files.readAllLines(tempFileDestination.toPath(), CS);
        assertEquals(strings1, strings2);
    }
}