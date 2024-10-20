package org.example;

public class Main {
    public static void main(String[] args) {

        int a[] = new int [] {1,6,2,7,3,4,8,0,5};
//        int a[] = new int [] {0,1,2,3,4,5,6,7,8};

//        int a[] = new int [] {1,2,5,3,4,0,6,7,8};
//        int a[] = new int [] {1,4,2,6,5,8,7,3,0};

        BFS_Solver bfs = new BFS_Solver();
        Puzzle_Solver ps = new Puzzle_Solver() ;

        ps.display_solution(bfs.Breadth_First_Search(new Puzzle_State(a, ps.search_start(a) , null)));


    }
}