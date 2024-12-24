package com.algorithms.sudoko.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Solver {

    private SudokuBoard sudokuBoard;

    public Solver(int[][] initialState) {
        this.sudokuBoard = new SudokuBoard();
        for (int i = 0; i < 9; i++)
            this.sudokuBoard.getBoard()[i] = initialState[i].clone();
    }

    public void solve() {

        this.initialDomainReduction();
        // initial arc consistency
        new ArcConsistency(this.sudokuBoard).arcConsistency();
        // apply back tracking
        if (new CSP().backtrack(this.sudokuBoard)) {
            System.out.println();
            for (int i = 0; i < 9; i++) {
                System.out.println(Arrays.toString(this.sudokuBoard.getBoard()[i]));
            }
            System.out.println("Solved !!!!!!");
        } else
            System.out.println("Inconsistent input");

        return solution;
    }

    /*
     * Initial Domain Reduction: Before applying arc consistency, initialize the
     * domains of each
     * variable based on the initial puzzle.
     * For each pre-filled cell, remove all other values from its domain. For each
     * empty cell,
     * initialize its domain to [1, 2, 3, 4, 5, 6, 7, 8, 9]
     * 
     * returns number of completed assignments.
     */
    private void initialDomainReduction() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.sudokuBoard.getDomains()[i][j] = new ArrayList<>();
                if (this.sudokuBoard.getBoard()[i][j] == 0)
                    Collections.addAll(this.sudokuBoard.getDomains()[i][j], 1, 2, 3, 4, 5, 6, 7, 8, 9);
                else {
                    this.sudokuBoard.getDomains()[i][j].add(this.sudokuBoard.getBoard()[i][j]);
                    this.sudokuBoard.increaseCountOfCompletedAssignments();
                }
            }
        }
    }

    public int[][] getBoard() {
        return this.sudokuBoard.getBoard();
    }

    public static void main(String[] args) {

        // lab game
        // int[][] initialState = new int[][]
        // {{7,9,0,0,1,3,6,0,0},{4,0,0,0,7,0,3,0,0},{1,0,0,2,4,0,9,7,5},
        // {5,0,0,6,0,0,2,0,7},{0,7,0,0,0,1,8,0,0},{8,0,6,9,2,0,5,0,0},
        // {6,0,1,0,0,2,0,5,3},{3,0,0,0,0,0,4,0,9},{0,2,4,0,3,5,0,0,0}} ;

        // // lecture
        // int[][] initialState = new int[][]
        // {{8,0,9,5,0,1,7,3,6},{2,0,7,0,6,3,0,0,0},{1,6,0,0,0,0,0,0,0},
        // {0,0,0,0,9,0,4,0,7},{0,9,0,3,0,7,0,2,0},{7,0,6,0,8,0,0,0,0},
        // {0,0,0,0,0,0,0,6,3},{0,0,0,9,3,0,5,0,2},{5,3,2,6,0,4,8,0,9}} ;

        // empty game
        // int[][] initialState = new int[][] { {0,0,0,0,0,0,0,0,0},
        // {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0},
        // {0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0},
        // {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}} ;

        // easy
        // int[][] initialState = new int[][]
        // {{8,0,9,2,0,1,0,7,4},{1,2,3,7,5,0,0,6,9},{5,0,4,8,9,6,3,1,0},
        // {7,4,0,1,6,9,2,0,8},{0,1,0,0,8,0,7,9,0},{0,0,0,0,0,7,0,0,1},
        // {0,0,0,6,7,8,9,0,3},{9,0,7,3,4,2,0,5,6},{2,3,0,0,0,0,4,8,7}} ;

        // easy
        // int[][] initialState = new int[][]
        // {{0,0,0,0,7,0,0,3,5},{0,0,0,5,9,1,0,0,6},{0,6,0,0,0,0,8,9,0},
        // {3,0,0,0,6,0,0,0,8},{1,0,0,3,0,8,0,0,4},{6,0,0,0,2,0,0,0,7},
        // {0,8,2,0,0,0,0,6,0},{5,0,0,9,1,4,0,0,0},{9,7,0,0,8,0,0,0,0}} ;

        // // medium
        // int[][] initialState = new int[][]
        // {{0,0,6,0,2,0,3,0,0},{1,0,0,5,0,3,0,0,9},{0,0,4,6,0,8,1,0,0},
        // {0,0,9,2,0,1,8,0,0},{8,0,0,0,0,0,0,0,7},{0,0,2,8,0,7,6,0,0},
        // {0,0,5,9,0,6,2,0,0},{9,0,0,3,0,2,0,0,8},{0,0,3,0,1,0,5,0,0}} ;

        // hard
        // int[][] initialState = new int[][]
        // {{0,0,0,0,8,0,0,0,0},{0,0,6,0,0,0,3,0,0},{0,9,0,0,0,0,0,7,0},
        // {8,0,0,4,0,5,0,0,0},{0,0,0,0,7,0,0,0,0},{0,0,0,2,0,8,0,0,6},
        // {0,3,0,0,0,0,0,4,0},{0,0,2,0,0,0,6,0,0},{0,0,0,0,1,0,0,0,0}} ;

        // expert
        // int[][] initialState = new int[][]
        // {{0,0,0,0,0,0,0,0,8},{0,0,0,0,0,6,3,0,0},{0,0,2,0,9,0,0,7,0},
        // {0,0,0,7,0,0,0,5,0},{0,0,7,5,4,0,0,0,0},{0,3,0,0,0,1,0,0,0},
        // {8,6,0,0,0,0,1,0,0},{0,1,0,0,0,5,8,0,0},{0,0,4,0,0,0,0,9,0}} ;

        // conflicting
        int[][] initialState = new int[][] { { 0, 0, 0, 0, 7, 0, 0, 3, 5 }, { 0, 0, 0, 5, 9, 1, 0, 0, 6 },
                { 0, 6, 0, 0, 0, 0, 8, 9, 0 },
                { 3, 0, 0, 0, 6, 0, 0, 0, 8 }, { 1, 0, 0, 3, 0, 8, 0, 0, 4 }, { 6, 0, 0, 0, 2, 0, 0, 0, 7 },
                { 0, 8, 2, 0, 0, 0, 0, 6, 0 }, { 5, 0, 0, 9, 1, 4, 0, 0, 0 }, { 9, 7, 0, 0, 8, 0, 0, 0, 0 } };

        new Solver(initialState).solve();
        long start = System.currentTimeMillis();
        // for (int i = 0; i < 9; i++) {
        // System.out.println(Arrays.toString(sudokuBoard.getBoard()[i]));
        // }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("elapsed time : " + elapsed + " ms");
    }

}
