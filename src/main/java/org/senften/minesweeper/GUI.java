package org.senften.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

/**
 * User: Daniel Senften <daniel@senften.org>
 * Date: 27.11.18, 11:29
 */
public class GUI extends JFrame {

    // Size in pixels of an indiwidual square used on our board
    private final int SQUARE_SIZE = 40;

    // Space in pixels bettween squares drawn on the board
    private final int SPACING = 1;

    // Startposition (upper left corner) of the board
    private final int X_OFFSET = 0, Y_OFFSET = 22;

    // Number of squares to be drawn on the board
    private final int GRID_SIZE;

    // Current position of the mouse pointer
    private double mousePositionX, mousePositionY;

    // Arrays to be used with the game
    private int[][] mines;
    private int[][] neighbours;
    private boolean[][] revealed;

    // Will be used for redrawing the GUI component.
    private static GUI gui;

    public GUI(int squares) {

        GRID_SIZE = squares;

        initArrays();
        initGUI();

        gui = this;
    }

    /**
     * Initialize GUI with all its visible components.
     */
    private void initGUI() {
        int width = X_OFFSET +
                (GRID_SIZE * SQUARE_SIZE) + SPACING;
        int height = Y_OFFSET +
                (GRID_SIZE * SQUARE_SIZE) + SPACING;

        this.setTitle("Minesweeper");
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        Board board = new Board(width, height);
        this.setContentPane(board);

        this.addMouseMotionListener(new Move());
        this.addMouseListener(new Click());
        this.setVisible(true);

    }

    /**
     * Initialize all internal arrays used during the game. The size
     * of the board (individual squares) is size x size.
     */
    private void initArrays() {

        mines = new int[GRID_SIZE][GRID_SIZE];
        neighbours = new int[this.GRID_SIZE][this.GRID_SIZE];
        revealed = new boolean[this.GRID_SIZE][this.GRID_SIZE];

        // Random number generator
        Random random = new Random();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {

                // Approximately 20% of all squares are mines
                if (random.nextInt(100) < 20) {
                    mines[row][col] = 1;
                } else {
                    mines[row][col] = 0;
                }
                revealed[row][col] = false;

            }
        }

        for (int row = 0; row < GRID_SIZE; row++) {

            // Rows considered above and below the current row
            int above, below;

            // Columns considered left and right of the current column
            int left, right;

            above = row - 1;
            below = row + 1;

            for (int col = 0; col < GRID_SIZE; col++) {

                left = col - 1;
                right = col + 1;

                // Number of mine cells in the 8 neighboring cells
                int countMines = 0;

                countMines += numberOfMines(above, left);
                countMines += numberOfMines(above, col);
                countMines += numberOfMines(above, right);
                countMines += numberOfMines(row, left);
                countMines += numberOfMines(row, right);
                countMines += numberOfMines(below, left);
                countMines += numberOfMines(below, col);
                countMines += numberOfMines(below, right);

                neighbours[row][col] = countMines;
            }
        }

    }

    /**
     * Reveal all cells around a zero mine cell.
     */
    private void revealZeroMineCells(int row, int col) {

        int above = row - 1;
        int below = row + 1;

        int left = col - 1;
        int right = col + 1;

        revealed[row][col] = true;

        if (zeroNeighbours(row, col)) {

            markZeroCells(above, left);
            markZeroCells(above, col);
            markZeroCells(above, right);
            markZeroCells(row, left);
            markZeroCells(row, right);
            markZeroCells(below, left);
            markZeroCells(below, col);
            markZeroCells(below, right);
        }
    }

    /**
     * Solange sich die Zelle innerhalb unseres Spielfeldes befindet
     * und noch nocht verarbeitet wurde rufen wir die Funktion
     * revealZeroMineCells() zur effektiven Verarbeitung der Zelle
     * auf.
     */
    private void markZeroCells(int row, int col) {
        if (cellInBoard(row, col) && !revealed[row][col])
            revealZeroMineCells(row, col);
    }

    /**
     * Liefert den Wert TRUE, sofern sich die Zelle innerhalb unseres
     * Spielbrettes befindet.
     */
    private boolean cellInBoard(int row, int col) {
        return row >= 0 && col >= 0 &&
                row < GRID_SIZE && col < GRID_SIZE;
    }

    /**
     * Liefert den Wert TRUE, sofern sich die Zelle innerhalb unseres
     * Spielbrettes befindet UND keine Nachbarn besitzt.
     */
    private boolean zeroNeighbours(int row, int col) {
        return cellInBoard(row, col) &&
                neighbours[row][col] == 0;
    }

    /**
     * Returns the number of mines in the given cell
     */
    private int numberOfMines(int row, int col) {
        return (row >= 0 && col >= 0 &&
                row < GRID_SIZE && col < GRID_SIZE &&
                mines[row][col] == 1) ? 1 : 0;
    }

    /**
     * Definition of our board including all fields with and without
     * mines.
     */
    private class Board extends JPanel {
        private final int width, height;

        Board(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            graphics.setColor(Color.DARK_GRAY);
            graphics.fillRect(0, 0, width, height);


            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {

                    int y = (row * SQUARE_SIZE) + SPACING;
                    int x = (col * SQUARE_SIZE) + SPACING;

                    // Initial color of all squares
                    graphics.setColor(Color.LIGHT_GRAY);

                    // Mark all mines
                    if (mines[row][col] == 1) {
                        graphics.setColor(Color.YELLOW);
                    }

                    // Show revealed fields
                    if (revealed[row][col]) {
                        graphics.setColor(Color.WHITE);
                    }

                    // Show the square as we move around
                    if (row == getSquareY() && col == getSquareX()) {
                        graphics.setColor(Color.RED);
                    }

                    graphics.fillRect(x, y,
                            SQUARE_SIZE - SPACING, SQUARE_SIZE - SPACING);

                    // Show number of mines
                    if (revealed[row][col]) {
                        graphics.setColor(Color.BLACK);
                        graphics.setFont(new Font("Arial", Font.BOLD, 20));
                        graphics.drawString("" + neighbours[row][col],
                                x + SQUARE_SIZE / 2 - 5,
                                y + SQUARE_SIZE / 2 + 5);
                    }

                }
            }
        }


    }

    private int getSquareX() {
        return (int) ((mousePositionX - X_OFFSET) / SQUARE_SIZE);
    }

    private int getSquareY() {
        return (int) ((mousePositionY - Y_OFFSET) / SQUARE_SIZE);
    }

    /**
     * Callback methods for every mouse motion on our board.
     */
    private class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mousePositionX = e.getPoint().getX();
            mousePositionY = e.getPoint().getY();
            //System.out.println("Moved: [X=" + mousePositionX + ", Y=" + mousePositionY + "]");
            gui.repaint();
        }
    }


    /**
     * Callback methods for every mouse click on our board
     */
    private class Click implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

            int row = getSquareY();
            int col = getSquareX();

            revealed[row][col] = true;
            if (neighbours[row][col] == 0)
                revealZeroMineCells(row, col);

            System.out.println("Clicked: [" +
                    row + "," +
                    col + "], Neighbours: " + neighbours[row][col]);

            if (mines[row][col] == 1)
                System.exit(0);

            gui.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

}
