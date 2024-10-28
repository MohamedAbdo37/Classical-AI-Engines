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
            this.board+= (int) (state[8-i] * Math.pow(10,i));
        }
        this.setEmptyCellPosition(searchZero());
    }

    public int searchZero(){
        int[] temp = this.toArray();
        for (int i = 0; i < temp.length; i++) {
            if(temp[i] == 0)
                return i;
        }
        return -1; // zero doesn't exist
    }


    // constructor
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


    // converting integer of board to array of integers
    public int[] toArray() {
        int[] output = {0,0,0,0,0,0,0,0,0};
        for (int i = 0; i < 9; i++) {
            output[i] = (int) (this.board/Math.pow(10,8-i));
            output[i]%= 10;
        }
        return output;
    }

    // getting all possible children (up-down-left-right)
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


    public int getDepth() {
        return depth;
    }

    public int getBoard() {
        return board;
    }

    // check solvability of board
    public static boolean checkSolvability(int[] input) {
        int invCount = 0;
        for (int i = 0; i < 9; i++)
            for (int j = i + 1; j < 9; j++)
                // Value 0 is used for empty space
                if (input[i] != 0 && input[j] != 0 && input[i] > input[j])
                    invCount++;

        return invCount % 2 != 0;  // solvable
    }
}

