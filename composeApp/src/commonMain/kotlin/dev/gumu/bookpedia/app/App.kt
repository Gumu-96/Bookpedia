package dev.gumu.bookpedia.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.gumu.bookpedia.book.presentation.booklist.BookListScreen

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = BookGraph.Graph
        ) {
            navigation<BookGraph.Graph>(
                startDestination = BookGraph.BookList
            ) {
                composable<BookGraph.BookList> {
                    BookListScreen(
                        onBookClick = { navController.navigate(BookGraph.BookDetails(it.id)) }
                    )
                }
                composable<BookGraph.BookDetails> { entry ->
                    val args = entry.toRoute<BookGraph.BookDetails>()

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Book ${args.id}")
                    }
                }
            }
        }
    }
}
