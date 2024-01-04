package wumpus;

public class Wumpus {
    private int column;
    private int row;
    private char direction;

    // A három paraméteres konstruktor
    public Wumpus(int column, int row, char direction) {
        this.column = column;
        this.row = row;
        this.direction = direction;
    }
    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

}
