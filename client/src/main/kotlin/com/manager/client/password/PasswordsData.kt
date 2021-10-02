package com.manager.client.password

import com.manager.client.password.Password

data class PasswordsData(
    var passwords : List<Password>
){
    constructor() : this(listOf())
}
