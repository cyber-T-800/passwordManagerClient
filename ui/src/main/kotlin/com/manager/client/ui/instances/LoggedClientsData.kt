package com.manager.client.ui.instances

import com.manager.client.Client

data class LoggedClientsData(
    var clients: HashMap<String, Client>
){
    constructor() : this(HashMap())
}