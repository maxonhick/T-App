package com.repositories

import com.ScreenState
import com.library.Book
import com.library.LibraryObjects

interface LibraryRepository {
    // Local data operations
    suspend fun getItems(sortByName: Boolean, limit: Int, offset: Int): List<LibraryObjects>
    suspend fun getItemDetails(id: Int, type: String): LibraryObjects?
    suspend fun getTotalCount(): Int
    suspend fun addItem(item: LibraryObjects)

    // Remote data operations
    suspend fun searchGoogleBooks(query: String): List<Book>
    suspend fun saveGoogleBook(book: Book): Boolean

    // Pagination
    suspend fun getPaginationState(): ScreenState.PaginationState
    suspend fun savePaginationState(loadingMore: Boolean, loadingPrevious: Boolean)
}