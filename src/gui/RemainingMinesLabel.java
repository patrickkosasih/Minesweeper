package gui;

import misc.ScaledImageIcon;
import game.Game;

import javax.swing.*;

public class RemainingMinesLabel extends JLabel {

    static final ImageIcon mineIcon = new ScaledImageIcon("sprites/mine.png",0.02f);

    public RemainingMinesLabel() {
        this.setFont(Game.DEFAULT_FONT.deriveFont(42f));  // Default font of size 42
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setVerticalTextPosition(JLabel.BOTTOM);
        this.setIcon(mineIcon);
    }
}
