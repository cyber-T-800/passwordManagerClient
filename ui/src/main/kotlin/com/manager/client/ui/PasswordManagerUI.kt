package com.manager.client.ui


import com.manager.client.ui.instances.client.LoggedClients
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class PasswordManagerUI : Application() {

    override fun start(stage: Stage) {
        LoggedClients.load()

        //Change to log-in view (if no client is logged on device)
        var fxmlLoader : FXMLLoader
        var scene : Scene
        if(LoggedClients.getClients().size == 0){
            fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("login-view.fxml"))
            scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        }
        //change to select client view
        else{
            fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("select-client-view.fxml"))
            scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        }
        stage.title = "Password Manager"
        stage.scene = scene
        stage.show()


    }
}

fun main() {
    Application.launch(PasswordManagerUI::class.java)
}