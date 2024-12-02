from time import time_ns
from numpy import maximum
from src.envi.envi_state import EnviState
# from concurrent.futures import ProcessPoolExecutor
from src.envi.tree_generation import tree_generation


class minmax:

    expanded_nodes = 0

    # maximize function
    def maximize(self, state , k , turn):
        
        # increase expanded nodes
        self.expanded_nodes = self.expanded_nodes +1

        # board is full
        if state.is_terminal():
            if(turn==1):
                state.utility = state.red_score() - state.blue_score()
                return state.utility , None
            else :
                state.utility = state.blue_score() - state.red_score()
                return state.utility , None

        # cut depth
        if state.depth == k :
            state.utility = state.heuristic(turn)
            return state.utility , None

        #initialize utility and child
        maximum_utility = float('-inf')
        maximum_child = None

        # generate node childs
        tree_generation.node_children(state)

        '''
        # if __name__ == "__main__":
        #     with ProcessPoolExecutor() as executor:
        #         futures = [executor.submit(self.minimize, child, k, turn) for child in state.children]
        #         results = [future.result() for future in futures]  # Collect results

        # for index, result in enumerate(results) :
        '''

        for child in state.children :
            utility , _ = self.minimize(child , k , turn) # type: ignore

            '''
            # if result[0] > maximum_utility :
            # maximum_utility = result[0]
            #     maximum_child = state.children[index].copy()
            '''

            # check utility
            if utility > maximum_utility :
                maximum_utility = utility
                maximum_child = child.copy()

        # updating utility
        state.utility = maximum_utility
        return maximum_utility , maximum_child

# ---------------------------------------------------------------------------------------------------

    # minimize function
    def minimize(self, state , k , turn):

        # increase expanded nodes 
        self.expanded_nodes = self.expanded_nodes +1

        # board is full
        if state.is_terminal():
            if(turn==1):
                state.utility = state.red_score() - state.blue_score()
                return state.utility , None
            else :
                state.utility = state.blue_score() - state.red_score()
                return state.utility , None

        # cut depth
        if state.depth == k :
            state.utility = state.heuristic(turn)
            return state.utility , None


        # initialze utility and child
        minimum_utility = float('inf')
        minimum_child = None
        
        # generating childs
        tree_generation.node_children(state)
        for child in state.children :
            utility , _ = self.maximize(child , k , turn)

            # check utility
            if utility < minimum_utility :
                minimum_utility = child.utility
                minimum_child = child.copy()

        # updating utility
        state.utility = minimum_utility
        return minimum_utility , minimum_child

# -------------------------------------------------------------------------------------------------------------

    # starting function
    def minmax(self, initial_state, k):
        _ , child = self.maximize(initial_state , k , initial_state.turn)

        for col in range (7) :
            if(child.cols[col] != initial_state.cols[col]) :
                return col



for i in range(1, 11):
    initial_state = EnviState()
    s = minmax()
    start_time = time_ns()
    s.minmax(initial_state , i)
    end_time = time_ns()
    
    print(f"k={i}: ",(end_time - start_time) / (1_000_000_000), "seconds" , ' , expanded nodes = ' , s.expanded_nodes)

    #tree_path = tree_generation.generating_tree(initial_state)
    #print(f"Tree Path: {tree_path}")

#py -m src.algorithms.minmax
