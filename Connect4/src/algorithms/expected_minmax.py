
import multiprocessing
import multiprocessing.process
import threading
from time import time_ns
import time

from src.envi.envi_state import EnviState

columns = [0]*7
threads = []

def chanceNode(col, children):
    if col == 0:
        return children[col] * 0.6 + children[col+1] * 0.4
    elif col == 6:
        return children[col-1] * 0.4 + children[col] * 0.6
    else:
        return children[col-1] * 0.2 + children[col] * 0.6 + children[col+1] * 0.2

def maximize(state, k, mode, p=False, i=None):
    if k <= -1 or state.is_terminal():
        return None, state.heuristic(mode) 
    
    maxChild , maxUtility = None, float('-inf')
    
    children = []
    threads = []
    for col in range(7):
        child = state.copy()
        try:
            child.play_at('x', col)
        except:
            children.append(0)
            continue
        # if k == 0:
        #     children.append('@')
        #     threads.append(threading.Thread(target=state.heuristic, args=(mode,children, col)))
        #     threads[col].start()
        # else:
        utility = minimize(child, k-2, mode)[1]
        children.append(utility)

    # for thread in threads:
    #     thread.join()
        
    
    for col in range(7):
        utility = chanceNode(col, children)
        
        if utility > maxUtility:
            maxChild, maxUtility = col, utility
    
    if p == True:
        columns[i]= maxUtility
        
    return maxChild, maxUtility

def minimize(state, k, mode, p=False, i=None):
    if k <= -1 or state.is_terminal():
        return None, state.heuristic(mode) 
    
    minChild , minUtility = None, float('inf')
    
    children = []
    # threads = []
    for col in range(7):
        child = state.copy()
        try:
            child.play_at('o', col)
        except:
            children.append(0)
            continue
        # if  k == 0:
        #     children.append('@')
        #     threads.append(threading.Thread(target=state.heuristic, args=(mode,children, col)))
        #     threads[col].start()
        # else:
        utility = maximize(child, k-2, mode)[1]
        
        children.append(utility)
            
    # for thread in threads:
    #     thread.join()
        
    for col in range(7):
        utility = chanceNode(col, children)

        if utility < minUtility:
            minChild, minUtility = col, utility
    if p == True:
        columns[i]= minUtility
        
    return minChild, minUtility

def decision(state, k, mode, p=False):
    
    
    if mode == 1:
        col = maximize(state, k-2, mode)[0]
    else:
        col,= minimize(state, k-2, mode)[0]
        
    return col
        
        # columns = [0]*7
        
        # if k < 4:
        #     col = -1 
        #     if mode == 1:
        #         col = maximize(state, k-2, mode)[0]
        #     else:
        #         col,= minimize(state, k-2, mode)[0]
                
        #     return col
        # else:
        #     threads = []
        #     if mode == 1:
        #         for i in range(7):
        #             threads.append(threading.Thread(target=minimize, args=(state, k-2, mode, True, i)))
                    
        #             threads[i].start()
        #     else:
        #         for i in range(7):
        #             threads.append(threading.Thread(target=maximize, args=(state, k-2, mode, True, i)))
        #             threads[i].start()
            
                
        #     while len(columns) < 7:
        #         time.sleep(0.5)
            
        #     # for thread in threads:
        #     #     thread.join()

        #     if mode == 1:
        #         max = float('-inf')
        #         for i in columns:
        #             if i > max:
        #                 max = i
        #         return max
            
        #     else:
        #         min = float('inf')
        #         for i in columns:
        #             if i < min:
        #                 min = i
        #         return min           

# state = EnviState()
# print(1)
# start_time = time_ns()
# print(decision(state, 1, 1)) # Call decision
# end_time = time_ns()
# print((end_time - start_time) / 1_000_000_000.0)

# print(2)
# start_time = time_ns()
# print(decision(state, 2, 1))  # Call decision
# end_time = time_ns()
# print((end_time - start_time) / 1_000_000_000.0)


# print(3)
# start_time = time_ns()
# print(decision(state, 3, 1))  # Call decision
# end_time = time_ns()
# print((end_time - start_time) / 1_000_000_000.0)

# print(4)
# start_time = time_ns()
# print(decision(state, 4, 1))  # Call decision
# end_time = time_ns()
# print((end_time - start_time) / 1_000_000_000.0)

# print(5)
# start_time = time_ns()
# print(decision(state, 5, 1))  # Call decision
# end_time = time_ns()
# print((end_time - start_time) / 1_000_000_000.0)

# print(6)
# start_time = time_ns()
# print(decision(state, 6, 1))  # Call decision
# end_time = time_ns()
# print((end_time - start_time) / 1_000_000_000.0)

# state = EnviState()
# print(7)
# start_time = time_ns()
# print(decision(state, 7, 1))  # Call decision
# end_time = time_ns()
# print((end_time - start_time) / 1_000_000_000.0)


# print(8)
# start_time = time_ns()
# print(decision(state, 8, 1))  # Call decision
# end_time = time_ns()
# print((end_time - start_time) / 1_000_000_000.0)
# print(columns)