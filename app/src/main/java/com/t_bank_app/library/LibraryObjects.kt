package com.t_bank_app.library

abstract class LibraryObjects(
    val objectId: Int,
    var access: Boolean,
    val name: String
): Returnable {
    fun smallInformation(){
        if (access){
            println("$name доступна: Да")
        } else {
            println("$name доступна: Нет")
        }
    }

    abstract fun longInformation()
}