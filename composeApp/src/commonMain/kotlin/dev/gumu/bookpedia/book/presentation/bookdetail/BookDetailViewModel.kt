package dev.gumu.bookpedia.book.presentation.bookdetail

import androidx.lifecycle.ViewModel
import dev.gumu.bookpedia.book.domain.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BookDetailViewModel : ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state.asStateFlow()

    private fun onFavoriteClick() {

    }

    private fun onSelectedBookChange(book: Book) {
        _state.update { it.copy(book = book) }
    }

    fun onIntent(intent: BookDetailIntent) {
        when (intent) {
            is BookDetailIntent.OnFavoriteClick -> onFavoriteClick()
            is BookDetailIntent.OnSelectedBookChange -> onSelectedBookChange(intent.book)
        }
    }
}
