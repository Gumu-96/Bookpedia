package dev.gumu.bookpedia.book.data.network

import dev.gumu.bookpedia.book.data.dto.BookWorkDto
import dev.gumu.bookpedia.book.data.dto.SearchResponseDto
import dev.gumu.bookpedia.core.domain.BookpediaResult
import dev.gumu.bookpedia.core.domain.DataError

// Abstraction to allow for a change of networking library in the future without affecting the rest of the codebase
interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): BookpediaResult<SearchResponseDto, DataError.Remote>

    suspend fun getBookDetails(workId: String): BookpediaResult<BookWorkDto, DataError.Remote>
}
