package org.omegat.gui.search;

import org.omegat.util.Log;

import javax.swing.text.*;
import java.awt.*;

import static org.omegat.util.StringUtil.isEmpty;

public class WordSearcher {
    protected JTextComponent comp;
    protected Highlighter.HighlightPainter painter;
    protected Highlighter highlighter;

    public WordSearcher(JTextComponent comp) {
        this.comp = comp;
        this.painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
    }

    public int search(String word) {
        highlighter = comp.getHighlighter();
        highlighter.removeAllHighlights();

        if (isEmpty(word)) {
            return -1;
        }

        String content;
        try {
            Document d = comp.getDocument();
            content = d.getText(0, d.getLength()).toLowerCase();
        } catch (BadLocationException e) {
            Log.severe("WordSearcher.search()", e);
            return -1;
        }

        word = word.toLowerCase();
        int lastIndex = 0;
        int wordSize = word.length();

        int firstOffset = -1;
        while ((lastIndex = content.indexOf(word, lastIndex)) != -1) {
            int endIndex = lastIndex + wordSize;
            try {
                highlighter.addHighlight(lastIndex, endIndex, painter);
            } catch (BadLocationException e) {
                Log.severe("WordSearcher.search()", e);
            }
            if (firstOffset == -1) {
                firstOffset = lastIndex;
            }
            lastIndex = endIndex;
        }

        return firstOffset;
    }

    public void removeAllHighlight() {
        if (highlighter != null) {
            highlighter.removeAllHighlights();
        }
    }
}