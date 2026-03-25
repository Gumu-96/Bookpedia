package dev.gumu.bookpedia.book.presentation.booklist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import bookpedia.composeapp.generated.resources.Res
import bookpedia.composeapp.generated.resources.book_load_error
import coil3.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.painterResource

@Composable
fun BookImage(
    url: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        var loadResult by remember { mutableStateOf<Result<Painter>?>(null) }
        val painter = rememberAsyncImagePainter(
            model = url,
            onSuccess = {
                loadResult = if (it.painter.intrinsicSize.width > 1 && it.painter.intrinsicSize.height > 1) {
                    Result.success(it.painter)
                } else {
                    Result.failure(Exception("Invalid image size"))
                }
            },
            onError = {
                it.result.throwable.printStackTrace()
                loadResult = Result.failure(it.result.throwable)
            }
        )
        when (val result = loadResult) {
            null -> {
                CircularProgressIndicator()
            }
            else -> {
                Image(
                    painter = if (result.isSuccess) painter else painterResource(Res.drawable.book_load_error),
                    contentDescription = null,
                    contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit,
                    modifier = Modifier.aspectRatio(ratio = 0.65f, matchHeightConstraintsFirst = true)
                )
            }
        }
    }
}
