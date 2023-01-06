/*
Minesweeper
Made by: Patrick K

A Minesweeper game I made using Java.
 */

package game;

import gui.ClockLabel;
import gui.GameMenuBar;
import gui.RemainingMinesLabel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game extends JFrame {

    public static final Font DEFAULT_FONT = new Font("Coolvetica Condensed Rg", Font.PLAIN, 24);

    private JPanel tilePanel;
    private GameTile[] tiles;
    private final RemainingMinesLabel remainingMinesLabel = new RemainingMinesLabel();
    private final ClockLabel clockLabel = new ClockLabel();
    private GameMenuBar gameMenuBar = new GameMenuBar(this);

    private int rows = 16;
    private int cols = 16;
    private int mines = 40;
    private int tileCount = rows * cols;
    private int revealedTiles;
    private int flags;

    private boolean gameRunning;
    private boolean gameOver;

    public Game() {

        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("sprites/mine.png").getImage());
        this.setTitle("Minesweeper");

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1,2));
        topPanel.setPreferredSize(new Dimension(0, 115));
        topPanel.add(remainingMinesLabel);
        topPanel.add(clockLabel);

        this.add(topPanel,BorderLayout.NORTH);
        this.setJMenuBar(gameMenuBar);

        setupTiles();
        resetGame();


        // some debug stuff (manually setup the mines)
        /*
        int[] mines = {11,12,41,55,56,66,70,80,90,92};
        for (int x : mines) {
            tiles[x].setMine(true);
        }
         */

        this.setVisible(true);
    }

    public void setupTiles() {
        // Sets up the tiles and tile panel

        tileCount = rows * cols;

        // Replace old tile panel with a new one
        if (tilePanel != null) {
            this.remove(tilePanel);
        }
        tilePanel = new JPanel();
        tilePanel.setLayout(new GridLayout(rows, cols, 5, 5));
        tilePanel.setPreferredSize(new Dimension(cols * 50, rows * 50));

        this.add(tilePanel);
        this.pack();

        tiles = new GameTile[rows * cols];
        for (int i = 0; i < rows * cols; i++) {
            tiles[i] = new GameTile(this,i);
            tilePanel.add(tiles[i]);
        }
    }

    public void generateMines(GameTile startingTile) {
        ArrayList<GameTile> shuffledTileList = new ArrayList<>(Arrays.asList(tiles));
        ArrayList<GameTile> safeRegion = startingTile.getSurroundingTiles(-1,-1,1,1,true);

        Collections.shuffle(shuffledTileList);
        shuffledTileList.removeAll(safeRegion);

        for (int i = 0; i < mines; i++) {
            shuffledTileList.get(i).setMine(true);
        }
    }

    public void resetGame() {
        gameRunning = false;
        gameOver = false;
        revealedTiles = 0;
        flags = 0;


        for (GameTile x : tiles) {
            x.reset();
        }

        remainingMinesLabel.setText(Integer.toString(mines));
        clockLabel.reset();
    }

    public void startGame(GameTile startingTile) {
        gameRunning = true;
        clockLabel.startClock();
        generateMines(startingTile);
    }

    public void checkWin() {
        if (revealedTiles == tileCount - mines) {
            clockLabel.stopClock();
            gameOver = true;
            JOptionPane.showMessageDialog(this,"you are win","Win",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void lose() {
        for (GameTile tile : tiles) {
            tile.revealTileAfterLosing();
        }
        clockLabel.stopClock();
        gameOver = true;
        JOptionPane.showMessageDialog(this,"you are die","Lose",JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateRemainingMines(boolean incrementFlags) {
        if (incrementFlags) flags++;
        else flags--;

        remainingMinesLabel.setText(Integer.toString(mines - flags));
    }

    public void setDifficulty(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        setupTiles();
        resetGame();
    }

    public void incrementRevealedTiles() {
        revealedTiles++;
    }

    public GameTile getTile(int i) {
        return tiles[i];
    }

    public GameTile getTile(int x, int y) {
        return tiles[x + cols * y];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public static void main(String[] args) {
        new Game();
    }
}
