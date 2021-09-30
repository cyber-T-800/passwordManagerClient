package com.manager.client.ui

import com.dlsc.formsfx.model.structure.StringField
import com.manager.client.ClientPinSetUp
import com.manager.client.WebClientManagerClient
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.PasswordField

class SetUpPinController {
    lateinit var key : String

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
    fun submit(){
        //checks if fields are not empty
        if(pin1.text == "" || pin2.text == ""){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Fields can't be empty"

            alert.showAndWait()
            return
        }
        //checks if pin are equal
        if (pin1.text != pin2.text){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Pins are not equal!"

            alert.showAndWait()
            return
        }
        //checks if pin length is at least 4 character long
        if(pin1.text.length < 4){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Pin can't be shorter that 4 characters!"

            alert.showAndWait()
            return
        }
        //checks if pin isn't longer than 8 characters
        if(pin1.text.length > 8){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Pin can't be longer that 8 characters!"

            alert.showAndWait()
            return
        }

        //send pin to server, return private key of client
        var privateKey = WebClientManagerClient().registerSetPin(ClientPinSetUp(key, pin1.text))

        //if can't connect to server
        if(key == "-1"){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Connection Error"
            alert.headerText = "Can't connect to server! Please try again later."

            alert.showAndWait()
            return
        }


        TODO("implement change screen")
        TODO("implement save client to device")
    }


}