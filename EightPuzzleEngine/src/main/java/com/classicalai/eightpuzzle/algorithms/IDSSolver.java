package com.classicalai.eightpuzzle.algorithms;

import com.classicalai.eightpuzzle.environment.EnvironmentState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class IDSSolver extends Engine{
    private int limit;

    public IDSSolver(EnvironmentState initialState){
        super(initialState);
        this.setSearchDepth(0);   // maximum depth of search tree
        this.setNodesExpanded(0) ;
        super.result = null ;
        this.limit = 0 ;  // limit of searching
    }

    /**
     * @return array of states from initial state to the goal state
     */
    @Override
    public EnvironmentState[] play() {
        Stack<EnvironmentState> frontier = new Stack<>() ;
        HashSet<Integer> Reached_Before = new HashSet<>() ;

        while (true) { // repeating dfs with increasing limit

            frontier.push(this.initialState) ;
            Reached_Before.add(this.initialState.getBoard()) ;

            while (!frontier.isEmpty()) {
                EnvironmentState state = frontier.pop();  // the node that will be next expanded
                this.nodesExpanded++;

                if(state.getDepth() == this.limit) { // search for goal in the search depth equal to limit only
                    if (state.getBoard() == 12345678) {  // goal is reached
                        this.result = state;
                        this.searchDepth = state.getDepth() ;
                        return this.display_solution();
                    }
                }

                if (state.getDepth() < this.limit) {
                    ArrayList<EnvironmentState> children = state.getChildren(); // Array list of all possible children
                    for (int i = children.size() - 1; i >= 0; i--) {
                        if (!Reached_Before.contains(children.get(i).getBoard())) {
                            frontier.push(children.get(i));
                            Reached_Before.add(children.get(i).getBoard());
                        }
                    }
                }
            }
            Reached_Before.clear();
            this.limit ++ ;
        }
        // goal not reached ( this case isn't possible ) , solvability will be checked earlier
    }

}
