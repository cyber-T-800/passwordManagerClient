package com.manager.client.ui.controllers.main

import com.manager.client.WebClientManagerPassword
import com.manager.client.client.ClientKeyPinData
import com.manager.client.password.Password
import com.manager.client.password.PasswordRequestData
import com.manager.client.ui.PasswordManagerUI
import com.manager.client.ui.instances.client.LoggedClient
import com.manager.client.ui.instances.client.LoggedClients
import com.manager.client.ui.instances.password.LoggedClientPasswords
import com.manager.client.ui.instances.password.SavedPasswords
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage

class AddPasswordController {
    @FXML
    lateinit var website : TextField

    @FXML
    lateinit var username : TextField

    @FXML
    lateinit var password : PasswordField

    fun save(actionEvent: ActionEvent) {
        if(website.text.isEmpty() || username.text.isEmpty() || password.text.isEmpty()){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Fields can't be empty"
            alert.showAndWait()
            return
        }

        val password = Password(0, website.text, username.text, password.text, LoggedClient.id)


        val result = WebClientManagerPassword().save(PasswordRequestData(ClientKeyPinData(LoggedClient.key, LoggedClient.password), password))

        if(result == -1L){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "can't connect to server, try again later"
            alert.showAndWait()
            return
        }
        else if(result == 0L){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "logged client instance is invalid, logging off device..."

            //remove logged client instance off device
            //if instance is deactivated on server
            LoggedClients.getClients().remove(LoggedClient.key)
            LoggedClients.save()
            alert.showAndWait()
            if(LoggedClients.getClients().size == 0){
                val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/login-view.fxml"))
                val stage = (actionEvent.source as Node).scene.window as Stage
                val scene = Scene(fxmlLoader.load())
                stage.scene = scene
                stage.show()
                return
            }else{
                val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/select-client-view.fxml"))
                val stage = (actionEvent.source as Node).scene.window as Stage
                val scene = Scene(fxmlLoader.load())
                stage.scene = scene
                stage.show()
                return
            }
            alert.showAndWait()
            return
        }

        password.id = result

        LoggedClientPasswords.passwords += password
        SavedPasswords.savedPasswordsData.passwords += password
        SavedPasswords.save()

        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/main-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }

}