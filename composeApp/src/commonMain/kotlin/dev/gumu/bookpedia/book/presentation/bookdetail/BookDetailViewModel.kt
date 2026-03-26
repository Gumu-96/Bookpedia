package dev.gumu.bookpedia.book.presentation.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.book.domain.repository.BookRepository
import dev.gumu.bookpedia.core.domain.onError
import dev.gumu.bookpedia.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookRepo: BookRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state.asStateFlow()

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
