package com.useCases

import com.PreferencesManager
import com.library.LibraryMode
import javax.inject.Inject

class SwitchModeUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(mode: LibraryMode): Result<LibraryMode> = try {
        preferencesManager.setLibraryMode(mode)
        Result.success(mode)
    } catch (e: Exception) {
        Result.failure(e)
    }
}