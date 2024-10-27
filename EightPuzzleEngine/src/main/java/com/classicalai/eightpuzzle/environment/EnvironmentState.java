package com.classicalai.eightpuzzle.environment;

import java.util.ArrayList;

public class EnvironmentState {

    private int depth;
    private int board;
    private EnvironmentState parentState;
    private int emptyCellPosition;

    public EnvironmentState(int[] state) {
        this.board = 0;
        for (int i = 0; i < 9; i++) {
            this.board+= (int) (state[i] * Math.pow(10,i));
        }
    }

    public EnvironmentState(int Board, int emptyCellPosition, EnvironmentState parent, int depth){
        this.board = Board;
        this.emptyCellPosition = emptyCellPosition;
        this.parentState = parent ;
        this.depth = depth ;
    }

    public EnvironmentState getParentState() {
        return parentState;
    }

    public void setParentState(EnvironmentState parentState) {
        this.parentState = parentState;
    }


    public boolean equals(EnvironmentState environmentState) {
        return false;
    }

    private int toNumeric(int[] board){

        return 0;
    }

    public int[] toArray() {
        return new int[0];
    }

    public ArrayList<EnvironmentState> getChildren(){
        ArrayList<EnvironmentState> children = new ArrayList<>() ;

        int i = this.getEmptyCellPosition() ; // position of empty cell
        String x = Integer.toString(this.getBoard()) ;     // converting state to string
        if(i==0) // check if zero at left
            x = "0" + x ;

        if(i-3 >= 0){ // move up
            String moving_up = x ;
            char cell_up = moving_up.charAt(i-3) ;
            moving_up = moving_up.substring(0 , i-3) + "0" + moving_up.substring(i-2 , i) + cell_up + moving_up.substring(i+1 ) ;
            int y = Integer.parseInt(moving_up) ;
            children.add(new EnvironmentState(y , i-3 , this , getDepth()+1)) ;
        }

        if(i+3 <= 8) { // move down

            String moving_down = x ;
            char cell_down = moving_down.charAt(i+3) ;
            moving_down = moving_down.substring(0 , i) + cell_down + moving_down.substring(i+1 , i+3) + "0" + moving_down.substring(i+4 ) ;
            int y = Integer.parseInt(moving_down) ;
            children.add(new EnvironmentState(y , i+3 ,this , this.getDepth()+1)) ;
        }

        if(i%3 !=0){ // move left
            String moving_left = x ;
            char cell_left = moving_left.charAt(i-1) ;
            moving_left = moving_left.substring(0 , i-1) + "0" +  cell_left + moving_left.substring(i+1) ;
            int y = Integer.parseInt(moving_left) ;
            children.add(new EnvironmentState(y , i-1 , this , this.getDepth()+1)) ;
        }

        if(i %3 !=2){ // move right
            String moving_right = x ;
            char cell_right = moving_right.charAt(i+1) ;
            moving_right = moving_right.substring(0 , i)  + cell_right + "0" +  moving_right.substring(i+2 ) ;
            int y = Integer.parseInt(moving_right) ;
            children.add(new EnvironmentState(y , i+1 , this , this.getDepth()+1)) ;
        }

        return children ;
    }

    public int getEmptyCellPosition() {
        return emptyCellPosition;
    }

    public void setEmptyCellPosition(int emptyCellPosition) {
        this.emptyCellPosition = emptyCellPosition;
    }

    public void setBoard(int board) {
        this.board = board;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getBoard() {
        return board;
    }
}

