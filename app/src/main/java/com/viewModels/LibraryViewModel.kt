package com.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ScreenState
import com.library.Book
import com.library.LibraryMode
import com.library.LibraryObjects
import com.useCases.AddItemUseCase
import com.useCases.GetLibraryItemsUseCase
import com.useCases.GetLibraryModeUseCase
import com.useCases.GetSortPreferenceUseCase
import com.useCases.GetTotalCountUseCase
import com.useCases.LoadMoreItemsUseCase
import com.useCases.LoadPreviousItemsUseCase
import com.useCases.SaveBookUseCase
import com.useCases.SearchBooksUseCase
import com.useCases.SetLibraryModeUseCase
import com.useCases.SetSortPreferenceUseCase
import com.useCases.SwitchModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val getLibraryItemsUseCase: GetLibraryItemsUseCase,
    private val getTotalCountUseCase: GetTotalCountUseCase,
    private val loadMoreItemsUseCase: LoadMoreItemsUseCase,
    private val loadPreviousItemsUseCase: LoadPreviousItemsUseCase,
    private val addItemUseCase: AddItemUseCase,
    private val searchBooksUseCase: SearchBooksUseCase,
    private val saveBookUseCase: SaveBookUseCase,
    private val setSortPreferenceUseCase: SetSortPreferenceUseCase,
    private val switchModeUseCase: SwitchModeUseCase,
    private val getSortPreferenceUseCase: GetSortPreferenceUseCase,
    private val setLibraryModeUseCase: SetLibraryModeUseCase,
    getLibraryModeUseCase: GetLibraryModeUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private val _items = MutableStateFlow<List<LibraryObjects>>(emptyList())
    val items: StateFlow<List<LibraryObjects>> = _items.asStateFlow()

    private val _googleBooks = MutableStateFlow<List<Book>>(emptyList())
    val googleBooks: StateFlow<List<Book>> = _googleBooks.asStateFlow()

    private val _sortByName = MutableStateFlow(true)
    val sortByName: StateFlow<Boolean> = _sortByName.asStateFlow()

    private val _currentMode = MutableStateFlow(LibraryMode.LOCAL)
    val currentMode: StateFlow<LibraryMode> = _currentMode.asStateFlow()

    private var currentOffset = 0
    private val currentLimit = 30

    init {
        _currentMode.value = getLibraryModeUseCase().getOrThrow()
        setSortPreference()
        loadInitialData()
    }

    private fun setSortPreference() {
        _sortByName.value = getSortPreferenceUseCase().getOrThrow()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading
            try {
                val itemsResult = getLibraryItemsUseCase(currentLimit, currentOffset)
                val totalCountResult = getTotalCountUseCase()

                if (itemsResult.isSuccess && totalCountResult.isSuccess) {
                    _items.value = itemsResult.getOrThrow()
                    val totalCount = totalCountResult.getOrThrow()

                    _screenState.value = ScreenState.Content(
                        canLoadMore = currentOffset + currentLimit < totalCount,
                        canLoadPrevious = currentOffset > 0
                    )
                } else {
                    val error = itemsResult.exceptionOrNull() ?: totalCountResult.exceptionOrNull()
                    handleError(error)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun loadMoreItems() {
        viewModelScope.launch {
            try {
                val totalCount = getTotalCountUseCase().getOrThrow()
                val result = loadMoreItemsUseCase(currentOffset, currentLimit, totalCount)

                if (result.isSuccess) {
                    val (items, newOffset) = result.getOrThrow()
                    currentOffset = newOffset
                    _items.value = items
                    updateContentState()
                } else {
                    handleError(result.exceptionOrNull())
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun loadPreviousItems() {
        viewModelScope.launch {
            try {
                val result = loadPreviousItemsUseCase(currentOffset, currentLimit)

                if (result.isSuccess) {
                    val (items, newOffset) = result.getOrThrow()
                    currentOffset = newOffset
                    _items.value = items
                    updateContentState()
                } else {
                    handleError(result.exceptionOrNull())
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun updateContentState() {
        viewModelScope.launch {
            val totalCount = getTotalCountUseCase().getOrThrow()
            _screenState.value = ScreenState.Content(
                canLoadMore = currentOffset + currentLimit < totalCount,
                canLoadPrevious = currentOffset > 0
            )
        }
    }

    fun addNewItem(item: LibraryObjects) {
        viewModelScope.launch {
            _screenState.value = ScreenState.AddingItem(_items.value.toList(), 0f)
            val result = addItemUseCase(item)

            if (result.isSuccess) {
                loadInitialData()
            } else {
                handleError(result.exceptionOrNull())
            }
        }
    }

    fun searchGoogleBooks(author: String?, title: String?) {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading
            try {
                val query = buildQuery(author, title)
                val result = searchBooksUseCase(query)

                if (result.isSuccess) {
                    _googleBooks.value = result.getOrThrow()
                    _screenState.value = if (result.getOrThrow().isEmpty()) {
                        ScreenState.Empty
                    } else {
                        ScreenState.Content(false, false)
                    }
                } else {
                    handleError(result.exceptionOrNull())
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun buildQuery(author: String?, title: String?): String {
        val parts = mutableListOf<String>()
        author?.takeIf { it.length >= 3 }?.let { parts.add("inauthor:$it") }
        title?.takeIf { it.length >= 3 }?.let { parts.add("intitle:$it") }
        return parts.joinToString("+")
    }

    fun saveGoogleBook(book: Book) {
        viewModelScope.launch {
            val result = saveBookUseCase(book)
            if (result.isFailure) {
                handleError(result.exceptionOrNull())
            }
        }
    }

    fun setSortByName(sortByName: Boolean) {
        viewModelScope.launch {
            val result = setSortPreferenceUseCase(sortByName)
            if (result.isSuccess) {
                _sortByName.value = sortByName
                loadInitialData()
            } else {
                handleError(result.exceptionOrNull())
            }
        }
    }

    fun switchToGoogleMode() {
        viewModelScope.launch {
            val result = switchModeUseCase(LibraryMode.GOOGLE)
            if (result.isSuccess) {
                _currentMode.value = result.getOrThrow()
                _googleBooks.value = emptyList()
                setLibraryModeUseCase(LibraryMode.GOOGLE)
            } else {
                handleError(result.exceptionOrNull())
            }
        }
    }

    fun switchToLocalMode() {
        viewModelScope.launch {
            val result = switchModeUseCase(LibraryMode.LOCAL)
            if (result.isSuccess) {
                _currentMode.value = result.getOrThrow()
                loadInitialData()
                setLibraryModeUseCase(LibraryMode.LOCAL)
            } else {
                handleError(result.exceptionOrNull())
            }
        }
    }

    private fun handleError(error: Throwable?) {
        _screenState.value = ScreenState.Error(
            message = error?.message ?: "Неизвестная ошибка",
            retryAction = { loadInitialData() }
        )
    }
}