package org.omegat.gui.dialogs;

import org.omegat.gui.common.PeroDialog;

import javax.swing.*;
import java.awt.*;

/**
 * Created by stsypanov on 16.04.2015.
 */
public class ApiKeysForm extends PeroDialog {

	public ApiKeysForm(Frame owner) {
		super(owner);

		Container contentPane = getContentPane();

		JTextField yandexKeyField = new JTextField();
		JTextField myMemoryUser = new JTextField();

		GroupLayout layout = new GroupLayout(contentPane);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		createHorizontalGroup("Yandex Translate", yandexKeyField, layout);
		createHorizontalGroup("MyMemory (User)", yandexKeyField, layout);
		createHorizontalGroup("Microsoft Translate", yandexKeyField, layout);
		createHorizontalGroup("Google Translate", yandexKeyField, layout);


		setPreferredSize(new Dimension(400, 100));
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void createHorizontalGroup(String labelName, JTextField yandexKeyField, GroupLayout layout) {

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(new JLabel(labelName))
				.addComponent(yandexKeyField));
	}

	public static void main(String[] args) {
		PeroDialog form = new ApiKeysForm(null);
	}
}
