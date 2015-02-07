package org.omegat.gui.clipboard;

import org.omegat.util.Log;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: stsypanov
 * Date: 23.07.2014
 * Time: 14:44
 */
public class TextUtils {
	/*
	 *  Attempt to center the line containing the caret at the center of the
	 *  scroll pane.
	 *
	 *  @param component the text component in the sroll pane
	 */
	public static void centerLineInScrollPane(JTextComponent component) {
		Container container = SwingUtilities.getAncestorOfClass(JViewport.class, component);

		if (container == null) return;

		try {
			Rectangle r = component.modelToView(component.getCaretPosition());
			JViewport viewPort = (JViewport) container;
			int extentHeight = viewPort.getExtentSize().height;
			int viewHeight = viewPort.getViewSize().height;

			int y = Math.max(0, r.y - extentHeight / 2);
			y = Math.min(y, viewHeight - extentHeight);

			viewPort.setViewPosition(new Point(0, y));
		} catch (BadLocationException ex) {
			Log.log(ex);
		}
	}

	/*
	 *  Return the column number at the Caret position.
	 *
	 *  The column returned will only make sense when using a
	 *  Monospaced font.
	 */
	public static int getColumnAtCaret(JTextComponent component) {
		//  Since we assume a monospaced font we can use the width of a single
		//  character to represent the width of each character

		FontMetrics fm = component.getFontMetrics(component.getFont());
		int characterWidth = fm.stringWidth("0");
		int column = 0;

		try {
			Rectangle r = component.modelToView(component.getCaretPosition());
			int width = r.x - component.getInsets().left;
			column = width / characterWidth;
		} catch (BadLocationException ex) {
			Log.log(ex);
		}

		return column + 1;
	}

	/*
	 *  Return the line number at the Caret position.
	 */
	public static int getLineAtCaret(JTextComponent component) {
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();

		return root.getElementIndex(caretPosition) + 1;
	}

	/*
	 *  Return the number of lines of text in the Document
	 */
	public static int getLinesCount(JTextComponent component) {
		Element root = component.getDocument().getDefaultRootElement();
		return root.getElementCount();
	}

	/*
	 *  Position the caret at the start of a line.
	 */
	public static void gotoStartOfLine(JTextComponent component, int line) {
		Element root = component.getDocument().getDefaultRootElement();
		line = Math.max(line, 1);
		line = Math.min(line, root.getElementCount());
		int startOfLineOffset = root.getElement(line - 1).getStartOffset();
		component.setCaretPosition(startOfLineOffset);
	}

	public static int getEndOfLine(JTextComponent component, int line) {
		Element root = component.getDocument().getDefaultRootElement();
		line = Math.max(line, 1);
		line = Math.min(line, root.getElementCount());
		return root.getElement(line - 1).getEndOffset();
	}

	/*
	 *  Position the caret on the first word of a line.
	 */
	public static void gotoFirstWordOnLine(JTextComponent component, int line) {
		gotoStartOfLine(component, line);

		//  The following will position the caret at the start of the first word

		try {
			int position = component.getCaretPosition();
			String first = component.getDocument().getText(position, 1);

			if (Character.isWhitespace(first.charAt(0))) {
				component.setCaretPosition(Utilities.getNextWord(component, position));
			}
		} catch (Exception e) {
			Log.log(e);
		}
	}

	/*
	 *  Return the number of lines of text, including wrapped lines.
	 */
	public static int getWrappedLines(JTextArea component) {
		View view = component.getUI().getRootView(component).getView(0);
		int preferredHeight = (int) view.getPreferredSpan(View.Y_AXIS);
		int lineHeight = component.getFontMetrics(component.getFont()).getHeight();
		return preferredHeight / lineHeight;
	}

	/*
	 *  Return the number of lines of text, including wrapped lines.
	 */
	public static int getWrappedLines(JTextComponent component) {
		int lines = 0;

		View view = component.getUI().getRootView(component).getView(0);

		int paragraphs = view.getViewCount();

		for (int i = 0; i < paragraphs; i++) {
			lines += view.getView(i).getViewCount();
		}

		return lines;
	}
}
