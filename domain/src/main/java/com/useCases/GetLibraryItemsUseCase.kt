package com.useCases

import com.PreferencesManager
import com.library.LibraryObjects
import com.repositories.LibraryRepository
import javax.inject.Inject

class GetLibraryItemsUseCase(
    private val repository: LibraryRepository,
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke(
        limit: Int,
        offset: Int
    ): Result<List<LibraryObjects>> = try {
        val sortByName = preferencesManager.getSortPreference()
        val items = repository.getItems(sortByName, limit, offset)
        Result.success(items)
    } catch (e: Exception) {
        Result.failure(e)
    }
}