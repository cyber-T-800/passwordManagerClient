package com.manager.client.client

data class ClientPinSetUp(
    var key : String,
    var pinCode : String
){
    constructor() : this("", "")
}
