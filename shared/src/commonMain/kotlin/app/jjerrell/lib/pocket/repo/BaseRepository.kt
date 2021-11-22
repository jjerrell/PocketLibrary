package app.jjerrell.lib.pocket.repo

import app.jjerrell.lib.pocket.service.AppApi
import app.jjerrell.lib.pocket.service.Dto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Provides basic CRUD operations that can be used directly. Inheritors are
 * encouraged to make use of the provided methods to extend repo functionality.
 *
 * @param api The API abstraction for this repo.
 *  regarding which methods will be available.
 * @param mapper Interface for providing DTO->Business Model mapping logic
 * @param dispatcher The coroutine dispatcher responsible for handling requests asynchronously
 *
 * @see [AppApi]
 */
abstract class BaseRepository<S: Dto, T> protected constructor(
    protected val api: AppApi<S>,
    protected val mapper: (S) -> T,
    protected val dispatcher: CoroutineDispatcher
) {
    fun get(
        id: String,
        onSuccess: (T) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        CoroutineScope(dispatcher).launch {
            val result = api.getById(id)
            handleResult(result, { onSuccess(mapper(it)) }, onFailure)
        }
    }

    fun getList(
        ids: List<String>,
        onSuccess: (List<T>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        CoroutineScope(dispatcher).launch {
            val result = api.getAllByIds(ids)
            handleResult(result, { onSuccess(it.map { obj -> mapper(obj) }) }, onFailure)
        }
    }

    fun create(
        obj: S,
        onSuccess: (T) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        CoroutineScope(dispatcher).launch {
            val result = api.create(obj)
            handleResult(result, { onSuccess(mapper(obj)) }, onFailure)
        }
    }

    fun update(
        obj: S,
        onSuccess: (T) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        CoroutineScope(dispatcher).launch {
            val result = api.update(obj)
            handleResult(result, { onSuccess(mapper(obj)) }, onFailure)
        }
    }

    fun delete(
        id: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        CoroutineScope(dispatcher).launch {
            val result = api.deleteById(id)
            handleResult(result, onSuccess, onFailure)
        }
    }

    protected fun <T> handleResult(
        result: Result<T>,
        succeeded: (T) -> Unit,
        failed: (Throwable) -> Unit
    ) {
        result.fold(
            onSuccess = succeeded,
            onFailure = failed
        )
    }
}