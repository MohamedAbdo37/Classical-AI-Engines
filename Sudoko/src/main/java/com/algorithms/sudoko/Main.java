package com.algorithms.sudoko;

import com.algorithms.sudoko.enumerations.Difficulty;
import com.algorithms.sudoko.models.GameGenerator;
import com.algorithms.sudoko.models.Solver;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static final int GRID_SIZE = 9; // 9x9 Sudoku grid
    private int[][] board = new int[GRID_SIZE][GRID_SIZE];
    private int[][] trueBoard = new int[GRID_SIZE][GRID_SIZE];
    private String mode;
    private boolean isProgrammaticallyChanged = false;
    @Override
    public void start(Stage primaryStage) {
        // Create the Sudoku grid
        GridPane sudokuGrid = createSudokuGrid();
        BorderPane root = new BorderPane();
        ComboBox<String> dropDownDefficuly = new ComboBox<>();
        ComboBox<String> dropDownMode = new ComboBox<>();
        Button actionButton = new Button("Solve");
        Button clearButton = new Button("Clear");
        Button generateBoardButton = new Button("Generate");
        Text faildText = new Text();
        faildText.setText("Entered board has no solution");
        faildText.setFill(Color.RED);
        faildText.setFont(Font.font("Time New Romans", 16));
        Text succeededText = new Text();
        succeededText.setText("Board solved successfully");
        succeededText.setFill(Color.GREEN);
        succeededText.setFont(Font.font("Time New Romans", 16));
        HBox bottomBox = new HBox(40, dropDownDefficuly, dropDownMode, clearButton);

        root.setCenter(sudokuGrid);
        root.setBottom(bottomBox);

        // Create and set the scene
        Scene scene = new Scene(root, 400, 500);
        // Create a ComboBox (drop-down list)
        dropDownDefficuly.getItems().addAll("Easy", "Medium", "Hard");
        dropDownDefficuly.setValue("Easy"); // Set default value

        // Create a ComboBox (drop-down list)
//        dropDownMode.getItems().addAll("Random", "Customized", "Human Solve");
        dropDownMode.getItems().addAll("Random", "Customized");
        dropDownMode.setValue("Choose Mode"); // Set default value
        disableCells(scene);
        // Create a Button

        // Create a VBox for dropdown and button
        dropDownMode.setOnAction(event->{
            if (bottomBox.getChildren().contains(faildText))
                bottomBox.getChildren().remove(faildText);
            if (bottomBox.getChildren().contains(succeededText))
                bottomBox.getChildren().remove(succeededText);
            clearBoard(scene);
            String selectedValue = dropDownMode.getSelectionModel().getSelectedItem();
            this.mode = selectedValue;
            if (selectedValue == "Customized") {
                enableCells(scene);
                if (bottomBox.getChildren().contains(dropDownDefficuly))
                    bottomBox.getChildren().remove(dropDownDefficuly);
                if (bottomBox.getChildren().contains(generateBoardButton))
                    bottomBox.getChildren().remove(generateBoardButton);
                if (!bottomBox.getChildren().contains(actionButton))
                    bottomBox.getChildren().add(actionButton);
            }
            else if (selectedValue == "Random"){
                disableCells(scene);
                if (!bottomBox.getChildren().contains(dropDownDefficuly))
                    bottomBox.getChildren().addFirst(dropDownDefficuly);
                if (!bottomBox.getChildren().contains(actionButton))
                    bottomBox.getChildren().add(actionButton);
                if (!bottomBox.getChildren().contains(generateBoardButton))
                    bottomBox.getChildren().add(generateBoardButton);
            }
//            else if ( selectedValue == "Human Solve"){
//                enableCells(scene);
//                if (!bottomBox.getChildren().contains(dropDownDefficuly))
//                    bottomBox.getChildren().addFirst(dropDownDefficuly);
//                if (!bottomBox.getChildren().contains(generateBoardButton))
//                    bottomBox.getChildren().add(generateBoardButton);
//                if (bottomBox.getChildren().contains(actionButton))
//                    bottomBox.getChildren().remove(actionButton);
//            }
        });
        bottomBox.setPadding(new Insets(10));
        // Combine everything in a BorderPane

        actionButton.setOnAction(event -> {
            this.isProgrammaticallyChanged = true;
            disableCells(scene);
            String selectedMode = dropDownMode.getValue();

            if (selectedMode == "Customized"){
                if (checkForSolution()) {
                    Solver solver = new Solver(board);
                    boolean state = solver.solve();
                    board = solver.getBoard();
                    if (!state) {
                        if (!bottomBox.getChildren().contains(faildText))
                            bottomBox.getChildren().add(faildText);
                        enableCells(scene);
                    }
                    else {
                        if (bottomBox.getChildren().contains(faildText))
                            bottomBox.getChildren().remove(faildText);
                        if (!bottomBox.getChildren().contains(succeededText))
                            bottomBox.getChildren().add(succeededText);
                        applyBoard(scene);
                    }
                }
                else {
                    if (!bottomBox.getChildren().contains(faildText))
                        bottomBox.getChildren().add(faildText);
                    enableCells(scene);
                }
            }
            else{
                this.board = this.trueBoard;
                if (bottomBox.getChildren().contains(faildText))
                    bottomBox.getChildren().remove(faildText);
                bottomBox.getChildren().add(succeededText);
                printBoard();
                applyBoard(scene);
            }
            this.isProgrammaticallyChanged = false;
        });

        clearButton.setOnAction(event->{
            if (bottomBox.getChildren().contains(faildText))
                bottomBox.getChildren().remove(faildText);
            if (bottomBox.getChildren().contains(succeededText))
                bottomBox.getChildren().remove(succeededText);
            clearBoard(scene);
            enableCells(scene);
        });

        generateBoardButton.setOnAction(event->{
            clearBoard(scene);
            enableCells(scene);
            this.isProgrammaticallyChanged = true;
            String selectedDifficulty = dropDownDefficuly.getValue();
            GameGenerator gameGenerator = new GameGenerator();
            switch (selectedDifficulty){
                case "Easy":
                    gameGenerator.build(Difficulty.EASY);
                    break;
                case "Medium":
                    gameGenerator.build(Difficulty.MEDIUM);
                    break;
                case "Hard":
                    gameGenerator.build(Difficulty.DIFFICULT);
                    break;
            }
            this.board = gameGenerator.getBoard();
            applyBoard(scene);
            this.trueBoard = gameGenerator.getSolvedBoard();
            this.isProgrammaticallyChanged = false;
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
                    if (cell.getCharacters().toString() == "")
                        this.board[mapIDToX(Integer.parseInt(cell.getId()))][mapIDToY(Integer.parseInt(cell.getId()))] = 0;
                    else {
                        try {
                            this.board[mapIDToX(Integer.parseInt(cell.getId()))][mapIDToY(Integer.parseInt(cell.getId()))]
                                    = Integer.parseInt(cell.getCharacters().toString());
                        } catch (NumberFormatException e) {
                            this.board[mapIDToX(Integer.parseInt(cell.getId()))][mapIDToY(Integer.parseInt(cell.getId()))]
                                    = 10; // dummy variable
                        }
                    }
                    if (this.mode == "Random" && cell.getCharacters().toString()!= "" && this.trueBoard[mapIDToX(Integer.parseInt(cell.getId()))]
                            [mapIDToY(Integer.parseInt(cell.getId()))] != Integer.parseInt(cell.getCharacters().toString()) && !this.isProgrammaticallyChanged)
                        cell.setStyle("-fx-border-color: red; -fx-border-width: 1; -fx-border-style: solid; -fx-alignment: center;");
                    else if (this.mode == "Random" && cell.getCharacters().toString()!= "" && this.trueBoard[mapIDToX(Integer.parseInt(cell.getId()))]
                            [mapIDToY(Integer.parseInt(cell.getId()))] == Integer.parseInt(cell.getCharacters().toString()) && !this.isProgrammaticallyChanged)
                        cell.setStyle("-fx-border-color: green; -fx-border-width: 1; -fx-border-style: solid; -fx-alignment: center;");
                });
                cell.setPrefSize(80, 80); // Set size for the text field
                cell.setStyle("-fx-alignment: center;"); // Center the text
                cell.setFont(Font.font(20));
                gridPane.add(cell, col, row);
            }
        }
        return gridPane;
    }

    private void enableCells(Scene scene){
        for (int i=0; i<GRID_SIZE*GRID_SIZE; i++){
            ((TextField)scene.lookup("#" + String.valueOf(i))).setEditable(true);
            ((TextField)scene.lookup("#" + String.valueOf(i))).setDisable(false);
        }
    }

    private void disableCells(Scene scene){
        for (int i=0; i<GRID_SIZE*GRID_SIZE; i++){
            ((TextField)scene.lookup("#" + String.valueOf(i))).setDisable(true);
        }
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

    private boolean checkForSolution(){
        printBoard();
        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                if (this.board[i][j] < 0 || this.board[i][j] > 9) return false;
            }
        }
        return true;
    }

    private void applyBoard(Scene scene){
        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                if (this.board[i][j] == 0)
                    ((TextField) scene.lookup("#" + mapCoorToID(i, j))).setText("");
                else {
                    ((TextField) scene.lookup("#" + mapCoorToID(i, j))).setText(String.valueOf(this.board[i][j]));
                    ((TextField) scene.lookup("#" + mapCoorToID(i, j))).setDisable(true);
                    ((TextField) scene.lookup("#" + mapCoorToID(i, j))).setStyle("-fx-border-color: green; -fx-border-width: 1; -fx-border-style: solid; -fx-alignment: center;");

                }
            }
        }
    }

    private void clearBoard (Scene scene){
        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                ((TextField) scene.lookup("#" + mapCoorToID(i, j))).clear();
                ((TextField) scene.lookup("#" + mapCoorToID(i, j))).setStyle("-fx-border-color: transparent; -fx-alignment: center;");
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
