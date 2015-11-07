package org.omegat.gui.glossary.editor;

import com.bulenkov.darcula.DarculaLaf;
import org.junit.Test;
import org.omegat.gui.glossary.GlossaryEntry;
import org.omegat.gui.glossary.GlossaryReaderTSV;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by stsypanov on 28.10.2015.
 */
public class GlossaryEditorDialogueTest {

	public static final String TEST_GLOSSARY = "test/data/glossaries/glossary.txt";

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new DarculaLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		GlossaryEditorDialogue dialogue = null;
		try {
			dialogue = new GlossaryEditorDialogue();
			dialogue.readGlossary(new File(TEST_GLOSSARY));
			dialogue.setVisible(true);

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (dialogue != null) {
			System.out.println(dialogue.getSize());
		}

		System.exit(0);
	}

	@Test
	public void testEditFile() throws Exception {
		List<GlossaryEntry> glossaryEntries = GlossaryReaderTSV.read(new File(TEST_GLOSSARY), false);
		GlossaryEditorDialogue dialogue = null;
		try {
			dialogue = new GlossaryEditorDialogue();
			dialogue.readGlossary(new File(TEST_GLOSSARY));
			dialogue.setVisible(true);

		} catch (IOException e) {
			e.printStackTrace();
		}

		assertEquals(glossaryEntries, GlossaryReaderTSV.read(new File(TEST_GLOSSARY), false));

	}
}
