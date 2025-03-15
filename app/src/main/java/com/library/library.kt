package com.library

class Library {
    val booksList: List<Book> = listOf(
        Book(objectId = 1289, access = true, name = "Маугли", pages = 100, author = "Киплинг"),
        Book(objectId = 3609, access = true, name = "Чёрный обелиск", pages = 479, author = "Ремарк"),
        Book(objectId = 6372, access = true, name = "1984", pages = 400, author = "Оруэл"),
        Book(objectId = 3876, access = true, name = "Война и мир", pages = 1472, author = "Толстой")
    )
    val newspapersList: List<Newspaper> = listOf(
        Newspaper(objectId =  6341, access = true, name = "WSJ", releaseNumber = 120225),
        Newspaper(objectId =  6371, access = true, name = "Зеленоград.ru", releaseNumber = 121124),
        Newspaper(objectId =  6383, access = true, name = "Спорт-Экпресс", releaseNumber = 230125),
        Newspaper(objectId =  6342, access = true, name = "WSJ", releaseNumber = 200225),
        Newspaper(objectId =  6392, access = true, name = "Коммерсантъ", releaseNumber = 130325)
    )
    val disksList: List<Disk> = listOf(
        Disk(objectId = 9234, access = true, name = "Назад в будущее", type = "DVD"),
        Disk(objectId = 9245, access = true, name = "Довод", type = "CD"),
        Disk(objectId = 9239, access = true, name = "Дивергент", type = "CD"),
        Disk(objectId = 9296, access = true, name = "Рио", type = "DVD"),
        Disk(objectId = 9212, access = true, name = "Люди в чёрном", type = "DVD")
    )

    fun printBooks(){
        if (booksList.isEmpty()) {
            println("В библиотеке нет книг.")
        } else {
            var index = 1
            booksList.forEach { bookToPrint ->
                print(index++.toString() + ". ")
                bookToPrint.smallInformation()
            }
        }

        println("Введите номер книги или любую другую комбинацию, чтобы вернуться в изначальное меню")
        val index = readlnOrNull()?.toIntOrNull() ?: 0
        if (0  < index && index < booksList.size){
            booksList[index - 1].workWithObject()
        }
        println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Завершить работу
        """.trimIndent())
    }

    fun printNewspapers(){
        if (newspapersList.isEmpty()) {
            println("В библиотеке нет газет.")
        } else {
            var index = 1
            newspapersList.forEach { newspaperToPrint ->
                print(index++.toString() + ". ")
                newspaperToPrint.smallInformation()
            }
        }

        println("Введите номер газеты или любую другую комбинацию, чтобы вернуться в изначальное меню")
        val index = readlnOrNull()?.toIntOrNull() ?: 0
        if (0  < index && index < newspapersList.size){
            newspapersList[index - 1].workWithObject()
        }
        println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Завершить работу
        """.trimIndent())
    }

    fun printDisks() {
        if (disksList.isEmpty()) {
            println("В библиотеке нет дисков.")
        } else {
            var index = 1
            disksList.forEach { diskToPrint ->
                print(index++.toString() + ". ")
                diskToPrint.smallInformation()
            }
        }

        println("Введите номер диска или любую другую комбинацию, чтобы вернуться в изначальное меню")
        val index = readlnOrNull()?.toIntOrNull() ?: 0
        if (0  < index && index < disksList.size){
            disksList[index - 1].workWithObject()
        }
        println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Завершить работу
        """.trimIndent())
    }
}