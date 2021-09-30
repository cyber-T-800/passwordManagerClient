package com.manager.client

data class LoggedClients(
    var clients: HashMap<String, Client>
){
    constructor() : this(HashMap())
}