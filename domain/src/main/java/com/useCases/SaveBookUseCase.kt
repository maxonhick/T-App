package com.useCases

import com.library.Book
import com.repositories.LibraryRepository
import javax.inject.Inject

class SaveBookUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(book: Book): Result<Boolean> = try {
        val exists = repository.getItemDetails(book.objectId, "book") != null
        if (!exists) {
            repository.addItem(book)
            Result.success(true)
        } else {
            Result.success(false)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}