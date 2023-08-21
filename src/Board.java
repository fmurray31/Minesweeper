


public class Board {
    private Tile[][] board = new Tile[Minesweeper.rows][Minesweeper.columns];

    public Board() {
        for (int x = 0; x<Minesweeper.rows; x++) {
            for (int y = 0; y<Minesweeper.columns; y++) {
                board[x][y] = new Tile();
            }
        }
    }
}
