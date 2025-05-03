package com.tBankApp.DataBase

data class LibraryItemDto(
    val itemType: String,
    val objectId: Int,
    val name: String,
    val access: Boolean,
    val createdAt: Long
)