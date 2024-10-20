package org.example;
import java.util.* ;

public class BFS_Solver {

    boolean check_goal(int[] array){
        int [] goal = new int[] {0,1,2,3,4,5,6,7,8} ;
        return Arrays.equals(goal , array) ;
    }

    boolean check_dublication(Queue frontier , Queue explored , int[] state){
        Iterator<Puzzle_State> puzzleIterator = frontier.iterator();

        while (puzzleIterator.hasNext()) {
            Puzzle_State next = puzzleIterator.next();
            if(Arrays.equals(next.getPuzzle(),state)){
                return true ;
            }
        }
        puzzleIterator = explored.iterator() ;
        while (puzzleIterator.hasNext()) {
            Puzzle_State next = puzzleIterator.next();
            if(Arrays.equals(next.getPuzzle(),state)){
                return true ;
            }
        }
        return false ;
    }

    Puzzle_State Breadth_First_Search(Puzzle_State initialState){
        Queue<Puzzle_State> frontier = new LinkedList<>() ;
        Queue<Puzzle_State> explored = new LinkedList<>() ;
        frontier.add(initialState) ;

        for(int i=0 ; i<9 ; i++)
            System.out.print(initialState.getPuzzle()[i]+" ");
        System.out.println();

        while(!frontier.isEmpty()){
            Puzzle_State state = frontier.remove() ;  // the node that will be next expanded
            explored.add(state);

            if(check_goal(state.getPuzzle())){  // goal is reached
                return state ;
            }

            int x = state.getempty_Position() ; // position of empty cell

            if(x-3 >= 0){ // move up
                int [] next_stage = state.getPuzzle().clone() ;
                next_stage[x] = next_stage[x-3] ;
                next_stage[x-3] = 0 ;   // exchange the empty cell with the up cell

                // check if the node is in frontier or explored queues
                if(!check_dublication(frontier , explored , next_stage)){
                    frontier.add(new Puzzle_State(next_stage , x-3 , state)) ;
                }
            }

            if(x+3 <= 8) { // move down
                int[] next_stage = state.getPuzzle().clone();
                next_stage[x] = next_stage[x + 3];
                next_stage[x + 3] = 0;// exchange the empty cell with the up cell

                // check if the node is in frontier or explored queues
                if (!check_dublication(frontier, explored, next_stage)) {
                    frontier.add(new Puzzle_State(next_stage, x + 3, state));
                }
            }

            if(x%3 !=0){ // move left
                int [] next_stage = state.getPuzzle().clone() ;
                next_stage[x] = next_stage[x-1] ;
                next_stage[x-1] = 0 ;   // exchange the empty cell with the up cell

                // check if the node is in frontier or explored queues
                if(!check_dublication(frontier , explored , next_stage)){
                    frontier.add(new Puzzle_State(next_stage , x-1 , state)) ;
                }
            }

            if(x %3 !=2){ // move right
                int [] next_stage = state.getPuzzle().clone() ;
                next_stage[x] = next_stage[x+1] ;
                next_stage[x+1] = 0 ;   // exchange the empty cell with the up cell

                // check if the node is in frontier or explored queues
                if(!check_dublication(frontier , explored , next_stage)){
                    frontier.add(new Puzzle_State(next_stage , x+1 , state)) ;
                }
            }
        }

        return null;

    }
}
