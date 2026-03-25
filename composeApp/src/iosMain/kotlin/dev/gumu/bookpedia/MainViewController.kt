package dev.gumu.bookpedia

import androidx.compose.ui.window.ComposeUIViewController
import dev.gumu.bookpedia.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    App()
}
