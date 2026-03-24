package dev.gumu.bookpedia

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform