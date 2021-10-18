package com.manager.client

import com.manager.client.client.Client
import com.manager.client.client.ClientKeyIdData
import com.manager.client.client.ClientKeyPinData
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import reactor.core.publisher.Mono

/*
    class for connection to server client api
 */
class WebClientManagerClient {

    val webClient : WebClient = WebClient.create("http://localhost:8080/api/client/")

    /*
        register client to server
        return client stay login key if succesfull
    */
    fun register(client: Client) : ClientKeyIdData{
           return webClient.post().uri("register").body(Mono.just(client), Client::class.java).retrieve().bodyToMono(
                ClientKeyIdData::class.java).block()!!
    }

    /*
        set up pin to stay login on server
        return client private key
    */
    fun registerSetPin(pinSetUp: ClientKeyPinData) : String{
        return webClient.post().uri("register/pin").body(Mono.just(pinSetUp), ClientKeyPinData::class.java).retrieve().bodyToMono(
                String::class.java).block()!!
    }

    /*
        login to server
        return client stay login key
    */
    fun login(client: Client): ClientKeyIdData {
        return webClient.post().uri("login").body(Mono.just(client), Client::class.java).retrieve()
            .bodyToMono(ClientKeyIdData::class.java).block()!!
    }


    /*
        login with pin
        return private key
     */
    fun loginWithPin(clientPinSetUp: ClientKeyPinData) : String{
        return webClient.post().uri("login/pin").body(Mono.just(clientPinSetUp), ClientKeyPinData::class.java).retrieve()
                .bodyToMono(String::class.java).block()!!
    }
}