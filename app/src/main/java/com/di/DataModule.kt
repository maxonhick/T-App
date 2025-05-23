package com.di

import android.content.Context
import androidx.room.Room
import com.PreferencesManager
import com.database.dao.LibraryDao
import com.library.data.LibraryDatabase
import com.mappers.BookMapper
import com.mappers.DiskMapper
import com.mappers.NewspaperMapper
import com.network.ApiClient
import com.preferences.PreferencesManagerImpl
import com.repositories.LibraryRepository
import com.repositories.LibraryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideApiClient(): ApiClient {
        return ApiClient // Инициализируйте ваш ApiClient здесь
    }

    // Предоставление репозитория
    @Provides
    fun provideLibraryRepository(
        localDataSource: LibraryDao,
        remoteDataSource: ApiClient,
        bookMapper: BookMapper,
        diskMapper: DiskMapper,
        newspaperMapper: NewspaperMapper,
        preferencesManager: PreferencesManager
    ): LibraryRepository {
        return LibraryRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            bookMapper = bookMapper,
            diskMapper = diskMapper,
            newspaperMapper = newspaperMapper,
            preferencesManager = preferencesManager
        )
    }

    // Предоставление DAO (предполагая, что он получается из Database)
    @Provides
    fun provideLibraryDao(database: LibraryDatabase): LibraryDao {
        return database.libraryDao()
    }

    // Предоставление PreferencesManager
    @Provides
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManagerImpl(context)
    }

    // Предоставление мапперов
    @Provides
    fun provideBookMapper(): BookMapper = BookMapper()

    @Provides
    fun provideDiskMapper(): DiskMapper = DiskMapper()

    @Provides
    fun provideNewspaperMapper(): NewspaperMapper = NewspaperMapper()

    // Предоставление базы данных (если используете Room)
    @Provides
    fun provideDatabase(context: Context): LibraryDatabase {
        return LibraryDatabase.getDatabase(context)
    }
}