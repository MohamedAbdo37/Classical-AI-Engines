package org.example;
import java.util.* ;

public class DFS_Solver {
    private int Search_depth;
    private int expanded_nodes ;
    private Puzzle_State result ;

    void Depth_First_Search(Puzzle_State initialState){

        Stack<Puzzle_State> frontier = new Stack<>() ;
        frontier.push(initialState) ;

        HashSet<Integer> Reached_Before = new HashSet<>() ;
        Reached_Before.add(initialState.getBoard()) ;

        this.Search_depth = 0 ;    // maximum depth of search tree
        this.expanded_nodes = 0 ;
        this.result = null ;

        //System.out.println(initialState.getBoard());

        while(!frontier.isEmpty()){
            Puzzle_State state = frontier.pop() ;  // the node that will be next expanded
            this.expanded_nodes ++ ;
            if(state.getBoard() == 12345678){  // goal is reached
                this.result = state ;
                return;
            }

            ArrayList<Puzzle_State> children = state.getStateChildren() ; // Array list of all possible children
            // iterate over state children to check if any one was reached before
            // if this state is distinct , then add it to frontier
            // since the stack is FILO we add the children in opposite order to get out from the stack in the same priority
            // (up - down - left - right)
            for(int i=children.size()-1 ; i>=0 ; i--){
                if(!Reached_Before.contains(children.get(i).getBoard())){
                    frontier.push(children.get(i));
                    // since DFS expand search tree deeply we will take the maximum of current node depth and maximum depth reached before
                    this.Search_depth = Math.max(children.get(i).getDepth() , this.Search_depth) ;
                    Reached_Before.add(children.get(i).getBoard()) ;
                }
            }
        }
        // goal not reached ( this case isn't possible ) , solvability will be checked earlier
    }

    // Getters
    public int getSearch_depth() {return Search_depth;}
    public int getExpanded_nodes() {return expanded_nodes;}
    public Puzzle_State getResult() {return result;}
}
