package com.library

import kotlinx.coroutines.flow.MutableStateFlow

fun main(){
    val books: Books = Books()
    val newspapers: Newspapers = Newspapers()
    val disks: Disks = Disks()

    println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Завершить работу
        """.trimIndent())
    while (true){
        when (readlnOrNull()?.toIntOrNull()){
            1 -> books.printBooks()
            2 -> newspapers.printNewspapers()
            3 -> disks.printDisks()
            4 -> return
            else -> println("Неверный выбор, попробуйте ещё раз.")
        }
    }
}

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
}

class Book(
    objectId: Int,
    access: Boolean,
    name: String,
    val pages: Int,
    val author: String
): LibraryObjects(objectId, access, name)

class Newspaper(
    objectId: Int,
    access: Boolean,
    name: String,
    val releaseNumber: Int
): LibraryObjects(objectId, access, name)

class Disk(
    objectId: Int,
    access: Boolean,
    name: String,
    val type: String
): LibraryObjects(objectId, access, name)

class Books {
    val booksList: List<Book> = listOf(
        Book(objectId = 1289, access = true, name = "Маугли", pages = 100, author = "Киплинг"),
        Book(objectId = 3609, access = true, name = "Чёрный обелиск", pages = 479, author = "Ремарк"),
        Book(objectId = 6372, access = true, name = "1984", pages = 400, author = "Оруэл"),
        Book(objectId = 3876, access = true, name = "Война и мир", pages = 1472, author = "Толстой")
    )

    private fun longInformation(book: Book) {
        if (book.access){
            println("книга: ${book.name} (${book.pages} стр.) автора: " +
                    "${book.author} с id: ${book.objectId} доступна: Да")
        } else {
            println("книга: ${book.name} (${book.pages} стр.) автора: " +
                    "${book.author} с id: ${book.objectId} доступна: Нет")
        }
    }

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
            workWithBooks(index)
        }
        println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Завершить работу
        """.trimIndent())
    }

    private fun workWithBooks(index: Int){
        while (true){
            println("""
                1. Взять домой
                2. Читать в читальном зале
                3. Показать подробную информацию
                4. Вернуть
                5. Вернуться в изначальное меню
            """.trimIndent())
            when (readlnOrNull()?.toIntOrNull()){
                1 -> takeHome(booksList[index - 1])
                2 -> takeRead(booksList[index - 1])
                3 -> longInformation(booksList[index - 1])
                4 -> returnBook(booksList[index - 1])
                5 -> break
                else -> println("Неверный выбор, попробуйте ещё раз.")
            }
        }
    }

    private fun takeHome(book: Book){
        if (book.access) {
            book.access = false
            println("Книга ${book.objectId} взяли домой")
        } else {
            println("Книгу уже кто-то взял")
        }
    }

    private fun takeRead(book: Book){
        if (book.access) {
            book.access = false
            println("Книга ${book.objectId} взяли в читальный зал")
        } else {
            println("Книгу уже кто-то взял")
        }
    }

    private fun returnBook(book: Book){
        if (book.access) {
            println("Книга ${book.objectId} находится в библиотеке, её нельзя вернуть")
        } else {
            book.access = true
            println("Книгу ${book.objectId} вернули в библиотеку")
        }
    }
}

class Newspapers {
    val newspapersList: List<Newspaper> = listOf(
        Newspaper(objectId =  6341, access = true, name = "WSJ", releaseNumber = 120225),
        Newspaper(objectId =  6371, access = true, name = "Зеленоград.ru", releaseNumber = 121124),
        Newspaper(objectId =  6383, access = true, name = "Спорт-Экпресс", releaseNumber = 230125),
        Newspaper(objectId =  6342, access = true, name = "WSJ", releaseNumber = 200225),
        Newspaper(objectId =  6392, access = true, name = "Коммерсантъ", releaseNumber = 130325)
    )

    private fun longInformation(newspaper: Newspaper) {
        if (newspaper.access){
            println("выпуск: ${newspaper.releaseNumber} газеты ${newspaper.name} c id: " +
                    "${newspaper.objectId} доступен: Да")
        } else {
            println("выпуск: ${newspaper.releaseNumber} газеты ${newspaper.name} c id: " +
                    "${newspaper.objectId} доступен: Нет")
        }
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
            workWithNewspapers(index)
        }
        println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Завершить работу
        """.trimIndent())
    }

    private fun workWithNewspapers(index: Int){
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
                2 -> takeRead(newspapersList[index - 1])
                3 -> longInformation(newspapersList[index - 1])
                4 -> returnBook(newspapersList[index - 1])
                5 -> break
                else -> println("Неверный выбор, попробуйте ещё раз.")
            }
        }
    }

    private fun takeHome(){
        println("Газету нельязя взять домой")
    }

    private fun takeRead(newspaper: Newspaper){
        if (newspaper.access) {
            newspaper.access = false
            println("Газету ${newspaper.objectId} взяли в читальный зал")
        } else {
            println("Газету уже кто-то взял")
        }
    }

    private fun returnBook(newspaper: Newspaper){
        if (newspaper.access) {
            println("Газета ${newspaper.objectId} находится в библиотеке, её нельзя вернуть")
        } else {
            newspaper.access = true
            println("Газету ${newspaper.objectId} вернули в библиотеку")
        }
    }
}

class Disks {
    val disksList: List<Disk> = listOf(
        Disk(objectId = 9234, access = true, name = "Назад в будущее", type = "DVD"),
        Disk(objectId = 9245, access = true, name = "Довод", type = "CD"),
        Disk(objectId = 9239, access = true, name = "Дивергент", type = "CD"),
        Disk(objectId = 9296, access = true, name = "Рио", type = "DVD"),
        Disk(objectId = 9212, access = true, name = "Люди в чёрном", type = "DVD")
    )

    private fun longInformation(disk: Disk) {
        if (disk.access){
            println("${disk.type} ${disk.name} доступен: Да")
        } else {
            println("${disk.type} ${disk.name} доступен: Нет")
        }
    }

    fun printDisks(){
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
            workWithDisks(index)
        }
        println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Завершить работу
        """.trimIndent())
    }

    private fun workWithDisks(index: Int){
        while (true){
            println("""
                1. Взять домой
                2. Читать в читальном зале
                3. Показать подробную информацию
                4. Вернуть
                5. Вернуться в изначальное меню
            """.trimIndent())
            when (readlnOrNull()?.toIntOrNull()){
                1 -> takeHome(disksList[index - 1])
                2 -> takeRead()
                3 -> longInformation(disksList[index - 1])
                4 -> returnBook(disksList[index - 1])
                5 -> break
                else -> println("Неверный выбор, попробуйте ещё раз.")
            }
        }
    }

    private fun takeHome(disk: Disk){
        if (disk.access) {
            disk.access = false
            println("Диск ${disk.objectId} взяли домой")
        } else {
            println("Диск уже кто-то взял")
        }
    }

    private fun takeRead(){
        println("Диск нельзя взять в читальный зал")
    }

    private fun returnBook(disk: Disk){
        if (disk.access) {
            println("Диск ${disk.objectId} находится в библиотеке, его нельзя вернуть")
        } else {
            disk.access = true
            println("Диск ${disk.objectId} вернули в библиотеку")
        }
    }
}