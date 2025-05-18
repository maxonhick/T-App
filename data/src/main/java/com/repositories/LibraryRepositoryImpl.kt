package com.repositories

import com.PreferencesManager
import com.ScreenState
import com.database.dao.LibraryDao
import com.database.entites.BookEntity
import com.database.entites.DiskEntity
import com.database.entites.NewspaperEntity
import com.library.Book
import com.library.Disk
import com.library.LibraryObjects
import com.library.Newspaper
import com.mappers.BookMapper
import com.mappers.DiskMapper
import com.mappers.NewspaperMapper
import com.network.ApiClient
import com.network.GoogleBooksApiService
import javax.inject.Inject

class LibraryRepositoryImpl(
    private val localDataSource: LibraryDao,
    private val remoteDataSource: ApiClient,
    private val bookMapper: BookMapper,
    private val diskMapper: DiskMapper,
    private val newspaperMapper: NewspaperMapper,
    private val preferencesManager: PreferencesManager
) : LibraryRepository {

    // region Local Data Operations

    override suspend fun getItems(
        sortByName: Boolean,
        limit: Int,
        offset: Int
    ): List<LibraryObjects> {
        return try {
            val dtos = localDataSource.getLibraryItems(sortByName, limit, offset)
            dtos.mapNotNull { dto ->
                when (dto.itemType) {
                    "book" -> getBookDetails(dto.objectId)
                    "disk" -> getDiskDetails(dto.objectId)
                    "newspaper" -> getNewspaperDetails(dto.objectId)
                    else -> null
                }
            }
        } catch (e: Exception) {
            throw LibraryRepositoryException("Failed to load items", e)
        }
    }

    override suspend fun getItemDetails(id: Int, type: String): LibraryObjects? {
        return when (type) {
            "book" -> getBookDetails(id)
            "disk" -> getDiskDetails(id)
            "newspaper" -> getNewspaperDetails(id)
            else -> null
        }
    }

    private suspend fun getBookDetails(id: Int): Book? {
        return localDataSource.getBookById(id)?.let(bookMapper::toDomain)
    }

    private suspend fun getDiskDetails(id: Int): Disk? {
        return localDataSource.getDiskById(id)?.let(diskMapper::toDomain)
    }

    private suspend fun getNewspaperDetails(id: Int): Newspaper? {
        return localDataSource.getNewspaperById(id)?.let(newspaperMapper::toDomain)
    }

    override suspend fun getTotalCount(): Int {
        return try {
            localDataSource.getTotalCount()
        } catch (e: Exception) {
            throw LibraryRepositoryException("Failed to get total count", e)
        }
    }

    override suspend fun addItem(item: LibraryObjects) {
        try {
            when (item) {
                is Book -> addBook(item)
                is Disk -> addDisk(item)
                is Newspaper -> addNewspaper(item)
            }
        } catch (e: Exception) {
            throw LibraryRepositoryException("Failed to add item", e)
        }
    }

    private suspend fun addBook(book: Book) {
        localDataSource.insertBook(bookMapper.toEntity(book))
    }

    private suspend fun addDisk(disk: Disk) {
        localDataSource.insertDisk(diskMapper.toEntity(disk))
    }

    private suspend fun addNewspaper(newspaper: Newspaper) {
        localDataSource.insertNewspaper(newspaperMapper.toEntity(newspaper))
    }

    // endregion

    // region Remote Data Operations

    override suspend fun searchGoogleBooks(query: String): List<Book> {
        return try {
            val response = remoteDataSource.googleBooksService.searchBooks(query)
            response.items?.mapNotNull { volume ->
                bookMapper.fromNetwork(volume)?.also { book ->
                    // Сохраняем время последнего поиска
                    preferencesManager.setLastSearchQuery(query)
                }
            } ?: emptyList()
        } catch (e: Exception) {
            throw LibraryRepositoryException("Failed to search books", e)
        }
    }

    override suspend fun saveGoogleBook(book: Book): Boolean {
        return try {
            val exists = localDataSource.getBookById(book.objectId) != null
            if (!exists) {
                localDataSource.insertBook(bookMapper.toEntity(book))
                true
            } else {
                false
            }
        } catch (e: Exception) {
            throw LibraryRepositoryException("Failed to save book", e)
        }
    }

    // endregion

    // region Pagination

    override suspend fun getPaginationState(): ScreenState.PaginationState {
        val (loadingMore, loadingPrevious) = preferencesManager.getPaginationConfig()
        return ScreenState.PaginationState(
            isLoadingMore = loadingMore,
            isLoadingPrevious = loadingPrevious
        )
    }

    override suspend fun savePaginationState(loadingMore: Boolean, loadingPrevious: Boolean) {
        preferencesManager.setPaginationConfig(loadingMore, loadingPrevious)
    }

    // endregion
}

class LibraryRepositoryException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)