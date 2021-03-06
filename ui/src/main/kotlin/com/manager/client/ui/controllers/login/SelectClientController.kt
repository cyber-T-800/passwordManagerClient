package com.manager.client.ui.controllers.login

import com.manager.client.client.Client
import com.manager.client.ui.PasswordManagerUI
import com.manager.client.ui.instances.client.LoggedClient
import com.manager.client.ui.instances.client.LoggedClients
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import java.lang.NullPointerException

class SelectClientController {

    @FXML
    lateinit var listOfClients : TableView<Client>

    /*
        initialize table of clients
     */
    @FXML
    fun initialize(){
        listOfClients.columns[0].cellValueFactory  = PropertyValueFactory<Client, String>("username")
        listOfClients.items.addAll(LoggedClients.getClients().values)
    }


    /*
        select client from table,
        change scene to log in with pin
     */
    @FXML
    fun clickItem(mouseEvent: MouseEvent) {

        var selectedClient: Client = try {
                listOfClients.selectionModel.selectedItem
            }catch (e : NullPointerException){
                return
            }

        LoggedClient.let {
            it.id = selectedClient.id
            it.username = selectedClient.username
            it.password = selectedClient.password

            for(c in LoggedClients.getClients()){
                if(c.value.equals(selectedClient))
                    it.key = c.key
            }
        }

        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/login-with-pin-view.fxml"))
        val stage = (mouseEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }

    //Change view to login
    fun backToLogin(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/login-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }

    //Change view to sign up
    fun backToSignUp(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/sign-up-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }


}