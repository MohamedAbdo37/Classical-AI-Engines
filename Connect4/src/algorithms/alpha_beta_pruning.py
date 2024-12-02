from src.envi.tree_generation import tree_generation
from src.envi.envi_state import EnviState
from time import time_ns


class alpha_beta_pruning:

    expanded_nodes = 0

    # maximize function
    def maximize(self, state , k , alpha , beta):

        # increase expanded nodes 
        self.expanded_nodes = self.expanded_nodes +1

        # board is full or cut depth 
        if state.is_terminal() or state.depth == k :
            state.utility = state.heuristic()
            return state.utility , None


        # initialize utility and child
        maximum_utility = float('-inf')
        maximum_child = None

        # generate node childs
        tree_generation.node_children(state , 'x')
        for child in state.children :
            utility , _ = self.minimize(child , k , alpha , beta) # type: ignore

            # check utility
            if utility > maximum_utility :
                maximum_utility = utility
                maximum_child = child.copy()

            # check pruning
            if maximum_utility >= beta :
                break

            if maximum_utility > alpha :
                alpha = maximum_utility

        # updating utility
        state.utility = maximum_utility
        return maximum_utility , maximum_child


# ---------------------------------------------------------------------------------------------------

    # minimize function
    def minimize(self, state , k , alpha , beta):

        # increase expanded nodes 
        self.expanded_nodes = self.expanded_nodes +1

        # board is full or cut depth 
        if state.is_terminal() or state.depth == k :
            state.utility = state.heuristic()
            return state.utility , None

        # initialize utility and child
        minimum_utility = float('inf')
        minimum_child = None
        
        # generate node childs
        tree_generation.node_children(state , 'o')

        for child in state.children :
            utility , _ = self.maximize(child , k , alpha , beta)

            # check utility
            if utility < minimum_utility :
                minimum_utility = child.utility
                minimum_child = child.copy()

            # check pruning
            if minimum_utility <= alpha :
                break 

            if minimum_utility < beta :
                beta = minimum_utility 

        # updating utility
        state.utility = minimum_utility
        return minimum_utility , minimum_child

# -------------------------------------------------------------------------------------------------------------


    # starting function
    def minmax_pruning(self, initial_state, k):
        _ , child = self.maximize(initial_state , k , float('-inf') , float('inf'))

        for col in range (7) :
            if(child.cols[col] != initial_state.cols[col]) :
                return col, initial_state
'''
initial = EnviState()
s = alpha_beta_pruning ()
_ , initial = s.minmax_pruning(initial , 2)
tree_generation.generating_tree(initial)
'''
