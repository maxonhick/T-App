package com.mappers

import com.database.entites.BookEntity
import com.library.Book
import com.library.TypeLibraryObjects
import com.network.Volume

class BookMapper{
    fun toDomain(entity: BookEntity): Book = Book(
        objectId = entity.objectId,
        access = entity.access,
        name = entity.name,
        objectType = TypeLibraryObjects.Book,
        author = entity.author,
        pages = entity.pages,
        createdAt = entity.createdAt
    )

    fun toEntity(book: Book): BookEntity = BookEntity(
        objectId = book.objectId,
        name = book.name,
        author = book.author,
        pages = book.pages,
        access = book.access,
        createdAt = book.createdAt,
        objectType = TypeLibraryObjects.Book
    )

    fun fromNetwork(volume: Volume): Book? {
        val isbn = volume.volumeInfo.industryIdentifiers
            ?.firstOrNull { it.type == "ISBN_13" || it.type == "ISBN_10" }
            ?.identifier ?: return null

        return Book(
            objectId = isbn.hashCode(),
            access = true,
            objectType = TypeLibraryObjects.Book,
            name = volume.volumeInfo.title ?: "No title",
            author = volume.volumeInfo.authors?.joinToString() ?: "Unknown",
            pages = volume.volumeInfo.pageCount ?: 0,
            createdAt = System.currentTimeMillis()
        )
    }
}