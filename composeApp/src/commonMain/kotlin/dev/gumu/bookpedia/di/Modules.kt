package dev.gumu.bookpedia.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.gumu.bookpedia.book.data.database.BookDatabase
import dev.gumu.bookpedia.book.data.database.DatabaseFactory
import dev.gumu.bookpedia.book.data.network.KtorRemoteBookDataSource
import dev.gumu.bookpedia.book.data.network.RemoteBookDataSource
import dev.gumu.bookpedia.book.data.repository.DefaultBookRepository
import dev.gumu.bookpedia.book.domain.repository.BookRepository
import dev.gumu.bookpedia.book.presentation.bookdetail.BookDetailViewModel
import dev.gumu.bookpedia.book.presentation.booklist.BookListViewModel
import dev.gumu.bookpedia.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()

    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<BookDatabase>().favoriteBookDao }

    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
}
