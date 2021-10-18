package com.manager.client.ui.controllers.login

import com.google.gson.Gson
import com.manager.client.client.Client
import com.manager.client.WebClientManagerClient
import com.manager.client.client.ClientKeyIdData
import com.manager.client.exceptions.ServerException
import com.manager.client.ui.PasswordManagerUI
import com.manager.client.ui.instances.client.LoggedClient
import com.manager.client.ui.instances.client.LoggedClients
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException


class LoginController {
    @FXML
    private lateinit var password: PasswordField

    @FXML
    private lateinit var username: TextField


    //Login client on server
    @FXML
    fun login(actionEvent: ActionEvent) {
        //check if fields are not empty
        if (password.text == "" || username.text == "") {
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Fields password and username can't be empty"

            alert.showAndWait()
            return
        }

        //check if account isn't already logged
        for (c in LoggedClients.getClients()) {
            if (c.value.username == username.text) {
                var alert = Alert(Alert.AlertType.WARNING)
                alert.title = "Warning"
                alert.headerText = "Account already logged, please continue to type pin code"
                alert.showAndWait()
                LoggedClient.let {
                    it.key = c.key
                    it.username = c.value.username
                    it.password = c.value.password
                }
                //if already logged, change view to log in with pin
                val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/login-with-pin-view.fxml"))
                val stage = (actionEvent.source as Node).scene.window as Stage
                val scene = Scene(fxmlLoader.load())
                stage.scene = scene
                stage.show()
                return
            }
        }

        //create client from fields, send it to server and receive stay-login key
        val client = Client(0, username.text, password.text, "")
        val clientKeyIdData : ClientKeyIdData;
        try {
            clientKeyIdData = WebClientManagerClient().login(client)
        } catch (e: WebClientRequestException) {
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Connection Error"
            alert.headerText = "Can't connect to server! Please try again later."

            alert.showAndWait()
            return
        }catch (e : WebClientResponseException.BadRequest){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = e.statusText
            alert.headerText = ServerException.fromBadRequestException(e).message

            alert.showAndWait()
            return
        }
        //create instance of logged client
        LoggedClient.let {
            it.key = clientKeyIdData.key
            it.id = clientKeyIdData.id
            it.username = client.username
        }

        //if is everything in order, change view to set-up-pin view
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/set-up-pin-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }

    //change view to sign up
    @FXML
    fun signUp(actionEvent: ActionEvent){
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/sign-up-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }

    //Change view to select logged account
    @FXML
    fun selectAccount(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/select-client-view.fxml"))
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