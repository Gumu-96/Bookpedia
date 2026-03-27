package dev.gumu.bookpedia.app

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSCharacterSet
import platform.Foundation.NSString
import platform.Foundation.URLQueryAllowedCharacterSet
import platform.Foundation.create
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
import platform.Foundation.stringByRemovingPercentEncoding

@OptIn(BetaInteropApi::class)
actual fun String.uriEncode(): String {
    return NSString.create(this).stringByAddingPercentEncodingWithAllowedCharacters(
        NSCharacterSet.URLQueryAllowedCharacterSet
    ) ?: this
}

@OptIn(BetaInteropApi::class)
actual fun String.uriDecode(): String = NSString.create(this).stringByRemovingPercentEncoding ?: this
