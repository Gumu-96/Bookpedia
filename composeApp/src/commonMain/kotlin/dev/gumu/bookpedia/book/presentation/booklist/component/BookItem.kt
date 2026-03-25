package dev.gumu.bookpedia.book.presentation.booklist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.gumu.bookpedia.book.domain.Book
import dev.gumu.bookpedia.core.presentation.LightBlue
import dev.gumu.bookpedia.core.presentation.SandYellow
import kotlin.math.round

@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightBlue,
            contentColor = Color.Black
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(IntrinsicSize.Min)
        ) {
            BookImage(
                url = book.imageUrl,
                modifier = Modifier.height(100.dp)
            )
            BookInfo(
                book = book,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun BookInfo(
    book: Book,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp, alignment = Alignment.CenterVertically),
        modifier = modifier
    ) {
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (book.authors.isNotEmpty()) {
            Text(
                text = book.authors.joinToString(", "),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        book.averageRating?.let {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${round(it * 10) / 10.0}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = SandYellow

                )
            }
        }
    }
}

@Preview
@Composable
private fun BookItemPreview() {
    MaterialTheme {
        BookItem(
            book = defaultBook(),
            onClick = {}
        )
    }
}

private fun defaultBook() = Book(
    id = "1",
    title = "Android dev for dummies",
    imageUrl = "",
    authors = listOf("dev", "gumu"),
    description = null,
    languages = emptyList(),
    firstPublishYear = null,
    averageRating = 4.1,
    ratingCount = 1000,
    numPages = null,
    numEditions = 1
)
