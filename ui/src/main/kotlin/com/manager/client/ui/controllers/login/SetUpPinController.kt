package com.manager.client.ui.controllers.login

import com.example.manager.utils.AsymmetricalCryptoUtils
import com.example.manager.utils.SymmetricalCryptoUtils
import com.manager.client.Client
import com.manager.client.ClientPinSetUp
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
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.stage.Stage
import java.nio.charset.StandardCharsets
import java.security.KeyPair
import java.util.*

class SetUpPinController {
    @FXML
    lateinit var pin1 : PasswordField
    @FXML
    lateinit var pin2 : PasswordField
    @FXML
    lateinit var label : Label


    /*
        set up pin for stay-login on device
        save logged client on device
     */
    @FXML
    fun submit(actionEvent : ActionEvent){
        //checks if fields are not empty
        if(pin1.text == "" || pin2.text == ""){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Fields can't be empty"

            alert.showAndWait()
            return
        }
        //checks if pin are equal
        if (pin1.text != pin2.text){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Pins are not equal!"

            alert.showAndWait()
            return
        }
        //checks if pin length is at least 4 character long
        if(pin1.text.length < 4){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Pin can't be shorter that 4 characters!"

            alert.showAndWait()
            return
        }
        //checks if pin isn't longer than 8 characters
        if(pin1.text.length > 8){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Pin can't be longer that 8 characters!"

            alert.showAndWait()
            return
        }

        //send pin to server, return private key of client
        val privateKey = WebClientManagerClient().registerSetPin(ClientPinSetUp(LoggedClient.key, pin1.text))

        //if can't connect to server
        if(LoggedClient.key == "-1"){
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Connection Error"
            alert.headerText = "Can't connect to server! Please try again later."

            alert.showAndWait()
            return
        }



        LoggedClient.let {
            val secretKey = SymmetricalCryptoUtils.getKeyFromPassword(it.password)
            val privateKeyInBytes = SymmetricalCryptoUtils.decryptMessage(secretKey, privateKey)

            //save decrypted crypto keys to instance
            val privateKey = AsymmetricalCryptoUtils.privateKeyFromBytes(privateKeyInBytes)
            var publicKey = AsymmetricalCryptoUtils.publicKeyFromPrivate(privateKey)
            it.keyPair = KeyPair(publicKey, privateKey)

            TODO("hash pin")
            //save pin as hash
           // it.password = Base64.getEncoder().encodeToString(Hashing.sha256().hashString(it.password, StandardCharsets.UTF_8).asBytes())


            var privateKeyInBase64 = Base64.getEncoder().encodeToString(privateKeyInBytes)
            //save
            LoggedClients.getClients()[it.key] = Client(0, it.username, it.password, privateKeyInBase64)
            LoggedClients.save()
        }





        //change to main view
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("main-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }


}