package com.manager.client

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.body
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono


class WebClientManagerClient {

    val webClient : WebClient = WebClient.create("http://localhost:8080/")

    fun register(client: Client) : String{
         return webClient.post().uri("api/client/register").body(Mono.just(client), Client::class.java).retrieve().bodyToMono(
            String::class.java).block()
    }

    fun registerSetPin(pinSetUp: ClientPinSetUp) : String{
        return webClient.post().uri("api/client/register/pin").body(Mono.just(pinSetUp), ClientPinSetUp::class.java).retrieve().bodyToMono(
            String::class.java).block()
    }

    fun login(client: Client) : String {
        return webClient.post().uri("api/client/login").body(Mono.just(client), Client::class.java).retrieve()
            .bodyToMono(String::class.java).block()
    }

    fun get() : Client {
        return webClient.get().uri("api/client").retrieve().bodyToMono(Client::class.java).block()
    }
}