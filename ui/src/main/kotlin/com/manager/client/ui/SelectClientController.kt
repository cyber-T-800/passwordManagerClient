package com.manager.client.ui

import com.manager.client.Client
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseEvent
import javafx.stage.Stage

class SelectClientController {

    @FXML
    lateinit var listOfClients : TableView<Client>

    /*
        initialize table of clients
     */
    @FXML
    fun initialize(){
        listOfClients.columns[0].cellValueFactory  = PropertyValueFactory<Client, String>("username")
        listOfClients.items.addAll(PasswordManagerUI.loggedClients.clients.values)
    }


    /*
        select client from table,
        change scene to log in with pin
     */
    @FXML
    fun clickItem(mouseEvent: MouseEvent) {
        var selectedClient: Client = listOfClients.selectionModel.selectedItem

        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("login-with-pin-view.fxml"))
        val stage = (mouseEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        fxmlLoader.getController<LoginWithPinController>().selectedClient = selectedClient
        stage.scene = scene
        stage.show()
    }

    //Change view to login
    fun backToLogin(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("login-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }

    //Change view to sign up
    fun backToSignUp(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("sign-up-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }


}