package com.algorithms.sudoko;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static final int GRID_SIZE = 9; // 9x9 Sudoku grid
    private int[][] board = new int[GRID_SIZE][GRID_SIZE];
    @Override
    public void start(Stage primaryStage) {
        // Create the Sudoku grid
        GridPane sudokuGrid = createSudokuGrid();

        // Create a ComboBox (drop-down list)
        ComboBox<String> dropDownDefficuly = new ComboBox<>();
        dropDownDefficuly.getItems().addAll("Easy", "Medium", "Hard");
        dropDownDefficuly.setValue("Easy"); // Set default value

        // Create a ComboBox (drop-down list)
        ComboBox<String> dropDownMode = new ComboBox<>();
        dropDownMode.getItems().addAll("Random", "Customized");
        dropDownMode.setValue("Random"); // Set default value

        // Create a Button
        Button actionButton = new Button("Start Game");

        // Create a VBox for dropdown and button
        HBox bottomBox = new HBox(40, dropDownDefficuly, dropDownMode, actionButton);
        dropDownMode.setOnAction(event->{
            String selectedValue = dropDownMode.getSelectionModel().getSelectedItem();
            if (selectedValue == "Customized" &&  bottomBox.getChildren().getFirst() == dropDownDefficuly){
                bottomBox.getChildren().removeFirst();
            }
            else if (selectedValue == "Random" &&  bottomBox.getChildren().getFirst() != dropDownDefficuly){
                bottomBox.getChildren().addFirst(dropDownDefficuly);
            }
        });
        bottomBox.setPadding(new Insets(10));
        // Combine everything in a BorderPane
        BorderPane root = new BorderPane();
        root.setCenter(sudokuGrid);
        root.setBottom(bottomBox);

        // Create and set the scene
        Scene scene = new Scene(root, 400, 500);
        actionButton.setOnAction(event -> {
            String selectedDifficulty = dropDownDefficuly.getValue();
            System.out.println("Selected Difficulty: " + selectedDifficulty);
            String selectedMode = dropDownMode.getValue();
            System.out.println("Selected Mode: " + selectedMode);

            if (selectedMode == "Customized"){
//                solve(board);
            }
            else{
//                board = generateBoard(selectedDifficulty);
//                applyBoard(scene);
//                solve();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Sudoku Game");
        primaryStage.show();
    }

    private GridPane createSudokuGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                GridPane cell = addBox(row, col);
                gridPane.add(cell, col, row);
            }
        }


        // Add bold lines for separating 3x3 blocks
        return gridPane;
    }

    private GridPane addBox (int boxRow, int boxCol){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-border-style: solid;");
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                TextField cell = new TextField();
                cell.setId(setCellID(boxRow, boxCol, row, col));
                cell.textProperty().addListener((event)->{
                    this.board[mapIDToX(Integer.parseInt(cell.getId()))][mapIDToY(Integer.parseInt(cell.getId()))]
                            = Integer.parseInt(cell.getCharacters().toString());
                    printBoard();
                });
                cell.setPrefSize(80, 80); // Set size for the text field
                cell.setStyle("-fx-alignment: center;"); // Center the text
                boolean isEditable = Math.random() > 0.5; // Example logic for enabling/disabling cells
                cell.setEditable(isEditable);
                cell.setDisable(!isEditable);
                if (!isEditable) {
                    cell.setText(String.valueOf((int) (Math.random() * 9) + 1)); // Example fixed number
                }
                cell.setFont(Font.font(20));
                gridPane.add(cell, col, row);
            }
        }
        return gridPane;
    }

    private String setCellID(int boxRow, int boxCol, int cellRow, int cellCol){
        return String.valueOf(27*boxRow + 3*boxCol + 9*cellRow + cellCol);
    }

    private int mapIDToX(int ID){
        return ID / GRID_SIZE;
    }

    private int mapIDToY(int ID){
        return ID % GRID_SIZE;
    }

    private String mapCoorToID (int x, int y){
        return String.valueOf(x*GRID_SIZE + y);
    }

    private void applyBoard(Scene scene){
        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                ((TextField) scene.lookup(mapCoorToID(i, j))).setText("#" + String.valueOf(this.board[i][j]));
            }
        }
    }

    private void printBoard(){
        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                System.out.print(this.board[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
