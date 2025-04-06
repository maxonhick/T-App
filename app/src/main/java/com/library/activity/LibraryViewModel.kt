package com.library.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.library.Book
import com.library.Disk
import com.library.DiskType
import com.library.LibraryObjects
import com.library.Month
import com.library.Newspaper
import com.library.TypeLibraryObjects

class LibraryViewModel : ViewModel(){
    private val _items = MutableLiveData<List<LibraryObjects>>()
    val items: LiveData<List<LibraryObjects>> = _items

    init {
        addNewItem(listOf(
            Book(
                objectId = 1, access = true, name = "Маугли", pages = 100, author = "Киплинг",
                objectType = TypeLibraryObjects.Book
            ),
            Book(objectId = 2, access = true, name = "Чёрный обелиск", pages = 479, author = "Ремарк",
                objectType = TypeLibraryObjects.Book),
            Book(objectId = 3, access = true, name = "1984", pages = 400, author = "Оруэл",
                objectType = TypeLibraryObjects.Book),
            Book(objectId = 4, access = true, name = "Война и мир", pages = 1472, author = "Толстой",
                objectType = TypeLibraryObjects.Book),
            Newspaper(objectId =  5, access = true, name = "WSJ", releaseNumber = 120225, month = Month.January,
                objectType = TypeLibraryObjects.Newspaper),
            Newspaper(objectId =  6, access = true, name = "Зеленоград.ru", releaseNumber = 121124, month = Month.March,
                objectType = TypeLibraryObjects.Newspaper),
            Newspaper(objectId =  7, access = true, name = "Спорт-Экпресс", releaseNumber = 230125, month = Month.October,
                objectType = TypeLibraryObjects.Newspaper),
            Newspaper(objectId =  8, access = true, name = "WSJ", releaseNumber = 200225, month = Month.June,
                objectType = TypeLibraryObjects.Newspaper),
            Newspaper(objectId =  9, access = true, name = "Коммерсантъ", releaseNumber = 130325, month = Month.July,
                objectType = TypeLibraryObjects.Newspaper),
            Disk(objectId = 10, access = true, name = "Назад в будущее", type = DiskType.DVD,
                objectType = TypeLibraryObjects.Disk),
            Disk(objectId = 11, access = true, name = "Довод", type = DiskType.CD,
                objectType = TypeLibraryObjects.Disk),
            Disk(objectId = 12, access = true, name = "Дивергент", type = DiskType.CD,
                objectType = TypeLibraryObjects.Disk),
            Disk(objectId = 13, access = true, name = "Рио", type = DiskType.DVD,
                objectType = TypeLibraryObjects.Disk),
            Disk(objectId = 14, access = true, name = "Люди в чёрном", type = DiskType.DVD,
                objectType = TypeLibraryObjects.Disk)
        ))
    }

    fun addNewItem(list: List<LibraryObjects>) {
        val oldList = _items.value
        _items.value = oldList?.plus(list) ?: list
    }


    fun getItemById(id: Int) : LibraryObjects = _items.value?.get(id - 1) ?: throw IllegalArgumentException("Неверный id")
}