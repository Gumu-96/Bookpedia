package dev.gumu.bookpedia.book.presentation.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.book.domain.repository.BookRepository
import dev.gumu.bookpedia.core.domain.onError
import dev.gumu.bookpedia.core.domain.onSuccess
import dev.gumu.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(
    private val bookRepo: BookRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BookListState())
    val state = _state
        .combine(bookRepo.getFavoriteBooks()) { state, favorites ->
            state.copy(favoriteBooks = favorites)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = BookListState()
        )

    private var cachedBooks: List<Book> = emptyList()
    private var searchJob: Job? = null

    private fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun onTabClick(index: Int) {
        _state.update { it.copy(selectedTabIndex = index) }
    }

    private fun searchBooks(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true,
                selectedTabIndex = 0
            )
        }
        bookRepo
            .searchBooks(query)
            .onSuccess { results ->
                _state.update {
                    it.copy(
                        searchResults = results,
                        isLoading = false,
                        errorMsg = null
                    )
                }
                cachedBooks = results
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        searchResults = emptyList(),
                        isLoading = false,
                        errorMsg = error.toUiText()
                    )
                }
            }
    }

    private fun onPerformSearch(query: String) {
        when {
            query.isBlank() -> _state.update {
                it.copy(
                    searchResults = cachedBooks,
                    errorMsg = null
                )
            }
            query.length > 2 -> {
                searchJob?.cancel()
                searchJob = searchBooks(query)
            }
        }
    }

    fun onIntent(intent: BookListIntent) {
        when (intent) {
            is BookListIntent.OnSearchQueryChange -> onSearchQueryChange(intent.query)
            is BookListIntent.OnClearSearchClick -> onSearchQueryChange("")
            is BookListIntent.OnTabClick -> onTabClick(intent.index)
            is BookListIntent.OnPerformSearch -> onPerformSearch(intent.query)
        }
    }
}
