package com.library.activity

import com.library.LibraryObjects

sealed class ScreenState {
    object Loading : ScreenState()
    data class AddingItem(val currentItems: List<LibraryObjects>) : ScreenState()
    data class Content(val items: List<LibraryObjects>) : ScreenState()
    data class Error(val message: String) : ScreenState()
}