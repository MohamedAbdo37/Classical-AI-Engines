
import threading
from time import time_ns
import time

from src.envi.envi_state import EnviState


def chanceNode(col, children):
    if col == 0:
        return children[col] * 0.6 + children[col+1] * 0.4
    elif col == 6:
        return children[col-1] * 0.4 + children[col] * 0.6
    else:
        return children[col-1] * 0.2 + children[col] * 0.6 + children[col+1] * 0.2

def maximize(state, k, mode):
    if k== -1 or state.is_terminal():
        return None, state.heuristic(mode) 
    
    maxChild , maxUtility = None, float('-inf')
    
    children = []
    
    for col in range(7):
        child = state.copy()
        try:
            child.play_at('x', col)
        except:
            children.append(0)
            continue
        if k != 0:
            utility = minimize(child, k-1, mode)[1]
            children.append(utility)
        else:
            children.append('@')
            threading.Thread(target=state.heuristic, args=(mode,children[col])).start()
            
    while '@' in children:
        time.sleep(0.01)
    
    for col in range(7):
        utility = chanceNode(col, children)
        
        if utility > maxUtility:
            maxChild, maxUtility = col, utility
            
    return maxChild, maxUtility

def minimize(state, k, mode):
    if k== -1 or state.is_terminal():
        return None, state.heuristic(mode) 
    
    minChild , minUtility = None, float('inf')
    
    children = []
    
    for col in range(7):
        child = state.copy()
        try:
            child.play_at('o', col)
        except:
            children.append(0)
            continue
        if  k == 0:
            children.append('@')
            threading.Thread(target=state.heuristic, args=(mode,children[col])).start()
        else:
            utility = maximize(child, k-1, mode)[1]
            children.append(utility)
            
    while '@' in children:
        time.sleep(0.001)
        
    for col in range(7):
        utility = chanceNode(col, children)

        if utility < minUtility:
            minChild, minUtility = col, utility
            
    return minChild, minUtility

def decision(state, k, mode, t=0):
    
    col, t = maximize(state, k-1, mode)
    
    return col

state = EnviState()
print(1)
start_time = time_ns()
print(decision(state, 1, 1)) # Call decision
end_time = time_ns()
print((end_time - start_time) / 1_000_000_000.0)

state = EnviState()
print(2)
start_time = time_ns()
print(decision(state, 2, 1))  # Call decision
end_time = time_ns()
print((end_time - start_time) / 1_000_000_000.0)


state = EnviState()
print(3)
start_time = time_ns()
print(decision(state, 3, 1))  # Call decision
end_time = time_ns()
print((end_time - start_time) / 1_000_000_000.0)

state = EnviState()
print(4)
start_time = time_ns()
print(decision(state, 4, 1))  # Call decision
end_time = time_ns()
print((end_time - start_time) / 1_000_000_000.0)

state = EnviState()
print(5)
start_time = time_ns()
print(decision(state, 5, 1))  # Call decision
end_time = time_ns()
print((end_time - start_time) / 1_000_000_000.0)

state = EnviState()
print(6)
start_time = time_ns()
print(decision(state, 6, 1))  # Call decision
end_time = time_ns()
print((end_time - start_time) / 1_000_000_000.0)

state = EnviState()
print(7)
start_time = time_ns()
decision(state, 7, 1)  # Call decision
end_time = time_ns()
print((end_time - start_time) / 1_000_000_000.0)

state = EnviState()
print(8)
start_time = time_ns()
decision(state, 8, 1)  # Call decision
end_time = time_ns()
print((end_time - start_time) / 1_000_000_000.0)