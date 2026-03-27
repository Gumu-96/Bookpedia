package dev.gumu.bookpedia.app

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import dev.gumu.bookpedia.book.domain.Book
import kotlinx.serialization.json.Json

object BookpediaNavType {
    val BookType = object : NavType<Book>(isNullableAllowed = false) {
        override fun put(bundle: SavedState, key: String, value: Book) {
            bundle.write {
                putString(key, Json.encodeToString(Book.serializer(), value))
            }
        }

        override fun get(bundle: SavedState, key: String): Book? {
            return Json.decodeFromString(
                deserializer = Book.serializer(),
                string = bundle.read { runCatching { getString(key) }.getOrNull() } ?: return null
            )
        }

        override fun parseValue(value: String): Book {
            return Json.decodeFromString(Book.serializer(), value.uriDecode())
        }

        override fun serializeAsValue(value: Book): String {
            return Json.encodeToString(Book.serializer(), value).uriEncode()
        }
    }
}
