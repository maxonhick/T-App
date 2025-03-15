package com.library

class Newspaper(
    objectId: Int,
    access: Boolean,
    name: String,
    val releaseNumber: Int
): LibraryObjects(objectId, access, name), Readable {
    override fun longInformation() {
        if (access){
            println("выпуск: $releaseNumber газеты $name c id: " +
                    "$objectId доступен: Да")
        } else {
            println("выпуск: $releaseNumber газеты $name c id: " +
                    "$objectId доступен: Нет")
        }
    }

    override fun returnObject() {
        if (access) {
            println("Газета $objectId находится в библиотеке, её нельзя вернуть")
        } else {
            access = true
            println("Газету $objectId вернули в библиотеку")
        }
    }

    override fun getRead() {
        if (access) {
            access = false
            println("Газету $objectId взяли в читальный зал")
        } else {
            println("Газету уже кто-то взял")
        }
    }
}