package com.library

class Book(
    objectId: Int,
    access: Boolean,
    name: String,
    val pages: Int,
    val author: String
): LibraryObjects(objectId, access, name), Home_able, Readable {
    override fun longInformation() {
        if (access){
            println("книга: $name ($pages стр.) автора: " +
                    "$author с id: $objectId доступна: Да")
        } else {
            println("книга: $name ($pages стр.) автора: " +
                    "$author с id: $objectId доступна: Нет")
        }
    }

    override fun getHome() {
        if (access) {
            access = false
            println("Книга $objectId взяли домой")
        } else {
            println("Книгу уже кто-то взял")
        }
    }

    override fun getRead() {
        if (access) {
            access = false
            println("Книга $objectId взяли в читальный зал")
        } else {
            println("Книгу уже кто-то взял")
        }
    }

    override fun returnObject() {
        if (access) {
            println("Книга $objectId находится в библиотеке, её нельзя вернуть")
        } else {
            access = true
            println("Книгу $objectId вернули в библиотеку")
        }
    }
}