package com.classicalai.eightpuzzle.algorithms;

import com.classicalai.eightpuzzle.environment.EnvironmentState;

public abstract class Engine {
    private final EnvironmentState initialState;
    private int searchDepth;
    private int nodesExpanded;
    private double runningTime;

    public Engine(EnvironmentState initialState){
        this.initialState = initialState;
        this.nodesExpanded = 1;
        this.runningTime = 0.0;
        this.searchDepth = 0;
    }

    /**
     * @return array of states from initial state to the goal state
     */
    abstract EnvironmentState[] play();

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
    public EnvironmentState getInitialState() {
        return initialState;
    }

    public int getSearchDepth() {
        return searchDepth;
    }

    public int getNodesExpanded() {
        return nodesExpanded;
    }

    public double getRunningTime() {
        return runningTime;
    }
}
