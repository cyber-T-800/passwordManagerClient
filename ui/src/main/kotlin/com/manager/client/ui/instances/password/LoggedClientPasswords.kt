package com.manager.client.ui.instances.password

import com.manager.client.password.Password

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

}