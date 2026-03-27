package dev.gumu.bookpedia.book.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.gumu.bookpedia.book.data.database.entity.BookEntity
import dev.gumu.bookpedia.book.data.database.entity.StringListTypeConverter

@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListTypeConverter::class)
@ConstructedBy(BookDatabaseConstructor::class)
abstract class BookDatabase : RoomDatabase() {
    abstract val favoriteBookDao: FavoriteBookDao

    companion object {
        const val DB_NAME = "book.db"
    }
}
