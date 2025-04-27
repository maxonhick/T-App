package com.library.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.library.TypeLibraryObjects

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val objectId: Int,
    val name: String,
    val access: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val pages: Int,
    val author: String,
    val objectType: TypeLibraryObjects = TypeLibraryObjects.Book
)