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


    @FXML
    fun submit(){
        if(pin1.text == "" || pin2.text == ""){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Fields can't be empty"

            alert.showAndWait()
            return
        }
        if (pin1.text != pin2.text){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Pins are not equal!"

            alert.showAndWait()
            return
        }

        if(pin1.text.length < 4){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Pin can't be shorter that 4 characters!"

            alert.showAndWait()
            return
        }

        if(pin1.text.length > 8){
            var alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Warning"
            alert.headerText = "Pin can't be longer that 8 characters!"

            alert.showAndWait()
            return
        }

        var privateKey = WebClientManagerClient().registerSetPin(ClientPinSetUp(key, pin1.text))


        TODO("implement change screen")
    }


}