package com.useCases

import com.PreferencesManager

class GetSortPreferenceUseCase(
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(): Result<Boolean> = try {
        val sortByName = preferencesManager.getSortPreference()
        Result.success(sortByName)
    } catch (e: Exception) {
        Result.failure(e)
    }
}