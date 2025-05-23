package com.useCases

import com.library.LibraryObjects
import javax.inject.Inject

class LoadMoreItemsUseCase @Inject constructor(
    private val getItemsUseCase: GetLibraryItemsUseCase
) {
    suspend operator fun invoke(
        currentOffset: Int,
        limit: Int,
        totalItems: Int
    ): Result<Pair<List<LibraryObjects>, Int>> = try {
        val newOffset = if (totalItems - (currentOffset + limit) < limit / 2) {
            totalItems - limit
        } else {
            currentOffset + limit / 2
        }

        val items = getItemsUseCase(limit, newOffset).getOrThrow()
        Result.success(Pair(items, newOffset))
    } catch (e: Exception) {
        Result.failure(e)
    }
}