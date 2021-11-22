package app.jjerrell.lib.pocket.service

import io.ktor.client.*
import io.ktor.client.request.*

enum class ApiLevel {
    READ,
    CREATE,
    UPDATE,
    DELETE
}

interface Route {
    val path: String
}

sealed interface AppApi<S: Dto> {
    val client: HttpClient
    val baseApiUrl: String
    val path: String

    suspend fun getById(id: String): Result<S>
    suspend fun getAllByIds(ids: List<String>): Result<List<S>>
    suspend fun create(obj: S): Result<S> = throw NotImplementedError()
    suspend fun update(obj: S): Result<S> = throw NotImplementedError()
    suspend fun deleteById(id: String): Result<Boolean> = throw NotImplementedError()
    suspend fun deleteObject(obj: S): Result<Boolean> = throw NotImplementedError()

    /**
     * The Base API class which provides Read-only access to a domain model
     *
     * @param client The HttpClient used for requests in this domain
     * @param baseApiUrl The Base URL for the API in use. i.e. http://someapi.com/api/v2
     * @param path The route to an endpoint supporting basic CRUD for an object model
     *
     * @see AppApi
     */
    open class ReadonlyApi<T: Dto> internal constructor(
        override val client: HttpClient,
        override val baseApiUrl: String,
        override val path: String
    ) : AppApi<T> {
        override suspend fun getById(id: String): Result<T> {
            return client.get("${baseApiUrl}/$path") {
                parameter("id", id)
            }
        }

        override suspend fun getAllByIds(ids: List<String>): Result<List<T>> {
            return client.post("${baseApiUrl}/$path") {
                body = ids
            }
        }
    }

    /**
     * Extends [ReadonlyApi] to allow creation of the domain model
     *
     * @param client The HttpClient used for requests in this domain
     * @param baseApiUrl The Base URL for the API in use. i.e. http://someapi.com/api/v2
     * @param path The route to an endpoint supporting basic CRUD for an object model
     *
     * @see AppApi
     */
    open class CreateReadApi<T: Dto> internal constructor(
        client: HttpClient,
        baseApiUrl: String,
        path: String
    ) : ReadonlyApi<T>(client, baseApiUrl, path) {
        override suspend fun create(obj: T): Result<T> {
            return client.post("${baseApiUrl}/$path") {
                body = obj
            }
        }
    }

    /**
     * Extends [CreateReadApi] to allow modification of the domain model
     *
     * @param client The HttpClient used for requests in this domain
     * @param baseApiUrl The Base URL for the API in use. i.e. http://someapi.com/api/v2
     * @param path The route to an endpoint supporting basic CRUD for an object model
     *
     * @see AppApi
     */
    open class CreateReadUpdateApi<T: Dto> internal constructor(
        client: HttpClient,
        baseApiUrl: String,
        path: String
    ) : CreateReadApi<T>(client, baseApiUrl, path) {
        override suspend fun update(obj: T): Result<T> {
            return client.put("${baseApiUrl}/$path") {
                body = obj
            }
        }
    }

    /**
     * Extends [CreateReadUpdateApi] to provide complete CRUD capabilities to the model
     *
     * @param client The HttpClient used for requests in this domain
     * @param baseApiUrl The Base URL for the API in use. i.e. http://someapi.com/api/v2
     * @param route The route to an endpoint supporting basic CRUD for an object model
     *
     * @see AppApi
     */
    open class CreateReadUpdateDeleteApi<T: Dto> internal constructor(
        client: HttpClient,
        baseApiUrl: String,
        route: String
    ) : CreateReadUpdateApi<T>(client, baseApiUrl, route) {
        override suspend fun deleteById(id: String): Result<Boolean> {
            return client.delete("${baseApiUrl}/$path") {
                parameter("id", id)
            }
        }

        override suspend fun deleteObject(obj: T): Result<Boolean> {
            return client.delete("${baseApiUrl}/$path") {
                body = obj
            }
        }
    }

    companion object {
        fun <T: Dto> build(client: HttpClient,
                           baseApiUrl: String,
                           route: Route,
                           level: ApiLevel
        ): AppApi<T> {
            return when (level) {
                ApiLevel.READ -> ReadonlyApi(client, baseApiUrl, route.path)
                ApiLevel.CREATE -> CreateReadApi(client, baseApiUrl, route.path)
                ApiLevel.UPDATE -> CreateReadUpdateApi(client, baseApiUrl, route.path)
                ApiLevel.DELETE -> CreateReadUpdateDeleteApi(client, baseApiUrl, route.path)
            }
        }
    }
}
