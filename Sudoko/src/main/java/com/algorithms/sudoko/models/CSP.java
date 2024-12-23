package com.algorithms.sudoko.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class CSP {

    // helper class for rank values of variable according to LCV
    private class valueRank {
        private int value;
        private int countOfConstrainingVariables;

        public valueRank(int value, int countOfConstrainingVariables) {
            this.value = value;
            this.countOfConstrainingVariables = countOfConstrainingVariables;
        }

        public int getValue() {
            return this.value;
        }
    }

    protected boolean backtrack(SudokuBoard sudokuBoard) {

        if (sudokuBoard.getCountOfCompletedAssignments() == 81) {
            return true;
        }

        ArrayList<Integer> variable = MRV(sudokuBoard.getDomains()); // getting MRV
        valueRank[] valueRanks = LCV(sudokuBoard , variable.get(0), variable.get(1)); // Sort values of variable according to LCV

        // iterate over all values and testing them
        for (valueRank rank : valueRanks) {

            // clone board to pass it to recursive call to save original image for backtracking
            SudokuBoard clonedObject = new SudokuBoard() ;
            sudokuBoard.clone(clonedObject) ;

            // clear domain and assign specific value to it
            clonedObject.getDomains()[variable.get(0)][variable.get(1)].clear();
            clonedObject.getDomains()[variable.get(0)][variable.get(1)].add(rank.getValue());

            // if assignment is consistent then apply backtracking again
            if (new ArcConsistency(clonedObject).arcConsistency()) {
                // if backtracking returned true (game has been solved)
                if (backtrack(clonedObject)) {
                    clonedObject.clone(sudokuBoard) ;
                    return true;
                }
                // if backtracking returned false (inconsistent assignment)
                // remove that value from variable domain
                else {
                    sudokuBoard.getDomains()[variable.get(0)][variable.get(1)].remove(Integer.valueOf(rank.getValue()));
                }
            }
            // if not consistent then remove that value from variable domain
            else {
                sudokuBoard.getDomains()[variable.get(0)][variable.get(1)].remove(Integer.valueOf(rank.getValue()));
            }
        }
        return false;
    }


    /*
     MRV: Choose the variable with the fewest legal values in its
     domain.
     order of variables is a tiebreaker.
    */
    private ArrayList MRV(ArrayList<Integer>[][] domains) {

        int MRV = 10; // since maximum domain size is 9 then 10 is considered as INFINITY
        int MRVRow = -1;
        int MRVCol = -1;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // if cell is not filled and has fewer values in its domain then it will be chosen as the next variable
                if (domains[i][j].size() > 1 && domains[i][j].size() < MRV) {
                    MRV = domains[i][j].size();
                    MRVRow = i;
                    MRVCol = j;
                }
            }
        }
        ArrayList<Integer> variable = new ArrayList<>();
        variable.add(MRVRow);
        variable.add(MRVCol);
        return variable; // return variable with fewer values in domain
    }

    /*
     LCV: choose the least constraining value: the one that rules out the fewest values in the remaining variables.
     order of values is a tiebreaker.
    */

    private valueRank[] LCV(SudokuBoard sudokuBoard, int MRVRow, int MRVCol) {

        // get constraining values count for all values then sort them
        valueRank[] valueRanks = new valueRank[sudokuBoard.getDomains()[MRVRow][MRVCol].size()];

        for (int k = 0; k < sudokuBoard.getDomains()[MRVRow][MRVCol].size(); k++) {   // iterate over possible values for that variable

            int x = sudokuBoard.getDomains()[MRVRow][MRVCol].get(k);
            int xCV = 0; // no constraints till now

            // iterate over elements in the same column
            for (int i = 0; i < 9; i++) {
                if (i != MRVRow && sudokuBoard.getDomains()[i][MRVCol].contains(x)) {
                    xCV++;
                }
            }

            // iterate over elements in the same row
            for (int j = 0; j < 9; j++) {
                if (j != MRVCol && sudokuBoard.getDomains()[MRVRow][j].contains(x)) {
                    xCV++;
                }
            }

            ArrayList<Integer> subgridIndexes = sudokuBoard.getSubgridNeighbours(MRVRow, MRVCol);
            int adjacentRow1 = subgridIndexes.get(0);
            int adjacentRow2 = subgridIndexes.get(1);
            int adjacentCol1 = subgridIndexes.get(2);
            int adjacentCol2 = subgridIndexes.get(3);

            if (sudokuBoard.getDomains()[adjacentRow1][adjacentCol1].contains(x))
                xCV++;

            if (sudokuBoard.getDomains()[adjacentRow1][adjacentCol2].contains(x))
                xCV++;

            if (sudokuBoard.getDomains()[adjacentRow2][adjacentCol1].contains(x))
                xCV++;

            if (sudokuBoard.getDomains()[adjacentRow2][adjacentCol2].contains(x))
                xCV++;

            valueRanks[k] = new valueRank(x, xCV);
        }

        Arrays.sort(valueRanks, Comparator.comparingInt(a -> a.countOfConstrainingVariables));

        return valueRanks;
    }

}