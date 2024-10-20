package org.example;

public class Puzzle_State {
    private int[] puzzle ;
    private int empty_Position ;
    private Puzzle_State parent ;

    public Puzzle_State(int[] puzzle , int empty_Position , Puzzle_State parent){
        this.puzzle = puzzle ;
        this.empty_Position = empty_Position ;
        this.parent = parent ;
    }

    public int[] getPuzzle() {
        return this.puzzle;
    }

    public int getempty_Position() {
        return this.empty_Position;
    }

    public Puzzle_State getParent() {
        return this.parent;
    }
}
