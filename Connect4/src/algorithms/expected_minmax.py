
import multiprocessing
import multiprocessing.process
import threading
from time import time_ns
import time

from src.envi.envi_state import EnviState

# columns = [0]*7
# threads = []

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
    
def maximize(state, k, p=False, i=None):
    if k <= -1 or state.is_terminal():
        return None, state.heuristic() 
    
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
            utility = minimize(child, k-2)[1]
            children.append(utility)
            

    # for thread in threads:
    #     thread.join()
        
    for col in range(7):

            if(children[col] != None) :

                utility , state.chance_nodes[col] = chance_node(col, children)
                
                if utility > maxUtility:
                    maxChild, maxUtility = col, utility
        
        # if p == True:
        #     columns[i]= maxUtility

    state.utility = maxUtility    
    return maxChild, maxUtility


def minimize(state, k, p=False, i=None):
    
    if k <= -1 or state.is_terminal():
        return None, state.heuristic() 
    
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
        utility = maximize(child, k-2)[1]
        
        children.append(utility)
            
    for col in range(7):
        if children[col] != None :
            utility ,  state.chance_nodes[col]= chance_node(col, children)

            if utility < minUtility:
                minChild, minUtility = col, utility
        
    state.utility = minUtility
    return minChild, minUtility

def decision(state, k, p=False):
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
    
    col = maximize(state, k-2)[0]
    return col, state
        