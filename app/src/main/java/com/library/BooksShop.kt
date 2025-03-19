package com.library

class BooksShop: Shops<LibraryObjects>() {
    override fun buy(): Book {
        val book = Book(123, true, "name", 490, "author")
        return book
    }
}