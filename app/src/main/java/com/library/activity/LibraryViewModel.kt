package com.library.activity

import android.app.Application
import android.content.Context
import android.widget.Toast
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
import com.tBankApp.model.Volume
import com.tBankApp.network.ApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryViewModel(private val application: Application) : AndroidViewModel(application) {
    private val database = LibraryDatabase.getDatabase(application)
    private val dao = database.libraryDao()

    private val _items = MutableLiveData<List<LibraryObjects>>(emptyList())
    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Loading)
    private val _paginationState = MutableLiveData<ScreenState.PaginationState>()
    private var _totalSize = 0
    private val _sortByName = MutableLiveData<Boolean>(true)
    private val _googleBooks = MutableLiveData<List<Book>>(emptyList())
    private val _searchQuery = MutableLiveData<String>("")
    private val _currentMode = MutableLiveData<LibraryMod>(LibraryMod.LOCAL)

    val screenState: LiveData<ScreenState> = _screenState
    val paginationState: LiveData<ScreenState.PaginationState> = _paginationState
    val sortByName: LiveData<Boolean> = _sortByName
    val totalSize = _totalSize
    val items: LiveData<List<LibraryObjects>> = _items
    val googleBooks: LiveData<List<Book>> = _googleBooks
    val searchQuery: LiveData<String> = _searchQuery
    val currentMode: LiveData<LibraryMod> = _currentMode

    private var currentOffset = 0
    private var currentLimit = 30

    init {
        loadTypeOfSort()
        loadInitialData()
    }

    fun loadInitialData() {
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
                _totalSize = dao.getTotalCount()
                _screenState.value = ScreenState.Content(
                    canLoadMore = currentLimit + currentOffset < _totalSize,
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

    fun reloadItemsInMemory(firstVisible: Int, lastVisible: Int, state: ScreenState.Content) {
        if ((firstVisible < (currentLimit / 3)) && state.canLoadPrevious) {
            _paginationState.value = ScreenState.PaginationState(
                isLoadingPrevious = true,
                isLoadingMore = false
            )
            loadPreviousItems()
        }
        if ((lastVisible > (currentLimit / 3) * 2) && state.canLoadMore) {
            _paginationState.value = ScreenState.PaginationState(
                isLoadingPrevious = false,
                isLoadingMore = true
            )
            loadMoreItems()
        }
    }

    private fun loadMoreItems() {
        if (_paginationState.value?.isLoadingMore == false) return
        viewModelScope.launch {
            try {
                if (_totalSize - (currentOffset + currentLimit) < currentLimit / 2) {
                    currentOffset = _totalSize - currentLimit
                } else {
                    currentOffset += currentLimit / 2
                }
                val items = loadItems(currentLimit, currentOffset)

                if (items.isEmpty()) {
                    _paginationState.value = ScreenState.PaginationState(isLoadingMore = false)
                    return@launch
                }

                _items.value = items
                _totalSize = dao.getTotalCount()
                _screenState.value = ScreenState.Content(
                    canLoadMore = currentLimit + currentOffset < _totalSize,
                    canLoadPrevious = currentOffset > 0
                )
            } catch (e: Exception) {
                _paginationState.value = ScreenState.PaginationState(
                    isLoadingMore = false,
                    isLoadingPrevious = false,
                    error = e.message
                )
            } finally {
                _paginationState.value = ScreenState.PaginationState(isLoadingMore = false)
            }
        }
    }

    private fun loadPreviousItems() {
        if (_paginationState.value?.isLoadingPrevious == false) return
        viewModelScope.launch {
            try {
                if (currentOffset < currentLimit / 2) {
                    currentOffset = 0
                } else {
                    currentOffset -= (currentLimit / 2)
                }

                val items = loadItems(currentLimit, currentOffset)

                if (items.isEmpty()) {
                    _paginationState.value = ScreenState.PaginationState(isLoadingPrevious = false)
                    return@launch
                }

                _items.value = items
                _totalSize = dao.getTotalCount()
                _screenState.value = ScreenState.Content(
                    canLoadMore = currentLimit + currentOffset < _totalSize,
                    canLoadPrevious = currentOffset > 0
                )
            } catch (e: Exception) {
                _paginationState.value = ScreenState.PaginationState(
                    isLoadingPrevious = false,
                    isLoadingMore = false,
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
                var currentItems = _items.value?.toMutableList() ?: mutableListOf()
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
                    }
                    is Disk -> {
                        val entity = item.toDiskEntity()
                        dao.insertDisk(entity)
                    }
                    is Newspaper -> {
                        val entity = item.toNewspaperEntity()
                        dao.insertNewspaper(entity)
                    }
                }

                currentItems = loadItems(currentLimit, currentOffset).toMutableList()

                _items.value = currentItems
                _totalSize = dao.getTotalCount()
                _screenState.value = ScreenState.Content(
                    canLoadMore = currentLimit + currentOffset < _totalSize,
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
        saveTypeOfSort()
        loadInitialData()
    }

    fun retryLoading() {
        loadInitialData()
    }

    private suspend fun loadItems(limit: Int, offset: Int): List<LibraryObjects> {
        val sortByName = _sortByName.value!!

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

    private fun saveTypeOfSort() {
        val sharedPref = application.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            putBoolean("TypeSort", _sortByName.value!!)
            apply()
        }
    }

    private fun loadTypeOfSort() {
        val sharedPref = application.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        _sortByName.value = sharedPref.getBoolean("TypeSort", true)
    }

    fun searchGoogleBooks(author: String?, title: String?) {
        viewModelScope.launch {
            _googleBooks.value = emptyList()
            _screenState.value = ScreenState.Loading

            try {
                val query = buildQuery(author, title)
                val books = ApiClient.googleBooksService.searchBooks(query)

                _googleBooks.value = books.items?.mapNotNull { it.toBook() } ?: emptyList()
                _screenState.value = if (_googleBooks.value.isNullOrEmpty()) {
                    ScreenState.Empty
                } else {
                    ScreenState.Content(false, false)
                }
            } catch (e: Exception) {
                _screenState.value = ScreenState.Error(
                    "Ошибка поиска: ${e.message}",
                    retryAction = { searchGoogleBooks(author, title) }
                )
            }
        }
    }

    private fun buildQuery(author: String?, title: String?): String {
        val parts = mutableListOf<String>()
        author?.takeIf { it.length >= 3 }?.let { parts.add("inauthor:$it") }
        title?.takeIf { it.length >= 3 }?.let { parts.add("intitle:$it") }
        return parts.joinToString("+")
    }

    private fun Volume.toBook(): Book? {
        val isbn = volumeInfo.industryIdentifiers
            ?.firstOrNull { it.type == "ISBN_13" || it.type == "ISBN_10" }
            ?.identifier ?: return null

        return Book(
            objectId = isbn.hashCode(),
            name = volumeInfo.title ?: "Без названия",
            author = volumeInfo.authors?.joinToString() ?: "Неизвестный автор",
            pages = volumeInfo.pageCount ?: 0,
            access = true,
            objectType = TypeLibraryObjects.Book
        )
    }

    fun saveGoogleBook(book: Book) {
        viewModelScope.launch {
            try {
                if (dao.getBookById(book.objectId) == null) {
                    dao.insertBook(book.toBookEntity())
                    Toast.makeText(application, "Книга сохранена", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(application, "Книга уже есть в библиотеке", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(application, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

    fun switchToGoogleMode() {
        _currentMode.value = LibraryMod.GOOGLE
        _items.value = emptyList()
        _googleBooks.value = emptyList()
        _screenState.value = ScreenState.Loading
    }

    fun switchToLocalMode() {
        _currentMode.value = LibraryMod.LOCAL
        _screenState.value = ScreenState.Loading
        loadInitialData()
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