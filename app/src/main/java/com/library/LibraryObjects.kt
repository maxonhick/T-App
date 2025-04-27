package com.library

import android.os.Parcelable

abstract class LibraryObjects(
    val objectId: Int,
    var access: Boolean,
    val name: String,
    val objectType: TypeLibraryObjects,
    val createdAt: Long = System.currentTimeMillis()
): Returnable, Parcelable{
    fun smallInformation(){
        if (access){
            println("$name доступна: Да")
        } else {
            println("$name доступна: Нет")
        }
    }

    abstract fun longInformation()
}