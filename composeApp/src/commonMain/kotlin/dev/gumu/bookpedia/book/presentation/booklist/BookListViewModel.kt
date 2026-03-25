package dev.gumu.bookpedia.book.presentation.booklist

import androidx.lifecycle.ViewModel
import dev.gumu.bookpedia.book.domain.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BookListViewModel : ViewModel() {
    private val _state = MutableStateFlow(BookListState())
    val state = _state.asStateFlow()

    private fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun onTabClick(index: Int) {
        _state.update { it.copy(selectedTabIndex = index) }
    }

    private fun onBookClick(book: Book) {
        // Handle book click, e.g., navigate to detail screen
    }

    fun onIntent(intent: BookListIntent) {
        when (intent) {
            is BookListIntent.OnSearchQueryChange -> onSearchQueryChange(intent.query)
            is BookListIntent.OnClearSearchClick -> onSearchQueryChange("")
            is BookListIntent.OnBookClick -> onBookClick(intent.book)
            is BookListIntent.OnTabClick -> onTabClick(intent.index)
        }
    }
}
