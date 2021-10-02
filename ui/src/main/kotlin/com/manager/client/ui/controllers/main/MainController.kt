package com.manager.client.ui.controllers.main

import com.manager.client.WebClientManagerPassword
import com.manager.client.client.ClientKeyPinData
import com.manager.client.password.Password
import com.manager.client.ui.instances.client.LoggedClient
import com.manager.client.ui.instances.password.LoggedClientPasswords
import com.manager.client.ui.instances.password.SavedPasswords
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory

class MainController {

    @FXML
    lateinit var listOfPasswords : TableView<Password>

    @FXML
    fun initialize(){
        listOfPasswords.columns[0].cellValueFactory  = PropertyValueFactory<Password, String>("website")
        listOfPasswords.columns[1].cellValueFactory  = PropertyValueFactory<Password, String>("username")
        listOfPasswords.columns[2].cellValueFactory  = PropertyValueFactory<Password, String>("encryptedPassword")
        SavedPasswords.load()
        LoggedClientPasswords.passwords += SavedPasswords.getByClientId(LoggedClient.id)
        val passwordsFromServer = WebClientManagerPassword().getPasswords(ClientKeyPinData(LoggedClient.key, LoggedClient.password))
        if(passwordsFromServer == null){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Connection Error"
            alert.headerText = "Something went Wrong."

            alert.showAndWait()
            return
        }else{
            LoggedClientPasswords.passwords += passwordsFromServer!!
        }


        listOfPasswords.items.addAll(LoggedClientPasswords.passwords)
    }

}