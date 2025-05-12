package com.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.useCases.*

class ViewModelFactory(
    private val getLibraryItemsUseCase: GetLibraryItemsUseCase,
    private val getTotalCountUseCase: GetTotalCountUseCase,
    private val loadMoreItemsUseCase: LoadMoreItemsUseCase,
    private val loadPreviousItemsUseCase: LoadPreviousItemsUseCase,
    private val addItemUseCase: AddItemUseCase,
    private val searchBooksUseCase: SearchBooksUseCase,
    private val saveBookUseCase: SaveBookUseCase,
    private val setSortPreferenceUseCase: SetSortPreferenceUseCase,
    private val switchModeUseCase: SwitchModeUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LibraryViewModel::class.java) -> {
                LibraryViewModel(
                    getLibraryItemsUseCase,
                    getTotalCountUseCase,
                    loadMoreItemsUseCase,
                    loadPreviousItemsUseCase,
                    addItemUseCase,
                    searchBooksUseCase,
                    saveBookUseCase,
                    setSortPreferenceUseCase,
                    switchModeUseCase
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}