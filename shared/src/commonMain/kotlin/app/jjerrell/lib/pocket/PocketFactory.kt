package app.jjerrell.lib.pocket

import app.jjerrell.lib.pocket.service.ApiLevel
import app.jjerrell.lib.pocket.service.AppApi
import kotlin.native.concurrent.SharedImmutable

/** Data unique to the application. */
interface ApplicationData {
    /** The unique identifier for this application */
    val id: String
    /** The name of this application */
    val name: String
    /** The unique identifier that was generated when the application was launched */
    val launchId: String
    /** The base url that the application uses to fetch data */
    val apiHost: String
}

/** Data unique to this user session. */
interface UserData {
    /** The users unique identifier */
    val id: String
    /** The users name or username */
    val name: String
    /** A unique identifier to represent this user session */
    val sessionId: String
}

/**
 * The main entry point for this library. Anything (service, repo, etc) that the application needs
 * access to should -- in most cases -- be implemented here or retrieved from here.
 */
class PocketFactory(
    private val appData: ApplicationData,
    private val userData: UserData
) {

    /**
     * This is a simple example of how we could expose network calls to the application using the
     * repository pattern. The application need only supply the level of access it needs (how much
     * CRUD the API implements).
     */
    fun getSampleRepo(level: ApiLevel): SampleRepository {
        val api: AppApi<SampleUserDto> = AppApi
            .build(
                PLATFORM.httpClient,
                appData.apiHost,
                SampleRoute.USER,
                level
            )
        return SampleRepository(api, PLATFORM.applicationDispatcher)
    }

    companion object {
        /**
         * This is a utility class which provides basic information about the host device and a few
         * tools that require OS specific implementations (Generating a new UUID, for instance).
         */
        val PLATFORM: Platform = Platform()
    }
}