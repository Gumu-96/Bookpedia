package dev.gumu.bookpedia.book.presentation.booklist

import dev.gumu.bookpedia.book.domain.Book

sealed interface BookListIntent {
    data class OnSearchQueryChange(val query: String) : BookListIntent
    data object OnClearSearchClick : BookListIntent
    data class OnBookClick(val book: Book) : BookListIntent
    data class OnTabClick(val index: Int) : BookListIntent
}
