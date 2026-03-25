package dev.gumu.bookpedia.book.data.network

import dev.gumu.bookpedia.book.data.dto.SearchResponseDto
import dev.gumu.bookpedia.core.data.safeCall
import dev.gumu.bookpedia.core.domain.BookpediaResult
import dev.gumu.bookpedia.core.domain.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorRemoteBookDataSource(
    private val httpClient: HttpClient
) : RemoteBookDataSource {
    override suspend fun searchBooks(
        query: String,
        resultLimit: Int?,
    ): BookpediaResult<SearchResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get(
                urlString = "$BASE_URL/search.json",
            ) {
                parameter("q", query)
                resultLimit?.let { parameter("limit", it) }
                parameter("language", "eng")
                parameter("fields", "key,title,author_key,author_name,language,cover_edition_key,cover_i,first_publish_year,ratings_average,ratings_count,number_of_pages_median,edition_count")
            }
        }
    }

    companion object {
        private const val BASE_URL = "https://openlibrary.org"
    }
}
