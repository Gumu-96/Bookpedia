package dev.gumu.bookpedia.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dev.gumu.bookpedia.book.presentation.SelectedBookViewModel
import dev.gumu.bookpedia.book.presentation.bookdetail.BookDetailIntent
import dev.gumu.bookpedia.book.presentation.bookdetail.BookDetailScreen
import dev.gumu.bookpedia.book.presentation.bookdetail.BookDetailViewModel
import dev.gumu.bookpedia.book.presentation.booklist.BookListScreen
import org.koin.compose.viewmodel.koinViewModel

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
                composable<BookGraph.BookList> { entry ->
                    val selectedBookViewModel = entry.sharedKoinViewModel<SelectedBookViewModel>(navController)

                    LaunchedEffect(Unit) {
                        selectedBookViewModel.onSelectBook(null)
                    }
                    BookListScreen(
                        onBookClick = {
                            selectedBookViewModel.onSelectBook(it)
                            navController.navigate(BookGraph.BookDetails(it.id))
                        }
                    )
                }
                composable<BookGraph.BookDetails> {
                    val viewModel = koinViewModel<BookDetailViewModel>()
                    val selectedBookViewModel = it.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    val selectedBook by selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()
                    LaunchedEffect(selectedBook) {
                        selectedBook?.let { book ->
                            viewModel.onIntent(BookDetailIntent.OnSelectedBookChange(book))
                        }
                    }
                    BookDetailScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val graphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) { navController.getBackStackEntry(graphRoute) }

    return koinViewModel<T>(viewModelStoreOwner = parentEntry)
}
