package com.t_bank_app.library

class Book(
    objectId: Int,
    access: Boolean,
    name: String,
    val pages: Int,
    val author: String
): LibraryObjects(objectId, access, name), Homeable, Readable {
    override fun longInformation() {
        val possible = if (access) "Да" else "Нет"
        println("книга: $name ($pages стр.) автора: $author с id: $objectId доступна: $possible")
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