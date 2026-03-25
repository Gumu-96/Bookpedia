package dev.gumu.bookpedia.book.data.repository

import dev.gumu.bookpedia.book.data.dto.toBook
import dev.gumu.bookpedia.book.data.network.RemoteBookDataSource
import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.book.domain.repository.BookRepository
import dev.gumu.bookpedia.core.domain.BookpediaResult
import dev.gumu.bookpedia.core.domain.DataError
import dev.gumu.bookpedia.core.domain.map

class DefaultBookRepository (
    private val remoteBookDataSource: RemoteBookDataSource
) : BookRepository {
    override suspend fun searchBooks(
        query: String
    ): BookpediaResult<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query)
            .map { response -> response.results.map { it.toBook() } }
    }
}
