package com.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("fields") fields: String = "items(volumeInfo(title,authors,pageCount,industryIdentifiers))"
    ): GoogleBooksResponse
}