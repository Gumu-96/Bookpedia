package dev.gumu.bookpedia.book.presentation.bookdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.gumu.bookpedia.app.BookGraph
import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.book.domain.repository.BookRepository
import dev.gumu.bookpedia.core.domain.onError
import dev.gumu.bookpedia.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookRepo: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = savedStateHandle.toRoute<BookGraph.BookDetails>()

    private val _state = MutableStateFlow(BookDetailState())
    val state = _state
        .combine(bookRepo.bookIsFavorite(args.id)) { state, favorite ->
            state.copy(favorite = favorite)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = BookDetailState()
        )

    private fun fetchBookDescription(workId: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            bookRepo
                .getBookDetails(workId)
                .onSuccess { description ->
                    _state.update {
                        it.copy(
                            book = it.book?.copy(description = description),
                            loading = false
                        )
                    }
                }
                .onError {
                    _state.update { it.copy(loading = false) }
                }
        }
    }

    private fun onFavoriteClick() {
        viewModelScope.launch {
            if (state.value.favorite) {
                bookRepo.deleteFavorite(args.id)
            } else {
                state.value.book?.let { book ->
                    bookRepo.addFavorite(book)
                }
            }
        }
    }

    private fun onSelectedBookChange(book: Book) {
        _state.update { it.copy(book = book) }
        fetchBookDescription(book.id)
    }

    fun onIntent(intent: BookDetailIntent) {
        when (intent) {
            is BookDetailIntent.OnFavoriteClick -> onFavoriteClick()
            is BookDetailIntent.OnSelectedBookChange -> onSelectedBookChange(intent.book)
        }
    }
}
