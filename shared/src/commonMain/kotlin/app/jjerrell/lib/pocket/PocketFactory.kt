package app.jjerrell.lib.pocket

import app.jjerrell.lib.pocket.repo.BaseRepository
import app.jjerrell.lib.pocket.service.ApiLevel
import app.jjerrell.lib.pocket.service.AppApi
import app.jjerrell.lib.pocket.service.Dto
import app.jjerrell.lib.pocket.service.Route
import kotlinx.coroutines.CoroutineDispatcher

interface ApplicationData {
    val id: String
    val name: String
    val launchId: String
    val apiHost: String
}

interface UserData {
    val id: String
    val name: String
    val sessionId: String
}

class PocketFactory(
    private val appData: ApplicationData,
    private val userData: UserData
) {

    fun getSampleRepo(level: ApiLevel): SampleRepository {
        val api: AppApi<SampleUserDto> = AppApi
            .build(
                platform.httpClient,
                appData.apiHost,
                SampleRoute.USER,
                level
            )
        return SampleRepository(api, platform.applicationDispatcher)
    }

    companion object {
        val platform: Platform = Platform()
    }
}