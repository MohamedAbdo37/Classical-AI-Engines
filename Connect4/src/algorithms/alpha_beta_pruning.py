from numpy import maximum
from src.envi.envi_state import EnviState
import time


class alpha_beta_pruning:

    def maximize(self, state , k , turn , alpha , beta):

        if state.is_terminal():
            if(turn==1):
                state.utility = state.red_score
                return state.utility , None
            else :
                state.utility = state.blue_score
                return state.utility , None

        if state.depth == k :
            if(turn==1):
                state.utility = state.heuristic(1)
                return state.utility , None
            else :
                state.utility = state.heuristic(2)
                return state.utility , None     

        maximum_utility = float('-inf')
        maximum_child = None

        self.node_children(state)
        for child in state.children :
            utility , _ = self.minimize(child , k , turn , alpha , beta) # type: ignore

            if utility > maximum_utility :
                maximum_utility = utility
                maximum_child = child.copy()

            if maximum_utility >= beta :
                break

            if maximum_utility > alpha :
                alpha = maximum_utility

        state.utility = maximum_utility
        return maximum_utility , maximum_child




    def minimize(self, state , k , turn , alpha , beta):

        if state.is_terminal():
            if(turn==1):
                state.utility = state.red_score()
                return state.utility , None
            else :
                state.utility = state.blue_score()
                return state.utility , None

                
        if state.depth == k :
            if(turn==1):
                state.utility = state.heuristic(1)
                return state.utility , None
            else :
                state.utility = state.heuristic(2)
                return state.utility , None


        minimum_utility = float('inf')
        minimum_child = None
        
        self.node_children(state)
        for child in state.children :
            utility , _ = self.maximize(child , k , turn , alpha , beta)

            if utility < minimum_utility :
                minimum_utility = child.utility
                minimum_child = child.copy()

            if minimum_utility <= alpha :
                break 

            if minimum_utility < beta :
                beta = minimum_utility 

        state.utility = minimum_utility
        return minimum_utility , minimum_child


    def minmax_pruning(self, initial_state, k):
        _ , child = self.maximize(initial_state , k , initial_state.turn , float('-inf') , float('inf'))

        for col in range (7) :
            if(child.cols[col] != initial_state.cols[col]) :
                return col


    def node_children(self, state) :
        for col in range(7):
            row = state.find_row(col)

            if  row < 6 :
                child = state.copy()
                child.children.clear() 

                if state.turn == 1 :
                    child.play_at('x' , col)
                elif state.turn == 2:
                    child.play_at('o' , col)

                child.depth = state.depth+1
                if state.turn ==1 :
                    child.turn =2
                else :
                    child.turn = 1
                state.children.append(child)

for i in range (8) :
    initial_state = EnviState()
    s = alpha_beta_pruning()
    start = time.time()
    s.minmax_pruning(initial_state , i+1)
    print('depth = ' + str(i+1)+ ' , time = ' + str(time.time() - start))

#py -m src.algorithms.alpha_beta_pruning
