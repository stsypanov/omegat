package org.omegat.gui.dialogs;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.omegat.util.Log;
import org.omegat.util.Preferences;
import org.omegat.util.network.ProxyPolicy;
import org.omegat.util.network.ProxyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.*;
import java.util.Objects;

/**
 * Created by stsypanov on 30.10.2014.
 */
public class ProxyDialog extends JFrame {

	private JLabel titleLabel;

	private JPanel mainContainer;

	private JRadioButton noProxy;
	private JRadioButton autoDetectProxy;

	private JRadioButton manualConfigurationProxy;
	private JCheckBox automaticProxyConfigurationURLCheckBox;

	private JTextField configurationURLField;
	private JRadioButton httpButton;
	private JRadioButton socksButton;

	private JLabel hostNameLabel;
	private JTextField hostField;

	private JLabel portNumberLabel;
	private JSpinner portSpinner;

	private JButton checkConnectionButton;
	private JButton cancelButton;
	private JButton applyButton;
	private JButton okButton;

	private ButtonGroup topLevelControls;
	private ButtonGroup http_socks_group;

	private JComponent[] autoConfigurationComponents;
	private JComponent[] manualConfigurationComponents;

	public ProxyDialog() {
		super("ProxyDialog");
		$$$setupUI$$$();
		setContentPane(mainContainer);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		topLevelControls = new ButtonGroup();
		topLevelControls.add(noProxy);
		topLevelControls.add(autoDetectProxy);
		topLevelControls.add(manualConfigurationProxy);

		noProxy.setFocusable(false);
		autoDetectProxy.setFocusable(false);
		manualConfigurationProxy.setFocusable(false);

		http_socks_group = new ButtonGroup();
		http_socks_group.add(httpButton);
		http_socks_group.add(socksButton);

		autoConfigurationComponents = new JComponent[]{automaticProxyConfigurationURLCheckBox, configurationURLField};
		manualConfigurationComponents = new JComponent[]{
				httpButton, socksButton,
				hostNameLabel, hostField,
				portNumberLabel, portSpinner};

		ItemListener listener = new RadioButtonChangeListener();

		noProxy.addItemListener(listener);
		autoDetectProxy.addItemListener(listener);
		manualConfigurationProxy.addItemListener(listener);

		checkConnectionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String url = getURL();
				checkConnection(url);
			}
		});

		ActionListener applyConfigurationListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ProxyDialog.this.dispose();
				ProxyUtils.applyProxyPreferences();
				saveProxyPreferences();
			}
		};

		okButton.addActionListener(applyConfigurationListener);
		applyButton.addActionListener(applyConfigurationListener);

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ProxyDialog.this.dispose();
			}
		});

		ProxyPolicy policy = Preferences.getPreferenceEnumDefault(Preferences.PROXY_POLICY, ProxyPolicy.NO_PROXY);
		switch (policy) {
			case NO_PROXY: {
				noProxy.setSelected(true);
				break;
			}
			case AUTO_DETECT: {
				autoDetectProxy.setSelected(true);
				break;
			}
			case MANUAL_CONFIGURATION: {
				manualConfigurationProxy.setSelected(true);
				loadProxyConfiguration();
				break;
			}
		}
		pack();
		setVisible(true);
	}

	private void saveProxyPreferences() {
		ProxyPolicy policy;
		if (noProxy.isSelected()) {
			policy = ProxyPolicy.NO_PROXY;
		} else if (autoDetectProxy.isSelected()) {
			policy = ProxyPolicy.AUTO_DETECT;
			try {
				Proxy proxy = ProxyUtils.getProxy("http://mail.ru");
				String host = ((InetSocketAddress) proxy.address()).getHostName();
				int port = ((InetSocketAddress) proxy.address()).getPort();

				writePreferences(host, port);
			} catch (URISyntaxException e) {
				Log.log(e);
			}
		} else {
			policy = ProxyPolicy.MANUAL_CONFIGURATION;
			String host = hostField.getText();
			int port = (Integer) portSpinner.getValue();

			writePreferences(host, port);
		}
		Preferences.setPreference(Preferences.PROXY_POLICY, policy);
	}

	private void writePreferences(String host, int port) {
		Preferences.setPreference(Preferences.HTTP_PROXY_HOST, host);
		Preferences.setPreference(Preferences.HTTP_PROXY_PORT, port);

		Preferences.setPreference(Preferences.HTTPS_PROXY_PORT, port);
		Preferences.setPreference(Preferences.HTTPS_PROXY_PORT, port);
	}

	private void loadProxyConfiguration() {
		String host = Preferences.getPreference(Preferences.HTTP_PROXY_HOST);
		int port = Integer.parseInt(Preferences.getPreference(Preferences.HTTP_PROXY_PORT));
		hostField.setText(host);
		portSpinner.setValue(port);
	}

	private void checkConnection(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.connect();
			int responseCode = urlConn.getResponseCode();
			switch (responseCode) {
				case HttpURLConnection.HTTP_OK: {
					JOptionPane.showMessageDialog(this, "Connection is OK\n" + responseCode, "Connection test", JOptionPane.INFORMATION_MESSAGE);
					break;
				}
				case HttpURLConnection.HTTP_FORBIDDEN: {
					JOptionPane.showMessageDialog(this, "Forbidden\n" + responseCode, "Connection test", JOptionPane.ERROR_MESSAGE);
					break;
				}
				default: {
					JOptionPane.showMessageDialog(this, "Response code: " + responseCode, "Connection test", JOptionPane.WARNING_MESSAGE);
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	public static String getURL() {
		return JOptionPane.showInputDialog("Enter any URL to check connection:");
	}


	public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
		new ProxyDialog();
	}

	private void switchToNoProxyMode() {
		setComponentState(autoConfigurationComponents, false);
		setComponentState(manualConfigurationComponents, false);
	}

	private void switchToManualConfigurationMode() {
		setComponentState(autoConfigurationComponents, false);
		setComponentState(manualConfigurationComponents, true);
	}

	private void switchToAutoConfigurationMode() {
		setComponentState(autoConfigurationComponents, true);
		setComponentState(manualConfigurationComponents, false);
	}

	private void setComponentState(JComponent[] components, boolean state) {
		for (JComponent component : components) {
			component.setEnabled(state);
		}
	}

	private void createUIComponents() {
		portSpinner = new JSpinner(new SpinnerNumberModel(80, 0, 65535, 1));
		portSpinner.setMaximumSize(new Dimension(100, 26));
		portSpinner.setMinimumSize(new Dimension(100, 26));
		portSpinner.setPreferredSize(new Dimension(70, 25));
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		mainContainer = new JPanel();
		mainContainer.setLayout(new GridBagLayout());
		noProxy = new JRadioButton();
		noProxy.setText("No Proxy");
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(10, 20, 10, 0);
		mainContainer.add(noProxy, gbc);
		autoDetectProxy = new JRadioButton();
		autoDetectProxy.setText("Auto Detect Proxy Settings");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 20, 10, 0);
		mainContainer.add(autoDetectProxy, gbc);
		final JPanel spacer1 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridheight = 2;
		gbc.weightx = 6.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainContainer.add(spacer1, gbc);
		titleLabel = new JLabel();
		titleLabel.setText("Proxy Configuration");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 20, 0, 0);
		mainContainer.add(titleLabel, gbc);
		automaticProxyConfigurationURLCheckBox = new JCheckBox();
		automaticProxyConfigurationURLCheckBox.setText("Automatic proxy configuration URL:");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 40, 0, 0);
		mainContainer.add(automaticProxyConfigurationURLCheckBox, gbc);
		configurationURLField = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 10);
		mainContainer.add(configurationURLField, gbc);
		manualConfigurationProxy = new JRadioButton();
		manualConfigurationProxy.setText("Manual Configuration");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 20, 10, 0);
		mainContainer.add(manualConfigurationProxy, gbc);
		final JPanel spacer2 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.gridwidth = 2;
		gbc.weighty = 10.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.insets = new Insets(10, 0, 0, 10);
		mainContainer.add(spacer2, gbc);
		httpButton = new JRadioButton();
		httpButton.setText("HTTP");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 40, 0, 0);
		mainContainer.add(httpButton, gbc);
		socksButton = new JRadioButton();
		socksButton.setText("SOCKS");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 20, 0, 0);
		mainContainer.add(socksButton, gbc);
		hostNameLabel = new JLabel();
		hostNameLabel.setText("Host name:");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 45, 0, 0);
		mainContainer.add(hostNameLabel, gbc);
		portNumberLabel = new JLabel();
		portNumberLabel.setText("Port number:");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 45, 0, 0);
		mainContainer.add(portNumberLabel, gbc);
		portSpinner.setMaximumSize(new Dimension(100, 26));
		portSpinner.setMinimumSize(new Dimension(100, 26));
		portSpinner.setPreferredSize(new Dimension(70, 25));
		portSpinner.setToolTipText("Porxy port number");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(2, 0, 2, 300);
		mainContainer.add(portSpinner, gbc);
		checkConnectionButton = new JButton();
		checkConnectionButton.setText("Check connection");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 20, 0, 60);
		mainContainer.add(checkConnectionButton, gbc);
		hostField = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(2, 0, 2, 10);
		mainContainer.add(hostField, gbc);
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 9;
		gbc.fill = GridBagConstraints.BOTH;
		mainContainer.add(panel1, gbc);
		cancelButton = new JButton();
		cancelButton.setText("Cancel");
		panel1.add(cancelButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer3 = new Spacer();
		panel1.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		applyButton = new JButton();
		applyButton.setText("Apply");
		panel1.add(applyButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		okButton = new JButton();
		okButton.setText("OK");
		panel1.add(okButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return mainContainer;
	}

	private class RadioButtonChangeListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			JRadioButton button = (JRadioButton) e.getSource();

			if (Objects.equals(button, noProxy)) {
				switchToNoProxyMode();
			} else if (Objects.equals(button, manualConfigurationProxy)) {
				switchToManualConfigurationMode();
			} else if (Objects.equals(button, autoDetectProxy)) {
				switchToAutoConfigurationMode();
			}
		}
	}
}
