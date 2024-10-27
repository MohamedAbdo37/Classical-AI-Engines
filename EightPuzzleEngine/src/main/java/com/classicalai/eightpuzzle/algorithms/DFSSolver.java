package com.classicalai.eightpuzzle.algorithms;

import com.classicalai.eightpuzzle.environment.EnvironmentState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class DFSSolver extends Engine{

    public DFSSolver(EnvironmentState initialState){
        super(initialState);
        this.setSearchDepth(0);    // maximum depth of search tree
        this.setNodesExpanded(0) ;
        super.result = null ;
    }

    /**
     * @return array of states from initial state to the goal state
     */
    @Override
    public EnvironmentState[] play() {

        Stack<EnvironmentState> frontier = new Stack<>() ;
        frontier.push(this.initialState) ;

        HashSet<Integer> Reached_Before = new HashSet<>() ;
        Reached_Before.add(this.initialState.getBoard()) ;

        while(!frontier.isEmpty()){
            EnvironmentState state = frontier.pop() ;  // the node that will be next expanded
            this.nodesExpanded ++ ;
            if(state.getBoard() == 12345678){  // goal is reached
                this.result = state ;
                return this.display_solution();
            }

            ArrayList<EnvironmentState> children = state.getChildren() ; // Array list of all possible children
            // iterate over state children to check if any one was reached before
            // if this state is distinct , then add it to frontier
            // since the stack is FILO we add the children in opposite order to get out from the stack in the same priority
            // (up - down - left - right)
            for(int i=children.size()-1 ; i>=0 ; i--){
                if(!Reached_Before.contains(children.get(i).getBoard())){
                    frontier.push(children.get(i));
                    // since DFS expand search tree deeply we will take the maximum of current node depth and maximum depth reached before
                    this.searchDepth = Math.max(children.get(i).getDepth() , this.searchDepth) ;
                    Reached_Before.add(children.get(i).getBoard()) ;
                }
            }
        }
        // goal not reached ( this case isn't possible ) , solvability was checked earlier
        return null ;
    }
}
