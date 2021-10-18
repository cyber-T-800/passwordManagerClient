package com.manager.client.ui.controllers.login

import com.example.manager.utils.AsymmetricalCryptoUtils
import com.example.manager.utils.SymmetricalCryptoUtils
import com.manager.client.client.Client
import com.manager.client.client.ClientKeyPinData
import com.manager.client.WebClientManagerClient
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
import javafx.stage.Stage
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.security.KeyPair
import java.security.MessageDigest
import java.util.*

class SetUpPinController {
    @FXML
    lateinit var pin1 : PasswordField
    @FXML
    lateinit var pin2 : PasswordField


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
        val privateKeyFromServer = try {
            WebClientManagerClient().registerSetPin(ClientKeyPinData(LoggedClient.key, pin1.text))
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


        LoggedClient.let {
            it.password = pin1.text
            val secretKey = SymmetricalCryptoUtils.getKeyFromPassword(it.password)

            val privateKeyInBytes = SymmetricalCryptoUtils.decryptMessage(secretKey, privateKeyFromServer)

            //save decrypted crypto keys to instance
            val privateKey = AsymmetricalCryptoUtils.privateKeyFromBytes(privateKeyInBytes)
            var publicKey = AsymmetricalCryptoUtils.publicKeyFromPrivate(privateKey)
            it.keyPair = KeyPair(publicKey, privateKey)


           val bytes = MessageDigest
                .getInstance("SHA-256")
                .digest(it.password.toByteArray())
            val hashedPin = Base64.getEncoder().encodeToString(bytes)


            //save logged clients to device
            LoggedClients.getClients()[it.key] = Client(it.id, it.username, hashedPin, privateKeyFromServer)
            LoggedClients.save()
        }

        //change to main view
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/main-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }


}