package com.manager.client.ui


import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.manager.client.LoggedClients
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.File

class PasswordManagerUI : Application() {
    override fun start(stage: Stage) {
        val loggedFile = File("ui/src/main/resources/com/manager/client/ui/logged.json")

        //confirms if file exists
        if(!loggedFile.exists()) {
            loggedFile.writeText(Gson().toJson(LoggedClients()))
        }
        //confirms if file isn't empty
        else if(loggedFile.readText().trim() == ""){
            loggedFile.writeText(Gson().toJson(LoggedClients()))
        }
        //confirms if content of file is in right form
        var loggedClients : LoggedClients = try{
            var loggedClientsCheck = Gson().fromJson(loggedFile.readText(), LoggedClients::class.java)
            loggedClientsCheck!!
        }catch (e : JsonSyntaxException){
            loggedFile.writeText(Gson().toJson(LoggedClients()))
            LoggedClients()
        }



        //Change to log-in view (if no client is logged on device)
        if(loggedClients.clients.size == 0){
            val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("login-view.fxml"))
            val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
            stage.title = "Hello!"
            stage.scene = scene
            stage.show()
        }
        //change to select client view
        else{
            TODO("not implemeted")
        }


    }
}

fun main() {
    Application.launch(PasswordManagerUI::class.java)
}