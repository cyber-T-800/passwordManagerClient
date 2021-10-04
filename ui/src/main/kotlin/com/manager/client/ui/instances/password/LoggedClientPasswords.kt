package com.manager.client.ui.instances.password

import com.example.manager.utils.AsymmetricalCryptoUtils
import com.manager.client.password.Password
import com.manager.client.ui.instances.client.LoggedClient
import java.lang.IllegalArgumentException
import javax.crypto.BadPaddingException

object LoggedClientPasswords {
    var passwords : List<Password> = listOf()

    @Override
    operator fun plusAssign(passwordsList: Array<Password>){
        for (p in passwordsList)
            this += p
    }

    @Override
    operator fun plusAssign(password: Password){
        if(!passwordAlreadyExists(password.id))
            passwords += password
    }

    fun passwordAlreadyExists(ID : Long) : Boolean{
        return  getByID(ID) != null
    }

    fun getByID(ID: Long) : Password?{
        for(p in passwords)
            if(p.id == ID)
                return p
        return null
    }

    fun decryptAllPasswords() {
        for(p in passwords)
            decryptPassword(p)
    }

    fun decryptPassword(p : Password){
        p.encryptedPassword = try {
            AsymmetricalCryptoUtils.decryptMessageToString(LoggedClient.keyPair.private, p.encryptedPassword)
        }catch ( e : BadPaddingException){
            p.encryptedPassword
        }catch (e : IllegalArgumentException){
            p.encryptedPassword
        }
    }

}