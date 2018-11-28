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
    private final int squareSize = 40;

    // Space in pixels bettween squares drawn on the board
    private final int spacing = 8;

    // Startposition (upper left corner) of the board
    private final int xOffset = 0, yOffset = 22;

    // Number of squares to be drawn on the board
    private final int numberOfSquares;

    // Current position of the mouse pointer
    private double currentX, currentY;

    // Arrays to be used with the game
    private int[][] mines;
//    private int[][] neighbours;
//    private int[][] revealed;
//    private int[][] flagged;

    public GUI(int squares) {

        numberOfSquares = squares;

        initArrays(numberOfSquares);
        initGUI();
    }

    /**
     * Initialize GUI with all its visible components.
     *
     */
    private void initGUI() {
        int width = xOffset +
                (numberOfSquares * squareSize) + spacing;
        int height = yOffset +
                (numberOfSquares * squareSize) + spacing;

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
     *
     * @param size Number of square on each side
     */
    private void initArrays(int size) {
        mines = new int[size][size];
//        neighbours = new int[numberOfSquares][numberOfSquares];
//        revealed = new int[numberOfSquares][numberOfSquares];
//        flagged = new int[numberOfSquares][numberOfSquares];

        // Random number generator
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Approximately 20% of all squares are mines
                if (random.nextInt(100) < 20) {
                    mines[i][j] = 1;
                } else {
                    mines[i][j] = 0;
                }
            }
        }
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


            for (int i = 0; i < numberOfSquares; i++) {
                for (int j = 0; j < numberOfSquares; j++) {
                    int xPosition = (i * squareSize) + spacing;
                    int yPosition = (j * squareSize) + spacing;

                    // Initial color of all squares
                    graphics.setColor(Color.LIGHT_GRAY);

                    // Mark all mines
                    if (mines[i][j] == 1) {
                        graphics.setColor(Color.YELLOW);
                    }

                    // Mark the squares during our mouse movements
                    if (mouseInSquare(i, xPosition, yPosition)) {
                        graphics.setColor(Color.RED);
                    }

                    graphics.fillRect(
                            xPosition,
                            yPosition ,
                            squareSize - spacing,
                            squareSize - spacing);
                }
            }
        }


    }

    /**
     * Returns true, if mouse pointer is in current square.
     *
     */
    private boolean mouseInSquare(int i, int xPosition, int yPosition) {
        return currentX >= xOffset + xPosition &&
                currentX < xOffset + (i * squareSize) + squareSize &&
                currentY >= yOffset + yPosition &&
                currentY < yOffset + yPosition + squareSize - spacing;
    }

    /**
     * Retrieve the X/Y coordinates of the current (mouse over) square.
     *
     */
    private int[] getCoordinatesOfSquare() {
        for (int i = 0; i < numberOfSquares; i++) {
            for (int j = 0; j < numberOfSquares; j++) {
                int xPosition = (i * squareSize) + spacing;
                int yPosition = (j * squareSize) + spacing;


                if (mouseInSquare(i, xPosition, yPosition)) {
                    return new int[]{i, j};
                }

            }
        }
        return new int[]{-1, -1};
    }


    /**
     * Callback methods for every mouse motion on our board.
     */
    private class Move implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            // TODO Replace auto generated code
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            currentX = e.getPoint().getX();
            currentY = e.getPoint().getY();
            // System.out.println("Moved: [X=" + currentX + ", Y=" + currentY + "]");
        }
    }


    /**
     * Callback methods for every mouse click on our board
     */
    private class Click implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("Clicked: [" + getCoordinatesOfSquare()[0] + "," + getCoordinatesOfSquare()[1] + "]");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Replace auto generated code
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Replace auto generated code
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Replace auto generated code
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Replace auto generated code
        }
    }
}
