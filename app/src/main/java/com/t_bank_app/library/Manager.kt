package com.t_bank_app.library

class Manager<T: Shops<LibraryObjects>> {
    fun buy(value: T): LibraryObjects {
        return value.sell();
    }
}