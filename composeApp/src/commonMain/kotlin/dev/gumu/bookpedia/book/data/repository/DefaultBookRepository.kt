package dev.gumu.bookpedia.book.data.repository

import androidx.sqlite.SQLiteException
import dev.gumu.bookpedia.book.data.database.FavoriteBookDao
import dev.gumu.bookpedia.book.data.database.entity.toBook
import dev.gumu.bookpedia.book.data.database.entity.toBookEntity
import dev.gumu.bookpedia.book.data.dto.toBook
import dev.gumu.bookpedia.book.data.network.RemoteBookDataSource
import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.book.domain.repository.BookRepository
import dev.gumu.bookpedia.core.domain.BookpediaResult
import dev.gumu.bookpedia.core.domain.DataError
import dev.gumu.bookpedia.core.domain.EmptyResult
import dev.gumu.bookpedia.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultBookRepository (
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao
) : BookRepository {
    override suspend fun searchBooks(
        query: String
    ): BookpediaResult<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query)
            .map { response -> response.results.map { it.toBook() } }
    }

    override suspend fun getBookDetails(workId: String): BookpediaResult<String?, DataError> {
        val localResult = favoriteBookDao.getFavoriteBookById(workId)
        return localResult?.let {
            BookpediaResult.Success(it.description)
        } ?: remoteBookDataSource
            .getBookDetails(workId)
            .map { it.description }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { entities -> entities.map { it.toBook() } }
    }

    override fun bookIsFavorite(id: String): Flow<Boolean> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { entities -> entities.any { it.id == id } }
    }

    override suspend fun addFavorite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favoriteBookDao.upsertFavorite(book.toBookEntity())
            BookpediaResult.Success(Unit)
        } catch (_: SQLiteException) {
            BookpediaResult.Error(DataError.Local.Database)
        }
    }

    override suspend fun deleteFavorite(id: String) {
        favoriteBookDao.deleteFavoriteBookById(id)
    }
}
