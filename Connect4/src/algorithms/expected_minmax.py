import multiprocessing
import multiprocessing.process
import threading
from time import time_ns
import time
from src.envi.envi_state import EnviState
from src.envi.tree_generation import tree_generation


# columns = [0]*7
# threads = []


class expected_minmax :


    def chance_node(self , col, children):

            if col == 0:
                if(children[1] == None):
                    return children[col] , 1
                return children[col] * 0.6 + children[col+1] * 0.4 , 2

            elif col == 6:
                if(children[5] == None) :
                    return children[col] , 1
                return children[col-1] * 0.4 + children[col] * 0.6 , 2

            elif children[col-1] == None and children[col+1] == None :
                return children[col] , 1

            elif children[col-1] == None:
                return children[col] * 0.6 + children[col+1] * 0.4 , 2

            elif children[col+1] == None :
                return children[col-1] * 0.4 + children[col] * 0.6 , 2  

            return children[col-1] * 0.2 + children[col] * 0.6 + children[col+1] * 0.2 , 3

# -------------------------------------------------------------------------------------------------------

    def maximize(self , state, k, p=False, i=None):
        if k <= -1 or state.is_terminal():
            state.utility = state.heuristic()
            return None, state.utility 
        
        maxChild , maxUtility = None, float('-inf')
        
        children = []
        # threads = []
        
        for col in range(7):
                child = state.copy()
                child.children.clear()

                try:
                    child.play_at('x', col)
                    state.children.append(child)
                    child.depth =state.depth+1

                except:
                    children.append(None)
                    state.children.append(None)
                    state.chance_nodes[col] = 0

                    continue
                # if k == 0:
                #     children.append('@')
                #     threads.append(threading.Thread(target=state.heuristic, args=(mode,children, col)))
                #     threads[col].start()
                # else:
                utility = self.minimize(child, k-2)[1]
                children.append(utility)
                

        # for thread in threads:
        #     thread.join()
            
        for col in range(7):

                if(children[col] != None) :

                    utility , state.chance_nodes[col] = self.chance_node(col, children)
                    
                    if utility > maxUtility:
                        maxChild, maxUtility = col, utility
            
            # if p == True:
            #     columns[i]= maxUtility

        state.utility = maxUtility    
        return maxChild, maxUtility


# -----------------------------------------------------------------------------------------------------------------------------


    def minimize(self , state, k, p=False, i=None):
        
        if k <= -1 or state.is_terminal():
            state.utility = state.heuristic ()
            return None, state.utility
        
        minChild , minUtility = None, float('inf')
        
        children = []
        # threads = []
        
        for col in range(7):
            child = state.copy()
            child.children.clear()
            try:
                child.play_at('o', col)
                state.children.append(child)
                child.depth =state.depth+1
            except:
                children.append(None)
                state.children.append(None)
                state.chance_nodes[col] = 0
                continue

            # if  k == 0:
            #     children.append('@')
            #     threads.append(threading.Thread(target=state.heuristic, args=(mode,children, col)))
            #     threads[col].start()
            # else:
            utility = self.maximize(child, k-2)[1]
            
            children.append(utility)
                
        for col in range(7):
            if children[col] != None :
                utility ,  state.chance_nodes[col]= self.chance_node(col, children)

                if utility < minUtility:
                    minChild, minUtility = col, utility
            
        state.utility = minUtility
        return minChild, minUtility

# --------------------------------------------------------------------------------------------------------------------

    def decision(self , state, k, p=False):
        """
        This function makes a decision based on the Expected Minimax algorithm.
        
        Parameters:
            state (EnviState): The current state of the environment.
            k (int): The depth of the search tree. 
            p (bool): Whether to use parallel processing or not. Default is False.
            
        Returns:
            int: The column of the best move.
            EnviState: The updated state of the environment.
        """
        
        col = self.maximize(state, k-2)[0]
        return col, state
'''
initial = EnviState()
s = expected_minmax ()
_ , initial = s.decision(initial , 2)
tree_generation.excepted_minmax_tree(initial)          
'''

state = EnviState()
import random
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
for i in range(2,17):
    s = time_ns()
    col , _ = expected_minmax().decision(state.copy(),i)
    e = time_ns()
    print(i ,'\t',((e-s)/1000_000_000.0),'\t',col)

