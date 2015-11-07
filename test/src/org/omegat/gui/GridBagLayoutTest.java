package org.omegat.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class GridBagLayoutTest extends JFrame {

    public GridBagLayoutTest() {
        super("GridBagLayout");
        final JPanel content = new JPanel(new GridBagLayout());
        JLabel lblImage = new JLabel();
        content.add(lblImage, new GridBagConstraints(0, 0, 1, 2, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        content.add(new JLabel("First name:"), new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
        content.add(new JTextField("<enter first name here>", 20), new GridBagConstraints(2, 0, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 0, 0));
        content.add(new JLabel("Last name:"), new GridBagConstraints(1, 1, 1, 1, 0, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
        content.add(new JTextField("<enter last name here>", 20), new GridBagConstraints(2, 1, 2, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
        JButton btnOk = new JButton("Ok");
        JButton btnCancel = new JButton("Cancel");
        btnOk.setPreferredSize(btnCancel.getPreferredSize());
        btnOk.setMinimumSize(btnOk.getPreferredSize());
        content.add(btnOk, new GridBagConstraints(2, 2, 1, 1, 1, 0, GridBagConstraints.LINE_END,
            GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
        content.add(btnCancel, new GridBagConstraints(3, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
            GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
        final JCheckBox chkOrientation = new JCheckBox("Right-to-left orientation");
        chkOrientation.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                content.setComponentOrientation( chkOrientation.isSelected() ?
                                                 ComponentOrientation.RIGHT_TO_LEFT :
                                                 ComponentOrientation.LEFT_TO_RIGHT);
                content.doLayout();
            }
        });
        content.setBorder(BorderFactory.createLineBorder(Color.red));
        getContentPane().add(content, BorderLayout.CENTER);
        getContentPane().add(chkOrientation, BorderLayout.SOUTH);
        setSize(410, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new GridBagLayoutTest().setVisible(true);
    }
}