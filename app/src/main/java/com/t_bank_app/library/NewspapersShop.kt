package com.t_bank_app.library

class NewspapersShop: Shops<LibraryObjects>() {
    override fun sell(): Newspaper {
        val newspaper = Newspaper(579, true, "газета", 45678, Month.April)
        return newspaper
    }
}