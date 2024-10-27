module com.classicalai.eightpuzzle {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
//    requires org.jetbrains.annotations;

    opens com.classicalai.eightpuzzle to javafx.fxml;
    exports com.classicalai.eightpuzzle;
    exports com.classicalai.eightpuzzle.algorithms;
    opens com.classicalai.eightpuzzle.algorithms to javafx.fxml;
}