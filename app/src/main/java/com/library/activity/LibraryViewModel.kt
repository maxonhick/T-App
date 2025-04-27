package com.library.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.Book
import com.library.Disk
import com.library.LibraryObjects
import com.library.Newspaper
import com.library.TypeLibraryObjects
import com.library.data.BookEntity
import com.library.data.DiskEntity
import com.library.data.LibraryDatabase
import com.library.data.NewspaperEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = LibraryDatabase.getDatabase(application)
    private val dao = database.libraryDao()

    private val _items = MutableLiveData<List<LibraryObjects>>(emptyList())
    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Loading)
    private val _paginationState = MutableLiveData<ScreenState.PaginationState>()
    private val _sortByName = MutableLiveData<Boolean>(true)

    val screenState: LiveData<ScreenState> = _screenState
    val paginationState: LiveData<ScreenState.PaginationState> = _paginationState
    val sortByName: LiveData<Boolean> = _sortByName

    private var currentOffset = 0
    private var currentLimit = 20

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _screenState.value = ScreenState.Loading
                val startTime = System.currentTimeMillis()

                val libraryObjects = loadItems(currentLimit, currentOffset)

                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed < ScreenState.Loading.minimumShowTime) {
                    delay(ScreenState.Loading.minimumShowTime - elapsed)
                }

                _items.value = libraryObjects
                _screenState.value = ScreenState.Content(
                    items = libraryObjects,
                    canLoadMore = _items.value?.size == currentLimit,
                    canLoadPrevious = currentOffset > 0
                )
            } catch (e: Exception) {
                _screenState.value = ScreenState.Error(
                    message = e.message ?: "Неизвестная ошибка",
                    retryAction = { loadInitialData() }
                )
            }
        }
    }

    fun loadMoreItems() {
        if (_paginationState.value?.isLoadingMore == true) return

        viewModelScope.launch {
            try {
                _paginationState.value = ScreenState.PaginationState(isLoadingMore = true)

                val newOffset = currentOffset + currentLimit
                val items = loadItems(currentLimit, currentOffset)

                if (items.isEmpty()) {
                    _paginationState.value = ScreenState.PaginationState(isLoadingMore = false)
                    return@launch
                }

                val currentItems = _items.value?.toMutableList() ?: mutableListOf()
                val itemsToRemove = minOf(currentLimit / 2, currentItems.size)

                if (itemsToRemove > 0) {
                    currentItems.subList(0, itemsToRemove).clear()
                }

                currentOffset = newOffset
                _items.value = currentItems
                _screenState.value = ScreenState.Content(
                    items = currentItems,
                    canLoadMore = items.size == currentLimit / 2,
                    canLoadPrevious = true
                )
            } catch (e: Exception) {
                _paginationState.value = ScreenState.PaginationState(
                    isLoadingMore = false,
                    error = e.message
                )
            } finally {
                _paginationState.value = ScreenState.PaginationState(isLoadingMore = false)
            }
        }
    }

    fun loadPreviousItems() {
        if (_paginationState.value?.isLoadingPrevious == true) return

        viewModelScope.launch {
            try {
                _paginationState.value = ScreenState.PaginationState(isLoadingPrevious = true)

                val newOffset = maxOf(0, currentOffset - currentLimit / 2)
                if (newOffset == currentOffset) {
                    _paginationState.value = ScreenState.PaginationState(isLoadingPrevious = false)
                    return@launch
                }

                val items = loadItems(currentLimit, currentOffset)

                if (items.isEmpty()) {
                    _paginationState.value = ScreenState.PaginationState(isLoadingPrevious = false)
                    return@launch
                }

                val currentItems = _items.value?.toMutableList() ?: mutableListOf()
                val itemsToRemove = minOf(currentLimit / 2, currentItems.size)

                if (itemsToRemove > 0) {
                    currentItems.subList(currentItems.size - itemsToRemove, currentItems.size).clear()
                }

                items.reversed().forEach { item ->
                    when (item) {
                        is Book -> dao.getBookById(item.objectId)?.let { currentItems.add(0, it.toBook()) }
                        is Disk -> dao.getDiskById(item.objectId)?.let { currentItems.add(0, it.toDisk()) }
                        is Newspaper -> dao.getNewspaperById(item.objectId)?.let { currentItems.add(0, it.toNewspaper()) }
                    }
                }

                currentOffset = newOffset
                _items.value = currentItems
                _screenState.value = ScreenState.Content(
                    items = currentItems,
                    canLoadMore = true,
                    canLoadPrevious = newOffset > 0
                )
            } catch (e: Exception) {
                _paginationState.value = ScreenState.PaginationState(
                    isLoadingPrevious = false,
                    error = e.message
                )
            } finally {
                _paginationState.value = ScreenState.PaginationState(isLoadingPrevious = false)
            }
        }
    }

    fun addNewItem(item: LibraryObjects) {
        viewModelScope.launch {
            try {
                val currentItems = _items.value?.toMutableList() ?: mutableListOf()

                _screenState.value = ScreenState.AddingItem(currentItems, 0f)

                // Имитация прогресса сохранения
                for (progress in 0..100 step 5) {
                    delay(50)
                    _screenState.value = ScreenState.AddingItem(currentItems, progress / 100f)
                }

                when (item) {
                    is Book -> {
                        val entity = item.toBookEntity()
                        dao.insertBook(entity)
                        currentItems.add(item)
                    }
                    is Disk -> {
                        val entity = item.toDiskEntity()
                        dao.insertDisk(entity)
                        currentItems.add(item)
                    }
                    is Newspaper -> {
                        val entity = item.toNewspaperEntity()
                        dao.insertNewspaper(entity)
                        currentItems.add(item)
                    }
                }

                _items.value = currentItems
                _screenState.value = ScreenState.Content(
                    items = currentItems,
                    canLoadMore = true,
                    canLoadPrevious = currentOffset > 0
                )
            } catch (e: Exception) {
                _screenState.value = ScreenState.Error(
                    message = "Ошибка при добавлении: ${e.message}",
                    retryAction = { addNewItem(item) }
                )
            }
        }
    }

    fun setSortByName(sortByName: Boolean) {
        _sortByName.value = sortByName
        loadInitialData()
    }

    fun retryLoading() {
        loadInitialData()
    }

    private suspend fun loadItems(limit: Int, offset: Int): List<LibraryObjects> {
        val sortByName = _sortByName.value ?: true // безопасное получение значения

        return try {
            // Получаем базовую информацию о всех элементах
            val items = dao.getLibraryItems(sortByName, limit, offset)

            // Загружаем полные данные для каждого элемента
            items.mapNotNull { item ->
                when (item.itemType) {
                    "book" -> dao.getBookById(item.objectId)?.toBook()
                    "disk" -> dao.getDiskById(item.objectId)?.toDisk()
                    "newspaper" -> dao.getNewspaperById(item.objectId)?.toNewspaper()
                    else -> null
                }
            }
        } catch (e: Exception) {
            _screenState.value = ScreenState.Error("Ошибка загрузки: ${e.message}")
            emptyList()
        }
    }

    // Extension functions for conversion
    private fun Book.toBookEntity() = BookEntity(
        objectId = objectId,
        name = name,
        author = author,
        pages = pages,
        access = access,
        createdAt = createdAt,
        objectType = TypeLibraryObjects.Book
    )

    private fun BookEntity.toBook() = Book(
        objectId = objectId,
        access = access,
        name = name,
        objectType = TypeLibraryObjects.Book,
        pages = pages,
        author = author,
        createdAt = createdAt
    )

    private fun Disk.toDiskEntity() = DiskEntity(
        objectId = objectId,
        name = name,
        type = type,
        access = access,
        createdAt = createdAt,
        objectType = TypeLibraryObjects.Disk
    )

    private fun DiskEntity.toDisk() = Disk(
        objectId = objectId,
        access = access,
        name = name,
        type = type,
        objectType = TypeLibraryObjects.Disk,
        createdAt = createdAt
    )

    private fun Newspaper.toNewspaperEntity() = NewspaperEntity(
        objectId = objectId,
        name = name,
        releaseNumber = releaseNumber,
        month = month,
        access = access,
        createdAt = createdAt,
        objectType = TypeLibraryObjects.Newspaper
    )

    private fun NewspaperEntity.toNewspaper() = Newspaper(
        objectId = objectId,
        access = access,
        name = name,
        releaseNumber = releaseNumber,
        month = month,
        objectType = TypeLibraryObjects.Newspaper,
        createdAt = createdAt
    )
}