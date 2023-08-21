// Written by Fionn Murray

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Minesweeper implements MouseListener {
    private final int V_SIZE = 650;
    private final int H_SIZE = 550;
    static int rows;
    static int columns;

    int totalMines;
    int placedFlags = 0;
    int revealedTiles = 0;

    JFrame frame = new JFrame();
    JLabel mineLabel = new JLabel();
    JPanel panel = new JPanel();
    JButton[] buttons;

    int numTiles;
    Tile[] tiles;

    Minesweeper() {
        // rows and columns are initialised in startNewGame
        startNewGame();

        buttons = new JButton[rows * columns];
        numTiles = buttons.length;
        tiles = new Tile[numTiles];
        totalMines = (rows * columns) / 8;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(H_SIZE, V_SIZE);
        frame.setTitle("Minesweeper");
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        mineLabel.setHorizontalAlignment(JLabel.CENTER);
        mineLabel.setText("Mines: " + (totalMines-placedFlags));

        panel.setLayout(new GridLayout(rows, columns));

        for (int i=0; i<numTiles; i++) {
            tiles[i] = new Tile();
        }

        for (int i=0; i<numTiles; i++) {
            buttons[i] = new JButton();
            panel.add(buttons[i]);
            buttons[i].setFocusable(false);
            buttons[i].addMouseListener(this);
        }

        constructBoard();

        frame.add(mineLabel, BorderLayout.NORTH);
        frame.add(panel);
        frame.setResizable(true);

        frame.revalidate();
    }

    public static void main(String[] args) {
        new Minesweeper();
    }

    // offers multiple sizes of game board at game start
    private static void startNewGame() {
        Object[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Welcome to Minesweeper! What difficulty of puzzle would you like?",
                "New Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );

        switch (choice) {
            case JOptionPane.CLOSED_OPTION: System.exit(0);
            case 0: rows = 8; columns = 6; break;
            case 1: rows = 10; columns = 8; break;
            case 2: rows = 14; columns = 12; break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i=0; i<numTiles; i++) {
            if (e.getSource() == buttons[i]) {
                if (e.getButton() == 3) {
                    toggleFlag(i);
                }

                if (e.getButton() == 1) {
                    // if mine tile is clicked, game over is called with the value false
                    if (tiles[i].isMine && !tiles[i].isFlagged) {
                        mineDisplayMine(i);
                        gameOver(false);
                    } else if (!tiles[i].isFlagged) {
                        revealTiles(i);
                    }
                }
            }
        }
        //updates mineLabel with the estimated remaining number of mines
        mineLabel.setText("Mines: " + (totalMines-placedFlags));

        // if all non-mine tiles have been uncovered, display mines then call gameOver(true)
        if (revealedTiles == numTiles-totalMines) {
            for (int i=0; i<numTiles; i++) {
                if (tiles[i].isMine) {
                    mineDisplayMine(i);
                }
            }
            gameOver(true);
        }
    }

    // method to display a mine tile
    private void mineDisplayMine(int index) {
        buttons[index].setText("M");
        buttons[index].setBackground(new Color(200, 0, 0));
    }

    // method to display a non-mine tile
    private void tileDisplayEmpty(int index) {
        buttons[index].setText(tiles[index].adjacentMines > 0 ? "" + tiles[index].adjacentMines : "");
        buttons[index].setBackground(new Color(0, 100, 200));
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

    // recursively reveals adjacent empty tiles
    void revealTiles(int index) {
        if (!tiles[index].isRevealed) {
            ArrayList<Integer> adjacent = getAdjacentTiles(index);

            tiles[index].isRevealed = true;
            revealedTiles++;
            tileDisplayEmpty(index);

            if (tiles[index].adjacentMines == 0) {
                for (int tileIndex : adjacent) {
                    revealTiles(tileIndex);
                }
            }
        }
    }

    // handles the logic for toggling flag on and off
    void toggleFlag(int index) {
        if (tiles[index].isRevealed) return;

        if (tiles[index].isFlagged) {
            tiles[index].isFlagged = false;
            placedFlags--;
            buttons[index].setText("");
        } else {
            tiles[index].isFlagged = true;
            placedFlags++;
            buttons[index].setText("F");
        }
    }

    // displays dialogue for game end, and offers a rematch.
    // closes the program if a rematch is not requested
    void gameOver (boolean won) {
        JFrame ggFrame = new JFrame("Game Over");
        ggFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel dialogue = new JLabel();
        dialogue.setHorizontalAlignment(JLabel.CENTER);

        dialogue.setText(won ? "Congratulations, you won! Would you like to play again?" : "Oh dear, you lost! would you like to play again?");
        ggFrame.add(dialogue);

        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        yesButton.addActionListener(e -> {
            ggFrame.dispose();
            frame.dispose();

            new Minesweeper();
        });

        noButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        ggFrame.add(buttonPanel);

        ggFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        ggFrame.setSize(400, 150);
        ggFrame.setVisible(true);
    }

    // constructs a random board
    void constructBoard() {
        Random rand = new Random();
        for (int i=0; i<totalMines; i++) {
            int index = rand.nextInt(numTiles);

            if (tiles[index].isMine) {
                i--;
            } else {
                tiles[index].isMine = true;
                ArrayList<Integer> adjacent = getAdjacentTiles(index);

                for (int tileIndex : adjacent) {
                    if (!tiles[tileIndex].isMine) {
                        tiles[tileIndex].adjacentMines++;
                    }
                }
            }
        }
    }

    // iterator of adjacent tiles to a given tile index, returns arraylist of tile indexes
    ArrayList<Integer> getAdjacentTiles(int index) {
        ArrayList<Integer> output = new ArrayList<>();

        if (index-columns >= 0) {
            output.add(index-columns);

            if (index-1 > 0 && (index-1)%columns < index%columns) {
                output.add(index-1-columns);
            }

            if (index+1 < numTiles && (index+1)%columns > index%columns) {
                output.add(index+1-columns);
            }
        }

        if (index-1 > 0 && (index-1)%columns < index%columns) {
            output.add(index-1);
        }

        if (index+1 < numTiles && (index+1)%columns > index%columns) {
            output.add(index+1);
        }

        if (index+columns < numTiles) {
            output.add(index+columns);

            if ((index-1)%columns < index%columns) {
                output.add(index+columns-1);
            }

            if ((index+1)%columns > index%columns) {
                output.add(index+columns+1);
            }
        }

        return output;
    }
}
