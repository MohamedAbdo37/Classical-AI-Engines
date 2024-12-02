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
                minimum_utility = utility
                minimum_child = child.copy()


        state.utility = minimum_utility
        return minimum_utility , minimum_child


    def minmax(self, initial_state, k, turn):
        _ , child = self.maximize(initial_state , k , turn)
        for col in range (7) :
            if(child.cols[col] != initial_state.cols[col]) :
                return col, initial_state


# initial_state = EnviState()
# initial_state.play_at('o', 0)
# s = minmax()
# for i in range(1, 9):
#     start_time = time_ns()
#     play , tree_path = s.minmax(initial_state , i)
#     print(initial_state)
#     end_time = time_ns()
#     print(f"play={play}, k={i}: ",(end_time - start_time) / (1_000_000_000*60), "min", f"\nTree Path: {tree_path}")

#py -m src.algorithms.alpha_beta_pruning
