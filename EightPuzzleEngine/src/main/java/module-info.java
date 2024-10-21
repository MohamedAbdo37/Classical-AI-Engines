module com.classicalai.eightpuzzle {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.classicalai.eightpuzzle to javafx.fxml;
    exports com.classicalai.eightpuzzle;
    exports com.classicalai.eightpuzzle.algorithms;
    opens com.classicalai.eightpuzzle.algorithms to javafx.fxml;
}