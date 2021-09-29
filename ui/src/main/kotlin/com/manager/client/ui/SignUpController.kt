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

class SignUpController {

    @FXML
    private lateinit var password : PasswordField

    @FXML
    private lateinit var username : TextField



    @FXML
    fun signUp(actionEvent: ActionEvent){
        if(password.text == "" || username.text == ""){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Fields password and username can't be empty"

            alert.showAndWait()
            return
        }

        if(username.text.length < 5){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Username can't be shorter than five characters!"

            alert.showAndWait()
            return
        }


        if(password.text.length < 8){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "password can't be shorter than eight characters!"

            alert.showAndWait()
            return
        }

        var key = WebClientManagerClient().register(Client(0, username.text, password.text, ""))

        if(key == "0"){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Username is used by someone already, please type another"

            alert.showAndWait()
            return
        }

        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("set-up-pin-view.fxml"))

        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        fxmlLoader.getController<SetUpPinController>().key = key
        stage.scene = scene
        stage.show()

    }
}