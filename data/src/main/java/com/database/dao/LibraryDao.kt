package com.database.dao

import androidx.room.*
import com.database.entites.BookEntity
import com.database.entites.DiskEntity
import com.database.entites.NewspaperEntity

@Dao
interface LibraryDao {
    // Отдельные операции для каждого типа
    @Insert
    suspend fun insertBook(book: BookEntity)

    @Insert
    suspend fun insertDisk(disk: DiskEntity)

    @Insert
    suspend fun insertNewspaper(newspaper: NewspaperEntity)

    // Комбинированные запросы с пагинацией
    @Transaction
    suspend fun getLibraryItems(sortByName: Boolean, limit: Int, offset: Int): List<LibraryItemDto> {
        return if (sortByName) {
            getItemsSortedByName(limit, offset)
        } else {
            getItemsSortedByDate(limit, offset)
        }
    }

    @Query("""
    SELECT 'book' as itemType, objectId, name, access, createdAt 
    FROM books 
    UNION ALL 
    SELECT 'disk' as itemType, objectId, name, access, createdAt 
    FROM disks 
    UNION ALL 
    SELECT 'newspaper' as itemType, objectId, name, access, createdAt 
    FROM newspapers 
    ORDER BY name ASC
    LIMIT :limit OFFSET :offset
""")
    suspend fun getItemsSortedByName(limit: Int, offset: Int): List<LibraryItemDto>

    @Query("""
    SELECT 'book' as itemType, objectId, name, access, createdAt 
    FROM books 
    UNION ALL 
    SELECT 'disk' as itemType, objectId, name, access, createdAt 
    FROM disks 
    UNION ALL 
    SELECT 'newspaper' as itemType, objectId, name, access, createdAt 
    FROM newspapers 
    ORDER BY createdAt DESC
    LIMIT :limit OFFSET :offset
""")
    suspend fun getItemsSortedByDate(limit: Int, offset: Int): List<LibraryItemDto>

    @Query("SELECT * FROM books WHERE objectId = :id")
    suspend fun getBookById(id: Int): BookEntity?

    @Query("SELECT * FROM disks WHERE objectId = :id")
    suspend fun getDiskById(id: Int): DiskEntity?

    @Query("SELECT * FROM newspapers WHERE objectId = :id")
    suspend fun getNewspaperById(id: Int): NewspaperEntity?

    @Query("""
        SELECT (SELECT COUNT(*) FROM books) + (SELECT COUNT(*) FROM disks) + (SELECT COUNT(*) FROM newspapers)
        """)
    suspend fun getTotalCount(): Int
}