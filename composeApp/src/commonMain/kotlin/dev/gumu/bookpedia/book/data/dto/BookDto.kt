package dev.gumu.bookpedia.book.data.dto

import dev.gumu.bookpedia.book.domain.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    @SerialName("key") val id: String,
    @SerialName("title") val title: String,
    @SerialName("author_key") val authorKeys: List<String>,
    @SerialName("author_name") val authorNames: List<String>,
    @SerialName("language") val languages: List<String>,
    @SerialName("cover_edition_key") val coverKey: Int?,
    @SerialName("cover_i") val coverAltKey: Int?,
    @SerialName("first_publish_year") val firstPublishYear: Int?,
    @SerialName("ratings_average") val ratingsAverage: Double?,
    @SerialName("ratings_count") val ratingsCount: Int?,
    @SerialName("number_of_pages_median") val numPages: Int?,
    @SerialName("edition_count") val numEditions: Int?
)

fun BookDto.toBook() = Book(
    id = id,
    title = title,
    imageUrl = coverKey?.let { "https://covers.openlibrary.org/b/olid/$it-L.jpg" }
        ?: coverAltKey?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" }
        ?: "",
    authors = authorNames,
    description = null, // The search endpoint doesn't return a description, so this will be populated in the BookDetailDto
    languages = languages,
    firstPublishYear = firstPublishYear?.toString(),
    averageRating = ratingsAverage,
    ratingCount = ratingsCount,
    numPages = numPages,
    numEditions = numEditions ?: 0
)
