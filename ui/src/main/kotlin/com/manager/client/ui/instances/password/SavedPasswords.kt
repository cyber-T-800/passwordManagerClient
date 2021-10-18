package com.manager.client.ui.instances.password

import com.example.manager.utils.AsymmetricalCryptoUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.manager.client.password.Password
import com.manager.client.ui.instances.client.LoggedClient
import java.io.File
import java.lang.IllegalArgumentException
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException

object SavedPasswords {

    private const val savedFilePath = "ui/src/main/resources/com/manager/client/ui/savedData/saved-passwords.json"


    var savedPasswordsData : PasswordsData = PasswordsData()

    //override assign operator to add passwords to saved passwords instance
    //used for filter off passwords that are actually in instance
    @Override
    operator fun plusAssign(passwordsList: Array<Password>){
        for (p in passwordsList)
            this += p
        save()
    }

    @Override
    operator fun plusAssign(password: Password){
        if(!passwordAlreadySaved(password.id)) {
            savedPasswordsData.passwords += password
        }
    }

    @Override
    operator fun minusAssign(password: Password){
        if(passwordAlreadySaved(password.id)){
            savedPasswordsData.passwords -= getByID(password.id)!!
        }
    }

    fun encryptAllPasswords(){
        for(p in savedPasswordsData.passwords){
            if(p.clientId == LoggedClient.id)
                encryptPassword(p)
        }
    }

    fun encryptPassword(password: Password){
        if(!passwordIsEncrypted(password)){
            password.encryptedPassword =try {
                AsymmetricalCryptoUtils.encryptMessageToBase64(LoggedClient.keyPair.public, password.encryptedPassword)
            }catch (e : IllegalBlockSizeException){
                password.encryptedPassword
            }
        }
    }

    private fun passwordIsEncrypted(password: Password): Boolean {
        try {
            AsymmetricalCryptoUtils.decryptMessage(LoggedClient.keyPair.private, password.encryptedPassword)
        }catch (e : BadPaddingException){
            return false
        }catch (e : IllegalArgumentException){
            return false
        }
        return true
    }

    fun save() {
        var saveFile = File(savedFilePath)
        encryptAllPasswords()
        saveFile.writeText(Gson().toJson(savedPasswordsData))
    }

    fun getByID(ID: Long) : Password?{
        for(p in savedPasswordsData.passwords)
            if(p.id == ID)
                return p
        return null
    }

    fun passwordAlreadySaved(ID: Long) : Boolean{
        return getByID(ID) != null
    }

    fun getByClientId(clientID : Long) : List<Password>{
        var result : MutableList<Password> = mutableListOf()
        for(p in savedPasswordsData.passwords)
            if(p.clientId == clientID)
                result += p
        return result
    }


    /*
        load logged clients from device
        create file of logged clients, if it doesn't exist or is corrupted
     */
    fun load(){
        var saveFile = File(savedFilePath)
        //confirms if file exists
        if(!saveFile.exists()) {
            save()
        }
        //confirms if file isn't empty
        else if(saveFile.readText().trim() == ""){
            save()
        }
        //confirms if content of file is in right form
        savedPasswordsData = try{
            var saveFileCheck = Gson().fromJson(saveFile.readText(), PasswordsData::class.java)
            saveFileCheck!!
        }catch (e : JsonSyntaxException){
            save()
            PasswordsData()
        }
    }
}