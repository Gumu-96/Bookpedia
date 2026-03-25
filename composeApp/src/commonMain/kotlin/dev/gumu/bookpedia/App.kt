package dev.gumu.bookpedia

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import dev.gumu.bookpedia.book.presentation.booklist.BookListScreen

@Composable
fun App() {
    MaterialTheme {
        BookListScreen()
    }
}
