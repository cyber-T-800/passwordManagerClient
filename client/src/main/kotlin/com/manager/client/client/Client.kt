package com.manager.client.client

data class Client (
    var id : Long,
    var username : String,
    var password : String,
    var privateKey: String
    ){
    constructor() : this(0, "", "", "")
}
