package com.library

abstract class Shops<T: LibraryObjects> {
    abstract fun sell(): T
}