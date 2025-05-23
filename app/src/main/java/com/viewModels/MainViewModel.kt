package com.viewModels

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.library.LibraryObjects
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    // Состояние
    private val _currentState = MutableStateFlow<Pair<LibraryObjects?, Boolean>?>(null)
    val currentState: StateFlow<Pair<LibraryObjects?, Boolean>?> = _currentState

    // Ориентация
    val isLandscape: Boolean
        get() = context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // События
    private val _orientationChanged = MutableLiveData<Unit>()
    val orientationChanged: LiveData<Unit> = _orientationChanged

    // Методы
    fun setCurrentItem(item: LibraryObjects, isNew: Boolean) {
        _currentState.value = Pair(item, isNew)
    }

    fun clearCurrentItem() {
        _currentState.value = null
    }

    fun restoreState(item: LibraryObjects?, isNew: Boolean) {
        _currentState.value = Pair(item, isNew)
    }

    fun updateOrientation(orientation: Int) {
        if (isLandscape != (orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            _orientationChanged.value = Unit
        }
    }
}