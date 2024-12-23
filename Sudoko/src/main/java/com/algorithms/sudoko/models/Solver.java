package com.algorithms.sudoko.models;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {

    public void solve(SudokuBoard sudokuBoard){
        initialDomainReduction(sudokuBoard) ;
        // initial arc consistency
        new ArcConsistency().arcConsistency(sudokuBoard) ;
        System.out.println(sudokuBoard.getCountOfCompletedAssignments()) ;
    }


    /*
    Initial Domain Reduction: Before applying arc consistency, initialize the domains of each
     variable based on the initial puzzle.
     For each pre-filled cell, remove all other values from its domain. For each empty cell,
     initialize its domain to [1, 2, 3, 4, 5, 6, 7, 8, 9]

     returns number of completed assignments.
    */
    private void initialDomainReduction(SudokuBoard sudokuBoard){

        for(int i=0; i<9 ; i++){
            for (int j=0 ; j<9 ; j++){
                sudokuBoard.getDomains()[i][j] = new ArrayList<>() ;
                if(sudokuBoard.getBoard()[i][j] == 0)
                    Collections.addAll(sudokuBoard.getDomains()[i][j] , 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9) ;
                else {
                    sudokuBoard.getDomains()[i][j].add(sudokuBoard.getBoard()[i][j]);
                    sudokuBoard.increaseCountOfCompletedAssignments() ;
                }
            }
        }
        System.out.println(sudokuBoard.getCountOfCompletedAssignments());
    }

    public static void main(String[] args) {

//            int[][] initialState = new int[][] {{7,9,0,0,1,3,6,0,0},{4,0,0,0,7,0,3,0,0},{1,0,0,2,4,0,9,7,5},
//            {5,0,0,6,0,0,2,0,7},{0,7,0,0,0,1,8,0,0},{8,0,6,9,2,0,5,0,0},
//            {6,0,1,0,0,2,0,5,3},{3,0,0,0,0,0,4,0,9},{0,2,4,0,3,5,0,0,0}} ;

//
//        int[][] initialState = new int[][] { {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0},
//                {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}} ;


        int[][] initialState = new int[][] {{8,0,9,2,0,1,0,7,4},{1,2,3,7,5,0,0,6,9},{5,0,4,8,9,6,3,1,0},
                                            {7,4,0,1,6,9,2,0,8},{0,1,0,0,8,0,7,9,0},{0,0,0,0,0,7,0,0,1},
                                            {0,0,0,6,7,8,9,0,3},{9,0,7,3,4,2,0,5,6},{2,3,0,0,0,0,4,8,7}} ;

        SudokuBoard sudokuBoard = new SudokuBoard() ;
        for(int i=0 ; i<9 ; i++)
            sudokuBoard.getBoard()[i] = initialState[i].clone() ;

        new Solver().solve(sudokuBoard);

    }

}
