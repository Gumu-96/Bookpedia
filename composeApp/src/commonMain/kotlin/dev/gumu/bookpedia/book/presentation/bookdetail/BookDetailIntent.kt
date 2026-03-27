package dev.gumu.bookpedia.book.presentation.bookdetail

sealed interface BookDetailIntent {
    data object OnFavoriteClick : BookDetailIntent
}
