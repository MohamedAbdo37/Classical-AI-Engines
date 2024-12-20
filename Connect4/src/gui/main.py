from time import time_ns
import tkinter as tk
from tkinter import ttk
import subprocess
import math
from src.envi.tree_generation import tree_generation
from src.algorithms.minmax import minmax
from src.algorithms.expected_minmax import expected_minmax
from src.algorithms.alpha_beta_pruning import alpha_beta_pruning
from src.envi.envi_state import EnviState

# default user plays first with blue and computer with red peices 
# default AI algorithm used is MinMax without Pruning with k = 8
class GUI:
    def __init__(self):
        # Main variables
        self.start_player = "User"
        self.player = self.start_player
        self.user_color = "blue"
        self.computer_color = "red"
        self.player_color = self.user_color
        self.game_status = "off"
        self.user_score = 0
        self.computer_score = 0
        self.grid_array = self.__init_grid_array()
        self.ai_algorithm = "MinMax without Pruning" # type: ignore
        self.k = 1
        self.time = 0
        self.tree_file = ''
        self.board = EnviState()

        # Create the main window of size 1400x600
        self.root = tk.Tk()
        self.root.geometry("1400x600")
        self.root.title("7x6 Connect 4")
        self.root.config(bg="antiquewhite")

        # Creating grid frame (7x6 table with circular cells of radius 20 = 700x600)
        self.grid_frame = tk.Frame()
        self.grid_frame.pack(side="right")
        self.__create_grid(6, 7, 44)

        # Creating a separating line
        self.separate_line = tk.Canvas(bg="black", width=1)
        self.separate_line.pack(fill='y', side="right")

        # Creating player's turn and score frame
        self.info_frame = tk.Frame(bg="lightblue")
        self.info_frame.pack(side="top", fill='x')
        self.__create_info_bar()

        # Creating a separating line
        self.separate_line = tk.Canvas(bg="black", height=1)
        self.separate_line.pack(fill='x', side="top")

        # Creating AI algorithm options selection list
        self.options_frame = tk.Frame(bg="lightgreen", border=1)
        self.options_frame.pack(side="top", fill='x')
        self.__create_options_bar()

        # Show selected option
        self.show_option_frame = tk.Frame(bg="lightgreen", border=1)
        self.show_option_frame.pack(side="top", fill='x')
        self.__creat_show_options()

        # Creating starting options
        self.starting_options_frame = tk.Frame(bg="antiquewhite", border=1)
        self.starting_options_frame.pack(side="top", fill='x', pady=100)
        self.__create_starting_options()

        # Creating tree area
        self.tree_frame = tk.Frame(bg="antiquewhite")
        self.tree_frame.pack(side="top", fill="both")
        self.tree_button = tk.Button(self.tree_frame, text="Show Tree", command= self.show_tree, width=10, cursor="hand2", height=20, font=(20))
        self.tree_button.pack(fill="x")

        self.root.mainloop()

    def board_is_full(self):
        for i in self.grid_array:
            if (i != 6): return False
        return True
    
    def is_the_game_over(self):
        if not self.board_is_full(): return
        if (self.computer_score > self.user_score):
            self.info_frame.winfo_children()[0].configure(text="Computer Wins", fg="green")
        elif (self.computer_score < self.user_score):
            self.info_frame.winfo_children()[0].configure(text="User Wins", fg="green")
        else:
            self.info_frame.winfo_children()[0].configure(text="Draw", fg="green")

    def check_line(self, line):
            """Check if there is a Connect 4 in the line, allowing overlapping."""
            count_x, count_o = 0, 0
            score_x, score_o = 0, 0

            for slot in line:
                if slot == 'x':
                    count_x += 1
                    count_o = 0
                elif slot == 'o':
                    count_o += 1
                    count_x = 0
                else:
                    count_x = 0
                    count_o = 0

                # Count a Connect 4 and continue counting for overlapping
                if count_x >= 4:
                    score_x += 1
                if count_o >= 4:
                    score_o += 1

            return score_x, score_o
    
    def calculate_scores(self, board):
        rows = len(board)
        cols = len(board[0])

        total_score_x, total_score_o = 0, 0

        # Check rows
        for row in board:
            sx, so = self.check_line(row)
            total_score_x += sx
            total_score_o += so

        # Check columns
        for col in range(cols):
            column = [board[row][col] for row in range(rows)]
            sx, so = self.check_line(column)
            total_score_x += sx
            total_score_o += so

        # Check diagonals (top-left to bottom-right)
        for r in range(rows - 3):
            for c in range(cols - 3):
                diagonal = [board[r + i][c + i] for i in range(4)]
                sx, so = self.check_line(diagonal)
                total_score_x += sx
                total_score_o += so

        # Check diagonals (bottom-left to top-right)
        for r in range(3, rows):
            for c in range(cols - 3):
                diagonal = [board[r - i][c + i] for i in range(4)]
                sx, so = self.check_line(diagonal)
                total_score_x += sx
                total_score_o += so

        return total_score_x, total_score_o

    def set_score(self):
        """Sets the scores of players"""
        score_x, score_o = self.calculate_scores(self.board.get_board_2d())
        self.computer_score = score_x
        self.user_score = score_o
        self.__refresh_info()

    def show_tree(self):
        command = f'D: & \"{self.tree_file}\"'
        subprocess.run(command, shell=True, capture_output=True, text=True)


    def computer_play(self):
        """Set computer's play"""
        play_col = ''
        state = None
        start_time = time_ns()
        if (self.ai_algorithm == "MinMax without Pruning"):
            play_col, state = minmax().minmax(self.board.copy(), int(self.k))
        elif (self.ai_algorithm == "MinMax with Pruning"):
            play_col, state = alpha_beta_pruning().minmax_pruning(self.board.copy(), int(self.k))
        else :
            play_col, state = expected_minmax().decision(self.board.copy(), int(self.k))

        finish_time = time_ns()
        
        self.time = int(((finish_time - start_time) / (1_000_000_000))*100)/100
        self.__set_play(self.__get_cell_id((play_col, 1)), "Computer")  

        if (self.ai_algorithm == "Expected MinMax"):
            self.tree_file = tree_generation.excepted_minmax_tree(state)
        else:
            self.tree_file = tree_generation.generating_tree(state)

    

    def get_ai_algorithm(self):
        """returns the used AI algorithm by the computer"""
        return self.ai_algorithm
    
    def __creat_show_options(self):
        self.option = tk.Label(self.show_option_frame, text=f"Algorithm: {self.ai_algorithm}, K: {self.k}, runtime: {self.time} sec", font=('Arial', 16), bg="lightgreen")
        self.option.pack(fill="x")

    def __init_grid_array(self):
        """returns an array that contains the next empty slote in each column of the grid"""
        return [0] * 7
     
    def __get_cell_coordinates(self, cell_id):
        """"Returns the cell coordinates given its id"""
        x = (cell_id-1) % 7
        y = 5 - math.floor((cell_id-1) / 7)
        return (x, y)
    
    def __get_cell_id(self, cell_coor):
        """Retruns the cell id given its coordinates"""
        cell_id = (cell_coor[0]+1) + 7*cell_coor[1]
        if (cell_coor[1] == 0): cell_id += 5*7
        elif (cell_coor[1] == 1): cell_id += 3*7
        elif (cell_coor[1] == 2): cell_id += 1*7
        elif (cell_coor[1] == 3): cell_id -= 1*7
        elif (cell_coor[1] == 4): cell_id -= 3*7
        elif (cell_coor[1] == 5): cell_id -= 5*7
        return cell_id

    def __refresh_info(self):
        for widget in self.info_frame.winfo_children():
            widget.destroy()
        self.__create_info_bar()

    def __refresh_options(self):
        for widget in self.options_frame.winfo_children():
            widget.destroy()
        self.__create_options_bar()
        self.show_option_frame.winfo_children()[0].destroy()
        self.__creat_show_options()
    
    def __set_ai_algorithm(self, list, k):
        self.ai_algorithm  = list.get()
        self.k = k.get()
        if (self.ai_algorithm == "Expected MinMax" and int(self.k) < 2):
            self.k = 2
        self.__refresh_options()
    
    def __set_starting_settings(self, player_list, color_list):
        self.start_player = player_list.get()
        self.player = self.start_player
        self.user_color = color_list.get()
        if (self.user_color == "red"):
            self.computer_color = "blue"
        else:
            self.computer_color = "red"
        if (self.player == "User"):
            self.player_color = self.user_color
        else:
            self.player_color = self.computer_color
        
        self.starting_options_frame.destroy()
        self.game_status = "on"
        self.__refresh_info()
        if (self.start_player == "Computer"): 
            self.computer_play()

    def __get_correct_cell_coor(self, req_cell):
        new_cell_y = self.grid_array[req_cell[0]]
        self.grid_array[req_cell[0]] += 1
        return (req_cell[0], new_cell_y)

    def __set_play(self, cell_id, turn):        
        """puts the correct peice in the grid"""
        # get the cooredinates of the required cell
        req_cell = self.__get_cell_coordinates(cell_id)
        # check if the play is not in a full column
        if (turn == self.player and self.grid_array[req_cell[0]] < 6 and self.game_status=="on"): 
            # identifies the peice color and the player
            current_color = self.player_color
            # choose the correct cell to insert the peice
            correct_cell_coor = self.__get_correct_cell_coor(req_cell)
            if (self.player == "Computer"):
                self.board.play_at('x', correct_cell_coor[0])
                self.player_color = self.user_color
                self.player = "User"
                self.grid_frame.winfo_children()[0].itemconfig(self.__get_cell_id(correct_cell_coor), fill=current_color)
                self.__refresh_info()
                self.__refresh_options()
            elif (self.player == "User"):
                self.board.play_at('o', correct_cell_coor[0])
                self.player_color = self.computer_color
                self.player = "Computer"
                self.grid_frame.winfo_children()[0].itemconfig(self.__get_cell_id(correct_cell_coor), fill=current_color)
                self.__refresh_options()
                self.__refresh_info()
                if not self.board_is_full(): self.computer_play()
            
            self.set_score()
            self.is_the_game_over()

    def __create_cell(self, grid, x, y, r, tag, color):
        """Helper function to draw a circle on the canvas."""
        return grid.create_oval(x - r, y - r, x + r, y + r, fill=color, tags=tag)

    def __create_grid(self, rows, cols, cell_radius):
        """Function to create a table with circular cells."""
        # Set up grid size
        cell_spacing = 10  # Space between cells
        grid_width = cols * (2 * cell_radius + cell_spacing)
        grid_height = rows * (2 * cell_radius + cell_spacing)
        
        grid = tk.Canvas(self.grid_frame, width=grid_width, height=grid_height, bg="#76EEC6")
        grid.pack()
        # Draw circular cells
        for row in range(rows):
            for col in range(cols):
                x = col * (2 * cell_radius + cell_spacing) + cell_radius + cell_spacing
                y = row * (2 * cell_radius + cell_spacing) + cell_radius + cell_spacing
                cell = self.__create_cell(grid, x, y, cell_radius, tag=f'({row},{col})', color="antiquewhite")
                grid.tag_bind(cell, "<Button-1>", lambda event, cell_id=cell: self.__set_play(cell_id, "User"))    # in implementation

    def __create_info_bar(self):
        """Creating player's turn and score frame"""
        player_turn = tk.Label(self.info_frame, text= f"Player's Turn: {self.player}", font=('Arial', 16), width=20, anchor="w", bg="lightblue", fg=self.player_color)
        player_turn.pack(side="left")
        reset_button = tk.Button(self.info_frame, text="Reset", command=self.__reset, width=10, cursor="hand2")
        reset_button.pack(fill="both", side="left", pady=10, padx=20)
        player_score = tk.Label(self.info_frame, text= f"User: {self.user_score} , Computer: {self.computer_score}", font=('Arial', 16), anchor="w", bg="lightblue")
        player_score.pack(side="right")
    
    def __create_options_bar(self):
        """Creating AI algorithm options selection list"""
        options_list_title = tk.Label(self.options_frame, text="AI Algorithm:", font=('Arial', 16), width=10, anchor="w", bg="lightgreen")
        options_list_title.pack(side="left")
        options = ["MinMax without Pruning", "MinMax with Pruning", "Expected MinMax"]
        options_list = ttk.Combobox(self.options_frame,font=('Arial', 14), values=options, width=20)
        options_list.pack(side="left", fill='x')
        options_list.set(self.ai_algorithm)

        k_title = tk.Label(self.options_frame, text="K:", font=('Arial', 16), anchor="w", bg="lightgreen")
        k_title.pack(side="left")
        default_value = tk.StringVar()
        default_value.set(self.k)
        k_entry = tk.Entry(self.options_frame, font=('Arial', 16), width=10, textvariable=default_value)
        k_entry.pack(side="left")
        

        # Create a Button to get selected value
        use_button = tk.Button(self.options_frame, text="Use", command= lambda list = options_list, k = k_entry: self.__set_ai_algorithm(list, k), width=10, cursor="hand2")
        use_button.pack(side="left", padx=10)

    def __create_starting_options(self):
        """Creating starting options (which player will start and with which color)"""
        player_label = tk.Label(self.starting_options_frame,text="Staring Player: ", font=('Arial', 14), width=20, anchor="w")
        player_label.pack(side="top", fill="x")
        
        player_options = ["User", "Computer"]
        player_options_list = ttk.Combobox(self.starting_options_frame,font=('Arial', 14), values=player_options, width=20)
        player_options_list.pack(side="top", fill='x')
        player_options_list.set("User")

        user_color_label = tk.Label(self.starting_options_frame,text="User Color: ", font=('Arial', 14), width=20, anchor="w")
        user_color_label.pack(side="top", fill="x")
        
        user_color_options = ["red", "blue"]
        user_color_options_list = ttk.Combobox(self.starting_options_frame,font=('Arial', 14), values=user_color_options, width=20)
        user_color_options_list.pack(side="top", fill='x')
        user_color_options_list.set("blue")

        # Create a Button to get selected value
        use_button = tk.Button(self.starting_options_frame, height=2, width=10, font=(20), text="Start", command= lambda player_list = player_options_list, color_list = user_color_options_list : self.__set_starting_settings(player_list, color_list), cursor="hand2")
        use_button.pack(side="top", padx=30, pady=10)

    def __reset(self):
        """Resets game"""
        self.player = self.start_player
        if (self.start_player == "User"):
            self.player_color = self.user_color
        else: 
            self.player_color = self.computer_color
        self.computer_score = 0
        self.user_score = 0
        self.tree_file = ''
        self.time = 0
        self.board = EnviState()
        self.__refresh_info()
        self.grid_array = self.__init_grid_array()
        self.grid_frame.winfo_children()[0].itemconfig("all", fill = "antiquewhite")

GUI()