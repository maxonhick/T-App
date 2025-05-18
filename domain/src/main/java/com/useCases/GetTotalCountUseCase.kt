package com.useCases

import com.repositories.LibraryRepository
import javax.inject.Inject

class GetTotalCountUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(): Result<Int> = try {
        val count = repository.getTotalCount()
        Result.success(count)
    } catch (e: Exception) {
        Result.failure(e)
    }
}