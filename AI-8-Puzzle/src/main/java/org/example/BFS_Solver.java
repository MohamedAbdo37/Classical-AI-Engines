package org.example;
import java.util.* ;

public class BFS_Solver {

    private int Search_depth;
    private int expanded_nodes ;
    private Puzzle_State result ;

    void Breadth_First_Search(Puzzle_State initialState){

        Queue<Puzzle_State> frontier = new LinkedList<>() ;
        frontier.add(initialState) ;

        HashSet<Integer> Reached_Before = new HashSet<>() ;
        Reached_Before.add(initialState.getBoard()) ;

        this.Search_depth = 0 ;    // maximum depth of search tree
        this.expanded_nodes = 0 ;
        this.result = null ;

        while(!frontier.isEmpty()){
            Puzzle_State state = frontier.remove() ;  // the node that will be next expanded
            this.expanded_nodes ++ ;
            if(state.getBoard() == 12345678){  // goal is reached
                this.result = state ;
                return;
            }

            ArrayList<Puzzle_State> children = state.getStateChildren() ; // Array list of all possible children
            // iterate over state children to check if any one was reached before
            // if this state is distinct , then add it to frontier
            for(int i=0 ; i<children.size() ; i++){
                if(!Reached_Before.contains(children.get(i).getBoard())){
                    frontier.add(children.get(i));
                    // since BFS expand search tree level by level , the maximum depth is the depth of the last level added to frontier
                    this.Search_depth = children.get(i).getDepth() ;
                    Reached_Before.add(children.get(i).getBoard()) ;
                }
            }
        }
        // goal not reached ( this case isn't possible ) , solvability will be checked earlier
    }

    // Getters
    public int getSearch_depth() {
        return Search_depth;
    }

    public int getExpanded_nodes() {
        return expanded_nodes;
    }

    public Puzzle_State getResult() {return result;}
}
