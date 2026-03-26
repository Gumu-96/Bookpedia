package dev.gumu.bookpedia.book.presentation.bookdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bookpedia.composeapp.generated.resources.Res
import bookpedia.composeapp.generated.resources.description_unavailable
import bookpedia.composeapp.generated.resources.languages
import bookpedia.composeapp.generated.resources.pages
import bookpedia.composeapp.generated.resources.rating
import bookpedia.composeapp.generated.resources.synopsis
import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.book.presentation.bookdetail.component.BlurredImageBackground
import dev.gumu.bookpedia.book.presentation.bookdetail.component.BookChip
import dev.gumu.bookpedia.book.presentation.bookdetail.component.ChipSize
import dev.gumu.bookpedia.book.presentation.bookdetail.component.TitledContent
import dev.gumu.bookpedia.core.presentation.DarkBlue
import dev.gumu.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

@Composable
fun BookDetailScreen(
    viewModel: BookDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookDetailScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onBackClick = onBackClick
    )
}

@Composable
fun BookDetailScreen(
    state: BookDetailState,
    onIntent: (BookDetailIntent) -> Unit,
    onBackClick: () -> Unit
) {
    BlurredImageBackground(
        imgUrl = state.book?.imageUrl,
        favorite = state.favorite,
        onFavoriteClick = { onIntent(BookDetailIntent.OnFavoriteClick) },
        onBackClick = onBackClick,
        modifier = Modifier.fillMaxSize()
    ) {
        state.book?.let { book ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .widthIn(700.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .navigationBarsPadding()
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = book.authors.joinToString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    book.averageRating?.let {
                        TitledContent(
                            title = stringResource(Res.string.rating)
                        ) {
                            BookChip {
                                Row {
                                    Text(text = "${round(it * 10) / 10.0}")
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = SandYellow
                                    )
                                }
                            }
                        }
                    }
                    book.numPages?.let {
                        TitledContent(
                            title = stringResource(Res.string.pages)
                        ) {
                            BookChip {
                                Text(text = it.toString())
                            }
                        }
                    }
                }
                if (book.languages.isNotEmpty()) {
                    TitledContent(
                        title = stringResource(Res.string.languages)
                    ) {
                        FlowRow (
                            horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            book.languages.forEach {
                                BookChip(size = ChipSize.Small) {
                                    Text(text = it.uppercase())
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.padding(24.dp))
                Text(
                    text = stringResource(Res.string.synopsis),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(Modifier.padding(8.dp))
                if (state.loading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    ) {
                        CircularProgressIndicator(color = DarkBlue)
                    }
                } else {
                    Text(
                        text = book.description?.takeIf { it.isNotBlank() }
                            ?: stringResource(Res.string.description_unavailable),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BookDetailScreenPreview() {
    MaterialTheme {
        BookDetailScreen(
            state = BookDetailState(
                book = book
            ),
            onIntent = {},
            onBackClick = {}
        )
    }
}

private val book = Book(
    id = "1",
    title = "Android dev for dummies",
    imageUrl = "",
    authors = listOf("dev", "gumu"),
    description = null,
    languages =  listOf("es", "en", "de"),
    firstPublishYear = "1996",
    averageRating = 4.1,
    ratingCount = 1000,
    numPages = 69,
    numEditions = 1
)
