package com.algorithms.sudoko.models;

public class Arc {

    // Arcs: An arc in Sudoku represents a binary constraint between two variables
    // (cells).
    private int sourceRow ;

    private int sourceCol ;
    private int destinationRow ;
    private int destinationCol ;

    public Arc(int sourceRow, int sourceCol , int destinationRow, int destinationCol) {
        this.sourceRow = sourceRow;
        this.destinationRow = destinationRow;
        this.sourceCol = sourceCol;
        this.destinationCol = destinationCol;
    }

    public int getSourceRow() {
        return this.sourceRow;
    }

    public int getDestinationRow() {
        return this.destinationRow;
    }

    public int getSourceCol() {
        return this.sourceCol;
    }

    public int getDestinationCol() {
        return this.destinationCol;
    }

    @Override
    public String toString() {
        return "Arc{" +
                "sourceRow=" + sourceRow +
                ", sourceCol=" + sourceCol +
                ", destinationRow=" + destinationRow +
                ", destinationCol=" + destinationCol +
                '}';
    }
}
