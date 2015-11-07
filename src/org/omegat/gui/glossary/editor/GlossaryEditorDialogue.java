package org.omegat.gui.glossary.editor;

import org.omegat.core.Core;
import org.omegat.gui.common.BaseDialog;
import org.omegat.gui.glossary.GlossaryEntry;
import org.omegat.gui.glossary.GlossaryReaderTSV;
import org.omegat.gui.glossary.GlossaryTSVWriter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by stsypanov on 26.10.2015.
 */
public class GlossaryEditorDialogue extends BaseDialog {

	protected GlossaryEditorTable table;
	protected File glossary;

	public GlossaryEditorDialogue() {
		super(null, "Glossary editor", true);
		final int height = calculateHeight();
		setPreferredSize(new Dimension(1200, height));
		pack();
	}

	protected int calculateHeight() {
		return GlossaryEditorTable.ROW_HEIGHT * 16 + 110;
	}

	@Override
	protected void initLayout() {
		super.initLayout();
		table = new GlossaryEditorTable();
		getContentPanel().add(table.asJComponent(), BorderLayout.CENTER);
	}

	public void readGlossary(File glossary) throws IOException {
		this.glossary = glossary;
		List<GlossaryEntry> glossaryEntries = GlossaryReaderTSV.read(glossary, false);
		GlossaryEditorModel model = new GlossaryEditorModel(glossaryEntries);
		table.setModel(model);
	}

	@Override
	public void okAction() {
		GlossaryEditorModel model = (GlossaryEditorModel) table.getModel();
		List<GlossaryEntry> entries = model.getEntries();
		try {
			GlossaryTSVWriter.writeIntoFile(glossary, entries);
		} catch (IOException e) {
			Core.getMainWindow().showErrorDialogRB("TF_ERROR", "FAILED_TO_WRITE_GLOSSARY", e);
		}
	}

	@Override
	public void cancelAction() {
		//do nothing
	}


}
