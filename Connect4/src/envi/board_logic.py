
def red_play_in(state, column):
    """
    Set the next slot in the column to red and update the state.

    Parameters
    ----------
    state : EnviState
        The state of the board.
    column : int
        The column to drop the red piece in.

    Returns
    -------
    EnviState
        The new state after the red piece was dropped.

    Raises
    ------
    ValueError
        If the column is full.
    """
    # Get the current row to drop the red piece in
    current_row = int(state.cols[column].decode("ASCII"))
    
    # Check if the column is full
    if current_row == 6:
        raise ValueError("Invalid move: Column is full")
    
    # Set the next slot in the column to red
    state.set_slot(current_row, column, 'x')
    
    # Insert the red move to the beginning of the list of moves
    state.red.insert(0, chr((current_row << 4) | column).encode("ASCII"))
    
    # Increase the column by one
    state.increase_col(column)
    
    # Return the updated state
    return state

def blue_play_in(state, column):
    """
    Set the next slot in the column to blue and update the state.

    Parameters
    ----------
    state : EnviState
        The state of the board.
    column : int
        The column to drop the blue piece in.

    Returns
    -------
    EnviState
        The new state after the blue piece was dropped.

    Raises
    ------
    ValueError
        If the column is full.
    """
    # Get the current row to drop the blue piece in
    current_row = int(state.cols[column].decode("ASCII"))
    # Check if the column is full
    if current_row == 6:
        raise ValueError("Invalid move: Column is full")
    # Set the next slot in the column to blue
    state.set_slot(current_row, column, 'o')
    # Insert the blue move to the beginning of the list of moves
    state.blue.insert(0, chr((current_row << 4) | column).encode("ASCII"))
    # Increase the column by one
    state.increase_col(column)
    # Return the new state
    return state

def red_score(state):
    return

def blue_score(state):
    return

