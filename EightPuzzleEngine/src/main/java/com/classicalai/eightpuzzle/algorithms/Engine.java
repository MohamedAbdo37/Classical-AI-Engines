package com.classicalai.eightpuzzle.algorithms;

public interface Engine {

    /**
     * @param initialState
     * @return array of state from intial state to the goal state
     */
    int[] play (int[] initialState);
}
