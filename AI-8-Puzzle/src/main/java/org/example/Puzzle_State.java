package org.example;

public class Puzzle_State {
    private int Board;
    private int Empty_cell_Position;
    private Puzzle_State parent ;
    private int depth ;

    public Puzzle_State(int Board, int Empty_cell_Position, Puzzle_State parent, int depth){
        this.Board = Board;
        this.Empty_cell_Position = Empty_cell_Position;
        this.parent = parent ;
        this.depth = depth ;
    }

    public int getBoard() {
        return this.Board;
    }

    public int getEmpty_cell_Position() {
        return this.Empty_cell_Position;
    }

    public Puzzle_State getParent() {
        return this.parent;
    }

    public int getDepth() {
        return depth;
    }
}
