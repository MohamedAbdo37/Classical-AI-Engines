package com.algorithms.sudoko.models;

import java.util.*;

public class GameGenerator {
    private Set<Integer>[] rows = new Set[9];
    private Set<Integer>[] columns = new Set[9];
    private Set<Integer>[] boxes = new Set[9];
    private int[][] board = new int[9][9];
    
    public GameGenerator(){
        Set<Integer> values = new HashSet<>(List.of(1,2,3,4,5,6,7,8,9));
        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>(values);
            columns[i] = new HashSet<>(values);
            boxes[i] = new HashSet<>(values);
        }
    }

    public void build(){
        for (int i = 0; i < 9; i++) {
            this.board[0][i] = this.generateValue(0,i,0);
        }

        for (int i = 1; i < 9; i++) {
            this.board[i][0] = this.generateValue(i,0,1);
        }

        for(int i= 0 ; i<9 ; i++){
            System.out.println(Arrays.toString(this.board[i]));
        }
        Solver solver = new Solver(board);
        solver.solve();
        this.board = solver.getSudokuBoard().getBoard();
    }

    private int generateValue(int r, int c, int mod){
        int b = r/3 + 3*(c/3);
        Set<Integer> row = this.rows[r];
        Set<Integer> col = this.columns[c];
        Set<Integer> box = this.boxes[b];
        System.out.println("("+r+", "+c+")");
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
        generator.build();
        long elapsed = System.currentTimeMillis()  - start ;
        System.out.println("elapsed time : " + elapsed + " ms");

        System.out.println("=================================================");




    }
}
