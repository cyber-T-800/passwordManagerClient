package com.manager.client.ui.instances

import com.manager.client.Client


//Instance of currently logged client
object LoggedClient {
    lateinit var key: String
    lateinit var client: Client
}