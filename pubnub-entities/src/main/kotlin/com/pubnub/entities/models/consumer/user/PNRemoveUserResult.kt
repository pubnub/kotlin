package com.pubnub.entities.models.consumer.user

import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

data class PNRemoveUserResult(
    val status: Int
)

internal fun PNRemoveMetadataResult.toPNRemoveUserResult(): PNRemoveUserResult {
    return this?.let { PNRemoveUserResult(status = it.status) }
}