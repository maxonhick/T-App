package com.library

class BooksShop: Shops<LibraryObjects>() {
    override fun sell(): Book {
        val book = Book(123, true, "name", TypeLibraryObjects.Book, 490, "author")
        return book
    }
}