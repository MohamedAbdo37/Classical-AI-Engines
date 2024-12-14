
from src.envi.envi_state import EnviState
from src.algorithms.alpha_beta_pruning import alpha_beta_pruning

def play(board, turn):
    state = EnviState()
    state.set_board(board, turn)
    
    play_col, _ = alpha_beta_pruning().minmax_pruning(state, 6)
    
    return play_col