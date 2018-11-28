package org.senften.minesweeper;

/**
 * User: Daniel Senften <daniel@senften.org>
 * Date: 27.11.18, 11:25
 */
public class Main implements Runnable {

    private static GUI gui;

    public static void main(String[] args) {

        // Default value, if no valid argiment is given
        int size = 8;
        try {
            size = Integer.parseInt(args[0]);
        } catch (Exception ignored) {}

        gui = new GUI(size);
        new Thread(new Main()).start();
    }

    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            gui.repaint();
        }
    }
}
