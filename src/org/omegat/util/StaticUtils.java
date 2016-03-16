/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey, Maxym Mykhalchuk, and Henry Pijffers
               2007 Didier Briel, Zoltan Bartko, Alex Buloichik
               2008-2011 Didier Briel
               2012 Martin Fleurke, Didier Briel
               2013 Aaron Madlon-Kay, Zoltan Bartko, Didier Briel, Alex Buloichik
               2014 Aaron Madlon-Kay, Alex Buloichik
               2015 Aaron Madlon-Kay
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

import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.omegat.util.Platform.OsType;

/**
 * Static functions taken from CommandThread to reduce file size.
 *
 * @author Keith Godfrey
 * @author Maxym Mykhalchuk
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 * @author Didier Briel
 * @author Zoltan Bartko - bartkozoltan@bartkozoltan.com
 * @author Alex Buloichik
 * @author Martin Fleurke
 * @author Aaron Madlon-Kay
 */
public class StaticUtils {
    /**
     * Configuration directory on Windows platforms
     */
    private final static String WINDOWS_CONFIG_DIR = "\\OmegaT\\";

    /**
     * Configuration directory on UNIX platforms
     */
    private final static String UNIX_CONFIG_DIR = "/.omegat/";

    /**
     * Configuration directory on Mac OS X
     */
    private final static String OSX_CONFIG_DIR = "/Library/Preferences/OmegaT/";

    /**
     * Script directory
     */
    private final static String SCRIPT_DIR = "script";

    /**
     * Char which should be used instead protected parts. It should be non-letter char, to be able to have
     * correct words counter.
     * 
     * This char can be placed around protected text for separate words inside protected text and words
     * outside if there are no spaces between they.
     */
    public static final char TAG_REPLACEMENT_CHAR = '\b';
    public static final String TAG_REPLACEMENT = "\b";

    /**
     * Contains the location of the directory containing the configuration
     * files.
     */
    private static String m_configDir = null;

    /**
     * Contains the location of the script dir containing the exported text
     * files.
     */
    private static String m_scriptDir = null;

    /**
     * Check if specified key pressed.
     *
     * @param e
     *            pressed key event
     * @param code
     *            required key code
     * @param modifiers
     *            required modifiers
     * @return true if checked key pressed
     */
    public static boolean isKey(KeyEvent e, int code, int modifiers) {
        return e.getKeyCode() == code && e.getModifiers() == modifiers;
    }

    /**
     * Returns a list of all files under the root directory by absolute path.
     */
    public static List<File> buildFileList(File rootDir, boolean recursive) throws Exception {
        final List<File> lst = new ArrayList<File>();
        iterateFileTree(rootDir.getCanonicalFile(), recursive, new ITreeIteratorCallback() {
            @Override
            public void processFile(File file) {
                lst.add(file);
            }
        });

        // Get the local collator and set its strength to PRIMARY
        final Collator localCollator = Collator.getInstance(Locale.getDefault());
        localCollator.setStrength(Collator.PRIMARY);
        Collections.sort(lst, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return localCollator.compare(o1.getPath(), o2.getPath());
            }
        });
        return lst;
    }

    public static List<String> buildRelativeFilesList(File rootDir, List<String> includes,
            List<String> excludes) throws Exception {
        List<File> files = buildFileList(rootDir, true);
        Pattern[] includesMasks;
        if (includes != null) {
            includesMasks = new Pattern[includes.size()];
            for (int i = 0; i < includes.size(); i++) {
                includesMasks[i] = compileFileMask(includes.get(i));
            }
        } else {
            includesMasks = new Pattern[0];
        }
        Pattern[] excludesMasks;
        if (excludes != null) {
            excludesMasks = new Pattern[excludes.size()];
            for (int i = 0; i < excludes.size(); i++) {
                excludesMasks[i] = compileFileMask(excludes.get(i));
            }
        } else {
            excludesMasks = new Pattern[0];
        }
        String prefix = rootDir.getCanonicalPath().replace('\\', '/');
        List<String> result = new ArrayList<String>();
        for (File f : files) {
            String fn = f.getPath().replace('\\', '/');
            if (fn.startsWith(prefix)) {
                // file path should starts from '/' for checking.
                fn = fn.substring(prefix.length());
            }
            boolean add = false;
            // check include masks
            for (Pattern p : includesMasks) {
                if (p.matcher(fn).matches()) {
                    add = true;
                    break;
                }
            }
            if (!add) {
                add = true;
                // check exclude masks
                for (Pattern p : excludesMasks) {
                    if (p.matcher(fn).matches()) {
                        add = false;
                        break;
                    }
                }
            }
            if (add) {
                result.add(fn);
            }
        }
        return result;
    }

    public static boolean checkFileInclude(String filePath, List<String> includes, List<String> excludes) {
        if (!filePath.startsWith("/")) {
            // file path should starts from '/' for checking.
            filePath = '/' + filePath;
        }
        Pattern[] includesMasks;
        if (includes != null) {
            includesMasks = new Pattern[includes.size()];
            for (int i = 0; i < includes.size(); i++) {
                includesMasks[i] = compileFileMask(includes.get(i));
            }
        } else {
            includesMasks = new Pattern[0];
        }
        Pattern[] excludesMasks;
        if (excludes != null) {
            excludesMasks = new Pattern[excludes.size()];
            for (int i = 0; i < excludes.size(); i++) {
                excludesMasks[i] = compileFileMask(excludes.get(i));
            }
        } else {
            excludesMasks = new Pattern[0];
        }
        boolean add = false;
        // check include masks
        for (Pattern p : includesMasks) {
            if (p.matcher(filePath).matches()) {
                add = true;
                break;
            }
        }
        if (!add) {
            add = true;
            // check exclude masks
            for (Pattern p : excludesMasks) {
                if (p.matcher(filePath).matches()) {
                    add = false;
                    break;
                }
            }
        }
        return add;
    }

    /**
     * Remove files by masks.
     */
    public static void removeFilesByMasks(List<String> lst, List<String> excludeMasks) {
        // exclude by masks
        for (String mask : excludeMasks) {
            Pattern re = compileFileMask(mask);
            for (Iterator<String> it = lst.iterator(); it.hasNext();) {
                String fn = "/" + it.next();
                if (re.matcher(fn).matches()) {
                    it.remove();
                }
            }
        }
    }

    static Pattern compileFileMask(String mask) {
        StringBuilder m = new StringBuilder();
        for (int cp, i = 0; i < mask.length(); i += Character.charCount(cp)) {
            cp = mask.codePointAt(i);
            if (cp >= 'A' && cp <= 'Z') {
                m.appendCodePoint(cp);
            } else if (cp >= 'a' && cp <= 'z') {
                m.appendCodePoint(cp);
            } else if (cp >= '0' && cp <= '9') {
                m.appendCodePoint(cp);
            } else if (cp == '/') {
                m.appendCodePoint(cp);
            } else if (cp == '?') {
                m.append('.');
            } else if (cp == '*') {
                if (mask.codePointCount(i, mask.length()) > 1 && mask.codePointAt(mask.offsetByCodePoints(i, 1)) == '*') {
                    // **
                    m.append(".*");
                    i++;
                } else {
                    // *
                    m.append("[^/]*");
                }
            } else {
                m.append('\\').appendCodePoint(cp);
            }
        }
        return Pattern.compile(m.toString());
    }

    /**
     * Sorts list by order. New lines sorted by alphabet.
     */
    public static void sortByList(final List<String> list, final List<String> order) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int pos1, pos2;
                if (order != null) {
                    pos1 = order.indexOf(o1);
                    pos2 = order.indexOf(o2);
                } else {
                    pos1 = 0;
                    pos2 = 0;
                }
                if (pos1 < 0) {
                    pos1 = Integer.MAX_VALUE;
                }
                if (pos2 < 0) {
                    pos2 = Integer.MAX_VALUE;
                }
                if (pos1 < pos2) {
                    return -1;
                } else if (pos1 > pos2) {
                    return 1;
                } else {
                    return o1.compareToIgnoreCase(o2);
                }
            }
        });
    }

    public interface ITreeIteratorCallback {
        public void processFile(File file) throws Exception;
    }

    public static void iterateFileTree(File rootDir, boolean recursive, ITreeIteratorCallback cb) throws Exception {
        iterateFileTree(rootDir, recursive, new HashSet<File>(), cb);
    }

    private static void iterateFileTree(File rootDir, boolean recursive, Set<File> visited, ITreeIteratorCallback cb)
            throws Exception {
        if (!rootDir.isDirectory()) {
            return;
        }
        if (visited.contains(rootDir)) {
            return;
        }
        visited.add(rootDir);
        for (File file : rootDir.listFiles()) {
            if (file.isDirectory() && recursive) {
                iterateFileTree(file.getCanonicalFile(), recursive, visited, cb);
            }
            if (file.isFile()) {
                cb.processFile(file.getCanonicalFile());
            }
        }
    }

    /**
     * Returns the names of all font families available.
     */
    public static String[] getFontNames() {
        GraphicsEnvironment graphics;
        graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return graphics.getAvailableFontFamilyNames();
    }

    /**
     * Extracts an element of a class path.
     *
     * @param fullcp
     *            the classpath
     * @param posInsideElement
     *            position inside a class path string, that fits inside some
     *            classpath element.
     */
    private static String classPathElement(String fullcp, int posInsideElement) {
        // semicolon before the path to the Jar
        int semicolon1 = fullcp.lastIndexOf(File.pathSeparatorChar, posInsideElement);
        // semicolon after the path to the Jar
        int semicolon2 = fullcp.indexOf(File.pathSeparatorChar, posInsideElement);
        if (semicolon1 < 0)
            semicolon1 = -1;
        if (semicolon2 < 0)
            semicolon2 = fullcp.length();
        return fullcp.substring(semicolon1 + 1, semicolon2);
    }

    /**
     * Extract classpath element that ends with <code>ending</code> from
     * the full classpath <code>cp</code>, if present. If not present, returns
     * null.
     */
    private static String extractClasspathElement(String cp, String ending) {
        try {
            int pos = cp.indexOf(ending);
            if (pos >= 0) {
                String path = classPathElement(cp, pos);
                return path.substring(0, path.indexOf(ending));
            }
        } catch (Exception e) {
            // should never happen, but just in case ;-)
        }
        return null;
    }

    /** Caching install dir */
    private static String INSTALLDIR = null;

    /**
     * Returns OmegaT installation directory. The code uses this method to look
     * up for OmegaT documentation.
     */
    public static String installDir() {
        if (INSTALLDIR == null) {
            String cp = System.getProperty("java.class.path");
            
            // See if we are running from a JAR
            String path = extractClasspathElement(cp, File.separator + OConsts.APPLICATION_JAR);
            
            if (path == null) {
                // We're not running from a JAR; probably debug mode (in IDE, etc.)
                path = extractClasspathElement(cp, OConsts.DEBUG_CLASSPATH);
            }
            
            // WTF?!! Falling back to current directory
            if (path == null) {
                path = ".";
            }
            
            // Cache the absolute path
            INSTALLDIR = new File(path).getAbsolutePath();
        }
        return INSTALLDIR;
    }

    /**
     * Returns the location of the configuration directory, depending on the
     * user's platform. Also creates the configuration directory, if necessary.
     * If any problems occur while the location of the configuration directory
     * is being determined, an empty string will be returned, resulting in the
     * current working directory being used.
     *
     * <ul><li>Windows XP: &lt;Documents and Settings>\&lt;User name>\Application Data\OmegaT
     * <li>Windows Vista: User\&lt;User name>\AppData\Roaming
     * <li>Linux: ~/.omegat
     * <li>Solaris/SunOS: ~/.omegat
     * <li>FreeBSD: ~/.omegat
     * <li>Mac OS X: ~/Library/Preferences/OmegaT
     * <li>Other: User home directory
     * </ul>
     *
     * @return The full path of the directory containing the OmegaT
     *         configuration files, including trailing path separator.
     */
    public static String getConfigDir() {
        // if the configuration directory has already been determined, return it
        if (m_configDir != null) {
            return m_configDir;
        }

        String cd = RuntimePreferences.getConfigDir();
        if (cd != null) {
            // use the forced specified directory
            m_configDir = new File(cd).getAbsolutePath() + File.separator;
            return m_configDir;
        }

        OsType os = Platform.getOsType(); // name of operating system
        String home; // user home directory

        // get os and user home properties
        try {
            // get the user's home directory
            home = System.getProperty("user.home");
        } catch (SecurityException e) {
            // access to the os/user home properties is restricted,
            // the location of the config dir cannot be determined,
            // set the config dir to the current working dir
            m_configDir = new File(".").getAbsolutePath() + File.separator;

            // log the exception, only do this after the config dir
            // has been set to the current working dir, otherwise
            // the log method will probably fail
            Log.logErrorRB("SU_USERHOME_PROP_ACCESS_ERROR");
            Log.log(e.toString());

            return m_configDir;
        }

        // if os or user home is null or empty, we cannot reliably determine
        // the config dir, so we use the current working dir (= empty string)
        if (os == null || StringUtil.isEmpty(home)) {
            // set the config dir to the current working dir
            m_configDir = new File(".").getAbsolutePath() + File.separator;
            return m_configDir;
        }

        // check for Windows versions
        if (os == OsType.WIN32 || os == OsType.WIN64) {
            String appData = null;

            // We do not use %APPDATA%
            // Trying first Vista/7, because "Application Data" exists also as virtual folder, 
            // so we would not be able to differentiate with 2000/XP otherwise
            File appDataFile = new File(home, "AppData\\Roaming");
            if (appDataFile.exists()) {
                appData = appDataFile.getAbsolutePath();
            } else {
                // Trying to locate "Application Data" for 2000 and XP
                // C:\Documents and Settings\<User>\Application Data
                appDataFile = new File(home, "Application Data");
                if (appDataFile.exists()) {
                    appData = appDataFile.getAbsolutePath();
                }
            }

            if (!StringUtil.isEmpty(appData)) {
                // if a valid application data dir has been found,
                // append an OmegaT subdir to it
                m_configDir = appData + WINDOWS_CONFIG_DIR;
            } else {
                // otherwise set the config dir to the user's home directory,
                // usually
                // C:\Documents and Settings\<User>\OmegaT
                m_configDir = home + WINDOWS_CONFIG_DIR;
            }
        }
        // Check for UNIX varieties
        // Solaris is generally detected as SunOS
        else if (os == OsType.LINUX32 || os == OsType.LINUX64 || os == OsType.OTHER) {
            // set the config dir to the user's home dir + "/.omegat/", so it's
            // hidden
            m_configDir = home + UNIX_CONFIG_DIR;
        }
        // check for Mac OS X
        else if (Platform.isMacOSX()) {
            // set the config dir to the user's home dir +
            // "/Library/Preferences/OmegaT/"
            m_configDir = home + OSX_CONFIG_DIR;
        }
        // other OSes / default
        else {
            // use the user's home directory by default
            m_configDir = home + File.separator;
        }

        // create the path to the configuration dir, if necessary
        if (!m_configDir.isEmpty()) {
            try {
                // check if the dir exists
                File dir = new File(m_configDir);
                if (!dir.exists()) {
                    // create the dir
                    boolean created = dir.mkdirs();

                    // if the dir could not be created,
                    // set the config dir to the current working dir
                    if (!created) {
                        Log.logErrorRB("SU_CONFIG_DIR_CREATE_ERROR");
                        m_configDir = new File(".").getAbsolutePath() + File.separator;
                    }
                }
            } catch (SecurityException e) {
                // the system doesn't want us to write where we want to write
                // reset the config dir to the current working dir
                m_configDir = new File(".").getAbsolutePath() + File.separator;

                // log the exception, but only after the config dir has been
                // reset
                Log.logErrorRB("SU_CONFIG_DIR_CREATE_ERROR");
                Log.log(e.toString());
            }
        }

        // we should have a correct, existing config dir now
        return m_configDir;
    }

    public static String getScriptDir() {
        // If the script directory has already been determined, return it
        if (m_scriptDir != null)
            return m_scriptDir;

        m_scriptDir = getConfigDir() + SCRIPT_DIR + File.separator;

        try {
            // Check if the directory exists
            File dir = new File(m_scriptDir);
            if (!dir.exists()) {
                // Create the directory
                boolean created = dir.mkdirs();

                // If the directory could not be created,
                // set the script directory to config directory
                if (!created) {
                    Log.logErrorRB("SU_SCRIPT_DIR_CREATE_ERROR");
                    m_scriptDir = getConfigDir();
                }
            }
        } catch (SecurityException e) {
            // The system doesn't want us to write where we want to write
            // reset the script dir to the current config dir
            m_scriptDir = getConfigDir();

            // log the exception, but only after the script dir has been reset
            Log.logErrorRB("SU_SCRIPT_DIR_CREATE_ERROR");
            Log.log(e.toString());
        }
        return m_scriptDir;
    }

    /**
     * Encodes the array of bytes to store them in a plain text file.
     */
    public static String uuencode(byte[] buf) {
        if (buf.length <= 0)
            return "";

        StringBuilder res = new StringBuilder();
        res.append(buf[0]);
        for (int i = 1; i < buf.length; i++) {
            res.append('#');
            res.append(buf[i]);
        }
        return res.toString();
    }

    /**
     * Decodes the array of bytes that was stored in a plain text file as a
     * string, back to array of bytes.
     */
    public static byte[] uudecode(String buf) {
        String[] bytes = buf.split("#");
        byte[] res = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            try {
                res[i] = Byte.parseByte(bytes[i]);
            } catch (NumberFormatException e) {
                res[i] = 0;
            }
        }
        return res;
    }

    /**
     * Makes the file name relative to the given path.
     */
    public static String makeFilenameRelative(String filename, String path) {
        if (filename.toLowerCase().startsWith(path.toLowerCase()))
            return filename.substring(path.length());
        else
            return filename;
    }

    /**
     * Escapes the passed string for use in regex matching, so special regex
     * characters are interpreted as normal characters during regex searches.
     *
     * This is done by prepending a backslash before each occurrence of the
     * following characters: \^.*+[]{}()&|-:=?!<>
     *
     * @param text
     *            The text to escape
     *
     * @return The escaped text
     */
    public static String escapeNonRegex(String text) {
        return escapeNonRegex(text, true);
    }

    /**
     * Escapes the passed string for use in regex matching, so special regex
     * characters are interpreted as normal characters during regex searches.
     *
     * This is done by prepending a backslash before each occurrence of the
     * following characters: \^.+[]{}()&|-:=!<>
     *
     * If the parameter escapeWildcards is true, asterisks (*) and questions
     * marks (?) will also be escaped. If false, these will be converted to
     * regex tokens (* ->
     *
     * @param text
     *            The text to escape
     * @param escapeWildcards
     *            If true, asterisks and question marks are also escaped. If
     *            false, these are converted to there regex equivalents.
     *
     * @return The escaped text
     */
    public static String escapeNonRegex(String text, boolean escapeWildcards) {
        // handle backslash
        text = text.replaceAll("\\\\", "\\\\\\\\"); // yes, that's the correct
                                                    // nr of backslashes

        // [3021915] Search window - search items containing $ behave strangely
        // If $ is included in "escape" below, it creates a
        // java.lang.StringIndexOutOfBoundsException: String index out of range:
        // 3
        // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5024613
        text = text.replace("$", "\\" + "$");

        // handle rest of characters to be escaped
        // String escape = "^.*+[]{}()&|-:=?!<>";
        for (char c : "^.+[]{}()&|-:=!<>".toCharArray()) {
            text = text.replaceAll("\\" + c, "\\\\" + c);
        }

        // handle "wildcard characters" ? and * (only if requested)
        // do this last, or the additional period (.) will cause trouble
        if (escapeWildcards) {
            // simply escape * and ?
            text = text.replaceAll("\\?", "\\\\?");
            text = text.replaceAll("\\*", "\\\\*");
        } else {
            // convert * (0 or more characters) and ? (1 character)
            // to their regex equivalents (\S* and \S? respectively)
            // text = text.replaceAll("\\?", "\\S?"); // do ? first, or * will
            // be converted twice
            // text = text.replaceAll("\\*", "\\S*");
            // The above lines were not working:
            // [ 1680081 ] Search: simple wilcards do not work
            // The following correction was contributed by Tiago Saboga
            text = text.replaceAll("\\?", "\\\\S"); // do ? first, or * will be
                                                    // converted twice
            text = text.replaceAll("\\*", "\\\\S*");
        }

        return text;
    }

    /**
     * dowload a file from the internet
     */
    public static String downloadFileToString(String urlString) throws IOException {
        URLConnection urlConn;
        InputStream in;

        URL url = new URL(urlString);
        urlConn = url.openConnection();
        //don't wait forever. 10 seconds should be enough.
        urlConn.setConnectTimeout(10000);
        in = urlConn.getInputStream();

        try {
            return IOUtils.toString(in, "UTF-8");
        } finally {
            in.close();
        }
    }

    /**
     * Download a file to the disk
     */
    public static void downloadFileToDisk(String address, String filename) throws MalformedURLException {
        URLConnection urlConn;
        InputStream in = null;
        OutputStream out = null;
        try {
            URL url = new URL(address);
            urlConn = url.openConnection();
            in = urlConn.getInputStream();
            out = new BufferedOutputStream(new FileOutputStream(filename));

            byte[] byteBuffer = new byte[1024];

            int numRead;
            while ((numRead = in.read(byteBuffer)) != -1) {
                out.write(byteBuffer, 0, numRead);
            }
        } catch (IOException ex) {
            Log.logErrorRB("IO exception");
            Log.log(ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // munch this
            }
        }
    }

    public static void extractFileFromJar(File archive, List<String> filenames, String destination)
            throws IOException {
        InputStream is = new FileInputStream(archive);
        extractFileFromJar(is, filenames, destination);
        is.close();
    }
    
    public static void extractFileFromJar(InputStream in, List<String> filenames, String destination) throws IOException {
        if (filenames == null || filenames.isEmpty()) {
            throw new IllegalArgumentException("Caller must provide non-empty list of files to extract.");
        }
        List<String> toExtract = new ArrayList<String>(filenames);
        JarInputStream jis = new JarInputStream(in);
        // parse the entries
        JarEntry entry;
        while ((entry = jis.getNextJarEntry()) != null) {
            if (toExtract.contains(entry.getName())) {
                // match found
                File f = new File(destination, entry.getName());
                f.getParentFile().mkdirs();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));

                byte[] byteBuffer = new byte[1024];

                int numRead;
                while ((numRead = jis.read(byteBuffer)) != -1) {
                    out.write(byteBuffer, 0, numRead);
                }
                out.close();
                toExtract.remove(entry.getName());
            }
        }
        jis.close();
        if (!toExtract.isEmpty()) {
            throw new FileNotFoundException("Failed to extract all of the specified files.");
        }
    }

    /**
     * Parse a command line string into arguments, interpreting
     * double and single quotes as Bash does.
     * @param cmd Command string
     * @return Array of arguments
     */
    public static String[] parseCLICommand(String cmd) {
        cmd = cmd.trim();
        if (cmd.isEmpty()) {
            return new String[] { "" };
        }
        
        StringBuilder arg = new StringBuilder();
        List<String> result = new ArrayList<>();
        
        final char noQuote = '\0';
        char currentQuote = noQuote;
        for (int cp, i = 0; i < cmd.length(); i += Character.charCount(cp)) {
            cp = cmd.codePointAt(i);
            if (cp == currentQuote) {
                currentQuote = noQuote;
            } else if (cp == '"' && currentQuote == noQuote) {
                currentQuote = '"';
            } else if (cp == '\'' && currentQuote == noQuote) {
                currentQuote = '\'';
            } else if (cp == '\\' && i + 1 < cmd.length()) {
                int ncp = cmd.codePointAt(cmd.offsetByCodePoints(i, 1));
                if ((currentQuote == noQuote && Character.isWhitespace(ncp))
                        || (currentQuote == '"' && ncp == '"')) {
                    arg.appendCodePoint(ncp);
                    i += Character.charCount(ncp);
                } else {
                    arg.appendCodePoint(cp);
                }
            } else {
                if (Character.isWhitespace(cp) && currentQuote == noQuote) {
                    if (arg.length() > 0) {
                        result.add(arg.toString());
                        arg = new StringBuilder();
                    } else {
                        // Discard
                    }
                } else {
                    arg.appendCodePoint(cp);
                }
            }
        }
        // Catch last arg
        if (arg.length() > 0) {
            result.add(arg.toString());
        }
        return result.toArray(new String[result.size()]);
    }

    public static boolean isProjectDir(File f) {
        if (f == null || f.getName().isEmpty()) {
            return false;
        }
        File projFile = new File(f.getAbsolutePath(), OConsts.FILE_PROJECT);
        return projFile.isFile();
    }
    
} // StaticUtils
