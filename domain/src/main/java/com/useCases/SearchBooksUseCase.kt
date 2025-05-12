package com.useCases

import com.library.Book
import com.repositories.LibraryRepository
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(query: String): Result<List<Book>> = try {
        val books = repository.searchGoogleBooks(query)
        if (books.isEmpty()) {
            Result.success(emptyList())
        } else {
            Result.success(books)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}