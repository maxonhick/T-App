package com.t_bank_app.library

class Disk(
    objectId: Int,
    access: Boolean,
    name: String,
    val type: DiskType
): LibraryObjects(objectId, access, name), Homeable {
    override fun longInformation() {
        val possible = if (access) "Да" else "Нет"
        println("$type $name доступен: $possible")
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