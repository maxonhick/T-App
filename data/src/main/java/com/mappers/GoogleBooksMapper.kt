package com.mappers

import com.library.Book
import com.library.LibraryObjects
import com.library.TypeLibraryObjects
import com.network.GoogleBooksResponse
import com.network.Volume

class GoogleBooksMapper {
    // Преобразование ответа API в список доменных моделей
    fun toDomainList(response: GoogleBooksResponse): List<LibraryObjects> {
        return response.items?.mapNotNull { volume ->
            volume.toDomain()
        } ?: emptyList()
    }

    // Преобразование Volume в Book
    fun Volume.toDomain(): Book? {
        return Book(
            objectId = volumeInfo.title?.hashCode() ?: return null,
            access = true,
            name = volumeInfo.title,
            objectType = TypeLibraryObjects.Book,
            pages = volumeInfo.pageCount ?: 0,
            author = volumeInfo.authors?.joinToString() ?: "Unknown author",
            createdAt = volumeInfo.industryIdentifiers?.get(0)?.identifier?.toLong() ?: System.currentTimeMillis()
        )
    }
}