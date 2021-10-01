module com.manager.client.ui {
    requires kotlin.stdlib;
    requires javafx.graphics;
    requires javafx.fxml;
    requires client;
    requires javafx.controls;
    requires com.google.gson;

    opens com.manager.client.ui to javafx.fxml, com.google.gson;
    opens com.manager.client.ui.controllers.main to com.google.gson, javafx.fxml;
    opens com.manager.client.ui.controllers.login to javafx.fxml;
    opens com.manager.client.ui.instances to com.google.gson;
    exports com.manager.client.ui;
    exports com.manager.client.ui.controllers.main;
    exports com.manager.client.ui.controllers.login;
    exports com.manager.client.ui.instances;

}