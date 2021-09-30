package com.manager.client.ui

import com.manager.client.Client
import com.manager.client.WebClientManagerClient
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Alert

import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.net.ConnectException

class SignUpController {

    @FXML
    private lateinit var password : PasswordField

    @FXML
    private lateinit var username : TextField

    //sign up client on server
    @FXML
    fun signUp(actionEvent: ActionEvent){
        //checks if fields are not empty
        if(password.text == "" || username.text == ""){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Fields password and username can't be empty"

            alert.showAndWait()
            return
        }

        //checks if username isn't too short
        if(username.text.length < 5){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Username can't be shorter than five characters!"

            alert.showAndWait()
            return
        }

        //checks if password isn't too short
        if(password.text.length < 8){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "password can't be shorter than eight characters!"

            alert.showAndWait()
            return
        }

        //create client from fields, send it to server and receive stay-login key
        var key = WebClientManagerClient().register(Client(0, username.text, password.text, ""))


        //if cannot connect server
        if(key == "-1"){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Connection Error"
            alert.headerText = "Can't connect to server! Please try again later."

            alert.showAndWait()
            return
        }

        //if username is already taken
        if(key == "0"){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Username is used by someone already, please type another"

            alert.showAndWait()
            return
        }


        //if is everything in order, change view to set-up-pin view
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("set-up-pin-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        fxmlLoader.getController<SetUpPinController>().key = key
        stage.scene = scene
        stage.show()

    }
}