package com.algorithms.sudoko.models;

import com.algorithms.sudoko.enumerations.Difficulty;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameGenerator {
    private Set<Integer>[] rows = new Set[9];
    private Set<Integer>[] columns = new Set[9];
    private Set<Integer>[] boxes = new Set[9];
    private int[][] board = new int[9][9];
    private int[][] solvedBoard = new int[9][9];
    private int emptyCells;
    
    public GameGenerator(){
        Set<Integer> values = new HashSet<>(List.of(1,2,3,4,5,6,7,8,9));
        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>(values);
            columns[i] = new HashSet<>(values);
            boxes[i] = new HashSet<>(values);
        }
    }

    public void build(Difficulty difficulty){
        for (int i = 0; i < 9; i++) {
            this.board[0][i] = this.generateValue(0,i,0);
        }

        for (int i = 1; i < 9; i++) {
            this.board[i][0] = this.generateValue(i,0,1);
        }

        Solver solver = new Solver(board);
        solver.solve();
        this.board = solver.getSudokuBoard().getBoard();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.solvedBoard[i][j] = this.board[i][j];
            }
        }

        if (difficulty == Difficulty.EASY)
            this.deleteCells(40,46);

        else if (difficulty == Difficulty.MEDIUM)
            this.deleteCells(46,50);

        else if (difficulty == Difficulty.DIFFICULT)
            this.deleteCells(50,54);

        else
            this.deleteCells(54,58);
    }

    private void deleteCells(int lower, int upper){
        Random random = new Random();
        this.emptyCells = random.nextInt(lower,upper);
        int row1, row2 , col1, col2;
        int temp1, temp2;
        while(this.emptyCells > 0){
            row1 = random.nextInt(9);
            col1 = random.nextInt(9);
            temp1 = this.board[row1][col1];
            if(temp1 == 0)
                continue;

            this.board[row1][col1] = 0;

            if(new Solver(this.board).solve())
                this.emptyCells--;
            else{
                this.board[row1][col1] = temp1;
                continue;
            }

            row2 = 8-row1;
            col2 = 8-col1;
            temp2 = this.board[row2][col2];
            this.board[row2][col2] = 0;

            if(new Solver(this.board).solve())
                this.emptyCells--;
            else{
                this.board[row2][col2] = temp2;
                this.board[row1][col1] = temp1;
                this.emptyCells++;
                continue;
            }


        }

    }

    private int generateValue(int r, int c, int mod){
        int b = r/3 + 3*(c/3);
        Set<Integer> row = this.rows[r];
        Set<Integer> col = this.columns[c];
        Set<Integer> box = this.boxes[b];
        int value = 0;
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            if(mod > -1){
                value = this.getRandomValue(row,random);
                if(col.contains(value) && box.contains(value))
                    break;
            }
            if(mod > 0){
                value = this.getRandomValue(col,random);
                if(row.contains(value) && box.contains(value))
                    break;
            }
            if(mod > 1){
                value = this.getRandomValue(box,random);
                if(col.contains(value) && row.contains(value))
                    break;
            }
            value = 0;
        }
//        if (value == 0)
//            throw new IllegalStateException("Violation happened");

        row.remove(value);
        col.remove(value);
        box.remove(value);

        return value;
    }

    private int getRandomValue(Set<Integer> set, Random random) {
        if (set == null || set.isEmpty()) {
            throw new IllegalArgumentException("The Set cannot be empty.");
        }
        int randomIndex = random.nextInt(set.size());
        int i = 0;
        for (Integer element : set) {
            if (i == randomIndex) {
                return element;
            }
            i++;
        }
        throw new IllegalStateException("Something went wrong while picking a random element.");
    }

    public int[][] getBoard() {
        return board;
    }

    public static void main(String[] args){
        GameGenerator generator = new GameGenerator();



        System.out.println("=================================================");
        long start = System.currentTimeMillis() ;
        generator.build(Difficulty.EXTREMELY_DIFFICULT);
        long elapsed = System.currentTimeMillis()  - start ;
        System.out.println("elapsed time : " + elapsed + " ms");

        System.out.println("=================================================");
        int[][] b = generator.getBoard();
        for (int i = 0; i <9; i++) {
            for (int j = 0; j <9; j++) {
                System.out.print(b[i][j] + "\t");
            }
            System.out.println();
        }


    }

    public int[][] getSolvedBoard() {
        return solvedBoard;
    }
}
