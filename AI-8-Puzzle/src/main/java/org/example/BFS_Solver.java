package org.example;
import java.util.* ;

public class BFS_Solver {

    private int Maximum_depth ;
    private int expanded_nodes ;
    Puzzle_State Breadth_First_Search(Puzzle_State initialState){

        Queue<Puzzle_State> frontier = new LinkedList<>() ;
        Queue<Puzzle_State> explored = new LinkedList<>() ;
        frontier.add(initialState) ;

        // adding states reached before
        HashSet<Integer> Reached_Before = new HashSet<>() ;
        Reached_Before.add(initialState.getBoard()) ;

        this.Maximum_depth = 0 ;
        this.expanded_nodes = 0 ;

        while(!frontier.isEmpty()){
            Puzzle_State state = frontier.remove() ;  // the node that will be next expanded
            explored.add(state);
            if(state.getBoard() == 12345678){  // goal is reached
                this.expanded_nodes = explored.size() -1 ;
                return state ;
            }

            int i = state.getEmpty_cell_Position() ; // position of empty cell
            String x = Integer.toString(state.getBoard()) ;     // converting state to string
            if(i==0)
                x = "0" + x ;

            if(i-3 >= 0){ // move up
                String moving_up = x ;
                char cell_up = moving_up.charAt(i-3) ;
                moving_up = moving_up.substring(0 , i-3) + "0" + moving_up.substring(i-2 , i) + cell_up + moving_up.substring(i+1 ) ;
                int y = Integer.parseInt(moving_up) ;

                // check if the node is in frontier or explored queues
                if(!Reached_Before.contains(y)){
                    frontier.add(new Puzzle_State(y,  i- 3, state, state.getDepth()+1 )) ;
                    Reached_Before.add(y) ;
                    this.Maximum_depth = state.getDepth()+1 ;
                }
            }

            if(i+3 <= 8) { // move down

                String moving_down = x ;
                char cell_down = moving_down.charAt(i+3) ;
                moving_down = moving_down.substring(0 , i) + cell_down + moving_down.substring(i+1 , i+3) + "0" + moving_down.substring(i+4 ) ;
                int y = Integer.parseInt(moving_down) ;

                // check if the node is in frontier or explored queues
                if(!Reached_Before.contains(y)){
                    frontier.add(new Puzzle_State(y,  i+ 3, state, state.getDepth()+1 )) ;
                    Reached_Before.add(y) ;
                    this.Maximum_depth = state.getDepth()+1 ;
                }
            }

            if(i%3 !=0){ // move left
                String moving_left = x ;
                char cell_left = moving_left.charAt(i-1) ;
                moving_left = moving_left.substring(0 , i-1) + "0" +  cell_left + moving_left.substring(i+1) ;
                int y = Integer.parseInt(moving_left) ;

                // check if the node is in frontier or explored queues
                if(!Reached_Before.contains(y)){
                    frontier.add(new Puzzle_State(y,  i-1 , state, state.getDepth()+1 )) ;
                    Reached_Before.add(y) ;
                    this.Maximum_depth = state.getDepth()+1 ;
                }
            }

            if(i %3 !=2){ // move right
                String moving_right = x ;
                char cell_right = moving_right.charAt(i+1) ;
                moving_right = moving_right.substring(0 , i)  + cell_right + "0" +  moving_right.substring(i+2 ) ;
                int y = Integer.parseInt(moving_right) ;

                // check if the node is in frontier or explored queues
                if(!Reached_Before.contains(y)){
                    frontier.add(new Puzzle_State(y,  i+ 1 , state, state.getDepth()+1 )) ;
                    Reached_Before.add(y) ;
                    this.Maximum_depth = state.getDepth()+1 ;
                }
            }
        }

        this.expanded_nodes = explored.size() ;
        return null;

    }

    public int getMaximum_depth() {
        return Maximum_depth;
    }

    public int getExpanded_nodes() {
        return expanded_nodes;
    }
}
