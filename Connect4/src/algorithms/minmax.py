from time import time_ns
from numpy import maximum
from src.envi.envi_state import EnviState
# from concurrent.futures import ProcessPoolExecutor
from src.envi.tree_generation import tree_generation


class minmax:

    def maximize(self, state , k , turn):

        if state.is_terminal():
            if(turn==1):
                state.utility = state.red_score
                return state.utility , None
            else :
                state.utility = state.blue_score
                return state.utility , None

        if state.depth == k :
            # print(state.depth)
            if(turn==1):
                state.utility = state.heuristic(1)
                return state.utility , None
            else :
                state.utility = state.heuristic(2)
                return state.utility , None     

        maximum_utility = float('-inf')
        maximum_child = None

        tree_generation.node_children(state)
        # if __name__ == "__main__":
        #     with ProcessPoolExecutor() as executor:
        #         futures = [executor.submit(self.minimize, child, k, turn) for child in state.children]
        #         results = [future.result() for future in futures]  # Collect results

        # for index, result in enumerate(results) :
        for child in state.children :
            utility , _ = self.minimize(child , k , turn) # type: ignore

            # if result[0] > maximum_utility :
            # maximum_utility = result[0]
            #     maximum_child = state.children[index].copy()
            if utility > maximum_utility :
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
            # print(state.depth) 
            if(turn==1):
                state.utility = state.heuristic(1)
                return state.utility , None
            else :
                state.utility = state.heuristic(2)
                return state.utility , None


        minimum_utility = float('inf')
        minimum_child = None
        
        tree_generation.node_children(state)
        for child in state.children :
            utility , _ = self.maximize(child , k , turn)

            if utility < minimum_utility :
                minimum_utility = child.utility
                minimum_child = child.copy()


        state.utility = minimum_utility
        return minimum_utility , minimum_child


    def minmax(self, initial_state, k):
        _ , child = self.maximize(initial_state , k , initial_state.turn)

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


initial_state = EnviState()
s = minmax()
for i in range(1, 9):
    start_time = time_ns()
    s.minmax(initial_state , i)
    end_time = time_ns()
    tree_path = tree_generation.generating_tree(initial_state)
    print(f"k={i}: ",(end_time - start_time) / (1_000_000_000*60), "min", f"\nTree Path: {tree_path}")

#py -m src.algorithms.alpha_beta_pruning
