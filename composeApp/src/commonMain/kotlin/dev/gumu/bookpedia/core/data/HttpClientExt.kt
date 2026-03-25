package dev.gumu.bookpedia.core.data

import dev.gumu.bookpedia.core.domain.BookpediaResult
import dev.gumu.bookpedia.core.domain.DataError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): BookpediaResult<T, DataError.Remote> {
    return try {
        responseToResult(execute())
    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        when (e) {
            is SocketTimeoutException -> BookpediaResult.Error(DataError.Remote.RequestTimeout)
            is UnresolvedAddressException -> BookpediaResult.Error(DataError.Remote.NoNetwork)
            else -> {
                e.printStackTrace()
                BookpediaResult.Error(DataError.Remote.Unknown)
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): BookpediaResult<T, DataError.Remote> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                BookpediaResult.Success(response.body<T>())
            } catch (_: NoTransformationFoundException) {
                BookpediaResult.Error(DataError.Remote.Serialization)
            } catch (_: MissingFieldException) {
                BookpediaResult.Error(DataError.Remote.Serialization)
            }
        }
        408 -> BookpediaResult.Error(DataError.Remote.RequestTimeout)
        429 -> BookpediaResult.Error(DataError.Remote.TooManyRequests)
        in 500..599 -> BookpediaResult.Error(DataError.Remote.Server)
        else -> BookpediaResult.Error(DataError.Remote.Unknown)
    }
}
