package gui;

import misc.ScaledImageIcon;
import game.Game;

import javax.swing.*;

public class ClockLabel extends JLabel implements Runnable {

    //static final ImageIcon clockIcon = new ImageIcon(new ImageIcon("textures/clock.png").getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
    static final ImageIcon clockIcon = new ScaledImageIcon("sprites/clock.png",0.02f);
    private int secondsElapsed;
    private boolean running;

    public ClockLabel() {
        this.setFont(Game.DEFAULT_FONT.deriveFont(42f));  // Default font of size 42
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setVerticalTextPosition(JLabel.BOTTOM);
        this.setIcon(clockIcon);
    }

    @Override
    public void run() {
        long lastUpdate = System.currentTimeMillis();

        while (running) {
            if (System.currentTimeMillis() - lastUpdate >= 1000) {
                secondsElapsed++;
                lastUpdate += 1000;
                updateClock();
            }

            try {
                Thread.sleep(10);  // Sleep for 10 milliseconds every loop to reduce CPU usage
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateClock() {
        int seconds = secondsElapsed % 60;
        String secondsText = (seconds >= 10) ? String.valueOf(seconds) : "0" + seconds;
        String minutesText = String.valueOf(secondsElapsed / 60);
        this.setText(minutesText + ":" + secondsText);
    }

    public void startClock() {
        running = true;

        Thread clockThread = new Thread(this);
        clockThread.setPriority(1);
        clockThread.start();
    }

    public void stopClock() {
        running = false;
    }

    public void reset() {
        stopClock();
        secondsElapsed = 0;
        this.setText("0:00");
    }
}
