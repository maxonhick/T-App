package com.library

class Manager<T: Shops<LibraryObjects>> {
    fun buy(value: T): LibraryObjects {
        return value.sell();
    }
}