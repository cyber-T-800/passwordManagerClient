package com.manager.client


import com.manager.client.client.ClientKeyPinData
import com.manager.client.password.Password
import com.manager.client.password.PasswordRequestData
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import reactor.core.publisher.Mono


class WebClientManagerPassword {
    val webClient : WebClient = WebClient.create("http://localhost:8080/api/password/")


    /*
        save password on server
        return -1 if it can't connect to server
        return 0 if password is saved successfully
        return 1 if stay-logged key is invalid
        return 2 if pin code is invalid
     */
    fun save(passwordRequestData: PasswordRequestData) : Int{
        val result : Int = try{
            webClient.post().uri("save").body(passwordRequestData, passwordRequestData::class.java).retrieve().bodyToMono(Int::class.java).block()
        }catch (e : WebClientRequestException){
            -1
        }
        return result
    }

    /*
       get passwords by combination of client API key and pin code
       return null combination is invalid
    */
    fun getPasswords(clientKeyPinData: ClientKeyPinData) : Array<Password>?{
        val result : Array<Password>? = try{
            webClient.post().uri("get").body(Mono.just(clientKeyPinData), ClientKeyPinData::class.java).retrieve().bodyToMono(Array<Password>::class.java).block()
        }catch (e : WebClientRequestException){
            null
        }
        return result
    }

}