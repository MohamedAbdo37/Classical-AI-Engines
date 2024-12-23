package com.algorithms.sudoko.models;

import java.util.ArrayList;

class Arc {

    // Arcs: An arc in Sudoku represents a binary constraint between two variables
    // (cells).

    // row , col of cell that has the arc out
    // row , col of cell that has the arc in
    private int sourceRow ;
    private int sourceCol ;
    private int destinationRow ;
    private int destinationCol ;

    protected Arc(int sourceRow, int sourceCol , int destinationRow, int destinationCol) {
        this.sourceRow = sourceRow;
        this.destinationRow = destinationRow;
        this.sourceCol = sourceCol;
        this.destinationCol = destinationCol;
    }

    protected int getSourceRow() {
        return this.sourceRow;
    }

    protected int getDestinationRow() {
        return this.destinationRow;
    }

    protected int getSourceCol() {
        return this.sourceCol;
    }

    protected int getDestinationCol() {
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
