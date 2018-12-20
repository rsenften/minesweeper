package org.senften.minesweeper;

/**
 * Hauptprogramm zum Starten des Spieles.
 */
public class Main  {

    public static void main(String[] args) {

        // Default value, if no valid argiment is given
        int size = 8;
        try {
            size = Integer.parseInt(args[0]);
        } catch (Exception ignored) {}

        new GUI(size);
    }

}
