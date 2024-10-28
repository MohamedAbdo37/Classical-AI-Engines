package com.classicalai.eightpuzzle.algorithms;
import com.classicalai.eightpuzzle.environment.EnvironmentState;

import java.util.* ;

public class IDSSolver extends Engine{
    private int limit;

    public IDSSolver(EnvironmentState initialState){
        super(initialState);
        this.setSearchDepth(0);   // maximum depth of search tree , in this case it is the depth of the cost and it is the limit
        this.setNodesExpanded(0) ;
        super.result = null ;
        this.limit = 0 ;  // limit of searching
    }

    /* @return array of states from initial state to the goal state */

    @Override
    public EnvironmentState[] play() {

        Stack<EnvironmentState> frontier = new Stack<>() ;
        // HashMap contains the reached states and their depth
        // HashMap <key , value> = HashMap<board , depth > because board is unique
        HashMap<Integer , Integer> Reached_Before = new HashMap<>() ;

        while (true) { // repeating dfs with increasing limit

            frontier.push(this.initialState) ;
            Reached_Before.put(this.initialState.getBoard() , 0) ;

            while (!frontier.isEmpty()) {
                EnvironmentState state = frontier.pop();  // the node that will be next expanded
                this.nodesExpanded++;

                if(state.getDepth() == this.limit) { // search for goal in the search depth equal to limit only
                    // because we are sure that no goal exists at lower limit
                    if (state.getBoard() == 12345678) {  // goal is reached
                        this.result = state;
                        this.searchDepth = state.getDepth() ;
                        return this.display_solution();
                    }
                }

                ArrayList<EnvironmentState> children = state.getChildren(); // Array list of all possible children
                for (int i=children.size()-1 ; i>=0 ; i--) {
                    if (!Reached_Before.containsKey(children.get(i).getBoard())) {
                        if(children.get(i).getDepth()<= limit) // if child isn't visited before and its depth <= limit , push it
                            //  if depth > limit ignore it
                            frontier.push(children.get(i));
                        Reached_Before.put(children.get(i).getBoard() , children.get(i).getDepth());
                    }
                    else{ // if the child was visited before , if the child was explored at higher depth push it again
                        //  if the child was visited at lower depth , there is no problem it is still in the stack and will be explored later
                        if(Reached_Before.get(children.get(i).getBoard()) > children.get(i).getDepth()){
                            frontier.push(children.get(i)) ;
                            Reached_Before.replace(children.get(i).getBoard() , children.get(i).getDepth()) ;
                        }
                    }
                }
            }
            Reached_Before.clear();
            this.limit ++ ; // increasing limit
        }
        // goal not reached ( this case isn't possible ) , solvability was checked earlier
    }

}