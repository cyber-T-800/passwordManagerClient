package com.manager
import com.manager.client.Client
import com.manager.client.ClientPinSetUp
import com.manager.client.WebClientManagerClient
import kotlin.test.Test
import kotlin.test.assertNotNull

class WebClientManagerClientTest{
    @Test
    fun registrationTest(){

        val webClient = WebClientManagerClient()

        var client = Client(0, "Roxor", "dfiofaso", "")

        val keyClient = webClient.register(client)
    }
}