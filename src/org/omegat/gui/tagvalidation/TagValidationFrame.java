/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey, Maxym Mykhalchuk, Henry Pijffers
               2007 Didier Briel
               2008-2009 Martin Fleurke
               2013 Aaron Madlon-Kay
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

package org.omegat.gui.tagvalidation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.data.PrepareTMXEntry;
import org.omegat.core.data.ProtectedPart;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.core.data.TMXEntry;
import org.omegat.core.events.IFontChangedEventListener;
import org.omegat.core.tagvalidation.ErrorReport;
import org.omegat.core.tagvalidation.ErrorReport.TagError;
import org.omegat.gui.HListener;
import org.omegat.gui.common.PeroFrame;
import org.omegat.gui.main.MainWindow;
import org.omegat.util.OStrings;
import org.omegat.util.PatternConsts;
import org.omegat.util.Preferences;
import org.omegat.util.StaticUtils;
import org.omegat.util.gui.StaticUIUtils;
import org.openide.awt.Mnemonics;

/**
 * A frame to display the tags with errors during tag validation.
 * 
 * @author Keith Godfrey
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 * @author Didier Briel
 * @author Martin Fleurke
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class TagValidationFrame extends PeroFrame {

    public TagValidationFrame(MainWindow parent) {
        super();
        setTitle(OStrings.getString("TF_NOTICE_BAD_TAGS"));

        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        };
        StaticUIUtils.setEscapeAction(this, escapeAction);

        // Configure close button
        JButton closeButton = new JButton();
        Mnemonics.setLocalizedText(closeButton, OStrings.getString("BUTTON_CLOSE"));
        closeButton.addActionListener(escapeAction);

        // Fix All button
        m_fixAllButton = new JButton();
        Mnemonics.setLocalizedText(m_fixAllButton, OStrings.getString("BUTTON_FIX_ALL"));
        m_fixAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(Core.getMainWindow().getApplicationFrame(),
                        StaticUtils.format(OStrings.getString("TAG_FIX_ALL_WARNING"), m_numFixableErrors),
                        OStrings.getString("CONFIRM_DIALOG_TITLE"), JOptionPane.YES_NO_OPTION)) {
                    return;
                }
                List<Integer> fixed = fixAllEntries();
                Core.getEditor().refreshViewAfterFix(fixed);
            }
        });

        m_editorPane = new JEditorPane();
        m_editorPane.setEditable(false);
        m_editorPane.addHyperlinkListener(new HListener(parent, this, true)); // fix for bug 1542937
        JScrollPane scroller = new JScrollPane(m_editorPane);

        Box bbut = Box.createHorizontalBox();
        bbut.add(Box.createHorizontalGlue());
        bbut.add(m_fixAllButton);
        bbut.add(Box.createHorizontalStrut(10));
        bbut.add(closeButton);
        bbut.add(Box.createHorizontalGlue());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroller, BorderLayout.CENTER);
        getContentPane().add(bbut, BorderLayout.SOUTH);

        CoreEvents.registerFontChangedEventListener(new IFontChangedEventListener() {
            @Override
            public void onFontChanged(Font newFont) {
                TagValidationFrame.this.setFont(newFont);
            }
        });
        setFont(Core.getMainWindow().getApplicationFont());
    }

    /** Call this to set OmegaT-wide font for the Tag Validation window. */
    @Override
    public final void setFont(Font f) {
        super.setFont(f);
        if (isVisible())
            update();
    }

    @Override
    public String getPreferenceBaseName() {
        return "tagv_window";
    }

    private void doCancel() {
        dispose();
    }

    /** replaces all &lt; and &gt; with &amp;lt; and &amp;gt; */
    private String htmlize(String str) {
        String htmld = str;
        htmld = htmld.replaceAll("\\<", "&lt;");
        htmld = htmld.replaceAll("\\>", "&gt;");
        htmld = htmld.replaceAll("\n", "<br>");
        return htmld;
    }

    /**
     * Replace tags with &lt;font
     * color="color"&gt;&lt;b&gt;&lt;tag&gt;&lt;/b&gt;&lt;/font&gt;
     */
    private String colorTags(String str, String color, Pattern removePattern, ProtectedPart[] protectedParts,
            Map<String, TagError> errors) {
        // show OmegaT tags in bold and color, and to-remove text also
        String htmlResult = formatRemoveTagsAndPlaceholders(str, color, removePattern, protectedParts, errors);

        // show linefeed as symbol
        Matcher lfMatch = PatternConsts.HTML_BR.matcher(htmlResult);
        // /simulate unicode symbol for linefeed "\u240A", which is not
        // displayed correctly.
        htmlResult = lfMatch.replaceAll("<font color=\"" + color + "\"><sup>L</sup>F<br></font>");
        return htmlResult;
    }
    
    /**
     * Formats plain text as html with placeholders in color 
     * @param str the text to format
     * @param color the color to use
     * @return html text
     */
    private String formatPlaceholders(String str, String color, ProtectedPart[] protectedParts,
            Map<String, TagError> errors) {
        List<TextPart> text = new ArrayList<>();
        text.add(new TextPart(str, false));
        while (true) {
            boolean updated = false;
            for (ProtectedPart pp : protectedParts) {
                for (int i = 0; i < text.size(); i++) {
                    TextPart tp = text.get(i);
                    if (tp.highlighted) {
                        continue;
                    }
                    int pos = tp.text.indexOf(pp.getTextInSourceSegment());
                    if (pos >= 0) {
                        split(text, i, pos, pos + pp.getTextInSourceSegment().length());
                        updated = true;
                    }
                }
            }
            if (!updated) {
                break;
            }
        }
        StringBuilder htmlResult = new StringBuilder();
        for (TextPart tp : text) {
            if (tp.highlighted) {
                htmlResult.append(colorize(htmlize(tp.text), errors.get(tp.text)));
            } else {
                htmlResult.append(htmlize(tp.text));
            }
        }
        return htmlResult.toString();
    }

    private void split(List<TextPart> text, int index, int beg, int end) {
        int i = index;
        String tpText = text.remove(i).text;
        if (beg > 0) {
            text.add(i, new TextPart(tpText.substring(0, beg), false));
            i++;
        }
        text.add(i, new TextPart(tpText.substring(beg, end), true));
        i++;
        if (end < tpText.length()) {
            text.add(i, new TextPart(tpText.substring(end), false));
        }
    }

    protected static class TextPart {
        String text;
        boolean highlighted;

        public TextPart(String text, boolean highlighted) {
            this.text = text;
            this.highlighted = highlighted;
        }
    }

    /**
     * Formats plain text as html with placeholders and to-remove text in color 
     * @param str the text to format
     * @param color the color to use for placeholders
     * @param placeholderPattern the pattern to decide what is a placeholder
     * @param removePattern the pattern to decide what text had to be removed.
     * @return html text
     */
    private String formatRemoveTagsAndPlaceholders(String str, String color, Pattern removePattern,
            ProtectedPart[] protectedParts, Map<String, TagError> errors) {
        if (removePattern != null) {
            Matcher removeMatcher = removePattern.matcher(str);
            String htmlResult="";
            int pos=0;
            while (removeMatcher.find()) {
                htmlResult += formatPlaceholders(str.substring(pos, removeMatcher.start()), color,
                        protectedParts, errors);
                htmlResult += colorize(htmlize(removeMatcher.group(0)), TagError.EXTRANEOUS);
                pos = removeMatcher.end();
            }
            htmlResult += formatPlaceholders(str.substring(pos), color, protectedParts, errors);
            return htmlResult;
        } else {
            return formatPlaceholders(str, color, protectedParts, errors);
        }
    }

    public void displayErrorList(List<ErrorReport> errorList) {
        this.m_errorList = errorList;
        update();
    }

    private void update() {
        Pattern removePattern = PatternConsts.getRemovePattern();

        m_numFixableErrors = 0;

        StringBuilder output = new StringBuilder();

        output.append("<html>\n");
        output.append("<head>\n");
        output.append("<style>\n");
        output.append("<style type=\"text/css\">\n");
        output.append("    <!--\n");
        output.append("    body {\n");
        output.append("            font-family: ").append(getFont().getName()).append(";\n");
        output.append("            font-size: ").append(getFont().getSize()).append("pt;\n");
        output.append("    }\n");
        output.append("    td {\n");
        output.append("            border: 1px solid gray;\n");
        output.append("    }\n");
        output.append("    -->\n");
        output.append("</style>\n");
        output.append("</head>\n");
        output.append("<body>\n");
        if (message != null) {
            output.append("<b>").append(message).append("</b>");
        }

        output.append("<table border=\"1\" cellspacing=\"1\" cellpadding=\"2\" width=\"100%\">\n");
        for (ErrorReport report : m_errorList) {
            output.append("<tr>");
            output.append("<td>");
            output.append("<a href=\"");
            output.append(report.entryNum);
            output.append("\"");
            output.append(">");
            output.append(report.entryNum);
            output.append("</a>");
            output.append("</td>");
            output.append("<td>");
            output.append(colorTags(report.source, "blue", null, report.ste.getProtectedParts(),
                    report.srcErrors));
            output.append("</td>");
            output.append("<td>");
            output.append(colorTags(report.translation, "blue", removePattern,
                    report.ste.getProtectedParts(), report.transErrors));
            output.append("</td>");
            output.append("<td width=\"10%\">");
            // Although NetBeans mentions that the HashSet can be replaced with java.util.EnumSet
            // Set<TagError> allErrors = EnumSet.copyOf(report.srcErrors.values());
            // creates a runtime exception in some cases, while the HashSet does not
            Set<TagError> allErrors = new HashSet<>(report.srcErrors.values());
            allErrors.addAll(report.transErrors.values());
            for (TagError err : allErrors) {
                output.append(colorize(ErrorReport.localizedTagError(err), err));
                output.append("<br/>");
            }
            if (!allErrors.contains(TagError.UNSPECIFIED)) {
                output.append("<p align=\"right\">&rArr;&nbsp;<a href=\"fix:");
                output.append(report.entryNum);
                output.append("\">");
                output.append(OStrings.getString("TAG_FIX_COMMAND"));
                output.append("</a></p>");
                m_numFixableErrors++;
            }
            output.append("</td>");
            output.append("</tr>\n");
        }
        output.append("</table>\n");
        output.append("</body>\n");
        output.append("</html>\n");

        m_fixAllButton.setEnabled(m_numFixableErrors > 0);
        m_editorPane.setContentType("text/html");
        m_editorPane.setText(output.toString());
        m_editorPane.setCaretPosition(0);
    }

    public void setMessage(String message) {
        this.message = message;
    }


    private String colorize(String text, TagError error) {
        String color = "black";
        if (error != null) {
            switch (error) {
            case EXTRANEOUS:
                text = String.format("<strike>%s</strike>", text);
            case MISSING:
            case MALFORMED:
            case WHITESPACE:
                color = "red";
                break;
            case DUPLICATE:
                color = "purple";
                break;
            case ORPHANED:
                text = String.format("<u>%s</u>", text);
            case ORDER:
                color = "#FF8C00"; // Orange. Pre-1.7 Java doesn't recognize the name "orange".
                break;
            case UNSPECIFIED:
                color = "blue";
            }
        }
        
        return String.format("<font color=\"%s\"><b>%s</b></font>", color, text);
    }
    
    /**
     * Automatically fix the tag errors in a particular entry.
     * @param entryNum The entry to fix
     * @return The source text of the fixed entry
     */
    public String fixEntry(int entryNum) {
        
        ErrorReport report = null;
        
        for (int i = 0; i < m_errorList.size(); i++) {
            
            report = m_errorList.get(i);
            
            if (report.entryNum != entryNum) {
                continue;
            }
            
            if (!doFix(report)) {
                // There was a problem, so show an error dialog.
               JOptionPane.showMessageDialog(Core.getMainWindow().getApplicationFrame(),
                       OStrings.getString("TAG_FIX_ERROR_MESSAGE"), OStrings.getString("TAG_FIX_ERROR_TITLE"),
                       JOptionPane.ERROR_MESSAGE);
               this.dispose();
               return null;
            }
            
            if (report.ste.getDuplicate() == SourceTextEntry.DUPLICATE.NONE) {
                m_errorList.remove(i);
            } else {
                m_errorList = Core.getTagValidation().listInvalidTags();
            }
            break;
        }
        
        if (!m_errorList.isEmpty()) {
            update();
        } else {
            this.dispose();
        }
        
        return report != null ? report.source : null;
    }
    
    /**
     * Automatically fix tag errors in all available entries.
     * @return A list of fixed entries
     */
    private List<Integer> fixAllEntries() {
        List<Integer> fixed = new ArrayList<>();
        for (ErrorReport report : m_errorList) {
            if (!doFix(report) && report.ste.getDuplicate() != SourceTextEntry.DUPLICATE.NEXT) {
                // Fixes will fail on duplicates of previously fixed segments. Ignore this.
                // Otherwise the user must have changed the translation, so show an error dialog.
                JOptionPane.showMessageDialog(Core.getMainWindow().getApplicationFrame(),
                        OStrings.getString("TAG_FIX_ERROR_MESSAGE"), OStrings.getString("TAG_FIX_ERROR_TITLE"),
                        JOptionPane.ERROR_MESSAGE);
                break;
            }
            fixed.add(report.entryNum);
        }
        this.dispose();
        
        return fixed;
    }
    
    /**
     * Fix all errors in a given report, and commit the changed translation to the project.
     * Checks to make sure the translation has not been changed in the meantime.
     * 
     * @param report The report to fix
     * @return Whether or not the fix succeeded
     */
    private boolean doFix(ErrorReport report) {
        // Make sure the translation hasn't changed in the editor.
        TMXEntry prevTrans = Core.getProject().getTranslationInfo(report.ste);
        if (!report.translation.equals(prevTrans.translation)) {
            return false;
        }
        
        String fixed = TagValidationTool.fixErrors(report);
        
        // Put modified translation back into project.
        if (fixed != null) {
            PrepareTMXEntry tr = new PrepareTMXEntry();
            tr.source = report.ste.getSrcText();
            tr.translation = fixed;
            tr.note = prevTrans.note;
            Core.getProject().setTranslation(report.ste, tr, prevTrans.defaultTranslation, null);
        }
        
        return true;
    }
    
    /** The URL prefix given to "Fix" links in the Tag Validation window */
    public static final String FIX_URL_PREFIX = "fix:";
    private String message;
    private final JEditorPane m_editorPane;
    private List<ErrorReport> m_errorList;
    private JButton m_fixAllButton;
    private int m_numFixableErrors;
}
