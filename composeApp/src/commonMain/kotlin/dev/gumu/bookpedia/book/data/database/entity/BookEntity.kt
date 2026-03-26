package dev.gumu.bookpedia.book.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.gumu.bookpedia.book.domain.Book

@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val title: String,
    val description: String?,
    val imgUrl: String?,
    val authors: List<String>,
    val languages: List<String>,
    val firstPublishYear: String?,
    val ratingsAverage: Double?,
    val ratingsCount: Int?,
    val numPages: Int?,
    val numEditions: Int,
)

fun Book.toBookEntity() = BookEntity(
    id = id,
    title = title,
    description = description,
    imgUrl = imageUrl,
    authors = authors,
    languages = languages,
    firstPublishYear = firstPublishYear,
    ratingsAverage = averageRating,
    ratingsCount = ratingCount,
    numPages = numPages,
    numEditions = numEditions
)