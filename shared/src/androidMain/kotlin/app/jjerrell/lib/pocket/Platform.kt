package app.jjerrell.lib.pocket

import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.*

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    actual val applicationDispatcher: CoroutineDispatcher = Dispatchers.Main
    actual val httpClient = HttpClient()

    actual companion object {
        actual fun newUUID(): String = UUID.randomUUID().toString()
    }
}