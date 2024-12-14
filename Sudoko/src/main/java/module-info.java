module com.algorithms.sudoko {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.algorithms.sudoko to javafx.fxml;
    exports com.algorithms.sudoko;
    exports com.algorithms.sudoko.controllers;
    opens com.algorithms.sudoko.controllers to javafx.fxml;
}