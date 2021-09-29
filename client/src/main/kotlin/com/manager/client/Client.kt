package com.manager.client

import java.security.PrivateKey


data class Client (
    var id : Long,
    var username : String,
    var password : String,
    var privateKey: String
    ){
    constructor() : this(0, "", "", "")
}
