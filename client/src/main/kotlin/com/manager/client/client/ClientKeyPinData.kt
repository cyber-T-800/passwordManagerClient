package com.manager.client.client

/*
    data class for sending stay-login key and pin over network
 */
data class ClientKeyPinData(
    var key : String,
    var pinCode : String
){
    constructor() : this("", "")
}
