package com.manager.client.password


data class Password(
    var id : Long,
    var website : String,
    var username : String,
    //in Base64
    //encrypted by public key
    var encryptedPassword : String,

    var clientId : Long
){
    constructor() : this(0, "", "", "", 0)
}