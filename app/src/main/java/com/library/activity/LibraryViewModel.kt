package com.library.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.library.Book
import com.library.Disk
import com.library.DiskType
import com.library.LibraryObjects
import com.library.Month
import com.library.Newspaper
import com.library.TypeLibraryObjects
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class LibraryViewModel : ViewModel(){
    private val _items = MutableLiveData<List<LibraryObjects>>(emptyList())
    private val _loading = MutableLiveData<Boolean>(false)
    private val _error = MutableLiveData<String?>()

    val items: LiveData<List<LibraryObjects>> = _items
    val loading: LiveData<Boolean> = _loading
    val error: LiveData<String?> = _error

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _loading.value = true
                delay(Random.nextLong(1000, 2000))

                if (Random.nextInt(5) == 0) {
                    throw Exception("Ошибка загрузки данных")
                }
                val list = loadLibraryItems()
                _items.value = list
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun loadLibraryItems(): List<LibraryObjects> {
        return listOf(
            Book(
                objectId = 1,
                access = true,
                name = "Маугли",
                pages = 100,
                author = "Киплинг",
                objectType = TypeLibraryObjects.Book
            ).also { delay(Random.nextLong(100, 300)) },
            Book(
                objectId = 2,
                access = true,
                name = "Чёрный обелиск",
                pages = 479,
                author = "Ремарк",
                objectType = TypeLibraryObjects.Book
            ).also { delay(Random.nextLong(100, 300)) },
            Book(
                objectId = 3, access = true, name = "1984", pages = 400, author = "Оруэл",
                objectType = TypeLibraryObjects.Book
            ).also { delay(Random.nextLong(100, 300)) },
            Book(
                objectId = 4,
                access = true,
                name = "Война и мир",
                pages = 1472,
                author = "Толстой",
                objectType = TypeLibraryObjects.Book
            ).also { delay(Random.nextLong(100, 300)) },
            Newspaper(
                objectId = 5,
                access = true,
                name = "WSJ",
                releaseNumber = 120225,
                month = Month.January,
                objectType = TypeLibraryObjects.Newspaper
            ).also { delay(Random.nextLong(100, 300)) },
            Newspaper(
                objectId = 6,
                access = true,
                name = "Зеленоград.ru",
                releaseNumber = 121124,
                month = Month.March,
                objectType = TypeLibraryObjects.Newspaper
            ).also { delay(Random.nextLong(100, 300)) },
            Newspaper(
                objectId = 7,
                access = true,
                name = "Спорт-Экпресс",
                releaseNumber = 230125,
                month = Month.October,
                objectType = TypeLibraryObjects.Newspaper
            ).also { delay(Random.nextLong(100, 300)) },
            Newspaper(
                objectId = 8,
                access = true,
                name = "WSJ",
                releaseNumber = 200225,
                month = Month.June,
                objectType = TypeLibraryObjects.Newspaper
            ).also { delay(Random.nextLong(100, 300)) },
            Newspaper(
                objectId = 9,
                access = true,
                name = "Коммерсантъ",
                releaseNumber = 130325,
                month = Month.July,
                objectType = TypeLibraryObjects.Newspaper
            ).also { delay(Random.nextLong(100, 300)) },
            Disk(
                objectId = 10, access = true, name = "Назад в будущее", type = DiskType.DVD,
                objectType = TypeLibraryObjects.Disk
            ).also { delay(Random.nextLong(100, 300)) },
            Disk(
                objectId = 11, access = true, name = "Довод", type = DiskType.CD,
                objectType = TypeLibraryObjects.Disk
            ).also { delay(Random.nextLong(100, 300)) },
            Disk(
                objectId = 12, access = true, name = "Дивергент", type = DiskType.CD,
                objectType = TypeLibraryObjects.Disk
            ).also { delay(Random.nextLong(100, 300)) },
            Disk(
                objectId = 13, access = true, name = "Рио", type = DiskType.DVD,
                objectType = TypeLibraryObjects.Disk
            ).also { delay(Random.nextLong(100, 300)) },
            Disk(
                objectId = 14, access = true, name = "Люди в чёрном", type = DiskType.DVD,
                objectType = TypeLibraryObjects.Disk
            ).also { delay(Random.nextLong(100, 300)) }
        )
    }

    fun addNewItem(item: LibraryObjects) {
        viewModelScope.launch {
            try {
                _loading.value = true
                delay(Random.nextLong(500, 1500))

                if (Random.nextInt(5) == 0) {
                    throw Exception("Ошибка сохранения данных")
                }

                val currentList = _items.value?.toMutableList() ?: mutableListOf()
                currentList.add(item)
                _items.value = currentList.distinctBy { it.objectId }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _loading.value = false
            }
        }
    }

    fun getSize(): Int = _items.value?.size ?: 0
}