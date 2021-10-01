package com.manager.client

import com.manager.client.client.Client
import com.manager.client.client.ClientKeyIdData
import com.manager.client.client.ClientPinSetUp
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import reactor.core.publisher.Mono

/*
    class for connection to server client api
 */
class WebClientManagerClient {

    val webClient : WebClient = WebClient.create("http://localhost:8080/api/")

    /*
        register client to server
        return client stay login key if succesfull
        return -1 if can't connect to server
    */
    fun register(client: Client) : ClientKeyIdData{
        var result : ClientKeyIdData?

        try{
           result = webClient.post().uri("client/register").body(Mono.just(client), Client::class.java).retrieve().bodyToMono(
                ClientKeyIdData::class.java).block()
        }catch (e : WebClientRequestException){
            return ClientKeyIdData(0, "-1")
        }

        return result
    }

    /*
        set up pin to stay login on server
        return client private key
        return -1 if can't connect to server
    */
    fun registerSetPin(pinSetUp: ClientPinSetUp) : String{
        var result : String?

        try{
            result = webClient.post().uri("client/register/pin").body(Mono.just(pinSetUp), ClientPinSetUp::class.java).retrieve().bodyToMono(
                String::class.java).block()
        }catch (e : WebClientRequestException){
            return "-1"
        }

        return result
    }

    /*
        login to server
        return client stay login key
        return -1
    */
    fun login(client: Client) : ClientKeyIdData {
        var result : ClientKeyIdData

        try{
            result = webClient.post().uri("client/login").body(Mono.just(client), Client::class.java).retrieve()
                .bodyToMono(ClientKeyIdData::class.java).block()
        }catch (e : WebClientRequestException){
            return ClientKeyIdData(0, "-1")
        }

        return result
    }


    /*
        login with pin
        return private key
        return -1 if cannot connect to serve
        return 0 if combination of stay-login key and pin is invalid
        return 1 if stay-login key is invalid
     */
    fun loginWithPin(clientPinSetUp: ClientPinSetUp) : String{
        var result : String?

        try{
            result = webClient.post().uri("client/login/pin").body(Mono.just(clientPinSetUp), ClientPinSetUp::class.java).retrieve()
                .bodyToMono(String::class.java).block()
        }catch (e : WebClientRequestException){
            return "-1"
        }

        return result
    }
}