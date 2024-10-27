package com.classicalai.eightpuzzle.environment;

public class EnvironmentState {

    private EnvironmentState parentState;
    private int emptyCellPosition;

    public EnvironmentState(int[] state) {
    }

    public EnvironmentState getParentState() {
        return parentState;
    }

    public void setParentState(EnvironmentState parentState) {
        this.parentState = parentState;
    }


    public boolean equals(EnvironmentState environmentState) {
        return false;
    }

    public int toNumeric(){
        return 0;
    }

    public int[] toArray(){
        return new int[0];
    }

    public EnvironmentState[] getChildren(){
        return new EnvironmentState[0];
    }

    public int getEmptyCellPosition() {
        return emptyCellPosition;
    }

    public void setEmptyCellPosition(int emptyCellPosition) {
        this.emptyCellPosition = emptyCellPosition;
    }
}

