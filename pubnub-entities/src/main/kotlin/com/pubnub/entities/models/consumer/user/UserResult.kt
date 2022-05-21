package com.pubnub.entities.models.consumer.user

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult

data class UserResult(
    val status: Int,
    val data: User?
)

internal fun PNUUIDMetadataResult.toUserResult(): UserResult? {
    if (this == null) {
        return null
    }
    return UserResult(status = status, data = data?.toUser())
}
