package app.jjerrell.lib.pocket.service

interface Dto {
    val id: String
    val name: String
    val createdDateTime: Long
    val createdByUserId: String
    val lastModifiedDateTime: Long?
    val lastModifiedByUserId: String?
}
