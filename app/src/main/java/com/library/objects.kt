package com.library

abstract class LibraryObjects(
    val objectId: Int,
    var access: Boolean,
    val name: String
){
    fun smallInformation(){
        if (access){
            println("$name доступна: Да")
        } else {
            println("$name доступна: Нет")
        }
    }

    abstract fun longInformation()
    abstract fun takeHome()
    abstract fun takeRead()
    abstract fun returnObject()
    abstract fun workWithObject()
}

class Book(
    objectId: Int,
    access: Boolean,
    name: String,
    val pages: Int,
    val author: String
): LibraryObjects(objectId, access, name) {
    override fun longInformation() {
        if (access){
            println("книга: $name ($pages стр.) автора: " +
                    "$author с id: $objectId доступна: Да")
        } else {
            println("книга: $name ($pages стр.) автора: " +
                    "$author с id: $objectId доступна: Нет")
        }
    }

    override fun takeHome() {
        if (access) {
            access = false
            println("Книга $objectId взяли домой")
        } else {
            println("Книгу уже кто-то взял")
        }
    }

    override fun takeRead() {
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

    override fun workWithObject() {
        while (true){
            println("""
                1. Взять домой
                2. Читать в читальном зале
                3. Показать подробную информацию
                4. Вернуть
                5. Вернуться в изначальное меню
            """.trimIndent())
            when (readlnOrNull()?.toIntOrNull()){
                1 -> takeHome()
                2 -> takeRead()
                3 -> longInformation()
                4 -> returnObject()
                5 -> break
                else -> println("Неверный выбор, попробуйте ещё раз.")
            }
        }
    }
}

class Newspaper(
    objectId: Int,
    access: Boolean,
    name: String,
    val releaseNumber: Int
): LibraryObjects(objectId, access, name) {
    override fun longInformation() {
        if (access){
            println("выпуск: $releaseNumber газеты $name c id: " +
                    "$objectId доступен: Да")
        } else {
            println("выпуск: $releaseNumber газеты $name c id: " +
                    "$objectId доступен: Нет")
        }
    }

    override fun takeHome() {
        println("Газету нельязя взять домой")
    }

    override fun takeRead() {
        if (access) {
            access = false
            println("Газету $objectId взяли в читальный зал")
        } else {
            println("Газету уже кто-то взял")
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

    override fun workWithObject() {
        while (true){
            println("""
                1. Взять домой
                2. Читать в читальном зале
                3. Показать подробную информацию
                4. Вернуть
                5. Вернуться в изначальное меню
            """.trimIndent())
            when (readlnOrNull()?.toIntOrNull()){
                1 -> takeHome()
                2 -> takeRead()
                3 -> longInformation()
                4 -> returnObject()
                5 -> break
                else -> println("Неверный выбор, попробуйте ещё раз.")
            }
        }
    }
}

class Disk(
    objectId: Int,
    access: Boolean,
    name: String,
    val type: String
): LibraryObjects(objectId, access, name) {
    override fun longInformation() {
        if (access){
            println("$type $name доступен: Да")
        } else {
            println("$type $name доступен: Нет")
        }
    }

    override fun takeHome() {
        if (access) {
            access = false
            println("Диск $objectId взяли домой")
        } else {
            println("Диск уже кто-то взял")
        }
    }

    override fun takeRead() {
        println("Диск нельзя взять в читальный зал")
    }

    override fun returnObject() {
        if (access) {
            println("Диск $objectId находится в библиотеке, его нельзя вернуть")
        } else {
            access = true
            println("Диск $objectId вернули в библиотеку")
        }
    }

    override fun workWithObject() {
        while (true){
            println("""
                1. Взять домой
                2. Читать в читальном зале
                3. Показать подробную информацию
                4. Вернуть
                5. Вернуться в изначальное меню
            """.trimIndent())
            when (readlnOrNull()?.toIntOrNull()){
                1 -> takeHome()
                2 -> takeRead()
                3 -> longInformation()
                4 -> returnObject()
                5 -> break
                else -> println("Неверный выбор, попробуйте ещё раз.")
            }
        }
    }
}