package app.jjerrell.lib.pocket

import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher

expect class Platform() {
    val platform: String
    val applicationDispatcher: CoroutineDispatcher
    val httpClient: HttpClient

    companion object {
        fun newUUID(): String
    }
}