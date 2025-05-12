package com.useCases

import com.library.LibraryObjects
import com.repositories.LibraryRepository
import javax.inject.Inject

class AddItemUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(item: LibraryObjects): Result<Unit> = try {
        repository.addItem(item)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}