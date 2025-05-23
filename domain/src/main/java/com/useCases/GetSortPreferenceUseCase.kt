package com.useCases

import com.PreferencesManager
import javax.inject.Inject

class GetSortPreferenceUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(): Result<Boolean> = try {
        val sortByName = preferencesManager.getSortPreference()
        Result.success(sortByName)
    } catch (e: Exception) {
        Result.failure(e)
    }
}