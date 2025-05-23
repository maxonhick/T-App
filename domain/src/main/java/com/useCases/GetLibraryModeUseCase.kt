package com.useCases

import com.PreferencesManager
import com.library.LibraryMode
import javax.inject.Inject

class GetLibraryModeUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(): Result<LibraryMode> = try {
        val libraryMode = preferencesManager.getLibraryMode()
        Result.success(libraryMode)
    } catch (e: Exception) {
        Result.failure(e)
    }
}