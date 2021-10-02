package com.manager.client.ui.instances.client

import java.security.KeyPair


//Instance of currently logged client
object LoggedClient {
    lateinit var key: String
    var id : Long = 0
    lateinit var username : String
    lateinit var password : String
    lateinit var keyPair: KeyPair
}