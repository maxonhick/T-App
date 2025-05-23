package com.useCases

import com.PreferencesManager
import com.library.LibraryMode
import javax.inject.Inject

class SetLibraryModeUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(libraryMode: LibraryMode): Result<Unit> = try {
        preferencesManager.setLibraryMode(libraryMode)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}