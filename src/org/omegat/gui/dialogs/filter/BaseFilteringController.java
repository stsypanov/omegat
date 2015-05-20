package org.omegat.gui.dialogs.filter;

import org.omegat.core.Core;
import org.omegat.core.data.ProjectProperties;
import org.omegat.util.Preferences;

import javax.swing.table.AbstractTableModel;
import java.io.File;

/**
 * Created by stsypanov on 12.05.2015.
 */
public class BaseFilteringController {
	private BaseFilteringDialog dialog;

	public BaseFilteringController(BaseFilteringDialog dialog) {
		this.dialog = dialog;
	}

	public void setModel(){
		BaseFilteringParser<BaseFilteringItems> items = new BaseFilteringParser<>();
		String projectRoot = Core.getProject().getProjectProperties().getProjectRoot();
		File file = new File(projectRoot);
//		items.getObject();
//		AbstractTableModel model = new BaseFilteringModel();
//		dialog.setTableModel(model);
	}
}
