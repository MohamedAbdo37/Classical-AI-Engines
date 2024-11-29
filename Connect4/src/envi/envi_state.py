import sys
import numpy as np

board = '\0\0\0\0\0\0\0\0\0\0\0\0'.encode("ASCII")
cols = '0000000'.encode("ASCII")
red = []
blue = []

class EnviState:
    def __init__(self):
        self.board = board
        self.red = red
        self.blue = blue
        self.cols = cols
    
    def __str__(self):
        board = []
        for row in range(6):
            r = []
            for col in range(7):
                r.append(self.slot(row, col))
            board.insert(0, r)
        return str(board)
        
    def __eq__(self, other):
        return str(self) == str(other)
    def __ne__(self, other):
        return str(self) != str(other)
    
    def copy(self):
        s = EnviState()
        s.board = self.board
        s.red = self.red
        s.blue = self.blue
        s.cols = self.cols
        return s

    
    def slot(self, row, col):
        
        r = self.board[(5-row)*2]  
        s = (r & 64 >> (col)) 

        if s >> (6- col) == 1:
            return 'x'
        
        r = self.board[(5-row)*2+1]

        s = (r & 64 >> (col)) 
        
        if s >> (6- col) == 1:
            return 'o'
            
        return 'e'
    
    def set_slot(self, row, col, value):
        s = 64 >> col
        
        if value == 'x':
            r = self.board[(5-row)*2]
            
            self.chage_char((5-row)*2, chr(r | s))
            
            r = self.board[(5-row)*2+1]
        
            
            self.chage_char((5-row)*2+1, chr(r & ~s))
            
        elif value == 'o':
            r = self.board[(5-row)*2+1]
    
            
            self.chage_char((5-row)*2+1, chr(r | s))
            
            r = self.board[(5-row)*2]
            
            self.chage_char((5-row)*2, chr(r & ~s))
        else:
            r = self.board[(5-row)*2]
            
            self.chage_char((5-row)*2, chr(r & ~s))
            
            r = self.board[(5-row)*2+1]
            
            self.chage_char((5-row)*2+1, chr(r & ~s))
        
        
    def chage_char(self, index, value):
        print(self.board)
        self.board = self.board[:index].decode("ASCII") + str(value) + self.board[index+1:].decode("ASCII")
        self.board = self.board.encode("ASCII")
        print(self.board)
        
    def red_weight(self):
        return
    
    def blue_weight(self):
        return
    
    def is_terminal(self):
        return  
    
    def heuristic(self):
        return  