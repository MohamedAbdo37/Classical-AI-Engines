package org.example;
import java.util.*;

public class Puzzle_Solver {

    int search_start(int [] array){
        for (int i=0 ; i<9 ; i++)
            if(array[i] == 0)
                return i ;
        return -1 ;  // no empty cell
    }


    void print_step(int [] array){  //مؤقتا لحد ال UI
        System.out.println(" ----------------------- ") ;
        for(int i=0 ; i<9 ; i++){
            System.out.print("|");
            if(array[i] !=0)
                System.out.print("   " + array[i]+"   ") ;
            else
                System.out.print("       ") ;
            if(i%3 == 2) {
                System.out.println("|");
                System.out.println(" ----------------------- ");
            }
        }
        System.out.println();
    }

    void display_solution(Puzzle_State goal){
        if(goal == null)
            System.out.println("no solution");
        else {
            Stack<int[]> path = new Stack<>() ;
            path.push(goal.getPuzzle()) ;
            while(goal.getParent()!= null){
                goal = goal.getParent() ;
                path.push(goal.getPuzzle()) ;
            }

            while (!path.empty())
                print_step(path.pop());
        }
    }


}
