
from src.envi.envi_state import EnviState


class Node:
    def __init__(self, board, parent=None, action=None, depth=0):
        self.board = board
        self.parent = parent
        self.action = action
        self.depth = depth
        self.val = 0
        self.children = []

    
    def get_parent(self):
        return self.parent
    
    def get_action(self):
        return self.action
    
    def get_val(self):
        return self.val
    
    def get_depth(self):
        return self.depth
    
    def get_board(self):
        return self.board
    
    def set_parent(self, parent):
        self.parent = parent
        
    def set_action(self, action):
        self.action = action
    
    def set_depth(self, depth):
        self.depth = depth
    
    def set_val(self, val):
        self.val = val
    
    def add_child(self, child):
        self.children.append(child)
