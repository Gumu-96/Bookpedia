package dev.gumu.bookpedia.book.presentation.bookdetail

import dev.gumu.bookpedia.book.domain.Book

data class BookDetailState(
    val loading: Boolean = false,
    val favorite: Boolean = false,
    val book: Book? = null,
)
