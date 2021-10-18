package com.manager.client.ui.controllers.login

import com.example.manager.utils.AsymmetricalCryptoUtils
import com.example.manager.utils.SymmetricalCryptoUtils
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
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.stage.Stage
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.security.KeyPair
import java.security.MessageDigest
import java.util.*

class LoginWithPinController {
    @FXML
    lateinit var label: Label
    @FXML
    lateinit var pin : PasswordField

    fun initialize(){
        label.text += LoggedClient.username
    }

    fun submit(actionEvent: ActionEvent) {
        if(pin.text == ""){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Field can't be empty!"

            alert.showAndWait()
            return
        }
        val privateKeyFromServer = try {
            WebClientManagerClient().loginWithPin(ClientKeyPinData(LoggedClient.key, pin.text))
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

        //save non-hashed pin to logged client instance
        //load private and public key and save it to instance
        LoggedClient.let{
            it.password = pin.text
            val secretKey = SymmetricalCryptoUtils.getKeyFromPassword(it.password)
            val privateKeyInBytes = SymmetricalCryptoUtils.decryptMessage(secretKey, privateKeyFromServer)

            //save decrypted crypto keys to instance
            val privateKey = AsymmetricalCryptoUtils.privateKeyFromBytes(privateKeyInBytes)
            var publicKey = AsymmetricalCryptoUtils.publicKeyFromPrivate(privateKey)
            it.keyPair = KeyPair(publicKey, privateKey)
        }



        //if everything is in order, change to main view
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/main-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()

    }

    fun back(actionEvent: ActionEvent) {
        val fxmlLoader = FXMLLoader(PasswordManagerUI::class.java.getResource("views/select-client-view.fxml"))
        val stage = (actionEvent.source as Node).scene.window as Stage
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.show()
    }


}