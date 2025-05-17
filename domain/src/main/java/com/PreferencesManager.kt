package com

import com.library.LibraryMode

interface PreferencesManager {
    fun getSortPreference(): Boolean
    fun setSortPreference(sortByName: Boolean)

    fun getLibraryMode(): LibraryMode
    fun setLibraryMode(mode: LibraryMode)

    fun getLastSearchQuery(): String
    fun setLastSearchQuery(query: String)

    fun getPaginationConfig(): Pair<Boolean, Boolean> // limit, offset
    fun setPaginationConfig(loadingMore: Boolean, loadingPrevious: Boolean)

    fun clearAllPreferences()
}