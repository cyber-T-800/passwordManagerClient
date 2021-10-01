package com.manager.client.ui

import com.manager.client.Client

data class LoggedClients(
    var clients: HashMap<String, Client>
){
    constructor() : this(HashMap())
}