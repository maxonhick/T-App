package com.library

class Manager<T: Shops<LibraryObjects>> {
    fun buySomething(value: T): LibraryObjects {
        return value.buy();
    }
}