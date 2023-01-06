package gui;

import game.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenuBar extends JMenuBar implements ActionListener {

    private final Game g;

    private final JMenuItem resetButton = new JMenuItem("Reset");
    private final JRadioButtonMenuItem[] diffButtons = new JRadioButtonMenuItem[4];

    public GameMenuBar(Game g) {
        this.g = g;

        // Settings menu
        JMenu settingsMenu = new JMenu("Settings");
        this.add(settingsMenu);

        // Reset button
        resetButton.addActionListener(this);
        settingsMenu.add(resetButton);

        // Difficulty select
        JMenu diffSelect = new JMenu("Difficulty");
        settingsMenu.add(diffSelect);
        ButtonGroup diffButtonGroup = new ButtonGroup();
        String[] diffNames = new String[]{"Easy","Medium","Hard","Custom"};
        for (int i = 0; i < 4; i++) {
            diffButtons[i] = new JRadioButtonMenuItem();
            diffButtons[i].addActionListener(this);
            diffButtonGroup.add(diffButtons[i]);
            diffSelect.add(diffButtons[i]);
            diffButtons[i].setText(diffNames[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) g.resetGame();
        else if (e.getSource() == diffButtons[0]) g.setDifficulty(10,10,10);
        else if (e.getSource() == diffButtons[1]) g.setDifficulty(16,16,40);
        else if (e.getSource() == diffButtons[2]) g.setDifficulty(16,30,99);
        else if (e.getSource() == diffButtons[3]) new CustomDiffWindow(g);

    }
}
