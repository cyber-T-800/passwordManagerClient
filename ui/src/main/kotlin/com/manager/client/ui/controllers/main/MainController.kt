package com.manager.client.ui.controllers.main

import com.manager.client.WebClientManagerPassword
import com.manager.client.client.ClientKeyPinData
import com.manager.client.exceptions.ServerException
import com.manager.client.password.Password
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
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.lang.NullPointerException

class MainController {

    @FXML
    lateinit var listOfPasswords : TableView<Password>

    @FXML
    fun initialize() {
        //set columns of table
        listOfPasswords.columns[0].cellValueFactory = PropertyValueFactory<Password, String>("website")
        listOfPasswords.columns[1].cellValueFactory = PropertyValueFactory<Password, String>("username")
        listOfPasswords.columns[2].cellValueFactory = PropertyValueFactory<Password, String>("encryptedPassword")

        //load passwords saved on device
        SavedPasswords.load()
        LoggedClientPasswords += SavedPasswords.getByClientId(LoggedClient.id).toTypedArray()

        //retrieve passwords from server
        try {
            val passwordsFromServer = WebClientManagerPassword().getPasswords(ClientKeyPinData(LoggedClient.key, LoggedClient.password))!!
            LoggedClientPasswords += passwordsFromServer
            SavedPasswords += passwordsFromServer
        } catch (e: WebClientRequestException) {
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Connection Error"
            alert.headerText = "Can't connect to server! Please try again later."

            alert.showAndWait()
        } catch (e: WebClientResponseException.BadRequest) {
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = e.statusText
            alert.headerText = ServerException.fromBadRequestException(e).message

            alert.showAndWait()
        }

        LoggedClientPasswords.decryptAllPasswords()

        //add all passwords to table with hidden passwords
        for (p in LoggedClientPasswords.passwords) {
            listOfPasswords.items += Password(p.id, p.website, p.username, "********", p.clientId)
        }
    }

    //change to add password view
    fun add(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/add-password-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }

    fun clickItem(mouseEvent: MouseEvent) {
        val selectedPassword: Password = try {
            LoggedClientPasswords.getByID(listOfPasswords.selectionModel.selectedItem.id)!!
        }catch (e : NullPointerException){
            return
        }

        //change view to show password view
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/show-password-view.fxml"))
        val stage = (mouseEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        fxmlLoader.getController<ShowPasswordController>().setPassword(selectedPassword)
        stage.scene = scene
        stage.show()

        return

    }
}