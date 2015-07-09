package org.omegat.gui.dialogs.filter;

import org.omegat.core.Core;
import org.omegat.util.Log;

import javax.swing.table.AbstractTableModel;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.logging.Level;

/**
 * Created by stsypanov on 12.05.2015.
 */
public class BaseFilteringController {
    private BaseFilteringDialog dialog;

    public BaseFilteringController(BaseFilteringDialog dialog) {
        this.dialog = dialog;
    }

    public void showDialog() {
        dialog.setVisible(true);
    }

    public void hideDialog() {

    }

    public void loadItems() {
        BaseFilteringParser parser = new BaseFilteringParser();
        String projectRoot = Core.getProject().getProjectProperties().getBaseFilteringItems();
        try {
            BaseFilteringItems items = BaseFilteringParser.getObject(new File(projectRoot), BaseFilteringItems.class);
            AbstractTableModel model = new BaseFilteringModel(items);
            dialog.setTableModel(model);
        } catch (JAXBException e) {
            dialog.showErrorMessage();
            Log.log(Level.SEVERE, "failed to load items", e);
        }
    }

    public void adjustColumnWidth() {
        dialog.adjustColumns();
    }
}
