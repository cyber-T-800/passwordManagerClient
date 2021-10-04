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
        return 0 if stay-login key or pin is invalid
        return -1 if it can't connect server
     */
    fun savePassword(passwordRequestData: PasswordRequestData) : Long{
        return  try{
            webClient.post().uri("save").body(Mono.just(passwordRequestData), passwordRequestData::class.java).retrieve().bodyToMono(Long::class.java).block()
        }catch (e : WebClientRequestException){
            -1
        }
    }

    /*
       get passwords by combination of client API key and pin code
       return null combination is invalid
    */
    fun getPasswords(clientKeyPinData: ClientKeyPinData) : Array<Password>?{
        return  try{
            webClient.post().uri("get").body(Mono.just(clientKeyPinData), ClientKeyPinData::class.java).retrieve().bodyToMono(Array<Password>::class.java).block()
        }catch (e : WebClientRequestException){
            null
        }
    }


    /*
       delete password from database
       return 0 operation successful
       return 1 if client stay-login data are invalid
       return 2 if password don't belong to requested client
       return -1 if it can't connect to server
   */
    fun editPassword(passwordRequestData: PasswordRequestData) :Int{
        return try{
             webClient.post().uri("edit").body(Mono.just(passwordRequestData), PasswordRequestData::class.java).retrieve().bodyToMono(Int::class.java).block()
        }catch (e : WebClientRequestException){
             -1
        }
    }


    /*
       delete password from database
       return 0 operation successful
       return 1 if client stay-login data are invalid
       return 2 if password don't belong to requested client
       return -1 if it can't connect to server
   */
    fun deletePassword(passwordRequestData: PasswordRequestData) : Int{
        return try{
            webClient.post().uri("delete").body(Mono.just(passwordRequestData), PasswordRequestData::class.java).retrieve().bodyToMono(Int::class.java).block()
        }catch (e : WebClientRequestException){
            -1
        }
    }

}