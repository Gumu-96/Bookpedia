package dev.gumu.bookpedia.book.presentation.booklist

sealed interface BookListIntent {
    data class OnSearchQueryChange(val query: String) : BookListIntent
    data object OnClearSearchClick : BookListIntent
    data class OnTabClick(val index: Int) : BookListIntent
}
