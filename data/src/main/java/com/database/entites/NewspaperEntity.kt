package com.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.library.Month
import com.library.TypeLibraryObjects

@Entity(tableName = "newspapers")
data class NewspaperEntity(
    @PrimaryKey val objectId: Int,
    val name: String,
    val access: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val releaseNumber: Int,
    val month: Month,
    val objectType: TypeLibraryObjects = TypeLibraryObjects.Newspaper
)