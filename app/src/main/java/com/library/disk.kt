package com.library

class Disk(
    objectId: Int,
    access: Boolean,
    name: String,
    val type: String
): LibraryObjects(objectId, access, name), Home_able {
    override fun longInformation() {
        if (access){
            println("$type $name доступен: Да")
        } else {
            println("$type $name доступен: Нет")
        }
    }

    override fun returnObject() {
        if (access) {
            println("Диск $objectId находится в библиотеке, его нельзя вернуть")
        } else {
            access = true
            println("Диск $objectId вернули в библиотеку")
        }
    }

    override fun getHome() {
        if (access) {
            access = false
            println("Диск $objectId взяли домой")
        } else {
            println("Диск уже кто-то взял")
        }
    }
}