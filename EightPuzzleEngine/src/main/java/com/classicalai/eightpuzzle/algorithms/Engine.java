package com.classicalai.eightpuzzle.algorithms;

import com.classicalai.eightpuzzle.environment.EnvironmentState;

public abstract class Engine {
    protected final EnvironmentState initialState;
    protected int searchDepth;
    protected int nodesExpanded;
    protected double runningTime;
    protected EnvironmentState result;

    public Engine(EnvironmentState initialState){
        this.initialState = initialState;
        this.nodesExpanded = 1;
        this.runningTime = 0.0;
        this.searchDepth = 0;
    }

    /**
     * @return array of states from initial state to the goal state
     */
    public abstract EnvironmentState[] play();

// Setters
    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public void setNodesExpanded(int nodesExpanded) {
        this.nodesExpanded = nodesExpanded;
    }

    public void setRunningTime(double runningTime) {
        this.runningTime = runningTime;
    }

//    Getters

    public int getSearchDepth() {
        return searchDepth;
    }

    public int getNodesExpanded() {
        return nodesExpanded;
    }

    public double getRunningTime() {
        return runningTime;
    }

    public EnvironmentState[] display_solution(){

        EnvironmentState[] path_to_goal = new EnvironmentState[this.result.getDepth()+1] ;
        for(int i=path_to_goal.length-1 ; i>=0 ; i--){
            path_to_goal[i] = this.result ;
            this.result = this.result.getParentState() ;
        }

        return path_to_goal ;
    }
}
