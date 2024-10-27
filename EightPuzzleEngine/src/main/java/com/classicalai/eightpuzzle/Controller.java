package com.classicalai.eightpuzzle;

import com.classicalai.eightpuzzle.algorithms.*;
import com.classicalai.eightpuzzle.environment.EnvironmentState;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;



import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private ImageView[] pieces;
    private Point[] places;
    private final String[] engines = {"BFS", "IDS", "DFS", "A*"};
    private Engine engine;
    private List<String> solution;
    private EnvironmentState[] path;
    private EnvironmentState currentState ;

    @FXML
    public Button play;
    public Button solve;
    public Label alarmLabel;
    public ImageView p1;
    public ImageView p2;
    public ImageView p3;
    public ImageView p4;
    public ImageView p5;
    public ImageView p6;
    public ImageView p7;
    public ImageView p8;
    public TextField initialState;
    public ChoiceBox<String> enginType;
    public ListView<String> pathState;


    @FXML
    protected void onPlayButtonClick() {
        System.out.println("Play!!!");
        int[][] p = {
                {1,2,5,3,4,0,6,7,8},
                {1,2,5,3,0,4,6,7,8},
                {1,2,5,3,4,6,7,0,8},
                {1,2,5,3,4,6,0,7,8},
                {1,2,5,3,4,6,7,0,8},
                {1,2,5,3,4,6,7,8,7}
        };

// 1,2,5,3,4,0,6,7,8

        Thread playThread = new Thread(){
            public void run(){
                TranslateTransition translate = new TranslateTransition();
                for (int i = 1; i < path.length; i++) {
                    int[] temp = path[i].toArray();
                    translate.setNode(pieces[temp[currentState.getEmptyCellPosition()] - 1]);
                    translate.setByX(places[currentState.getEmptyCellPosition()].getX() - places[path[i].getEmptyCellPosition()].getX());
                    translate.setByY(places[currentState.getEmptyCellPosition()].getY() - places[path[i].getEmptyCellPosition()].getY());
                    translate.play();
                    currentState = path[i];
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

//        Thread myThread =
//                new Thread(){
//                    public void run(){
//                        TranslateTransition translate = new TranslateTransition();
//                        translate.setNode(pieces[4-1]);
//                        translate.setByX(places[5].getX() - places[4].getX());
//                        translate.setByY(places[5].getY() - places[4].getY());
//                        translate.play();
//                        System.out.println("Runnable running");
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                        translate.setNode(pieces[7-1]);
//                        translate.setByX(places[4].getX() - places[7].getX());
//                        translate.setByY(places[4].getY() - places[7].getY());
//                        translate.play();
//                        System.out.println("Runnable running");
//                    }
//                };
//
        playThread.start();
    }

//    private int getMovedPiece(int[] ints) {
//        int piece;
//        for (int i = 0; i < ; i++) {
//
//        }
//    }

    @FXML
    protected void onSolveButtonClick() {
        System.out.println("Solve!!!");
        try {
            int[] state = this.toStateArray(initialState.getText());
            System.out.printf("Initial State: %s%n", Arrays.toString(state));
            this.setPuzzleBoard(state);
            this.selectEngin(state);
            System.out.println(engine.getInitialState().getBoard());
            this.path = engine.play();
            toPathState(path);
            this.pathState.getItems().addAll(this.solution);
//            String[] p = {
//                    "{1,2,5,3,4,0,6,7,8}",
//                    "{1,2,5,3,0,4,6,7,8}",
//                    "{1,2,5,3,4,6,7,0,8}",
//                    "{1,2,5,3,4,6,0,7,8}",
//                    "{1,2,5,3,4,6,7,0,8}",
//                    "{1,2,5,3,4,6,7,8,7}"
//            };
//            this.pathState.getItems().addAll(p);
        } catch (Exception ex) {
            alarmLabel.setText(ex.getMessage());
            System.out.println(ex.getMessage());
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    private void toPathState(EnvironmentState[] play) {
        this.solution = new ArrayList<>();
        for (int i = 0; i < play.length; i++)
          solution.add(Arrays.toString(play[0].toArray()));
    }


    private void setPuzzleBoard(int[] puzzle) throws Exception {
        for (int i = 0; i < puzzle.length; i++)
            for (int j = i+1; j < puzzle.length ; j++)
                if (puzzle[i] == puzzle[j])
                    throw  new Exception("Invalid State: Duplicated Piece");

        System.out.println("no duplicates check DONE.");
        for (int i = 0; i < puzzle.length; i++) {
            if(puzzle[i] == 0) continue;
            this.pieces[puzzle[i]-1].setLayoutX(places[i].getX());
            this.pieces[puzzle[i]-1].setLayoutY(places[i].getY());
        }
        System.out.println("All pieces in place check DONE.");
    }

    private int[] toStateArray(String state) throws Exception{
        String[] ps = state.split(",");
        if (ps.length != 9)
            throw new Exception("Invalid State: Wrong Format");
        int[] values  = {0,0,0,0,0,0,0,0,0};
        for (int i = 0; i < values.length; i++)
            values[i] = Integer.parseInt(ps[i]);

        return  values;
    }

    private void selectEngin(int[] state) throws Exception {
        String s = enginType.getValue();

        if (s == null)
            throw new Exception("Choose an Engin!!");
        System.out.println("Engine is "+ s);
        switch (s){
            case "BFS":
                this.engine = new BFSSolver(new EnvironmentState(state));
                break;
            case "DFS":
                this.engine = new DFSSolver(new EnvironmentState(state));
                break;
            case "IDS":
                this.engine = new IDSSolver(new EnvironmentState(state));
                break;
            case "A*":
                this.engine = new AStarSolver(new EnvironmentState(state),"h1");
                break;
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.enginType.getItems().addAll(engines);
        this.pieces = new ImageView[8];

        pieces[0] = p1;
        pieces[1] = p2;
        pieces[2] = p3;
        pieces[3] = p4;
        pieces[4] = p5;
        pieces[5] = p6;
        pieces[6] = p7;
        pieces[7] = p8;

        this.places = new Point[9];
        places[0] = new Point(249,1);
        places[1] = new Point(446,1);
        places[2] = new Point(643,1);
        places[3] = new Point(249,198);
        places[4] = new Point(446,198);
        places[5] = new Point(643,198);
        places[6] = new Point(249,395);
        places[7] = new Point(446,395);
        places[8] = new Point(643,395);

        for (int i = 0; i < pieces.length; i++) {
            pieces[i].setLayoutX(places[i+1].getX());
            pieces[i].setLayoutY(places[i+1].getY());
        }
    }

}