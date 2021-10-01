package com.manager.client

import org.springframework.web.reactive.function.client.WebClient

class WebClientManagerPassword {
    val webClient : WebClient = WebClient.create("http://localhost:8080/api/password/")


}