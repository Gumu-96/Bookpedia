package dev.gumu.bookpedia.book.presentation.booklist

import androidx.compose.runtime.Immutable
import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.core.presentation.UiText

@Immutable
data class BookListState(
    val searchQuery: String = "",
    val searchResults: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val errorMsg: UiText? = null
)
