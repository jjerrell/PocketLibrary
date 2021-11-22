package app.jjerrell.lib.pocket

import app.jjerrell.lib.pocket.repo.BaseRepository
import app.jjerrell.lib.pocket.service.AppApi
import app.jjerrell.lib.pocket.service.Dto
import app.jjerrell.lib.pocket.service.Route
import kotlinx.coroutines.CoroutineDispatcher

enum class SampleRoute(override val path: String): Route {
    USER("User");
}

class SampleRepository internal constructor(
    apiLevel: AppApi<SampleUserDto>,
    appDispatcher: CoroutineDispatcher,
    objectMapper: (SampleUserDto) -> SampleUserDto = { it }
) : BaseRepository<SampleUserDto, SampleUserDto>(apiLevel, objectMapper, appDispatcher)

data class SampleUserDto(
    override val id: String,
    override val name: String,
    override val createdDateTime: Long,
    override val createdByUserId: String,
    override val lastModifiedDateTime: Long?,
    override val lastModifiedByUserId: String?
) : Dto