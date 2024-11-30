import tkinter as tk
from tkinter import ttk
import math

# user plays with blue and computer with red peices
class GUI:
    def __init__(self):
        # Main variables
        self.player = "User"
        self.user_score = 0
        self.computer_score = 0
        self.grid_array = self.__init_grid_array()
        self.grid_string_array = []
        self.AI_algorithm = "MinMax without Pruning"

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

        self.root.mainloop()

    def is_computer_turn(self):
        """Checks whether it's Computer's turn or not"""
        return self.player == "Computer"
    
    def set_score(self, player, score):
        """Sets the scores of players"""
        if (player == "Computer"):
            self.computer_score = score
        else:
            self.user_score = score
    
        self.__refresh_info()

    def computer_play(self, play_coordinates):
        """Set computer's play"""
        self.__set_play(self.__get_cell_id(play_coordinates))   # for testing only
        # self.__set_play(self.__get_cell_id(play_coordinates, "Computer"))    # in implementation

    def get_grid(self):
        """retuns the grid"""
        return self.grid_string_array

    def get_AI_algorithm(self):
        """returns the used AI algorithm by the computer"""
        return self.AI_algorithm
    
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
        id = (cell_coor[0]+1) + 7*cell_coor[1]
        if (cell_coor[1] == 0): id += 5*7
        elif (cell_coor[1] == 1): id += 3*7
        elif (cell_coor[1] == 2): id += 1*7
        elif (cell_coor[1] == 3): id -= 1*7
        elif (cell_coor[1] == 4): id -= 3*7
        elif (cell_coor[1] == 5): id -= 5*7
        return id

    def __refresh_info(self):
        for widget in self.info_frame.winfo_children():
            widget.destroy()
        self.__create_info_bar()

    def __refresh_options(self):
        for widget in self.options_frame.winfo_children():
            widget.destroy()
        self.__create_options_bar()
    
    def __set_AI_algorithm(self, list):
        self.AI_algorithm  = list.get()
        print(self.AI_algorithm)
        self.__refresh_options()

    def __set_play(self, cell_id):      # for testing only
    # def __set_play(self, cell_id, turn):        # in implementation
        """puts the correct peice in the grid"""

        # get the cooredinates of the required cell
        req_cell = self.__get_cell_coordinates(cell_id)

        # check if the play is not in a full column
        if (self.grid_array[req_cell[0]] < 6):              # for testing only
        # if (turn == self.player and self.grid_array[req_cell[0]] < 6):     # in implementation
            # identifies the peice color and the player
            fill_color = ""
            if (self.player == "Computer"):
                fill_color = "#FF4040"
                self.player = "User"
            elif (self.player == "User"):
                fill_color = "#007FFF"
                self.player = "Computer"

            # choose the correct cell to insert the peice
            new_cell_y = self.grid_array[req_cell[0]]
            self.grid_array[req_cell[0]] += 1
            new_cell_coor = (req_cell[0], new_cell_y)
        
            self.grid_frame.winfo_children()[0].itemconfig(self.__get_cell_id(new_cell_coor), fill=fill_color)
            
            self.__refresh_info()


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
                grid.tag_bind(cell, "<Button-1>", lambda event, cell_id=cell: self.__set_play(cell_id))  # for testing only
                # grid.tag_bind(cell, "<Button-1>", lambda event, cell_id=cell: self.__set_play(cell_id, "User"))    # in implementation

    def __create_info_bar(self):
        """Creating player's turn and score frame"""
        player_turn = tk.Label(self.info_frame, text= f"Player's Turn: {self.player}", font=('Arial', 16), width=20, anchor="w", bg="lightblue")
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

        # Set default value
        options_list.set("MinMax without Pruning")

        # Create a Button to get selected value
        use_button = tk.Button(self.options_frame, text="Use", command= lambda list = options_list: self.__set_AI_algorithm(list), width=10, cursor="hand2")
        use_button.pack(side="left", padx=10)

        # Show selected option
        option = tk.Label(self.options_frame, text=self.AI_algorithm, font=('Arial', 16), anchor="w", bg="lightgreen")
        option.pack(side="right")

    def __reset(self):
        """Resets game"""
        self.player = "User"
        self.computer_play = 0
        self.user_score = 0
        self.__refresh_info()
        self.grid_array = self.__init_grid_array()
        self.grid_frame.winfo_children()[0].itemconfig("all", fill = "antiquewhite")
GUI()
