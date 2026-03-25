package dev.gumu.bookpedia.core.domain

sealed interface DataError : BaseError {
    enum class Remote : DataError {
        RequestTimeout,
        TooManyRequests,
        NoNetwork,
        Server,
        Serialization,
        Unknown
    }

    enum class Local : DataError {
        Database,
        Unknown
    }
}
