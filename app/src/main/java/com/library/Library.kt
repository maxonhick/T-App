package com.library

inline fun <reified T: LibraryObjects> returnList(list: List<LibraryObjects>): List<T> {
    val lastList = ArrayList<T>()
    list.forEach { element ->
        if (element is T) {
            lastList.add(element)
        }
    }
    return lastList
}

class Library {
    private val list = listOf(
        Book(objectId = 1289, access = true, name = "Маугли", pages = 100, author = "Киплинг"),
        Book(objectId = 3609, access = true, name = "Чёрный обелиск", pages = 479, author = "Ремарк"),
        Book(objectId = 6372, access = true, name = "1984", pages = 400, author = "Оруэл"),
        Book(objectId = 3876, access = true, name = "Война и мир", pages = 1472, author = "Толстой"),
        Newspaper(objectId =  6341, access = true, name = "WSJ", releaseNumber = 120225, month = "январь"),
        Newspaper(objectId =  6371, access = true, name = "Зеленоград.ru", releaseNumber = 121124, month = "март"),
        Newspaper(objectId =  6383, access = true, name = "Спорт-Экпресс", releaseNumber = 230125, month = "октябрь"),
        Newspaper(objectId =  6342, access = true, name = "WSJ", releaseNumber = 200225, month = "июнь"),
        Newspaper(objectId =  6392, access = true, name = "Коммерсантъ", releaseNumber = 130325, month = "июль"),
        Disk(objectId = 9234, access = true, name = "Назад в будущее", type = "DVD"),
        Disk(objectId = 9245, access = true, name = "Довод", type = "CD"),
        Disk(objectId = 9239, access = true, name = "Дивергент", type = "CD"),
        Disk(objectId = 9296, access = true, name = "Рио", type = "DVD"),
        Disk(objectId = 9212, access = true, name = "Люди в чёрном", type = "DVD")
    )
    private val booksList: List<Book> = returnList<Book>(list)
    private val newspapersList: List<Newspaper> = returnList<Newspaper>(list)
    private val disksList: List<Disk> = returnList<Disk>(list)
    private val digitization = OfficeOfDigitization<LibraryObjects>()

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
            while (true){
                println("""
                1. Взять домой
                2. Читать в читальном зале
                3. Показать подробную информацию
                4. Вернуть
                5. Оцифровать
                6. Вернуться в изначальное меню
            """.trimIndent())
                when (readlnOrNull()?.toIntOrNull()){
                    1 -> booksList[index - 1].getHome()
                    2 -> booksList[index - 1].getRead()
                    3 -> booksList[index - 1].longInformation()
                    4 -> booksList[index - 1].returnObject()
                    5 -> {
                        println("Был создан диск:")
                        digitization.transform(booksList[index - 1]).longInformation()
                    }
                    6 -> break
                    else -> println("Неверный выбор, попробуйте ещё раз.")
                }
            }
        }
        println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Управление менеджером
            5. Завершить работу
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
            while (true){
                println("""
                1. Взять домой
                2. Читать в читальном зале
                3. Показать подробную информацию
                4. Вернуть
                5. Оцифровать
                6. Вернуться в изначальное меню
            """.trimIndent())
                when (readlnOrNull()?.toIntOrNull()){
                    1 -> println("Газету нельзя взять домой")
                    2 -> newspapersList[index - 1].getRead()
                    3 -> newspapersList[index - 1].longInformation()
                    4 -> newspapersList[index - 1].returnObject()
                    5 -> {
                        println("Был создан диск:")
                        digitization.transform(newspapersList[index - 1]).longInformation()
                    }
                    6 -> break
                    else -> println("Неверный выбор, попробуйте ещё раз.")
                }
            }
        }
        println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Управление менеджером
            5. Завершить работу
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
            while (true){
                println("""
                1. Взять домой
                2. Читать в читальном зале
                3. Показать подробную информацию
                4. Вернуть
                5. Вернуться в изначальное меню
            """.trimIndent())
                when (readlnOrNull()?.toIntOrNull()){
                    1 -> disksList[index - 1].getHome()
                    2 -> println("Диск нельзя взять в читальный зал")
                    3 -> disksList[index - 1].longInformation()
                    4 -> disksList[index - 1].returnObject()
                    5 -> break
                    else -> println("Неверный выбор, попробуйте ещё раз.")
                }
            }
        }
        println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Управление менеджером
            5. Завершить работу
        """.trimIndent())
    }

    fun workManager(manager: Manager<Shops<LibraryObjects>>){
        val booksShop = BooksShop()
        val disksShop = DisksShop()
        val newspapersShop = NewspapersShop()
        while (true){
            println("""
                1. Купить книгу
                2. Купить диск
                3. Купить газету
                4. Вернуться в изначальное меню
            """.trimIndent())
            when (readlnOrNull()?.toIntOrNull()){
                1 -> {
                    println("Менеджер купил следующий объект:")
                    manager.buySomething(booksShop).longInformation()
                }
                2 -> {
                    println("Менеджер купил следующий объект:")
                    manager.buySomething(disksShop).longInformation()
                }
                3 -> {
                    println("Менеджер купил следующий объект:")
                    manager.buySomething(newspapersShop).longInformation()
                }
                4 -> break
                else -> println("Неверный выбор, попробуйте ещё раз.")
            }
        }
        println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Управление менеджером
            5. Завершить работу
        """.trimIndent())
    }
}