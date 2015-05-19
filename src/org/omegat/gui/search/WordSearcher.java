package org.omegat.gui.search;

import org.omegat.util.Log;

import javax.swing.text.*;
import java.awt.*;

public class WordSearcher {
    protected JTextComponent comp;
    protected Highlighter.HighlightPainter painter;
    private Highlighter highlighter;

    public WordSearcher(JTextComponent comp) {
        this.comp = comp;
        this.painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
    }

    // Search for a word and return the offset of the
    // first occurrence. Highlights are added for all
    // occurrences found.
    public int search(String word) {
        int firstOffset = -1;
        highlighter = comp.getHighlighter();
//
//        // Remove any existing highlights for last word
//        Highlighter.Highlight[] highlights = highlighter.getHighlights();
//        for (Highlighter.Highlight h : highlights) {
//            if (h.getPainter() instanceof UnderlineHighlighter.UnderlineHighlightPainter) {
//                highlighter.removeHighlight(h);
//            }
//        }
        highlighter.removeAllHighlights();

        if (word == null || word.equals("")) {
            return -1;
        }

        // Look for the word we are given - insensitive search
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

    public void removeAllHighlight(){
        highlighter.removeAllHighlights();
    }
}