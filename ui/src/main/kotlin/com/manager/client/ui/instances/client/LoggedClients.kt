package com.manager.client.ui.instances.client

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.manager.client.client.Client
import java.io.File

/*
    instance of all logged clients
    used primary to load and save clients on device
 */
object LoggedClients {
    private const val loggedFilePath = "ui/src/main/resources/com/manager/client/ui/logged.json"

    var loggedClients = LoggedClientsData()

    fun getClients() : HashMap<String, Client>{
        return loggedClients.clients
    }

    fun save() {
        var loggedFile = File(loggedFilePath)
        loggedFile.writeText(Gson().toJson(loggedClients))

    }


    /*
        load logged clients from device
        create file of logged clients, if it doesn't exist or is corrupted
     */
    fun load(){
        var loggedFile = File(loggedFilePath)
        //confirms if file exists
        if(!loggedFile.exists()) {
            loggedFile.writeText(Gson().toJson(loggedClients))
        }
        //confirms if file isn't empty
        else if(loggedFile.readText().trim() == ""){
            loggedFile.writeText(Gson().toJson(loggedClients))
        }
        //confirms if content of file is in right form
        loggedClients = try{
            var loggedClientsCheck = Gson().fromJson(loggedFile.readText(), LoggedClientsData::class.java)
            loggedClientsCheck!!
        }catch (e : JsonSyntaxException){
            loggedFile.writeText(Gson().toJson(loggedClients))
            LoggedClientsData()
        }
    }
}