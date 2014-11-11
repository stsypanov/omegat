package org.omegat.gui.dialogs;

import org.omegat.gui.lf.PeroDarkLookAndFeel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rad1kal on 30.10.2014.
 */
public class ProxyDialog {

    private JRadioButton noProxy;
    private JRadioButton autoDetect;
    private JLabel titleLabel;
    private JCheckBox checkBox1;
    private JTextField configurationURLField;
    private JRadioButton manualConfigurationRadioButton;
    private JRadioButton httpButton;
    private JRadioButton socksRadioButton;
    private JSpinner spinner1;
    private JButton checkConnectionButton;
    private JPanel mainContainer;
    private JTextField hostField;

    public ProxyDialog() {
        checkConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = getURL();
                checkConnection(url);
            }
        });
    }

    private void checkConnection(String urlString) {
        try {
            URL url = new URL(urlString);
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public String getURL() {
        return JOptionPane.showInputDialog("Enter any URL to check connection:");
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(PeroDarkLookAndFeel.class.getCanonicalName());
        JFrame frame = new JFrame("ProxyDialog");
        frame.setContentPane(new ProxyDialog().mainContainer);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
