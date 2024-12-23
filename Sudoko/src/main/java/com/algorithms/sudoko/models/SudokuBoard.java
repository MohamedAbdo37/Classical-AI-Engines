package com.algorithms.sudoko.models;

import java.util.ArrayList;

class SudokuBoard {

    /*
    Sudoku board is defined as 2d 9x9 array(board) of integers specifies value in each cell , 0 -> empty cell.

    sudoku domain is defined as 2d array , 9x9 array
    each cell is defined as arraylist contains the domain of this cell.

    countOfCompletedAssignments in board.
    */
    private ArrayList<Integer>[][] domains = new ArrayList[9][9] ;
    private int[][] board = new int[9][9] ;

    protected ArrayList<Integer>[][] getDomains() {
        return this.domains;
    }

    protected int[][] getBoard() {
        return this.board;
    }

    private int countOfCompletedAssignments = 0 ;

    protected int getCountOfCompletedAssignments() {
        return this.countOfCompletedAssignments;
    }

    protected void increaseCountOfCompletedAssignments(){
        this.countOfCompletedAssignments ++ ;
    }

    protected void setCountOfCompletedAssignments(int countOfCompletedAssignments) {
        this.countOfCompletedAssignments = countOfCompletedAssignments;
    }

    protected void clone(SudokuBoard clonedObject) {
        clonedObject.setCountOfCompletedAssignments(this.countOfCompletedAssignments);

        for(int i=0 ; i<9 ; i++){
            clonedObject.getBoard()[i] = this.board[i].clone();
            for(int j=0 ; j<9 ; j++){
                clonedObject.getDomains()[i][j] = new ArrayList<>(this.domains[i][j]) ;
            }
        }
    }

    // getSubgridNeighbours
    // get cells that in the 3x3 subgrid and not in the same row nor same column

    protected ArrayList<Integer> getSubgridNeighbours(int row , int col){

        int adjacentRow1 ;
        int adjacentRow2 ;
        int adjacentCol1 ;
        int adjacentCol2 ;

        if(row %3 == 0){
            adjacentRow1 = row + 1 ;
            adjacentRow2 = row + 2 ;

        }

        else if(row %3 == 1){
            adjacentRow1 = row - 1 ;
            adjacentRow2 = row + 1 ;
        }

        else {
            adjacentRow1 = row - 2 ;
            adjacentRow2 = row - 1 ;
        }



        if(col %3 == 0){
            adjacentCol1 = col + 1 ;
            adjacentCol2 = col + 2 ;

        }

        else if(col %3 == 1){
            adjacentCol1 = col - 1 ;
            adjacentCol2 = col + 1 ;
        }

        else {
            adjacentCol1 = col - 2 ;
            adjacentCol2 = col - 1 ;
        }

        ArrayList<Integer> subgridNeighbours= new ArrayList<>() ;
        subgridNeighbours.add(adjacentRow1) ;
        subgridNeighbours.add(adjacentRow2) ;
        subgridNeighbours.add(adjacentCol1) ;
        subgridNeighbours.add(adjacentCol2) ;

        return subgridNeighbours ;
    }
}
