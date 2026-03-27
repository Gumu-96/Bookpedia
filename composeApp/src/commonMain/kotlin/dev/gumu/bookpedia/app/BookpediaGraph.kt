package dev.gumu.bookpedia.app

import dev.gumu.bookpedia.book.domain.Book
import kotlinx.serialization.Serializable

sealed interface Route

sealed interface BookGraph : Route {
    @Serializable
    data object Graph : BookGraph // Entry point for the book feature graph

    @Serializable
    data object BookList : BookGraph

    @Serializable
    data class BookDetails(val book: Book) : BookGraph
}
