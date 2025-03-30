package com.t_bank_app.library

abstract class Shops<T: LibraryObjects> {
    abstract fun sell(): T
}