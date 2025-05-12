package com.useCases

import com.library.LibraryObjects
import com.repositories.LibraryRepository
import javax.inject.Inject

class GetItemDetailsUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(id: Int, type: String): Result<LibraryObjects?> = try {
        val item = repository.getItemDetails(id, type)
        Result.success(item)
    } catch (e: Exception) {
        Result.failure(e)
    }
}