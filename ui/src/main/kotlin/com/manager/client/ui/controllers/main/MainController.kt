package com.manager.client.ui.controllers.main

import com.manager.client.WebClientManagerPassword
import com.manager.client.client.ClientKeyPinData
import com.manager.client.password.Password
import com.manager.client.ui.PasswordManagerUI
import com.manager.client.ui.instances.client.LoggedClient
import com.manager.client.ui.instances.client.LoggedClientsData
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
import javafx.stage.Stage

class MainController {

    @FXML
    lateinit var listOfPasswords : TableView<Password>

    @FXML
    fun initialize(){
        //set columns of table
        listOfPasswords.columns[0].cellValueFactory  = PropertyValueFactory<Password, String>("website")
        listOfPasswords.columns[1].cellValueFactory  = PropertyValueFactory<Password, String>("username")
        listOfPasswords.columns[2].cellValueFactory  = PropertyValueFactory<Password, String>("encryptedPassword")

        //load passwords saved on device
        SavedPasswords.load()
        LoggedClientPasswords.passwords += SavedPasswords.getByClientId(LoggedClient.id)

        //retrieve passwords from server
        val passwordsFromServer = WebClientManagerPassword().getPasswords(ClientKeyPinData(LoggedClient.key, LoggedClient.password))
        if(passwordsFromServer == null){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Connection Error"
            alert.headerText = "Can't synchronise passwords with server."

            alert.showAndWait()
            return
        }else{
            LoggedClientPasswords.passwords += passwordsFromServer
            SavedPasswords.savedPasswordsData.passwords += passwordsFromServer
            SavedPasswords.save()
        }


        listOfPasswords.items.addAll(LoggedClientPasswords.passwords)
    }

    //change to add password view
    fun add(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/add-password-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }
}