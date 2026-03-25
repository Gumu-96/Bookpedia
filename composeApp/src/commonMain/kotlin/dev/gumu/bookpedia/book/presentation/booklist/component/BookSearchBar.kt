package dev.gumu.bookpedia.book.presentation.booklist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import bookpedia.composeapp.generated.resources.Res
import bookpedia.composeapp.generated.resources.search_hint
import dev.gumu.bookpedia.core.presentation.DarkBlue
import dev.gumu.bookpedia.core.presentation.SandYellow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.input.ImeAction
import bookpedia.composeapp.generated.resources.clear_hint
import dev.gumu.bookpedia.core.presentation.DesertWhite
import org.jetbrains.compose.resources.stringResource

@Composable
fun BookSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onImeSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = SandYellow,
            backgroundColor = SandYellow
        )
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = DarkBlue,
                focusedBorderColor = SandYellow,
            ),
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(Res.string.search_hint)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onImeSearch() }
            ),
            trailingIcon = if (query.isNotBlank()) {
                {
                    IconButton(
                        onClick = onClearClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(Res.string.clear_hint),
                        )
                    }
                }
            } else null,
            modifier = modifier.background(
                color = DesertWhite,
                shape = CircleShape
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookSearchBarPreview() {
    MaterialTheme {
        BookSearchBar(
            query = "Search query",
            onQueryChange = {},
            onClearClick = {},
            onImeSearch = {}
        )
    }
}
