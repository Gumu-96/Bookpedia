package dev.gumu.bookpedia.book.domain.repository

import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.core.domain.BookpediaResult
import dev.gumu.bookpedia.core.domain.DataError
import dev.gumu.bookpedia.core.domain.EmptyResult
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBooks(query: String): BookpediaResult<List<Book>, DataError.Remote>
    suspend fun getBookDetails(workId: String): BookpediaResult<String?, DataError>

    fun getFavoriteBooks(): Flow<List<Book>>
    fun bookIsFavorite(id: String): Flow<Boolean>
    suspend fun addFavorite(book: Book): EmptyResult<DataError.Local>
    suspend fun deleteFavorite(id: String)
}
