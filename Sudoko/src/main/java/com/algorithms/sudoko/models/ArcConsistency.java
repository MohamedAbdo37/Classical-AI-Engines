package com.algorithms.sudoko.models;
import java.util.*;




public class ArcConsistency {

    private boolean addAgain = false;
    private int exceptedRow = -1 ;
    private int exceptedCol = -1 ;


    // return false if in consistency is found , true otherwise
    public boolean arcConsistency(SudokuBoard sudokuBoard){

        // queue of all arcs , each cell has arc with all cells in same row , column , 3x3 sub grid
        // total 20 arc for each cell with total 20*81 = 1620 arc
        Queue<Arc> arcs = new LinkedList<>() ;
        for(int i=0 ; i<9 ; i++){
            for(int j=0 ; j<9 ; j++){
                addArcsToQueue(i,j,arcs);
            }
        }

        while (!arcs.isEmpty()){

            // (Xi , Xj) = remove queue
            Arc arc = arcs.remove() ;
            if (Revise(arc , sudokuBoard.getDomains())){

                if(sudokuBoard.getDomains()[arc.getSourceRow()][arc.getSourceCol()].isEmpty()) { // inconsistent -> return false
                    System.out.println(arc);
                    return false;
                }

                // for each Xk in Xi.neighbors-{Xj} add (Xk , Xi) to queue
                this.addAgain = true ;
                this.exceptedRow = arc.getDestinationRow() ;
                this.exceptedCol = arc.getDestinationCol() ;
                addArcsToQueue(arc.getSourceRow() , arc.getSourceCol() , arcs);
                this.addAgain = false ;
                this.exceptedRow = -1 ;
                this.exceptedCol = -1 ;
            }

        }
        updateSudokuGrid(sudokuBoard);
        return true ;
    }

    // return true if we revised the domain of Xi (domain is reduced)
    private boolean Revise(Arc arc , ArrayList<Integer> [][] domains){
        boolean revised = false ;

        for (int i=0 ; i<domains[arc.getSourceRow()][arc.getSourceCol()].size() ; i++){

            boolean thereIsMatchInY = false ;

            for(int y : domains[arc.getDestinationRow()][arc.getDestinationCol()]){
                if(domains[arc.getSourceRow()][arc.getSourceCol()].get(i)!=y){
                    thereIsMatchInY = true ; // there is match , x in Di is consistent with Dj
                    break ;
                }
            }

            // if no value y in Dj allows (x,y) to satisfy the constraint between Xi and Xj then delete x from Di
            if(!thereIsMatchInY){
                domains[arc.getSourceRow()][arc.getSourceCol()].remove(i) ;
                i-- ;
                revised = true ; // domain has reduced
            }
        }
        return revised ;
    }

    // add all arcs that point to specific cell to arcs queue
    private void addArcsToQueue(int row , int col , Queue<Arc> arcs){

        int adjacentRow1 ;
        int adjacentRow2 ;
        int adjacentCol1 ;
        int adjacentCol2 ;

        if(row %3 == 0){
            adjacentRow1 = row + 1 ;
            adjacentRow2 = row + 2 ;

        }

        else if(row %3 == 1){
            adjacentRow1 = row - 1 ;
            adjacentRow2 = row + 1 ;
        }

        else {
            adjacentRow1 = row - 2 ;
            adjacentRow2 = row - 1 ;
        }



        if(col %3 == 0){
            adjacentCol1 = col + 1 ;
            adjacentCol2 = col + 2 ;

        }

        else if(col %3 == 1){
            adjacentCol1 = col - 1 ;
            adjacentCol2 = col + 1 ;
        }

        else {
            adjacentCol1 = col - 2 ;
            adjacentCol2 = col - 1 ;
        }

        // get cells that in the 3x3 subgrid and not in the same row nor same column
        if(!(addAgain && this.exceptedRow == adjacentRow1 && this.exceptedCol == adjacentCol1))
            arcs.add(new Arc(adjacentRow1 , adjacentCol1 , row , col)) ;

        if(!(addAgain && this.exceptedRow == adjacentRow1 && this.exceptedCol == adjacentCol2))
            arcs.add(new Arc(adjacentRow1 , adjacentCol2 , row , col)) ;

        if(!(addAgain && this.exceptedRow == adjacentRow2 && this.exceptedCol == adjacentCol1))
            arcs.add(new Arc(adjacentRow2 , adjacentCol1 , row , col)) ;

        if(!(addAgain && this.exceptedRow == adjacentRow2 && this.exceptedCol == adjacentCol2))
            arcs.add(new Arc(adjacentRow2 , adjacentCol2 , row , col)) ;


        // add cells that are in the same col
        for(int i=0 ; i<9 ; i++){
            if(i != row) {
                if(!(addAgain && this.exceptedRow == i && this.exceptedCol == col))
                    arcs.add(new Arc(i , col , row , col)) ;
            }
        }

        // add cells that are in the same row
        for(int j=0 ; j<9 ; j++){
            if(j != col) {
                if(!(addAgain && this.exceptedRow == row && this.exceptedCol == j))
                    arcs.add(new Arc(row , j , row , col)) ;
            }
        }
    }


    /*
    Update Sudoku Grid: After applying arc consistency, update the Sudoku grid based on
    the reduced domains:
    For each cell with a singleton domain (a domain with only one value), assign that value
    to the cell.
    */
    private void updateSudokuGrid(SudokuBoard sudokuBoard){
        for(int i=0 ; i<9 ; i++){
            for(int j=0 ; j<9 ; j++){
                if(sudokuBoard.getBoard()[i][j] == 0 && sudokuBoard.getDomains()[i][j].size() == 1){ // cell isn't filled
                    sudokuBoard.getBoard()[i][j] = sudokuBoard.getDomains()[i][j].get(0) ;  // update cell
                    sudokuBoard.increaseCountOfCompletedAssignments();
                }
            }
        }

        for (int i=0 ; i<9 ; i++){
            System.out.println(Arrays.toString(sudokuBoard.getBoard()[i]));
        }
    }
}





