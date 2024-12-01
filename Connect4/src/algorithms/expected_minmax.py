
from calendar import c


def chanceNode(col, children):
    if col == 0:
        return children[col] * 0.6 + children[col+1] * 0.4
    elif col == 6:
        return children[col-1] * 0.4 + children[col] * 0.6
    else:
        return children[col-1] * 0.2 + children[col] * 0.6 + children[col+1] * 0.2

def maximize(state, k, mode):
    if k==0 or state.is_terminal():
        return None, state.heuristic(mode) 
    
    maxChild , maxUtility = None, float('-inf')
    
    children = []
    
    for col in range(7):
        child = state.copy().play_at(col)
        utility = minimize(child, k, mode)[1]
        
        children.append(utility)
    
    for col in range(7):
        utility = chanceNode(col, children)
        
        if utility > maxUtility:
            maxChild, maxUtility = col, utility
            
    return maxChild, maxUtility

def minimize(state, k, mode):
    if k==0 or state.is_terminal():
        return None, state.heuristic(mode) 
    
    minChild , minUtility = None, float('inf')
    
    children = []
    
    for col in range(7):
        child = state.copy().play_at(col)
        utility = maximize(child, k, mode)[1]
    
        children.append(utility)
    
    for col in range(7):
        utility = chanceNode(col, children)

        if utility < minUtility:
            minChild, minUtility = col, utility
            
    return minChild, minUtility

def decision(state, k, mode, t=0):
    
    col, t = maximize(state, k-1, mode)
    
    return col
