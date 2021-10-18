package com.manager.client.ui.controllers.main

import com.manager.client.WebClientManagerPassword
import com.manager.client.client.ClientKeyPinData
import com.manager.client.exceptions.ServerException
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
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException

class AddPasswordController {
    @FXML
    lateinit var website : TextField

    @FXML
    lateinit var username : TextField

    @FXML
    lateinit var password : TextField

    fun save(actionEvent: ActionEvent) {
        if(website.text.isEmpty() || username.text.isEmpty() || password.text.isEmpty()){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Fields can't be empty"
            alert.showAndWait()
            return
        }

        val password = Password(0, website.text, username.text, password.text, LoggedClient.id)


        val result = try {
            WebClientManagerPassword().savePassword(
                PasswordRequestData(
                    ClientKeyPinData(
                        LoggedClient.key,
                        LoggedClient.password
                    ), password
                )
            )
        }
        catch (e: WebClientRequestException) {
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


        password.id = result

        LoggedClientPasswords += password
        SavedPasswords += password

        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/main-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }

}