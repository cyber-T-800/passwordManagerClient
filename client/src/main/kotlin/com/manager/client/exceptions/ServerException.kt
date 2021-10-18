package com.manager.client.exceptions

import com.google.gson.Gson
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.time.ZonedDateTime

data class ServerException(
    val message : String,
    val httpStatus: HttpStatus,
    val timestamp : String
){
    companion object{
        fun fromBadRequestException(e :WebClientResponseException.BadRequest) : ServerException{
            return Gson().fromJson(e.responseBodyAsString, ServerException::class.java)
        }
    }
}

