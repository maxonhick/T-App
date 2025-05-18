package com.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.DependencyContainer

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(context) as T
            }
            modelClass.isAssignableFrom(LibraryViewModel::class.java) -> {
                LibraryViewModel(
                    getLibraryItemsUseCase = DependencyContainer.getLibraryItemsUseCase,
                    getTotalCountUseCase = DependencyContainer.getTotalCountUseCase,
                    loadMoreItemsUseCase = DependencyContainer.loadMoreItemsUseCase,
                    loadPreviousItemsUseCase = DependencyContainer.loadPreviousItemsUseCase,
                    addItemUseCase = DependencyContainer.addItemUseCase,
                    searchBooksUseCase = DependencyContainer.searchBooksUseCase,
                    saveBookUseCase = DependencyContainer.saveBookUseCase,
                    setSortPreferenceUseCase = DependencyContainer.setSortPreferenceUseCase,
                    switchModeUseCase = DependencyContainer.switchModeUseCase,
                    getSortPreferenceUseCase = DependencyContainer.getSortPreferenceUseCase,
                    setLibraryModeUseCase = DependencyContainer.setLibraryModeUseCase,
                    getLibraryModeUseCase = DependencyContainer.getLibraryModeUseCase
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}