package com.manager.client.ui.instances.client

import com.manager.client.client.Client

data class LoggedClientsData(
    var clients: HashMap<String, Client>
){
    constructor() : this(HashMap())
}