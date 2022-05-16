package com.pubnub.entities.models.consumer.user

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult

data class PNUserResult(
    val status: Int,
    val data: PNUser?
)

internal fun PNUUIDMetadataResult.toPNUserResult(): PNUserResult? {
    if (this == null) {
        return null
    }
    return PNUserResult(status = status, data = data?.toPNUser())
}