package dev.gumu.bookpedia.app

import android.net.Uri

actual fun String.uriEncode(): String = Uri.encode(this)
actual fun String.uriDecode(): String = Uri.decode(this)
