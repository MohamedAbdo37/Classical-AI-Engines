
from time import time_ns
from src.envi.envi_state import EnviState


class min_max:

    def maximize(self, state , k , turn):

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
            utility = self.minimize(child , k , turn)

            if utility[0] > maximum_utility :
                maximum_utility = utility
                maximum_child = child.copy()


        state.utility = maximum_utility
        return maximum_utility , maximum_child




    def minimize(self, state , k , turn):

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
            utility = self.maximize(child , k , turn)

            if utility[0] < minimum_utility :
                minimum_utility = child.utility
                minimum_child = child.copy()

        state.utility = minimum_utility
        return minimum_utility , minimum_child


    def minmax(self, initial_state, k):
        _ , child = self.maximize(initial_state , k , initial_state.turn)
        cols1 = child.cols.decode("ASCII")
        cols2 = initial_state.cols.decode("ASCII")

        for col in range (7) :
            if(cols1[col] != cols2[col]) :
                return col


    def node_children(self, state) :
        for col in range(7):
            row = state.find_row(col)
            if  row != -1 :
                child = state.copy()
                if state.turn == 1 :
                    child.set_slot(row , col , 'x')
                elif state.turn == 2:
                    child.set_slot(row , col , 'o')

                child.increase_col(col)
                child.depth = child.depth+1
                state.children.append(child)
                if state.turn ==1 :
                    child.turn =2
                else :
                    child.turn = 1

state = EnviState()
minmax = min_max()
print(1)
start_time = time_ns()
print( minmax.minmax(state, 1))
end_time = time_ns()
print((end_time - start_time) / 1_000_000)