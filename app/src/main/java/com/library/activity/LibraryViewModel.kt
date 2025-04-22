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
    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Loading)

    val items: LiveData<List<LibraryObjects>> = _items
    val screenState: LiveData<ScreenState> = _screenState
    private var currentItems: List<LibraryObjects> = emptyList()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _screenState.value = ScreenState.Loading

                delay(Random.nextLong(1000, 2000))

                if (Random.nextInt(5) == 0) {
                    throw Exception("Ошибка загрузки данных")
                }

                currentItems = loadLibraryItems()
                _screenState.value = ScreenState.Content(currentItems)
            } catch (e: Exception) {
                _screenState.value = ScreenState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun retryLoading() {
        loadInitialData()
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
            val previousState =  when (val state = _screenState.value) {
                is ScreenState.Content -> state.items
                is ScreenState.AddingItem -> state.currentItems
                else -> emptyList()
            }
            try {
                _screenState.value = ScreenState.AddingItem(currentItems)
                delay(Random.nextLong(500, 1500))

                if (Random.nextInt(5) == 0) {
                    throw Exception("Ошибка сохранения данных")
                }

                val newItems = currentItems.toMutableList().apply {
                    add(item)
                }
                currentItems = newItems
                _screenState.value = ScreenState.Content(newItems)
            } catch (e: Exception) {
                _screenState.value = when (val state = _screenState.value) {
                    is ScreenState.AddingItem -> ScreenState.Content(state.currentItems)
                    else -> ScreenState.Error(e.message ?: "Ошибка при добавлении")
                }
            }
        }
    }

    fun getSize(): Int = currentItems.size
}