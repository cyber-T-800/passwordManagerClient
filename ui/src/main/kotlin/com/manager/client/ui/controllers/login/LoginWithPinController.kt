package com.manager.client.ui.controllers.login

import com.manager.client.client.ClientKeyPinData
import com.manager.client.WebClientManagerClient
import com.manager.client.ui.PasswordManagerUI
import com.manager.client.ui.instances.client.LoggedClient
import com.manager.client.ui.instances.client.LoggedClients
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.stage.Stage
import java.security.MessageDigest
import java.util.*

class LoginWithPinController {
    @FXML
    lateinit var label: Label
    @FXML
    lateinit var pin : PasswordField

    fun initialize(){
        label.text += LoggedClient.username
    }

    fun submit(actionEvent: ActionEvent) {
        if(pin.text == ""){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Field can't be empty!"

            alert.showAndWait()
            return
        }
        val privateKey = WebClientManagerClient().loginWithPin(ClientKeyPinData(LoggedClient.key, pin.text))
        //if can't connect server
        if(privateKey == "-1"){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Cannot connect to server! Trying to login on local device..."
            alert.showAndWait()

            val bytes = MessageDigest
                .getInstance("SHA-256")
                .digest(pin.text.toByteArray())
            val hashedPin = Base64.getEncoder().encodeToString(bytes)

            //if pin is invalid
            if (hashedPin != LoggedClient.password){
                var alert = Alert(Alert.AlertType.WARNING)
                alert.title = "Warning"
                alert.headerText = "Pin code is invalid!"

                alert.showAndWait()
                return
            }

        }
        //if typed pin is invalid
        if(privateKey == "0"){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Pin code is invalid!"

            alert.showAndWait()
            return
        }
        //if stay login key is invalid
        //delete client instance from device
        if(privateKey == "1"){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Stay login key is invalid, deleting instance from device ...!"

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
        }

        //save non-hashed pin to logged client instance
        LoggedClient.password = pin.text


        //if everything is in order, change to main view
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/main-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()

    }


}