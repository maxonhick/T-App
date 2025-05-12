package com.database.dao

data class LibraryItemDto(
    val itemType: String,
    val objectId: Int,
    val name: String,
    val access: Boolean,
    val createdAt: Long
)