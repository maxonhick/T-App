package com.preferences

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class AppPreferences @Inject constructor(
    private val context: Context
) {
    private val sharedPrefs by lazy {
        context.getSharedPreferences("library_prefs", Context.MODE_PRIVATE)
    }

    // Сохранение типа сортировки
    fun setSortByName(sortByName: Boolean) {
        sharedPrefs.edit {
            putBoolean("sort_by_name", sortByName)
        }
    }

    // Получение типа сортировки
    fun getSortByName(default: Boolean = true): Boolean {
        return sharedPrefs.getBoolean("sort_by_name", default)
    }
}