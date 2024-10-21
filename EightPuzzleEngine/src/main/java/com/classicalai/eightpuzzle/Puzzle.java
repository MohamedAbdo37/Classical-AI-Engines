package com.classicalai.eightpuzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Puzzle extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Puzzle.class.getResource("PuzzleUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("8-Puzzle!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}