package com.manager.client.ui.instances.password

import com.manager.client.password.Password

data class SavedPasswordsData(
    var passwords : List<Password>
){
    constructor() : this(listOf())
}
