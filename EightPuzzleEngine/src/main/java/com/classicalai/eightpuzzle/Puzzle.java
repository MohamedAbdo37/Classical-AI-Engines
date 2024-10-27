package com.classicalai.eightpuzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Puzzle extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Puzzle.class.getResource("PuzzleUI_2.fxml"));
        Parent root = loader.load();
//        Controller controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setTitle("8-Puzzle!");
        stage.setScene(scene);
        stage.setResizable(false);
//        controller.initialize();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}