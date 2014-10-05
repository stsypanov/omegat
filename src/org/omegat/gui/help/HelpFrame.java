/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey, Maxym Mykhalchuk, 
                            Sandra Jean Chua, and Henry Pijffers
               2007 Didier Briel
               2009 Alex Buloichik
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

package org.omegat.gui.help;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.omegat.gui.common.PeroFrame;
import org.omegat.util.OConsts;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.StaticUtils;
import org.omegat.util.gui.StaticUIUtils;
import org.openide.awt.Mnemonics;

/**
 * Frame that displays help HTML files. Singleton.
 * 
 * @author Keith Godfrey
 * @author Sandra Jean Chua - sachachua at users.sourceforge.net
 * @author Maxym Mykhalchuk
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
@SuppressWarnings("serial")
public class HelpFrame extends PeroFrame {
    /*
     * The Singleton design pattern allows us to have just one instance of the
     * help frame at all times. In order to use this pattern, we need to prevent
     * other classes from calling HelpFrame's constructor. To get a reference to
     * the help frame, classes should call the static getInstance() method.
     */
    private static HelpFrame singleton;

    private static final String ANCH_SETHOME = "#__sethome";

    /** Creates the Help Frame */
    private HelpFrame() {
        m_historyList = new ArrayList<>();

        // set window size & position
        initWindowLayout();

        Container cp = getContentPane();
        m_helpPane = new JEditorPane();
        m_helpPane.setEditable(false);
        m_helpPane.setContentType("text/html");
        JScrollPane scroller = new JScrollPane(m_helpPane);
        cp.add(scroller, "Center");

        m_homeButton = new JButton();
        m_homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_historyList.add(m_filename);
                displayHome();
                m_backButton.setEnabled(!m_historyList.isEmpty());
            }
        });

        m_backButton = new JButton();
        m_backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (m_historyList.size() > 0) {
                    URL u = m_historyList.remove(m_historyList.size() - 1);
                    displayURL(u);
                }
                m_backButton.setEnabled(!m_historyList.isEmpty());
            }
        });
        m_backButton.setEnabled(false);

        m_closeButton = new JButton();
        m_closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        Box bbut = Box.createHorizontalBox();
        bbut.add(m_backButton);
        bbut.add(Box.createHorizontalStrut(10));
        bbut.add(m_homeButton);
        bbut.add(Box.createHorizontalGlue());
        bbut.add(m_closeButton);
        cp.add(bbut, "North");

        StaticUIUtils.setEscapeClosable(this);

        m_helpPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent he) {
                if (he.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    m_historyList.add(m_filename);
                    gotoLink(he.getDescription());
                    m_backButton.setEnabled(!m_historyList.isEmpty());
                }
            }
        });

        updateUIText();
        displayHome();
    }

    /**
     * Gets the only instance of Help Frame
     */
    public static HelpFrame getInstance() {
        if (singleton == null) {
            singleton = new HelpFrame();
        }
        return singleton;
    }

    public static URL getHelpFileURL(String lang, String filename) {
        // find in install dir
        String path;
        if (lang != null) {
            path = lang + File.separator + filename;
        } else {
            path = filename;
        }
        File f = new File(StaticUtils.installDir() + File.separator + OConsts.HELP_DIR + File.separator
                + path);
        try {
            if (f.exists()) {
                return f.toURI().toURL();
            }
        } catch (IOException ex) {
        }
        // find in classpath
        if (lang != null) {
            path = lang + '/' + filename;
        } else {
            path = filename;
        }
        URL r = HelpFrame.class.getResource('/' + OConsts.HELP_DIR + '/' + path);

        return r;
    }

    public final void displayHome() {
        if (m_home != null) {
            // home was already displayed, we know URL
            displayURL(m_home);
        } else {
            // Need to detect home URL.
            String lang = detectInitialLanguage();
            displayURL(getHelpFileURL(lang, OConsts.HELP_HOME));
        }
    }

    /**
     * Displays some file in Online Help.
     * <p>
     * If the <code>file</code> is a full URL starting from <code>http://</code>
     * , then say
     * 
     * <pre>
     * &lt;p&gt;You can display the User Manual in a normal web browser and have
     * access to external links by opening the &lt;b&gt;index.html&lt;/b&gt; file
     * located in the &lt;b&gt;/docs/&lt;/b&gt; directory of the OmegaT application
     * directory.&lt;/p&gt;
     * </pre>
     * 
     * @param file
     *            the file to display
     */
    private void gotoLink(String link) {
        if (link.startsWith("http://")) {
            String txt = "<b>" + link + "</b>";
            StringBuilder buf = new StringBuilder();
            buf.append("<html><body><p>");
            buf.append(StaticUtils.format(OStrings.getString("HF_ERROR_EXTLINK_TITLE"), new Object[] { txt }));
            buf.append("<p>");
            buf.append(StaticUtils.format(OStrings.getString("HF_ERROR_EXTLINK_MSG"),
                    new Object[] { "<b>index.html</b>" }));
            buf.append("</body></html>");

            m_helpPane.setText(buf.toString());
        } else {
            try {
                URL newPage = new URL(m_filename, link);
                if (link.endsWith(ANCH_SETHOME)) {
                    String s = newPage.toExternalForm();
                    s = s.substring(0, s.length() - ANCH_SETHOME.length());
                    newPage = new URL(s);
                    m_home = newPage;
                }
                displayURL(newPage);
            } catch (IOException e) {
                String s = errorHaiku() + "<p>&nbsp;<p>" + OStrings.getString("HF_CANT_FIND_HELP") + link;

                m_helpPane.setText(s);
            }
        }
    }

    /**
     * Display url in the help pane.
     * 
     * @param url
     */
    private void displayURL(URL url) {
        try {
            m_helpPane.setPage(url);
            m_filename = url;
        } catch (IOException e) {
            String s = errorHaiku() + "<p>&nbsp;<p>" + OStrings.getString("HF_CANT_FIND_HELP") + url;

            m_helpPane.setText(s);
        }
    }

    // immortalize the BeOS 404 messages (some modified a bit for context)
    public static String errorHaiku() {
        int id = new Random().nextInt(11) + 1;
        return OStrings.getString("HF_HAIKU_" + id);
    }

    private void updateUIText() {
        Mnemonics.setLocalizedText(m_closeButton, OStrings.getString("BUTTON_CLOSE"));
        Mnemonics.setLocalizedText(m_homeButton, OStrings.getString("BUTTON_HOME"));
        Mnemonics.setLocalizedText(m_backButton, OStrings.getString("BUTTON_BACK"));
        setTitle(OStrings.getString("HF_WINDOW_TITLE"));
    }

    /**
     * Detects the documentation language to use.
     * 
     * If the latest manual is not available in the system locale language, it
     * returns null, i.e. show a language selection screen.
     * 
     * @author Henry Pijffers (henry.pijffers@saxnot.com)
     */
    private static String detectInitialLanguage() {
        // Get the system locale (language and country)
        String language = Locale.getDefault().getLanguage().toLowerCase(Locale.ENGLISH);
        String country = Locale.getDefault().getCountry().toUpperCase(Locale.ENGLISH);

        // Check if there's a translation for the full locale (lang + country)
        String locale = language + "_" + country;
        String version = getDocVersion(locale);
        if (version != null && version.equals(OStrings.VERSION))
            return locale;

        // Check if there's a translation for the language only
        locale = language;
        version = getDocVersion(locale);
        if (version != null && version.equals(OStrings.VERSION))
            return locale;

        // No suitable translation found
        return null;
    }

    /**
     * Returns the version of (a translation of) the user manual. If there is no
     * translation for the specified locale, null is returned.
     * 
     * @author Henry Pijffers (henry.pijffers@saxnot.com)
     */
    private static String getDocVersion(String locale) {
        // Check if there's a manual for the specified locale
        // (Assume yes if the index file is there)

        if (getHelpFileURL(locale, OConsts.HELP_HOME) == null) {
            return null;
        }

        // Load the property file containing the doc version
        Properties prop = new Properties();
        InputStream in = null;
        try {
            URL u = getHelpFileURL(locale, "version.properties");
            in = u.openStream();
            if (in == null) {
                return null;
            }
            prop.load(in);
        } catch (IOException ex) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }

        // Get the doc version and return it
        // (null if the version entry is not present)
        return prop.getProperty("version");
    }

    /**
     * Loads/sets the position and size of the help window.
     */
    private void initWindowLayout() {
        // main window
        try {
            String dx = Preferences.getPreference(Preferences.HELPWINDOW_X);
            String dy = Preferences.getPreference(Preferences.HELPWINDOW_Y);
            int x = Integer.parseInt(dx);
            int y = Integer.parseInt(dy);
            setLocation(x, y);
            String dw = Preferences.getPreference(Preferences.HELPWINDOW_WIDTH);
            String dh = Preferences.getPreference(Preferences.HELPWINDOW_HEIGHT);
            int w = Integer.parseInt(dw);
            int h = Integer.parseInt(dh);
            setSize(w, h);
        } catch (NumberFormatException nfe) {
            // set default size and position
            setSize(600, 500);
        }
    }

    /**
     * Saves the size and position of the help window
     */
    private void saveWindowLayout() {
        Preferences.setPreference(Preferences.HELPWINDOW_WIDTH, getWidth());
        Preferences.setPreference(Preferences.HELPWINDOW_HEIGHT, getHeight());
        Preferences.setPreference(Preferences.HELPWINDOW_X, getX());
        Preferences.setPreference(Preferences.HELPWINDOW_Y, getY());
    }

    @Override
    public void processWindowEvent(WindowEvent w) {
        int evt = w.getID();
        if (evt == WindowEvent.WINDOW_CLOSING || evt == WindowEvent.WINDOW_CLOSED) {
            // save window size and position
            saveWindowLayout();
        }
        super.processWindowEvent(w);
    }

    private final JEditorPane m_helpPane;
    private final JButton m_closeButton;
    private final JButton m_homeButton;
    private JButton m_backButton;
    private List<URL> m_historyList;

    /**
     * Stores the full information about the currently opened HTML file,
     * including trailing #...
     */
    private URL m_filename;

    /** Page which should be displayed as home. */
    private URL m_home;
}
