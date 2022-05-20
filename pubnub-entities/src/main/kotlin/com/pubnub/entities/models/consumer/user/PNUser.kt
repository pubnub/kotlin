package com.pubnub.entities.models.consumer.user

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata

data class PNUser(
    val id: String,
    val name: String? = null,
    val externalId: String? = null,
    val profileUrl: String? = null,
    val email: String? = null,
    val custom: Any? = null,
    val updated: String? = null,
    val eTag: String? = null
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
