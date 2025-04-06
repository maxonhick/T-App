package com.library.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LibraryViewModel::class.java) -> LibraryViewModel() as T
            else -> throw IllegalArgumentException("Такой модельки нет")
        }
    }
}