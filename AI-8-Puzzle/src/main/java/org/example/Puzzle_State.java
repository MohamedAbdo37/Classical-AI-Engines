package org.example;

import java.util.*;

public class Puzzle_State {
    private int Board; // order of elements in board
    private int Empty_cell_Position; // index of empty cell
    private Puzzle_State parent ; // parent of this state
    private int depth ; // depth in search tree

    // constructor
    public Puzzle_State(int Board, int Empty_cell_Position, Puzzle_State parent, int depth){
        this.Board = Board;
        this.Empty_cell_Position = Empty_cell_Position;
        this.parent = parent ;
        this.depth = depth ;
    }

    // Getters
    public int getBoard() {
        return this.Board;
    }

    public int getEmpty_cell_Position() {
        return this.Empty_cell_Position;
    }

    public Puzzle_State getParent() {
        return this.parent;
    }

    public int getDepth() {return this.depth;
    }

    // get the possible four children (up - down - left - right)
    public ArrayList<Puzzle_State> getStateChildren (){
        ArrayList<Puzzle_State> children = new ArrayList<>() ;

        int i = this.getEmpty_cell_Position() ; // position of empty cell
        String x = Integer.toString(this.getBoard()) ;     // converting state to string
        if(i==0) // check if zero at left
            x = "0" + x ;

        if(i-3 >= 0){ // move up
            String moving_up = x ;
            char cell_up = moving_up.charAt(i-3) ;
            moving_up = moving_up.substring(0 , i-3) + "0" + moving_up.substring(i-2 , i) + cell_up + moving_up.substring(i+1 ) ;
            int y = Integer.parseInt(moving_up) ;
            children.add(new Puzzle_State(y , i-3 , this , getDepth()+1)) ;
        }

        if(i+3 <= 8) { // move down

            String moving_down = x ;
            char cell_down = moving_down.charAt(i+3) ;
            moving_down = moving_down.substring(0 , i) + cell_down + moving_down.substring(i+1 , i+3) + "0" + moving_down.substring(i+4 ) ;
            int y = Integer.parseInt(moving_down) ;
            children.add(new Puzzle_State(y , i+3 ,this , this.getDepth()+1)) ;
        }

        if(i%3 !=0){ // move left
            String moving_left = x ;
            char cell_left = moving_left.charAt(i-1) ;
            moving_left = moving_left.substring(0 , i-1) + "0" +  cell_left + moving_left.substring(i+1) ;
            int y = Integer.parseInt(moving_left) ;
            children.add(new Puzzle_State(y , i-1 , this , this.getDepth()+1)) ;
        }

        if(i %3 !=2){ // move right
            String moving_right = x ;
            char cell_right = moving_right.charAt(i+1) ;
            moving_right = moving_right.substring(0 , i)  + cell_right + "0" +  moving_right.substring(i+2 ) ;
            int y = Integer.parseInt(moving_right) ;
            children.add(new Puzzle_State(y , i+1 , this , this.getDepth()+1)) ;
        }

        return children ;
    }
}
