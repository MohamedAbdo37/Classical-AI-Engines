package com.classicalai.eightpuzzle.algorithms;
import com.classicalai.eightpuzzle.environment.EnvironmentState;

import java.util.* ;

public class BFSSolver extends Engine{

    public BFSSolver(EnvironmentState initialState){
        super(initialState);
        this.setSearchDepth(0);   // maximum depth of search tree
        this.setNodesExpanded(0) ;
        super.result = null ;
    }

    /* @return array of states from initial state to the goal state */
    @Override
    public EnvironmentState[] play() {

        // queue for nodes that will expanded at the next times 
        Queue<EnvironmentState> frontier = new LinkedList<>() ;
        frontier.add(this.initialState) ;

        // Hashset contains all nodes that were visited or nodes that are in the frontier at the current time
        // Hashset gives O(1) operations (instead of linear search in visited and frontier queues)
        // we now don't want visited array
        HashSet<Integer> Reached_Before = new HashSet<>() ;
        Reached_Before.add(this.initialState.getBoard()) ;

        while(!frontier.isEmpty()){
            EnvironmentState state = frontier.remove() ;  // the node that will be next expanded
            this.nodesExpanded ++ ;
            if(state.getBoard() == 12345678){  // goal is reached
                this.result = state ;
                return this.display_solution();
            }

            ArrayList<EnvironmentState> children = state.getChildren() ; // Array list of all possible children
            // iterate over state children to check if any one was reached before
            // if this state is distinct , then add it to frontier
            for (EnvironmentState child : children) {
                if (!Reached_Before.contains(child.getBoard())) {
                    frontier.add(child);
                    // since BFS expand search tree level by level , the maximum depth is the depth of the last level added to frontier
                    this.searchDepth = child.getDepth();
                    Reached_Before.add(child.getBoard());
                }
            }
        }
        // goal not reached ( this case isn't possible ) , solvability was checked earlier
        return null ;

    }
}
