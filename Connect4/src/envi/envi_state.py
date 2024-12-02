import sys
import numpy as np


class EnviState:
    def __init__(self):
        """Initialize the EnviState with default values."""
        # Initialize the board with an empty state
        self.board = '\0\0\0\0\0\0\0\0\0\0\0\0'.encode("ASCII")
        # Initialize the list of red player moves
        self.red = 0
        # Initialize the list of blue player moves
        self.blue = 0
        # Initialize the columns with default values
        self.cols = '0000000'
        self.blocked_seqs = 0
        self.red_last = -1
        self.blue_last = -1
        #initialize the children with the default value
        self.children = [] 
        #initialize the depth with the default value
        self.depth = 0
        #initialize the turn with the default value
        self.turn = 1
        #initialize the node_name with the default value for building tree
        self.node_name = ''
        #initialize the utility with the default value for building tree
        self.utility = None
    
    def get_board_2d(self):
        """
        Return a 2D list representing the current state of the board.

        Returns
        -------
        list
            A 2D list representing the current state of the board.
        """
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
        # Return the completed 2D list
        return board



    def __str__(self):
        """
        Return a string representation of the board state.

        Returns
        -------
        str
            A string representation of the board state.
        """
        # Convert the board list to a string
        return str(self.get_board_2d())
        


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
        s.board = self.board  # Use the copy method to create a new copy of the board
        s.red = self.red  # Use the copy method to create a new copy of the red list
        s.blue = self.blue  # Use the copy method to create a new copy of the blue list
        s.cols = self.cols  # Use the copy method to create a new copy of the columns
        s.blocked_seqs = self.blocked_seqs  # Use the copy method to create a new copy of the blocked_seqs
        s.red_last = self.red_last  # Use the copy method to create a new copy of the red_last
        s.blue_last = self.blue_last  # Use the copy method to create a new copy of the blue_last
        s.children = self.children.copy()   # Use the copy method to create a new copy of the children
        s.depth = self.depth        # Use the copy method to create a new copy of the depth
        s.turn = self.turn      # Use the copy method to create a new copy of the turn
        s.node_name = self.node_name        # Use the copy method to create a new copy of the node_name
        s.utility = self.utility
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
        # Check if the row or column is out of bounds
        if row < 0 or row > 5 or col < 0 or col > 6:
            raise ValueError("Invalid move: Invalid row or column")

        # Get the character representing the specified row's first byte
        r = self.board[(5-row)*2]

        # Determine if the slot is occupied by the red player
        # Check the least significant bit of the byte by shifting it to the right
        # and then check if the result is 1
        s = (r & 64 >> col)  # 64 is the ASCII value of '@'

        if s >> (6-col) == 1:
            return 'x'

        # Get the character representing the specified row's second byte
        r = self.board[(5-row)*2+1]

        # Determine if the slot is occupied by the blue player
        # Check the least significant bit of the byte by shifting it to the right
        # and then check if the result is 1
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
        if row < 0 or row > 5 or col < 0 or col > 6:
            raise ValueError("Invalid move: Invalid row or column")

        # Create a mask to set the value of the slot
        s = 64 >> col

        if value == 'x':
            # Set the red player's slot
            # Set the least significant bit of the byte at the specified row
            # and column to 1
            r = self.board[(5-row)*2]
            self.chage_char((5-row)*2, chr(r | s))
            # Set the least significant bit of the byte at the specified row
            # and column to 0
            r = self.board[(5-row)*2+1]
            self.chage_char((5-row)*2+1, chr(r & ~s))
        elif value == 'o':
            # Set the blue player's slot
            # Set the least significant bit of the byte at the specified row
            # and column to 1
            r = self.board[(5-row)*2+1]
            self.chage_char((5-row)*2+1, chr(r | s))
            # Set the least significant bit of the byte at the specified row
            # and column to 0
            r = self.board[(5-row)*2]
            self.chage_char((5-row)*2, chr(r & ~s))
        else:
            # Set the slot to empty
            # Set the least significant bit of the byte at the specified row
            # and column to 0
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
        # Update the board by replacing the character at the specified index
        self.board = (
            self.board[:index] + value.encode("ASCII") + self.board[index+1:]
        )



    def find_row(self, col):
        """
        Find the row index of the first empty slot in a column.

        Parameters
        ----------
        col : int
            The column index.

        Returns
        -------
        int
            The row index of the first empty slot in the column.
        """
        row = int(self.cols[col])        
        return row


    
    def play_at(self, player, col):
        if col < 0 or col > 6:
            raise ValueError("Invalid move: Invalid column")
        
        row = self.find_row(col)
        
        if row == 6:
            raise ValueError("Invalid move: Column is full")
        
        if player == 'x':
            self.blocked_seqs = self.new_points(row, col,'o')
            self.red_last = col
        else:
            self.blocked_seqs = self.new_points(row, col,'x')
            self.blue_last = col
            
        self.set_slot(row, col, player)
        self.increase_col(col)
        
        if player == 'x':
            self.red += self.new_points(row, col,'x')
        else:
            self.blue += self.new_points(row, col,'o')


        
    def red_weight(self):
        
        col = self.red_last
        if col == -1:
            return 0
        row = self.find_row(col) - 1
        
        # Initialize the set of chances to gain at least one point         
        self.chance_set = {}
        # Initialize the score to 0
        blocking = 0
        print(row, col)
        if self.slot(row, col) != 'x':
            blocking = 125 * self.blocked_seqs

        score = 200 * self.red_score()
        
        # Calculate the one-step chances to win
        one_step = 100 * self.one_step_chance('x')
        # Calculate the two-step chances to win
        two_step = 50 * self.two_step_chance('x', col)
        # Calculate the three-step chances to win
        three_step = 25 * self.three_step_chance('x', col)
        
        # Calculate the weight based on the score and the number of chances to win
        weight = score + one_step + two_step + three_step + blocking
        
        # Delete the set of chances to gain at least one point
        del self.chance_set
        
        # Return the weight of the move
        return weight 


    def blue_weight(self):
        
        col = self.blue_last
        if col == -1:
            return 0
        row = self.find_row(col) - 1
        # Initialize the set of chances to gain at least one point         
        self.chance_set = {}
        # Initialize the score to 0
        blocking = 0
        if self.slot(row, col) != 'o':
            blocking = 125 * self.blocked_seqs
            
        
        score = 200 * self.blue_score()             
        
        # Calculate the one-step, two-step, three-step, and four-step chances
        one_step = 100 * self.one_step_chance('o')
        two_step = 50 * self.two_step_chance('o', col)
        three_step = 25 * self.three_step_chance('o', col)
        
        # Calculate the weight
        weight = score + one_step + two_step + three_step + blocking
        
        # Remove the set of chances
        del self.chance_set
        
        # Return the weight of the move
        return weight

    
    def new_points(self, row, col, player):
        """
        Return the number of new points for the given player.

        Parameters
        ----------
        row : int
            The row index of the slot.
        col : int
            The column index of the slot.
        player : str
            The player to check for new points.

        Returns
        -------
        int
            The number of new points for the given player.
        """
        points = 0
        grid = [(1,-1),(0,-1),(-1,-1),(-1,0),(-1,1),(0,1),(1,1)]
        
        for move in grid:
                # Check if the move is out of bounds
                if row + move[0] < 0 or row + move[0] > 5 or col + move[1] < 0 or col + move[1] > 6:
                    continue
                
                # Check if the point is occupied by the given player
                if self.chance_point(row, col, move, player):
                    # Increment the chances
                    points += 1
        
        return points
    
    def blue_score(self):
        """
        Return the score of the blue player.

        Returns
        -------
        int
            The score of the blue player.
        """
        # Convert the blue player's score from a byte to an integer
        return self.blue
    
    def red_score(self):
        """
        Return the score of the red player.

        Returns
        -------
        int
            The score of the red player.
        """
        # Convert the red player's score from a byte to an integer
        return self.red
    
    def one_step_chance(self, player, c = -1):
        """
        Calculate the number of one-step chances for the player to win.

        A one-step chance is a potential move that allows the player to 
        achieve a winning condition in one move. This function evaluates 
        the board to identify such opportunities for the specified player.

        Parameters
        ----------
        player : str
            The player for whom the chances are being calculated ('x' for 
            red player and 'o' for blue player).
        c : int, optional
            The specific column to evaluate for chances. If set to -1, 
            evaluates all columns on the board.

        Returns
        -------
        int
            The total number of one-step chances available to the player.
        """
        chances = 0
        # The grid represents the possible moves in a Connect Four game
        grid = [(1,-1),(0,-1),(-1,-1),(-1,0),(-1,1),(0,1),(1,1)]
        
        if c == -1:
            # Iterate over each column in the board
            for col in range(7):
                row = self.find_row(col)
                
                # If the column is full, skip it
                if row == 6:
                    continue
                
                # Check if the point is occupied by the given player
                for move in grid:
                    if (col + 7 * row) not in self.chance_set and self.chance_point(row, col, move, player):
                        chances += 1
                        self.chance_set.add(col + 7 * row)
        else:
            col = c
            row = self.find_row(col)
            
            # If the column is full, skip it
            if row == 6:
                return 0
            
            # Check if the point is occupied by the given player
            for move in grid:
                if (col + 7 * row) not in self.chance_set and self.chance_point(row, col, move, player):
                    chances += 1
                    self.chance_set.add(col + 7 * row)
            
        return chances
    
    def two_step_chance(self, player, col):
        chances = 0

        if col < 0 or col > 6:
            raise ValueError("Invalid move: Invalid column")
        
        # Iterate over each column on the board
        for col in range(7):
            # Get the current row to drop the player's piece
            row = self.find_row(col)

            # If the column is full, skip it
            if row == 6:
                continue

            # Simulate placing the player's piece in the current slot
            self.set_slot(row, col, player)
            self.increase_col(col)
            
            if col > 0:
                chances += self.one_step_chance(player, col-1)
            
            chances += self.one_step_chance(player, col)
            
            if col < 6:
                chances += self.one_step_chance(player, col+1)

            # Undo the move to restore the original state
            self.decrease_col(col)
            self.set_slot(row, col, 'e')

        # Return the total number of two-step chances
        return chances

    def three_step_chance(self, player, col, state = None):
        chances = 0
        
        if col < 0 or col > 6:
            raise ValueError("Invalid move: Invalid column")
        
        if state == None:
            state = self
        else:
            state.red = None
            state.blue = None
        
        
        # Get the current row to drop the player's piece
        row = state.find_row(col)
        
        # If the column is full, skip it
        if row == 6:
            return 0
        
        # Simulate placing the player's piece in the current slot
        state.set_slot(row, col, player)
        state.increase_col(col)
        
        if col > 1:
            chances += state.two_step_chance(player, col-1)
        
        chances += state.two_step_chance(player, col)
        
        if col < 5:
            chances += state.two_step_chance(player, col+1)
        
        # Undo the move to restore the original state
        state.decrease_col(col)
        state.set_slot(row, col, 'e')
            
        # Return the total number of three-step chances
        return chances

    def four_step_chance(self, player, col, state = None):
        chances = 0
        if col < 0 or col > 6:
            raise ValueError("Invalid move: Invalid column")
        
        if state == None:
            state = self
        else:
            state.red = None
            state.blue = None
        
        
        # Get the current row to drop the player's piece
        row = self.find_row(col)
        
        # If the column is full, skip it
        if row == 6:
            return 0
        
        # Simulate placing the player's piece in the current slot
        state.set_slot(row, col, player)
        state.increase_col(col)
        
        if col > 2:
            chances += state.three_step_chance(player, col-1)
        
        chances += state.three_step_chance(player, col)
        
        if col < 4:
            chances += state.three_step_chance(player, col+1)
            
        chances += state.three_step_chance(player, state, col)
        
        # Undo the move to restore the original state
        state.decrease_col(col)
        state.set_slot(row, col, 'e')
            
        # Return the total number of four-step chances
        return chances

    def chance_point(self, row, col, move, player):
        
        """
        Check if the given player has a chance to win in two moves.

        This function checks if the given player has a chance to win in two moves
        by checking if the player's piece can be moved in the given direction
        and if the point is occupied by the given player.

        Parameters
        ----------
        row : int
            The row index of the slot.
        col : int
            The column index of the slot.
        move : tuple
            A tuple of two elements, (row move, col move), which specifies the
            direction of the move.
        player : str
            The player for whom the chance is being checked.

        Returns
        -------
        bool
            True if the given player has a chance to win in two moves, False otherwise.
        """
        for i in range(2):
            # Check if the move is out of bounds
            if row + move[0] * i < 0 or row + move[0] * i > 5 or col + move[1] * i < 0 or col + move[1] * i > 6:
                return False
            
            # Check if the point is occupied by the given player
            if self.slot(row + move[0] * i, col + move[1] * i) != player:
                return False
        
        # If all checks pass, return True
        return True
    
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

    
    def heuristic(self, mode, c= None):
        """
        Calculate a heuristic value for the current state.

        The heuristic value is a measure of how good the current state is for
        the given player. The heuristic value is calculated as the difference
        between the estimated score of the red player and the estimated score
        of the blue player.

        Parameters
        ----------
        mode : int
            The player for which the heuristic value is calculated.
            if mode = 1, then player 1 is the AI engine.
            if mode = 2, then player 2 is the AI engine.

        Returns
        -------
        int
            The heuristic value of the current state for the given player.
        """
        if mode == 1:
            # Calculate the heuristic value for the red player
            c = self.red_weight() - self.blue_weight()
            return c
        elif mode == 2:
            # Calculate the heuristic value for the blue player.
            c = self.blue_weight() - self.red_weight()
            return 
        else:
            raise ValueError("Invalid mode")


    def increase_col(self, col):
        """
        Increase the value of the column at the specified index.

        Parameters
        ----------
        col : int
            The index of the column to be increased.
        """

        # Increase the value of the column at the specified index
        self.cols = self.cols[:col] + str(int(self.cols[col]) + 1) + self.cols[col+1:]
        
    def decrease_col(self, col):
        """
        Decrease the value of the column at the specified index.

        Parameters
        ----------
        col : int
            The index of the column to be decreased.
        """
        # Decrease the value of the column at the specified index
        self.cols = self.cols[:col] + str(int(self.cols[col]) - 1) + self.cols[col+1:]
