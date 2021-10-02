package com.manager.client.ui.instances.password

import com.manager.client.password.Password

data class PasswordsData(
    var passwords : List<Password>
){
    constructor() : this(listOf())
}
