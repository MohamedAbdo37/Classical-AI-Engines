//package com.algorithms.sudoko.models;
//
//import java.util.ArrayList;
//
//public class CSP {
//
//
//    public void backtrack(int [][] board , ArrayList<Integer>[][] domains){
//
//        ArrayList<Integer> variable = MRV(domains) ;
//
//
//    }
//
//
//    /*
//     MRV: Choose the variable with the fewest legal values in its
//     domain.
//     order of variables ia a tiebreaker.
//    */
//    public ArrayList MRV(ArrayList<Integer>[][] domains){
//
//        int MRV = 10 ; // since maximum domain size is 9 then 10 is considered as INFINITY
//        int MRVRow = -1 ;
//        int MRVCol = -1 ;
//
//        for(int i=0 ; i<9 ; i++){
//            for (int j=0 ; j<9 ; j++){
//                // if cell is not filled and has fewer values in its domain then it is chosen as the next variable
//                if(domains[i][j].size() >1 && domains[i][j].size() < MRV){
//                    MRV = domains[i][j].size();
//                    MRVRow = i ;
//                    MRVCol = j ;
//                }
//            }
//        }
//        ArrayList<Integer> variable = new ArrayList<>() ;
//        variable.add(MRVRow) ;
//        variable.add(MRVCol) ;
//        return variable ; // return variable with fewer values in domain
//    }
//
//}
