package dev.gumu.bookpedia.book.presentation.bookdetail

import dev.gumu.bookpedia.book.domain.Book

sealed interface BookDetailIntent {
    data object OnFavoriteClick : BookDetailIntent
    data class OnSelectedBookChange(val book: Book) : BookDetailIntent
}
