package dev.gumu.bookpedia.book.presentation.booklist.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bookpedia.composeapp.generated.resources.Res
import bookpedia.composeapp.generated.resources.book_load_error
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import dev.gumu.bookpedia.core.presentation.component.PulseAnimation
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
        val painter = rememberAsyncImagePainter(
            model = url,
            error = painterResource(Res.drawable.book_load_error),
        )
        val painterState by painter.state.collectAsStateWithLifecycle()
        val transition by animateFloatAsState(
            targetValue = if (painterState is AsyncImagePainter.State.Loading) 0f else 1f,
            animationSpec = tween(1000)
        )
        AnimatedContent(targetState = painterState) { result ->
            val isSuccess = result is AsyncImagePainter.State.Success
            when (result) {
                is AsyncImagePainter.State.Loading -> PulseAnimation(modifier = Modifier.size(60.dp))
                else -> {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = if (isSuccess) ContentScale.Crop else ContentScale.Fit,
                        modifier = Modifier
                            .aspectRatio(ratio = 0.65f, matchHeightConstraintsFirst = true)
                            .graphicsLayer {
                                rotationX = (1f - transition) * 45f // rotate by 45 degrees
                                // start at 80% then add the remaining 20% with the transition
                                val scale = 0.8f + (0.2f * transition)
                                scaleX = scale
                                scaleY = scale
                            }
                    )
                }
            }
        }
    }
}
