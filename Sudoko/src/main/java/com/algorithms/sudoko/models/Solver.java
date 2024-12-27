package com.algorithms.sudoko.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Solver {

    private final SudokuBoard sudokuBoard;
    private boolean generate = false ;

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    public Solver(int[][] initialState) {
        this.sudokuBoard = new SudokuBoard();
        for (int i = 0; i < 9; i++)
            this.sudokuBoard.getBoard()[i] = initialState[i].clone();
    }

    public SudokuBoard getSudokuBoard() {
        return sudokuBoard;
    }

    public boolean solve() {

        this.initialDomainReduction();

        if(!this.generate) {
            // writing initial domains before applying arc consistency
            String filename = "sudoku.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write("initial domains :\n");
                new printingDomains(writer).printDomains(this.sudokuBoard.getDomains());
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }

        // initial arc consistency
        boolean check = new ArcConsistency(this.sudokuBoard , this.generate).arcConsistency();
        if (!check)
            return false;
        boolean solution = new CSP(this.generate).backtrack(this.sudokuBoard);
        // apply back tracking
        if (solution) {
            System.out.println();
            for (int i = 0; i < 9; i++) {
                System.out.println(Arrays.toString(this.sudokuBoard.getBoard()[i]));
            }
            System.out.println("Solved !!!!!!");
        } else
            System.out.println("Inconsistent input");

        return solution;
    }

    public boolean haveOneSolution() {
        this.initialDomainReduction();
        new ArcConsistency(this.sudokuBoard , true).arcConsistency();
        return new CSP(this.generate).isUnique(this.sudokuBoard);
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

        // empty game
//         int[][] initialState = new int[][] { {0,0,0,0,0,0,0,0,0},
//         {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0},
//         {0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0},
//         {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0}} ;

         // easy
//         int[][] initialState = new int[][]
//         {{8,0,9,2,0,1,0,7,4},{1,2,3,7,5,0,0,6,9},{5,0,4,8,9,6,3,1,0},
//         {7,4,0,1,6,9,2,0,8},{0,1,0,0,8,0,7,9,0},{0,0,0,0,0,7,0,0,1},
//         {0,0,0,6,7,8,9,0,3},{9,0,7,3,4,2,0,5,6},{2,3,0,0,0,0,4,8,7}} ;


        // medium game
//         int[][] initialState = new int[][] { {7,0,0,9,0,3,4,6,0},
//         {3,6,9,4,0,0,0,8,0}, {2,0,8,0,1,0,0,0,5},
//         {5,0,6,3,0,0,0,1,0},{0,0,7,0,0,0,2,0,0}, {0,8,0,0,0,2,6,0,9},
//         {6,0,0,0,7,0,8,0,1}, {0,7,0,0,0,6,5,4,3}, {0,5,4,2,0,8,0,0,6}} ;

        // hard game
//         int[][] initialState = new int[][] { {3,0,0,2,0,0,9,7,0},
//         {4,0,0,8,9,0,0,0,6}, {0,0,0,1,0,0,0,4,0},
//         {1,6,0,0,0,4,7,0,0},{0,0,0,0,0,0,0,0,0}, {0,0,7,6,0,0,0,2,8},
//         {0,3,0,0,0,6,0,0,0}, {8,0,0,0,7,2,0,0,9}, {0,9,5,0,0,8,0,0,7}} ;


        //expert game
//         int[][] initialState = new int[][] { {0,4,3,0,5,0,2,0,7},
//         {0,2,0,0,0,4,0,0,6}, {7,0,0,0,0,0,0,0,0},
//         {0,0,7,0,1,0,6,0,0},{0,1,0,6,0,8,0,7,0}, {0,0,6,0,9,0,8,0,0},
//         {0,0,0,0,0,0,0,0,9}, {5,0,0,9,0,0,0,8,0}, {6,0,1,0,7,0,5,2,0}} ;


        // extremely hard game
         int[][] initialState = new int[][] { {0,0,4,0,0,7,0,0,0},
         {0,9,0,0,0,0,0,6,0}, {0,0,0,0,5,0,0,0,0},
         {8,0,0,0,0,0,7,0,0},{0,1,0,9,0,0,0,0,0}, {5,0,0,0,0,0,0,0,0},
         {0,2,0,0,0,0,9,0,1}, {7,0,0,0,8,0,0,0,0}, {0,0,0,6,0,0,0,5,0}} ;


        long start = System.currentTimeMillis();
        new Solver(initialState).solve();
        //System.out.println(new Solver(initialState).haveOneSolution());
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("elapsed time : " + elapsed + " ms");

    }


}
