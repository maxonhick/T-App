package com.tBankApp.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponse(
    @SerialName("items")
    val items: List<Volume>? = null
)

@Serializable
data class Volume(
    @SerialName("volumeInfo")
    val volumeInfo: VolumeInfo
)

@Serializable
data class VolumeInfo(
    @SerialName("title")
    val title: String? = null,

    @SerialName("authors")
    val authors: List<String>? = null,

    @SerialName("pageCount")
    val pageCount: Int? = null,

    @SerialName("industryIdentifiers")
    val industryIdentifiers: List<IndustryIdentifier>? = null
)

@Serializable
data class IndustryIdentifier(
    @SerialName("type")
    val type: String,

    @SerialName("identifier")
    val identifier: String
)