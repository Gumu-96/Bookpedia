package dev.gumu.bookpedia.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.book.presentation.bookdetail.BookDetailScreen
import dev.gumu.bookpedia.book.presentation.booklist.BookListScreen
import org.koin.compose.viewmodel.koinViewModel
import kotlin.reflect.typeOf

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = BookGraph.Graph,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(500)
                )
            }
        ) {
            navigation<BookGraph.Graph>(
                startDestination = BookGraph.BookList
            ) {
                composable<BookGraph.BookList> {
                    BookListScreen(
                        onBookClick = {
                            navController.navigate(BookGraph.BookDetails(it))
                        }
                    )
                }
                composable<BookGraph.BookDetails>(
                    typeMap = mapOf(typeOf<Book>() to BookpediaNavType.BookType)
                ) {
                    BookDetailScreen(
                        onBackClick = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}

/**
 * Helper fun to scope a viewModel instance to a navigation graph
 */
@Suppress("unused")
@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val graphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) { navController.getBackStackEntry(graphRoute) }

    return koinViewModel<T>(viewModelStoreOwner = parentEntry)
}
