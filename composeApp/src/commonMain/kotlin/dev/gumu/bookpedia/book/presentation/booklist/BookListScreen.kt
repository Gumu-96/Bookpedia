package dev.gumu.bookpedia.book.presentation.booklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bookpedia.composeapp.generated.resources.Res
import bookpedia.composeapp.generated.resources.favorites
import bookpedia.composeapp.generated.resources.no_favorites
import bookpedia.composeapp.generated.resources.no_search_results
import bookpedia.composeapp.generated.resources.search_results
import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.book.presentation.booklist.component.BookList
import dev.gumu.bookpedia.book.presentation.booklist.component.BookSearchBar
import dev.gumu.bookpedia.core.presentation.DarkBlue
import dev.gumu.bookpedia.core.presentation.DesertWhite
import dev.gumu.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookListScreen(
    viewModel: BookListViewModel = koinViewModel(),
    onBookClick: (Book) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookListScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onBookClick = onBookClick
    )
}

@Composable
private fun BookListScreen(
    state: BookListState,
    onIntent: (BookListIntent) -> Unit,
    onBookClick: (Book) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .statusBarsPadding()
    ) {
        BookSearchBar(
            query = state.searchQuery,
            onQueryChange = { onIntent(BookListIntent.OnSearchQueryChange(it)) },
            onClearClick = { onIntent(BookListIntent.OnClearSearchClick) },
            onImeSearch = {
                focusManager.clearFocus()
                onIntent(BookListIntent.OnPerformSearch(state.searchQuery))
            },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )
        TabContainer(
            state = state,
            onBookClick = onBookClick,
            onTabClick = { onIntent(BookListIntent.OnTabClick(it)) },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun TabContainer(
    state: BookListState,
    onBookClick: (Book) -> Unit,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { 2 }
    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        onTabClick(pagerState.currentPage)
    }
    Surface(
        color = DesertWhite,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.navigationBarsPadding()
        ) {
            PrimaryTabRow(
                selectedTabIndex = state.selectedTabIndex,
                containerColor = DesertWhite,
                contentColor = SandYellow,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(state.selectedTabIndex, matchContentSize = true),
                        width = Dp.Unspecified,
                        color = SandYellow
                    )
                },
                modifier = Modifier
                    .widthIn(700.dp)
                    .fillMaxWidth()
            ) {
                Tab(
                    selected = state.selectedTabIndex == 0,
                    onClick = { onTabClick(0) },
                    selectedContentColor = SandYellow,
                    unselectedContentColor = Color.Black.copy(alpha = 0.5f),
                    text = {
                        Text(
                            text = stringResource(Res.string.search_results),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
                Tab(
                    selected = state.selectedTabIndex == 1,
                    onClick = { onTabClick(1) },
                    selectedContentColor = SandYellow,
                    unselectedContentColor = Color.Black.copy(alpha = 0.5f),
                    text = {
                        Text(
                            text = stringResource(Res.string.favorites),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { currentPage ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (currentPage) {
                        0 -> when {
                            state.isLoading -> CircularProgressIndicator(color = DarkBlue)
                            state.errorMsg != null -> {
                                Text(
                                    text = state.errorMsg.asString(),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                            state.searchResults.isEmpty() -> {
                                Text(
                                    text = stringResource(Res.string.no_search_results),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                            else -> BookList(
                                books = state.searchResults,
                                onBookClick = onBookClick,
                                contentPadding = PaddingValues(16.dp),
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        1 -> when {
                            state.favoriteBooks.isEmpty() -> {
                                Text(
                                    text = stringResource(Res.string.no_favorites),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                            else -> BookList(
                                books = state.favoriteBooks,
                                onBookClick = onBookClick,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun BookListScreenPreview() {
    MaterialTheme {
        BookListScreen(
            state = BookListState(
                searchResults = books,
                isLoading = false,
            ),
            onIntent = {},
            onBookClick = {}
        )
    }
}

private val books = (1..10).map {
    Book(
        id = it.toString(),
        title = "Book $it",
        imageUrl = "",
        authors = listOf("dev", "gumu"),
        description = "Description of book $it",
        languages = emptyList(),
        firstPublishYear = null,
        averageRating = 4.1612,
        ratingCount = 1000,
        numPages = 100,
        numEditions = 1
    )
}
