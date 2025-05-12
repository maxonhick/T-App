package com.useCases

import com.PreferencesManager
import javax.inject.Inject

class SetSortPreferenceUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(sortByName: Boolean): Result<Boolean> = try {
        preferencesManager.setSortPreference(sortByName)
        Result.success(sortByName)
    } catch (e: Exception) {
        Result.failure(e)
    }
}