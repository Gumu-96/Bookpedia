package dev.gumu.bookpedia.core.presentation

import bookpedia.composeapp.generated.resources.Res
import bookpedia.composeapp.generated.resources.error_disk_full
import bookpedia.composeapp.generated.resources.error_no_network
import bookpedia.composeapp.generated.resources.error_serialization
import bookpedia.composeapp.generated.resources.error_timeout
import bookpedia.composeapp.generated.resources.error_too_many_requests
import bookpedia.composeapp.generated.resources.error_unknown
import dev.gumu.bookpedia.core.domain.DataError

fun DataError.toUiText(): UiText {
    val stringRes = when (this) {
        DataError.Local.Database -> Res.string.error_disk_full
        DataError.Local.Unknown -> Res.string.error_unknown
        DataError.Remote.RequestTimeout -> Res.string.error_timeout
        DataError.Remote.TooManyRequests -> Res.string.error_too_many_requests
        DataError.Remote.NoNetwork -> Res.string.error_no_network
        DataError.Remote.Server -> Res.string.error_unknown
        DataError.Remote.Serialization -> Res.string.error_serialization
        DataError.Remote.Unknown -> Res.string.error_unknown
    }
    return UiText.StringResourceId(stringRes)
}
