package com.classicalai.eightpuzzle.algorithms;

import com.classicalai.eightpuzzle.environment.EnvironmentState;

public class DFSSolver extends Engine{

    public DFSSolver(EnvironmentState initialState){
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
