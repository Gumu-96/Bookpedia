package dev.gumu.bookpedia.core.domain

typealias EmptyResult<E> = BookpediaResult<Unit, E>

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

inline fun <T, E: BaseError, R> BookpediaResult<T, E>.map(map: (T) -> R): BookpediaResult<R, E> {
    return when(this) {
        is BookpediaResult.Error -> this
        is BookpediaResult.Success -> BookpediaResult.Success(map(data))
    }
}

fun <T, E: BaseError> BookpediaResult<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}
