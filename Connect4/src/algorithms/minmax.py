from src.envi.tree_generation import tree_generation
from src.envi.envi_state import EnviState

class minmax:

    expanded_nodes = 0

    # maximize function
    def maximize(self, state , k):
        
        # increase expanded nodes
        self.expanded_nodes = self.expanded_nodes +1

        # board is full or cut depth 
        if state.is_terminal() or state.depth == k :
            state.utility = state.heuristic()
            return state.utility , None


        #initialize utility and child
        maximum_utility = float('-inf')
        maximum_child = None

        # generate node childs
        tree_generation.node_children(state , 'x')

        for child in state.children :
            utility , _ = self.minimize(child , k) # type: ignore

            # check utility
            if utility > maximum_utility :
                maximum_utility = utility
                maximum_child = child.copy()

        # updating utility
        state.utility = maximum_utility
        return maximum_utility , maximum_child

# ---------------------------------------------------------------------------------------------------

    # minimize function
    def minimize(self, state , k):

        # increase expanded nodes 
        self.expanded_nodes = self.expanded_nodes +1

        # board is full or cut depth 
        if state.is_terminal() or state.depth == k :
            state.utility = state.heuristic()
            return state.utility , None


        # initialze utility and child
        minimum_utility = float('inf')
        minimum_child = None
        
        # generating childs
        tree_generation.node_children(state , 'o')
        for child in state.children :
            utility , _ = self.maximize(child , k)

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
        _ , child = self.maximize(initial_state , k)

        for col in range (7) :
            if(child.cols[col] != initial_state.cols[col]) :
                return col, initial_state

'''
initial = EnviState()
s = minmax ()
_ , initial = s.minmax(initial , 2)
tree_generation.generating_tree(initial)
'''

state = EnviState()
import random
from time import time_ns

t = 1
co = [0,1,2,3,4,5,6]
for i in range(16):
    if t == 1:
        state.play_at('x', random.choice(co))
        t = 2
    else:
        state.play_at('o', random.choice(co))
        t = 1

print('expected_minmax')
print('k\ttime\tcol')
for i in range(8):
    s = time_ns()
    col , _ = minmax().minmax(state.copy(),i)
    e = time_ns()
    print(i ,'\t',((e-s)/1000_000_000.0),'\t',col)