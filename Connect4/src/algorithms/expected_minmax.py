
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

def maximize(state, k, p=False, i=None):
    if k <= -1 or state.is_terminal():
        return None, state.heuristic() 
    
    maxChild , maxUtility = None, float('-inf')
    
    children = []
    # threads = []
    
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
        _ , utility = minimize(child, k-2)
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

def minimize(state, k, p=False, i=None):
    if k <= -1 or state.is_terminal():
        return None, state.heuristic() 
    
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
        utility = maximize(child, k-2)[1]
        
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

def decision(state, k, p=False):
    """
    This function makes a decision based on the Expected Minimax algorithm.
    
    Parameters:
        state (EnviState): The current state of the environment.
        k (int): The depth of the search tree. 
        p (bool): Whether to use parallel processing or not. Default is False.
        
    Returns:
        int: The column of the best move.
    """
    
    col = maximize(state, k-2)[0]
    return col
        
        # columns = [0]*7
        
        # if k < 4:
        #     col = -1 
        #     if mode == 1:
        #         # If the mode is 1 and k is less than 4, call the maximize function
        #         col = maximize(state, k-2, mode)[0]
        #     else:
        #         # If the mode is -1 and k is less than 4, call the minimize function
        #         col,= minimize(state, k-2, mode)[0]
                
        #     return col
        # else:
        #     # If k is greater than or equal to 4, use parallel processing
        #     threads = []
        #     if mode == 1:
        #         # If the mode is 1, start a thread for each column
        #         for i in range(7):
        #             threads.append(threading.Thread(target=minimize, args=(state, k-2, mode, True, i)))
                    
        #             threads[i].start()
        #     else:
        #         # If the mode is -1, start a thread for each column
        #         for i in range(7):
        #             threads.append(threading.Thread(target=maximize, args=(state, k-2, mode, True, i)))
        #             threads[i].start()
            
                
        #     # Wait for all threads to finish
        #     while len(columns) < 7:
        #         time.sleep(0.5)
            
        #     # for thread in threads:
        #     #     thread.join()

        #     # If the mode is 1, find the maximum value in the columns list
        #     if mode == 1:
        #         max = float('-inf')
        #         for i in columns:
        #             if i > max:
        #                 max = i
        #         return max
            
        #     # If the mode is -1, find the minimum value in the columns list
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