package com.manager
import com.manager.client.client.Client
import com.manager.client.WebClientManagerClient
import kotlin.test.Test

class WebClientManagerClientTest{
    @Test
    fun registrationTest(){

        val webClient = WebClientManagerClient()

        var client = Client(0, "Roxor", "dfiofaso", "")

        val keyClient = webClient.register(client)
    }
}