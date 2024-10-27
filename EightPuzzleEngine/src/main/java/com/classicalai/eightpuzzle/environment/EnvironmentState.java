package com.classicalai.eightpuzzle.environment;

public class EnvironmentState {

    private int board;
    private EnvironmentState parentState;

    public EnvironmentState getParentState() {
        return parentState;
    }

    public void setParentState(EnvironmentState parentState) {
        this.parentState = parentState;
    }

    public void setBoard(int board) {
        this.board = board;
    }

    public boolean equals(EnvironmentState environmentState) {
        return false;
    }

    public int toNumeric() {
        return 0;
    }

    public int[] toArray() {
        return new int[0];
    }

    public EnvironmentState[] getChildren() {
        return new EnvironmentState[0];
    }

    public int getBoard() {
        return board;
    }
}
