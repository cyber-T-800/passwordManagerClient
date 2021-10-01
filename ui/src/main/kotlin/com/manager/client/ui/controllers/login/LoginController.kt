package com.manager.client.ui.controllers.login

import com.manager.client.Client
import com.manager.client.WebClientManagerClient
import com.manager.client.ui.PasswordManagerUI
import com.manager.client.ui.instances.LoggedClient
import com.manager.client.ui.instances.LoggedClients
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage


class LoginController {
    @FXML
    private lateinit var password : PasswordField

    @FXML
    private lateinit var username : TextField


    //Login client on server
    @FXML
    fun login(actionEvent: ActionEvent){
        //check if fields are not empty
        if(password.text == "" || username.text == ""){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Fields password and username can't be empty"

            alert.showAndWait()
            return
        }

        //create client from fields, send it to server and receive stay-login key
        val client = Client(0, username.text, password.text, "")
        val key = WebClientManagerClient().login(client)

        //if cannot connect server
        if(key == "-1"){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Connection Error"
            alert.headerText = "Can't connect to server! Please try again later."

            alert.showAndWait()
            return
        }

        //if client not exists
        if(key == "0"){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Client with this combination of username and password doesn't exists"

            alert.showAndWait()
            return
        }

        //create instance of logged client
        LoggedClient.let {
            it.client = client
            it.key = key
        }

        //if is everything in order, change view to set-up-pin view
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("set-up-pin-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        fxmlLoader.getController<SetUpPinController>().key = key
        stage.scene = scene
        stage.show()
    }

    //change view to sign up
    @FXML
    fun signUp(actionEvent: ActionEvent){
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("sign-up-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }

    //Change view to select logged account
    @FXML
    fun selectAccount(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("select-client-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        if( LoggedClients.getClients().size == 0 ){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Seems like no account is logged, please log in to continue."

            alert.showAndWait()
            return
        }else{
            stage.scene = scene
            stage.show()
        }

    }
}