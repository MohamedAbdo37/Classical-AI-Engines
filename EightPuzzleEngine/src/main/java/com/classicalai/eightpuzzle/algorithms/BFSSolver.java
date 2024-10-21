package com.classicalai.eightpuzzle.algorithms;

import com.classicalai.eightpuzzle.environment.EnvironmentState;

public class BFSSolver extends Engine{

    public BFSSolver(EnvironmentState initialState){
        super(initialState);
    }

    /**
     * @return array of states from initial state to the goal state
     */
    @Override
    public EnvironmentState[] play() {
        return new EnvironmentState[0];
    }
}
