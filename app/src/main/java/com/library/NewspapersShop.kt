package com.library

class NewspapersShop: Shops<LibraryObjects>() {
    override fun buy(): Newspaper {
        val newspaper = Newspaper(579, true, "газета", 45678, "месяц" )
        return newspaper
    }
}