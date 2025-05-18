package com

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.database.dao.LibraryDao
import com.library.data.LibraryDatabase
import com.mappers.BookMapper
import com.mappers.DiskMapper
import com.mappers.NewspaperMapper
import com.network.ApiClient
import com.preferences.PreferencesManagerImpl
import com.repositories.LibraryRepository
import com.repositories.LibraryRepositoryImpl
import com.useCases.AddItemUseCase
import com.useCases.GetLibraryItemsUseCase
import com.useCases.GetLibraryModeUseCase
import com.useCases.GetSortPreferenceUseCase
import com.useCases.GetTotalCountUseCase
import com.useCases.LoadMoreItemsUseCase
import com.useCases.LoadPreviousItemsUseCase
import com.useCases.SaveBookUseCase
import com.useCases.SearchBooksUseCase
import com.useCases.SetLibraryModeUseCase
import com.useCases.SetSortPreferenceUseCase
import com.useCases.SwitchModeUseCase
import com.viewModels.ViewModelFactory

object DependencyContainer {

    // Контекст приложения (должен быть инициализирован в Application классе)
    private lateinit var appContext: Context

    // Инициализация контейнера
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // --- База данных (Room) ---
    private val libraryDatabase: LibraryDatabase by lazy {
        LibraryDatabase.getDatabase(appContext)
    }

    private val libraryDao: LibraryDao by lazy {
        libraryDatabase.libraryDao()
    }

    // --- Мапперы ---
    private val bookMapper: BookMapper by lazy { BookMapper() }
    private val diskMapper: DiskMapper by lazy { DiskMapper() }
    private val newspaperMapper: NewspaperMapper by lazy { NewspaperMapper() }

    // --- Preferences ---
    val preferencesManager: PreferencesManager by lazy {
        PreferencesManagerImpl(appContext)
    }

    // --- Репозиторий ---
    val libraryRepository: LibraryRepository by lazy {
        LibraryRepositoryImpl(
            localDataSource = libraryDao,
            remoteDataSource = ApiClient,
            bookMapper = bookMapper,
            diskMapper = diskMapper,
            newspaperMapper = newspaperMapper,
            preferencesManager = preferencesManager
        )
    }

    // --- UseCases ---
    val getLibraryItemsUseCase: GetLibraryItemsUseCase by lazy {
        GetLibraryItemsUseCase(libraryRepository, preferencesManager)
    }

    val getTotalCountUseCase: GetTotalCountUseCase by lazy {
        GetTotalCountUseCase(libraryRepository)
    }

    val loadMoreItemsUseCase: LoadMoreItemsUseCase by lazy {
        LoadMoreItemsUseCase(getLibraryItemsUseCase)
    }

    val loadPreviousItemsUseCase: LoadPreviousItemsUseCase by lazy {
        LoadPreviousItemsUseCase(getLibraryItemsUseCase)
    }

    val addItemUseCase: AddItemUseCase by lazy {
        AddItemUseCase(libraryRepository)
    }

    val searchBooksUseCase: SearchBooksUseCase by lazy {
        SearchBooksUseCase(libraryRepository)
    }

    val saveBookUseCase: SaveBookUseCase by lazy {
        SaveBookUseCase(libraryRepository)
    }

    val setSortPreferenceUseCase: SetSortPreferenceUseCase by lazy {
        SetSortPreferenceUseCase(preferencesManager)
    }

    val switchModeUseCase: SwitchModeUseCase by lazy {
        SwitchModeUseCase(preferencesManager)
    }

    val getSortPreferenceUseCase: GetSortPreferenceUseCase by lazy {
        GetSortPreferenceUseCase(preferencesManager)
    }

    val getLibraryModeUseCase: GetLibraryModeUseCase by lazy {
        GetLibraryModeUseCase(preferencesManager)
    }

    val setLibraryModeUseCase: SetLibraryModeUseCase by lazy {
        SetLibraryModeUseCase(preferencesManager)
    }

    fun getViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(context)
    }
}