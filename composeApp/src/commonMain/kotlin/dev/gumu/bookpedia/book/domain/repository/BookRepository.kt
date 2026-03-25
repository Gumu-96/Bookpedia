package dev.gumu.bookpedia.book.domain.repository

import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.core.domain.BookpediaResult
import dev.gumu.bookpedia.core.domain.DataError

interface BookRepository {
    suspend fun searchBooks(query: String): BookpediaResult<List<Book>, DataError.Remote>
}
