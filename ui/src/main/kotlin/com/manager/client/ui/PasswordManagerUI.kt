package com.manager.client.ui


import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.manager.client.Client
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.File

class PasswordManagerUI : Application() {
    companion object{
        var loggedClients = LoggedClients()
    }

    override fun start(stage: Stage) {
        val loggedFile = File("ui/src/main/resources/com/manager/client/ui/logged.json")

        //confirms if file exists
        if(!loggedFile.exists()) {
            loggedFile.writeText(Gson().toJson(loggedClients))
        }
        //confirms if file isn't empty
        else if(loggedFile.readText().trim() == ""){
            loggedFile.writeText(Gson().toJson(loggedClients))
        }
        //confirms if content of file is in right form
        loggedClients = try{
            var loggedClientsCheck = Gson().fromJson(loggedFile.readText(), LoggedClients::class.java)
            loggedClientsCheck!!
        }catch (e : JsonSyntaxException){
            loggedFile.writeText(Gson().toJson(loggedClients))
            LoggedClients()
        }


        //Change to log-in view (if no client is logged on device)
        var fxmlLoader : FXMLLoader
        var scene : Scene
        if(loggedClients.clients.size == 0){
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