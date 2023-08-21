// class for the tile object
public class Tile {
    public boolean isMine;
    public boolean isRevealed;
    public boolean isFlagged;
    public int adjacentMines;

    Tile () {
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
    }
}
