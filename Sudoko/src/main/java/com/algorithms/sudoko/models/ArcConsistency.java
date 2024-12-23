package com.algorithms.sudoko.models;
import java.util.*;

class ArcConsistency {

    private SudokuBoard sudokuBoard ;

    protected ArcConsistency(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
    }

    private boolean addAgain = false;
    private int exceptedRow = -1 ;
    private int exceptedCol = -1 ;


    // return false if in consistency is found , true otherwise
    protected boolean arcConsistency(){

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
            if (this.Revise(arc)){

                if(this.sudokuBoard.getDomains()[arc.getSourceRow()][arc.getSourceCol()].isEmpty()) { // inconsistent -> return false
                    //System.out.println(arc);
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
        this.updateSudokuGrid();
        return true ;
    }

    // return true if we revised the domain of Xi (domain is reduced)
    private boolean Revise(Arc arc){
        boolean revised = false ;

        for (int i=0 ; i<this.sudokuBoard.getDomains()[arc.getSourceRow()][arc.getSourceCol()].size() ; i++){

            boolean thereIsMatchInY = false ;

            for(int y : this.sudokuBoard.getDomains()[arc.getDestinationRow()][arc.getDestinationCol()]){
                if(this.sudokuBoard.getDomains()[arc.getSourceRow()][arc.getSourceCol()].get(i)!=y){
                    thereIsMatchInY = true ; // there is match , x in Di is consistent with Dj
                    break ;
                }
            }

            // if no value y in Dj allows (x,y) to satisfy the constraint between Xi and Xj then delete x from Di
            if(!thereIsMatchInY){
                this.sudokuBoard.getDomains()[arc.getSourceRow()][arc.getSourceCol()].remove(i) ;
                i-- ; // to reflect removal effect
                revised = true ; // domain has been reduced
            }
        }
        return revised ;
    }

    // add all arcs that point to specific cell to arcs queue
    private void addArcsToQueue(int row , int col , Queue<Arc> arcs){

        // getSubgridNeighbours
        ArrayList<Integer> subgridIndexes = this.sudokuBoard.getSubgridNeighbours(row , col); ;
        int adjacentRow1 = subgridIndexes.get(0) ;
        int adjacentRow2 = subgridIndexes.get(1) ;
        int adjacentCol1 = subgridIndexes.get(2);
        int adjacentCol2 = subgridIndexes.get(3);


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
    private void updateSudokuGrid(){
        for(int i=0 ; i<9 ; i++){
            for(int j=0 ; j<9 ; j++){
                if(this.sudokuBoard.getBoard()[i][j] == 0 && this.sudokuBoard.getDomains()[i][j].size() == 1){ // cell isn't filled
                    this.sudokuBoard.getBoard()[i][j] = this.sudokuBoard.getDomains()[i][j].get(0) ;  // update cell
                    this.sudokuBoard.increaseCountOfCompletedAssignments();
                }
            }
        }
    }
}





