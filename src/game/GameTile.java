package game;

import misc.ScaledImageIcon;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GameTile extends JLabel implements MouseListener {

    private boolean revealed;
    private boolean flagged;
    private boolean mine;
    private boolean touchingMouse = false;

    private final Game g;
    private final int id;
    private final int xPos;
    private final int yPos;

    public static final Color NEUTRAL_COLOR = new Color(0x76E2E0);
    public static final Color REVEALED_COLOR = new Color(0xD4D4D4);
    public static final Color[] DIGIT_COLORS = new Color[]{
            new Color(0x309AFF),
            new Color(0x1EA848),
            new Color(0xE22929),
            new Color(0x00348D),
            new Color(0x6F2011),
            new Color(0x177887),
            new Color(0x000000),
            new Color(0x535353),
    };

    public static final Border BORDER = BorderFactory.createLineBorder(new Color(0x59BBBA), 3);
    public static final Border REVEALED_BORDER = BorderFactory.createLineBorder(new Color(0xB8B8B8), 3);

    public static final ImageIcon FLAG_ICON = new ScaledImageIcon("sprites/flag.png",0.015f);
    public static final ImageIcon MINE_ICON = new ScaledImageIcon("sprites/mine.png",0.015f);
    public static final ImageIcon FLAG_CROSS_ICON = new ScaledImageIcon("sprites/flag with cross.png",0.015f);

    GameTile(Game g, int id) {
        // Field initialization
        this.g = g;
        this.id = id;
        this.xPos = id % g.getCols();
        this.yPos = id / g.getCols();

        // GUI setup
        //this.setText(Integer.toString(xPos) + "," + Integer.toString(yPos));
        this.setBackground(NEUTRAL_COLOR);
        this.setOpaque(true);
        this.addMouseListener(this);
        this.setVerticalAlignment(JButton.CENTER);
        this.setHorizontalAlignment(JButton.CENTER);
        this.setFont(Game.DEFAULT_FONT);
        this.setBorder(BORDER);
    }

    void revealTile() {
        if (revealed || flagged || g.isGameOver()) return;

        if (!g.isGameRunning()) g.startGame(this);

        revealed = true;
        this.setBackground(REVEALED_COLOR);
        this.setBorder(REVEALED_BORDER);

        if (mine) {
            this.setIcon(MINE_ICON);
            this.setBackground(Color.red);
            g.lose();
        } else {
            int surroundingMines = countSurroundingMines();

            if (surroundingMines > 0) {
                this.setText(String.valueOf(surroundingMines));
                this.setForeground(DIGIT_COLORS[surroundingMines - 1]);
            } else {
                revealSurroundingTiles();
            }

            g.incrementRevealedTiles();
            g.checkWin();
        }
    }

    void revealTileAfterLosing() {
        if (mine && !flagged) {
            this.setIcon(MINE_ICON);
        } else if (!mine && flagged) {
            this.setIcon(FLAG_CROSS_ICON);
        }
    }

    void toggleFlag() {
        if (g.isGameOver()) return;

        flagged = !flagged;

        if (flagged) {
            g.updateRemainingMines(true);
            this.setIcon(FLAG_ICON);
        } else {
            g.updateRemainingMines(false);
            this.setIcon(null);
        }
    }

    ArrayList<GameTile> getSurroundingTiles() {
        return getSurroundingTiles(-1,-1,1,1,false);
    }

    ArrayList<GameTile> getSurroundingTiles(int xStart, int yStart, int xEnd, int yEnd, boolean includeSelf) {

        ArrayList<GameTile> ret = new ArrayList<>();

        /*
        Set the range of the tiles to scan

        Example:
        If this tile is in the leftmost column then the tiles on the left mustn't be scanned
        # X X   O = This tile
        # O X   X = Surrounding tiles
        # X X   # = Wall
        */

        if (-xStart > xPos) xStart = -xPos;
        if (-yStart > yPos) yStart = -yPos;
        if (xEnd + xPos >= g.getCols()) xEnd = g.getCols() - xPos - 1;
        if (yEnd + yPos >= g.getRows()) yEnd = g.getRows() - yPos - 1;

        // Start scanning
        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                //System.out.println((this.xPos + x) + ", " + (this.yPos + y));
                ret.add(g.getTile(this.xPos + x, this.yPos + y));
            }
        }

        if (!includeSelf) ret.remove(this);  // Remove the original tile from the list if includeSelf is false
        return ret;
    }

    int countSurroundingMines() {
        int mines = 0;
        for (GameTile x : getSurroundingTiles()) {
            if (x.mine) mines++;
        }
        return mines;
    }

    void revealSurroundingTiles() {
        for (GameTile x : getSurroundingTiles()) {
            x.revealTile();
        }
    }

    void reset() {
        revealed = false;
        mine = false;
        flagged = false;

        this.setBackground(NEUTRAL_COLOR);
        this.setForeground(null);
        this.setText(null);
        this.setIcon(null);
        this.setBorder(BORDER);
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void optimizedMouseClicked(MouseEvent e) {
        if (!revealed) {
            if (e.getButton() == 1) {
                // Left click -> Reveal the tile
                revealTile();
            } else if (e.getButton() == 3) {
                // Right click -> Place/remove flag
                toggleFlag();
            }
        }
    }

    // MouseListener implemented methods
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!revealed) {
            this.setBackground(NEUTRAL_COLOR.darker());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!revealed) {
            this.setBackground(NEUTRAL_COLOR);
            if (touchingMouse) {
                this.optimizedMouseClicked(e);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.touchingMouse = true;

        if (!revealed) {
            this.setBackground(NEUTRAL_COLOR.brighter());
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.touchingMouse = false;

        if (!revealed) {
            this.setBackground(NEUTRAL_COLOR);
        }
    }

    public String toString() {
        //return Integer.toString(id);
        return "GameTile(" + xPos + ", " + yPos + ")";
    }
}
