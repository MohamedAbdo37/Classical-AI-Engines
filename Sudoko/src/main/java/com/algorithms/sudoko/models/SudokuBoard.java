package com.algorithms.sudoko.models;

import java.util.ArrayList;

public class SudokuBoard {

    /*
    Sudoku board is defined as 2d 9x9 array(board) of integers specifies value in each cell , 0 -> empty cell.

    sudoku domain is defined as 2d array , 9x9 array
    each cell is defined as arraylist contains the domain of this cell.

    countOfCompletedAssignments in board.
    */
    private ArrayList<Integer>[][] domains = new ArrayList[9][9] ;
    private int[][] board = new int[9][9] ;

    public ArrayList<Integer>[][] getDomains() {
        return this.domains;
    }

    public int[][] getBoard() {
        return this.board;
    }

    private int countOfCompletedAssignments = 0 ;

    public int getCountOfCompletedAssignments() {
        return this.countOfCompletedAssignments;
    }

    public void increaseCountOfCompletedAssignments(){
        this.countOfCompletedAssignments ++ ;
    }

    public void setCountOfCompletedAssignments(int countOfCompletedAssignments) {
        this.countOfCompletedAssignments = countOfCompletedAssignments;
    }
}
