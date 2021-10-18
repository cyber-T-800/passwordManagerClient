package com.manager.client.ui.controllers.main

import com.manager.client.WebClientManagerPassword
import com.manager.client.client.ClientKeyPinData
import com.manager.client.exceptions.ServerException
import com.manager.client.password.Password
import com.manager.client.password.PasswordRequestData
import com.manager.client.ui.PasswordManagerUI
import com.manager.client.ui.instances.client.LoggedClient
import com.manager.client.ui.instances.password.LoggedClientPasswords
import com.manager.client.ui.instances.password.SavedPasswords
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class ShowPasswordController {
    lateinit var selectedPassword: Password

    @FXML
    lateinit var website: TextField

    @FXML
    lateinit var username: TextField

    @FXML
    lateinit var password: TextField


    fun setPassword(selectedPassword: Password) {
        this.selectedPassword = selectedPassword
        website.text = selectedPassword.website
        username.text = selectedPassword.username
        password.text = selectedPassword.encryptedPassword
    }

    fun copy() {
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(password.text), null)
    }

    fun delete(actionEvent: ActionEvent) {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Deleting password"
        alert.headerText = "Are you sure, you want to delete password?"
        if (alert.showAndWait().get() == ButtonType.OK) {
            LoggedClientPasswords -= selectedPassword
            SavedPasswords -= selectedPassword
            try {
                WebClientManagerPassword().deletePassword(
                    PasswordRequestData(
                        ClientKeyPinData(
                            LoggedClient.key,
                            LoggedClient.password
                        ), selectedPassword
                    )
                )
            } catch (e: WebClientRequestException) {
                val alert = Alert(Alert.AlertType.WARNING)
                alert.title = "Connection Error"
                alert.headerText = "Can't connect to server! Please try again later."

                alert.showAndWait()
                return
            } catch (e: WebClientResponseException.BadRequest) {
                val alert = Alert(Alert.AlertType.WARNING)
                alert.title = e.statusText
                alert.headerText = ServerException.fromBadRequestException(e).message

                alert.showAndWait()
                return
            }
            val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/main-view.fxml"))
            val stage = (actionEvent.source as Node).scene.window as Stage
            val scene = Scene(fxmlLoader.load())
            stage.scene = scene
            stage.show()
        }
    }

    fun back(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/main-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }

    fun edit(actionEvent: ActionEvent) {

        if (website.text.isEmpty() || username.text.isEmpty() || password.text.isEmpty()) {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Fields can't be empty"
            alert.showAndWait()
            return
        }

        val alert: Alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Editing password"
        alert.headerText = "Are you sure, you want to edit password?"
        if (alert.showAndWait().get() == ButtonType.OK)
            selectedPassword.let {
                it.website = website.text
                it.username = username.text
                it.encryptedPassword = password.text
                try {
                    WebClientManagerPassword().editPassword(
                        PasswordRequestData(
                            ClientKeyPinData(
                                LoggedClient.key,
                                LoggedClient.password
                            ), it
                        )
                    )

                } catch (e: WebClientRequestException) {
                    var alert = Alert(Alert.AlertType.WARNING)
                    alert.title = "Connection Error"
                    alert.headerText = "Can't connect to server! Please try again later."

                    alert.showAndWait()
                    return
                } catch (e: WebClientResponseException.BadRequest) {
                    var alert = Alert(Alert.AlertType.WARNING)
                    alert.title = e.statusText
                    alert.headerText = ServerException.fromBadRequestException(e).message

                    alert.showAndWait()
                    return
                }
            }
    }
}