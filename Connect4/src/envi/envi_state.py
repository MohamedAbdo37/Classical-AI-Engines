import sys
import numpy as np

board = '\0\0\0\0\0\0\0\0\0\0\0\0'.encode("ASCII")
cols = '0000000'.encode("ASCII")
red = []
blue = []

class EnviState:
    def __init__(self):
        """Initialize the EnviState with default values."""
        # Initialize the board with an empty state
        self.board = board
        # Initialize the list of red player moves
        self.red = red
        # Initialize the list of blue player moves
        self.blue = blue
        # Initialize the columns with default values
        self.cols = cols
    
    def __str__(self):
        """Return a string representation of the board state."""
        board = []
        # Iterate over each row
        for row in range(6):
            r = []
            # Iterate over each column in the row
            for col in range(7):
                # Append the current slot's value to the row
                r.append(self.slot(row, col))
            # Insert the completed row at the beginning of the board list
            board.insert(0, r)
        # Convert the board list to a string
        return str(board)
        
    def __eq__(self, other):
        """
        Check if two EnviStates are equal by comparing their string
        representations.

        Parameters
        ----------
        other : EnviState
            The state to compare to.

        Returns
        -------
        bool
            True if the states are equal, False otherwise.
        """
        return str(self) == str(other)
    
    def __ne__(self, other):
        """
        Check if two EnviStates are not equal by comparing their string
        representations.

        Parameters
        ----------
        other : EnviState
            The state to compare to.

        Returns
        -------
        bool
            True if the states are not equal, False otherwise.
        """
        return str(self) != str(other)
    
    def copy(self):
        """
        Return a copy of the current EnviState.

        Returns
        -------
        EnviState
            A copy of the current EnviState.
        """
        s = EnviState()
        s.board = self.board.copy()  # Use the copy method to create a new copy of the board
        s.red = self.red.copy()  # Use the copy method to create a new copy of the red list
        s.blue = self.blue.copy()  # Use the copy method to create a new copy of the blue list
        s.cols = self.cols.copy()  # Use the copy method to create a new copy of the columns
        return s

    
    def slot(self, row, col):
        """
        Retrieve the value at a specific slot on the board.

        Parameters
        ----------
        row : int
            The row index of the slot.
        col : int
            The column index of the slot.

        Returns
        -------
        str
            'x' if the slot is occupied by the red player,
            'o' if occupied by the blue player, 
            'e' if empty.
        """
        # Get the character representing the specified row's first byte
        r = self.board[(5-row)*2]
        # Determine if the slot is occupied by the red player
        s = (r & 64 >> col)

        if s >> (6-col) == 1:
            return 'x'
        
        # Get the character representing the specified row's second byte
        r = self.board[(5-row)*2+1]
        # Determine if the slot is occupied by the blue player
        s = (r & 64 >> col)
        
        if s >> (6-col) == 1:
            return 'o'
        
        # Return 'e' if the slot is empty
        return 'e'
    
    def set_slot(self, row, col, value):
        """
        Set the value of a slot on the board.

        Parameters
        ----------
        row : int
            The row index of the slot.
        col : int
            The column index of the slot.
        value : str
            The value to be set in the slot.
        """
        s = 64 >> col
        
        if value == 'x':
            # Set the red player's slot
            r = self.board[(5-row)*2]
            
            self.chage_char((5-row)*2, chr(r | s))
            
            r = self.board[(5-row)*2+1]
        
            self.chage_char((5-row)*2+1, chr(r & ~s))
            
        elif value == 'o':
            # Set the blue player's slot
            r = self.board[(5-row)*2+1]
    
            
            self.chage_char((5-row)*2+1, chr(r | s))
            
            r = self.board[(5-row)*2]
            
            self.chage_char((5-row)*2, chr(r & ~s))
        else:
            # Set the slot to empty
            r = self.board[(5-row)*2]
            
            self.chage_char((5-row)*2, chr(r & ~s))
            
            r = self.board[(5-row)*2+1]
            
            self.chage_char((5-row)*2+1, chr(r & ~s))
        
        
    def chage_char(self, index, value):
        """
        Change the character at a specific index in the board.

        Parameters
        ----------
        index : int
            The index in the board where the character is to be changed.
        value : str
            The new character value to be set at the specified index.
        """
        # Print the current board state
        print(self.board)
        
        # Update the board by replacing the character at the specified index
        self.board = (
            self.board[:index].decode("ASCII") + str(value) + self.board[index+1:].decode("ASCII")
        ).encode("ASCII")
        
        # Print the updated board state
        print(self.board)
        
    def red_weight(self):
        return
    
    def blue_weight(self):
        return
    
    def is_terminal(self):
        """
        Check if the current state is a terminal state.

        A state is terminal if the game is over, either because the board is full
        or a player has won.

        Returns
        -------
        bool
            True if the state is terminal, False otherwise.
        """
        # Check if the game is over
        if self.cols == '6666666':
            return True
        # If the game is not over, return False
        return False

    
    def heuristic(self):
        return  
    
    def increase_col(self, col):
        """
        Increase the value of the column at the specified index.

        Parameters
        ----------
        col : int
            The index of the column to be increased.
        """
        # Decode the columns to a string
        self.cols = self.cols.decode("ASCII")
        # Increase the value of the column at the specified index
        self.cols = self.cols[:col] + str(int(self.cols[col]) + 1) + self.cols[col+1:]
        # Encode the columns back to bytes
        self.cols = self.cols.encode("ASCII")
        
    def decrease_col(self, col):
        """
        Decrease the value of the column at the specified index.

        Parameters
        ----------
        col : int
            The index of the column to be decreased.
        """
        # Decode the columns to a string
        self.cols = self.cols.decode("ASCII")
        # Decrease the value of the column at the specified index
        self.cols = self.cols[:col] + str(int(self.cols[col]) - 1) + self.cols[col+1:]
        # Encode the columns back to bytes
        self.cols = self.cols.encode("ASCII")
