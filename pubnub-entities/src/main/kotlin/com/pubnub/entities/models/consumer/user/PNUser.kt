package com.pubnub.entities.models.consumer.user

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata

data class PNUser(
    val id: String,
    val name: String?,
    val externalId: String?,
    val profileUrl: String?,
    val email: String?,
    val custom: Any?,
    val updated: String?,
    val eTag: String?
)

internal fun PNUUIDMetadata.toPNUser(): PNUser {
    return PNUser(
        id = id,
        name = name,
        externalId = externalId,
        profileUrl = profileUrl,
        email = email,
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}