package gui;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomDiffWindow extends JFrame implements ActionListener {

    private static final String[] TEXTS = new String[] {"Rows", "Columns", "Mines"};

    private JLabel[] labels = new JLabel[3];
    private JSpinner[] inputs = new JSpinner[3];
    private JButton okButton;

    private final Game g;

    public CustomDiffWindow(Game g) {
        this.g = g;

        this.setTitle("Custom Difficulty");

        JPanel topPanel = new JPanel();
        this.add(topPanel);

        topPanel.setLayout(new GridLayout(3,2,20,20));

        for (int i = 0; i < 3; i++) {
            labels[i] = new JLabel();
            labels[i].setFont(Game.DEFAULT_FONT);
            labels[i].setText(TEXTS[i]);
            labels[i].setPreferredSize(new Dimension(200,40));
            labels[i].setHorizontalAlignment(JLabel.CENTER);
            labels[i].setVerticalAlignment(JLabel.CENTER);

            inputs[i] = new JSpinner();
            inputs[i].setPreferredSize(new Dimension(100,30));

            topPanel.add(labels[i]);
            topPanel.add(inputs[i]);
        }

        okButton = new JButton();
        okButton.setText("OK");
        okButton.setFont(Game.DEFAULT_FONT);
        okButton.addActionListener(this);
        this.add(okButton, BorderLayout.SOUTH);

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int rows = (int) inputs[0].getValue();
        int cols = (int) inputs[1].getValue();
        int mines = (int) inputs[2].getValue();

        g.setDifficulty(rows, cols, mines);
        this.setVisible(false);
    }
}
