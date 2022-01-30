package app.jjerrell.lib.pocket.service

/**
 * Establishes requirements for a standard data transfer object.
 * Date/time properties are expected to be a measure of milliseconds since the epoch date.
 */
interface Dto {
    val id: String
    val name: String
    val createdDateTime: Long
    val createdByUserId: String
    val lastModifiedDateTime: Long?
    val lastModifiedByUserId: String?
}
