package com

import com.library.LibraryObjects

sealed class ScreenState {
    object Loading : ScreenState() {
        const val minimumShowTime = 1000L
    }

    data class Content(
        val canLoadMore: Boolean = false,
        val canLoadPrevious: Boolean = false
    ) : ScreenState()

    data class AddingItem(
        val currentItems: List<LibraryObjects>,
        val progress: Float = 0f
    ) : ScreenState()

    data class Error(
        val message: String,
        val retryAction: (() -> Unit)? = null
    ) : ScreenState()

    object Empty : ScreenState() {
        const val emptyMessage = "Нет доступных элементов"
    }

    data class PaginationState(
        val isLoadingMore: Boolean = false,
        val isLoadingPrevious: Boolean = false,
        val error: String? = null
    ) : ScreenState()
}