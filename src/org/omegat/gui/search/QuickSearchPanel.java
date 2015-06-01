package org.omegat.gui.search;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Сергей on 17.05.2015.
 */
public class QuickSearchPanel extends JPanel {

    private final JComboBox<String> textField;
    private final JButton closeButton;
    private final JCheckBox matchCaseCheckBox;
    private final JCheckBox regexCheckBox;

    public QuickSearchPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        textField = new JComboBox<>();
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));
        textField.setEditable(true);
        closeButton = new JButton("x");

        matchCaseCheckBox = new JCheckBox("Match case");
        matchCaseCheckBox.setFocusable(false);

        regexCheckBox = new JCheckBox("Regex");
        regexCheckBox.setFocusable(false);

        Box.Filler filler = new Box.Filler(new Dimension(0, 0), new Dimension(2, 0), new Dimension(0, 0));
        filler.setBackground(UIManager.getColor("Panel.background"));
        add(filler);
        add(textField);
        add(new Box.Filler(new Dimension(0, 0), new Dimension(20, 0), new Dimension(0, 0)));
        add(matchCaseCheckBox);
        add(new Box.Filler(new Dimension(0, 0), new Dimension(20, 0), new Dimension(0, 0)));
        add(regexCheckBox);
        add(new Box.Filler(new Dimension(0, 0), new Dimension(20, 0), new Dimension(0, 0)));
        add(closeButton);

        add(new Box.Filler(new Dimension(0, 0), new Dimension(300, 0), new Dimension(0, 0)));


        textField.requestFocus();
        setVisible(false);
    }

    public String getText() {
        return textField.getEditor().getItem().toString();
    }

    public JComboBox<String> getTextField() {
        return textField;
    }

    public void setCloseButtonActionListener(ActionListener actionListener) {
        closeButton.addActionListener(actionListener);
    }
}
