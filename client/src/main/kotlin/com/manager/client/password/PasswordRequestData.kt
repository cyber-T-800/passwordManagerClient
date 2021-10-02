package com.manager.client.password

import com.manager.client.client.ClientKeyPinData


/*
    data class for password http request
    store password alongside with client data
 */
data class PasswordRequestData(
    var clientKeyPinData: ClientKeyPinData,
    var password: Password
){
    constructor() : this(ClientKeyPinData(), Password())
}
