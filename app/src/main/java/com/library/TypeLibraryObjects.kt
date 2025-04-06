package com.library

enum class TypeLibraryObjects {
    Book {
        override fun toString(): String {
            return "Книга"
        }
    },
    Disk {
        override fun toString(): String {
            return "Диск"
        }
    },
    Newspaper {
        override fun toString(): String {
            return "Газета"
        }
    },
}