package app.jjerrell.lib.pocket

import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Basic information and a number of utilities that require (or benefit from) having platform
 * specific implementations.
 */
expect class Platform() {
    val platform: String
    val applicationDispatcher: CoroutineDispatcher
    val httpClient: HttpClient

    companion object {
        fun newUUID(): String
    }
}