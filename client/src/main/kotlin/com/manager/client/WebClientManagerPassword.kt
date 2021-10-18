package com.manager.client


import com.manager.client.client.ClientKeyPinData
import com.manager.client.password.Password
import com.manager.client.password.PasswordRequestData
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import reactor.core.publisher.Mono
import javax.security.auth.login.LoginContext


class WebClientManagerPassword {
    val webClient : WebClient = WebClient.create("http://localhost:8080/api/password/")

    /*
        save password on server
        return password id
     */
    fun savePassword(passwordRequestData: PasswordRequestData) : Long{
        return webClient.post().uri("save").body(Mono.just(passwordRequestData), passwordRequestData::class.java).retrieve().bodyToMono(Long::class.java).block()!!
    }

    /*
       get passwords by combination of client API key and pin code
    */
    fun getPasswords(clientKeyPinData: ClientKeyPinData) : Array<Password>?{
        return webClient.post().uri("get").body(Mono.just(clientKeyPinData), ClientKeyPinData::class.java).retrieve().bodyToMono(Array<Password>::class.java).block()
    }


    /*
       delete password from database
   */
    fun editPassword(passwordRequestData: PasswordRequestData){
        webClient.post().uri("edit").body(Mono.just(passwordRequestData), PasswordRequestData::class.java).retrieve().bodyToMono(Int::class.java).block()
    }


    /*
       delete password from database
   */
    fun deletePassword(passwordRequestData: PasswordRequestData){
        webClient.post().uri("delete").body(Mono.just(passwordRequestData), PasswordRequestData::class.java).retrieve().bodyToMono(Int::class.java).block()
    }

}