package com.library

fun main(){
    val library: Library = Library()
    val manager = Manager<Shops<LibraryObjects>>()

    println("""
            1. Показать книги
            2. Показать газеты
            3. Показать диски
            4. Управление менеджером
            5. Завершить работу
        """.trimIndent())
    while (true){
        library.run {
            when (readlnOrNull()?.toIntOrNull()) {
                1 -> printBooks()
                2 -> printNewspapers()
                3 -> printDisks()
                4 -> workManager(manager)
                5 -> return
                else -> println("Неверный выбор, попробуйте ещё раз.")
            }
        }
    }
}