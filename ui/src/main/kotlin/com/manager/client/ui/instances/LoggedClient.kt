package com.manager.client.ui.instances

import java.security.KeyPair


//Instance of currently logged client
object LoggedClient {
    lateinit var key: String
    lateinit var username : String
    lateinit var password : String
    lateinit var keyPair: KeyPair
}