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
import javafx.util.Duration;


import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private ImageView[] pieces;
    private Point[] places;
    private final String[] engines = {"BFS", "IDS", "DFS", "A* - Manhattan", "A* - Euclidean" };
    private Engine engine;
    private List<String> solution;
    private EnvironmentState[] path;
    private EnvironmentState currentState ;
    private Thread playThread;
    private boolean isPlay = false;
    private int step;

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
    public Label time;
    public Label cost;
    public Label depth;
    public Label nodes;


    @FXML
    protected void onPlayButtonClick() {
        System.out.println("Play!!!");
        if(path.length == 0)
            return;
        if (isPlay) {
            isPlay= false;
            return;
        }
        isPlay = true;
        playThread = new Thread(() -> {
            TranslateTransition translate = new TranslateTransition();
            currentState = path[0];
            solve.setDisable(true);
            try {
                setPuzzleBoard(currentState.toArray());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int i = 1;
            for (; i < path.length; i++) {
                if(!isPlay) {
                    break;
                }
                
                int[] temp = path[i].toArray();
                translate.setNode(pieces[temp[currentState.getEmptyCellPosition()]-1]);
                translate.setByX(places[currentState.getEmptyCellPosition()].getX() - places[path[i].getEmptyCellPosition()].getX());
                translate.setByY(places[currentState.getEmptyCellPosition()].getY() - places[path[i].getEmptyCellPosition()].getY());
                translate.setDuration(Duration.millis(500));
                translate.play();
                currentState = path[i];
                try {
                    Thread.sleep(550);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            solve.setDisable(false);
            isPlay = false;
            if(i >= path.length) step = 1;
        });

        playThread.start();
    }

    @FXML
    protected void onSolveButtonClick() {
        System.out.println("Solve!!!");
        this.alarmLabel.setText("");
        this.pathState.getItems().clear();
        try {
            int[] state = this.toStateArray(initialState.getText());
            System.out.printf("Initial State: %s%n", Arrays.toString(state));

            this.setPuzzleBoard(state);
            this.selectEngin(state);

            double startTime = System.nanoTime();
            this.path = engine.play();
            double endTime = System.nanoTime();
            step = 1;
            double duration = (endTime - startTime);

            toPathState(path);
            System.out.println("time: "+this.timeFormat(duration));
            this.time.setText(this.timeFormat(duration));
            this.depth.setText(String.valueOf(engine.getSearchDepth()));
            this.nodes.setText(String.valueOf(engine.getNodesExpanded()));
            this.cost.setText(String.valueOf(solution.size() -1));
            this.pathState.getItems().addAll(this.solution);

        } catch (Exception ex) {
            alarmLabel.setText(ex.getMessage());
            System.out.println(ex.getMessage());
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    private void toPathState(EnvironmentState[] play) {
        this.solution = new ArrayList<>();
        for (EnvironmentState environmentState : play) {
            solution.add(Arrays.toString(environmentState.toArray()));
        }
    }


    private void setPuzzleBoard(int[] puzzle) throws Exception {
        for (int i = 0; i < puzzle.length; i++)
            for (int j = i+1; j < puzzle.length ; j++)
                if (puzzle[i] == puzzle[j])
                    throw  new Exception("Invalid State: Duplicated Piece");

        System.out.println("no duplicates check DONE.");

        if(EnvironmentState.checkSolvability(puzzle))
            throw new Exception("this puzzle can't be solved");

        for (int i = 0; i < puzzle.length; i++) {
            if(puzzle[i] == 0) continue;
            this.pieces[puzzle[i]-1].setLayoutX(places[i].getX());
            this.pieces[puzzle[i]-1].setX(0);
            this.pieces[puzzle[i]-1].setTranslateX(0);
            this.pieces[puzzle[i]-1].setLayoutY(places[i].getY());
            this.pieces[puzzle[i]-1].setTranslateY(0);

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
            throw new Exception("Choose an Engine!!");
        System.out.println("Engine is "+ s);
        this.currentState = new EnvironmentState(state);
        switch (s){
            case "BFS":
                this.engine = new BFSSolver(this.currentState);
                break;
            case "DFS":
                this.engine = new DFSSolver(this.currentState);
                break;
            case "IDS":
                this.engine = new IDSSolver(this.currentState);
                break;
            case "A* - Manhattan":
                this.engine = new AStarSolver(this.currentState,"h1");
                break;
            case "A* - Euclidean":
                this.engine = new AStarSolver(this.currentState,"h2");
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

    private String timeFormat(double nanoseconds){
        if (nanoseconds < 1000) return (float) nanoseconds + " ns";
        nanoseconds /= 1000.0;
        if (nanoseconds < 1000) return (float) nanoseconds + " μs";
        nanoseconds /= 1000.0;
        if (nanoseconds < 1000) return (float) nanoseconds + " ms";
        nanoseconds /= 1000.0;
        return (float) nanoseconds + " s";
    }

}
