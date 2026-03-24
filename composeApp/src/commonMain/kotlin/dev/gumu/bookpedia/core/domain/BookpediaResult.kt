package dev.gumu.bookpedia.core.domain

sealed interface BookpediaResult<out T, out E : BaseError> {
    data class Success<out T>(val data: T) : BookpediaResult<T, Nothing>
    data class Error<out E : BaseError>(val error: E) : BookpediaResult<Nothing, E>
}

inline fun <T, E : BaseError> BookpediaResult<T, E>.onSuccess(action: (T) -> Unit): BookpediaResult<T, E> {
    if (this is BookpediaResult.Success) action(data)
    return this
}

inline fun <T, E : BaseError> BookpediaResult<T, E>.onError(action: (E) -> Unit): BookpediaResult<T, E> {
    if (this is BookpediaResult.Error) action(error)
    return this
}
