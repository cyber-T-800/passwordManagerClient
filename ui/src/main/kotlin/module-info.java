module com.manager.client.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires client;
    requires com.google.gson;

    opens com.manager.client.ui to javafx.fxml, com.google.gson;
    exports com.manager.client.ui;
    exports com.manager.client.ui.controllers.main;
    opens com.manager.client.ui.controllers.main to com.google.gson, javafx.fxml;
}