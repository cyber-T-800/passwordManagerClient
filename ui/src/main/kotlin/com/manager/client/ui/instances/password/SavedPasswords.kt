package com.manager.client.ui.instances.password

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.File

object SavedPasswords {

    private const val savedFilePath = "ui/src/main/resources/com/manager/client/ui/savedData/saved-passwords.json"


    var savedPasswordsData : SavedPasswordsData = SavedPasswordsData()

    fun save() {
        var saveFile = File(savedFilePath)
        saveFile.writeText(Gson().toJson(savedPasswordsData))
    }


    /*
        load logged clients from device
        create file of logged clients, if it doesn't exist or is corrupted
     */
    fun load(){
        var saveFile = File(savedFilePath)
        //confirms if file exists
        if(!saveFile.exists()) {
            saveFile.writeText(Gson().toJson(savedPasswordsData))
        }
        //confirms if file isn't empty
        else if(saveFile.readText().trim() == ""){
            saveFile.writeText(Gson().toJson(savedPasswordsData))
        }
        //confirms if content of file is in right form
        savedPasswordsData = try{
            var saveFileCheck = Gson().fromJson(saveFile.readText(), SavedPasswordsData::class.java)
            saveFileCheck!!
        }catch (e : JsonSyntaxException){
            saveFile.writeText(Gson().toJson(savedPasswordsData))
            SavedPasswordsData()
        }
    }
}