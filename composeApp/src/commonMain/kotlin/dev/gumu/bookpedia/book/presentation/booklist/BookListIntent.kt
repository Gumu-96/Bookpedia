package dev.gumu.bookpedia.book.presentation.booklist

sealed interface BookListIntent {
    data class OnSearchQueryChange(val query: String) : BookListIntent
    data object OnClearSearchClick : BookListIntent
    data class OnPerformSearch(val query: String) : BookListIntent
    data class OnTabClick(val index: Int) : BookListIntent
}
