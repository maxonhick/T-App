package com

import android.content.Context
import com.library.LibraryMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

class PreferencesManagerImpl (
    private val context: Context
) : PreferencesManager {

    private val sharedPref by lazy {
        context.getSharedPreferences("LibraryAppPrefs", Context.MODE_PRIVATE)
    }

    companion object {
        private const val KEY_SORT_BY_NAME = "sort_by_name"
        private const val KEY_LIBRARY_MODE = "library_mode"
        private const val KEY_SEARCH_QUERY = "search_query"
        private const val KEY_PAGINATION_LOADING_MORE = "pagination_loading_more"
        private const val KEY_PAGINATION_LOADING_PREVIOUS = "pagination_loading_previous"
    }

    override fun getSortPreference(): Boolean {
        return sharedPref.getBoolean(KEY_SORT_BY_NAME, true)
    }

    override fun setSortPreference(sortByName: Boolean) {
        sharedPref.edit { putBoolean(KEY_SORT_BY_NAME, sortByName) }
    }

    override fun getLibraryMode(): LibraryMode {
        return try {
            LibraryMode.valueOf(
                sharedPref.getString(KEY_LIBRARY_MODE, LibraryMode.LOCAL.name) ?: LibraryMode.LOCAL.name
            )
        } catch (e: Exception) {
            LibraryMode.LOCAL
        }
    }

    override fun setLibraryMode(mode: LibraryMode) {
        sharedPref.edit { putString(KEY_LIBRARY_MODE, mode.name) }
    }

    override fun getLastSearchQuery(): String {
        return sharedPref.getString(KEY_SEARCH_QUERY, "") ?: ""
    }

    override fun setLastSearchQuery(query: String) {
        sharedPref.edit { putString(KEY_SEARCH_QUERY, query) }
    }

    override fun getPaginationConfig(): Pair<Boolean, Boolean> {
        val loadingMore = sharedPref.getBoolean(KEY_PAGINATION_LOADING_MORE, false)
        val loadingPrevious = sharedPref.getBoolean(KEY_PAGINATION_LOADING_PREVIOUS, false)
        return Pair(loadingMore, loadingPrevious)
    }

    override fun setPaginationConfig(loadingMore: Boolean, loadingPrevious: Boolean) {
        sharedPref.edit {
            putBoolean(KEY_PAGINATION_LOADING_MORE, loadingMore)
            putBoolean(KEY_PAGINATION_LOADING_PREVIOUS, loadingPrevious)
        }
    }

    override fun clearAllPreferences() {
        sharedPref.edit { clear() }
    }
}