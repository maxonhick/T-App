package com.library.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.library.LibraryObjects

class LibraryViewModel : ViewModel(){
    private val _items = MutableLiveData<List<LibraryObjects>>(emptyList())
    val items: LiveData<List<LibraryObjects>> = _items

    fun addNewItem(list: List<LibraryObjects>) {
        val currentList = _items.value?.toMutableList() ?: mutableListOf()
        currentList.addAll(list)
        _items.value = currentList.distinctBy { it.objectId }
    }


    fun getItemById(id: Int) : LibraryObjects {
        return _items.value?.find { it.objectId == id } ?: throw IllegalArgumentException("Неверный id")
    }

    fun getSize(): Int = _items.value?.size ?: 0
}