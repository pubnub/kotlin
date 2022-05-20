package com.pubnub.entities.models.consumer.user

import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

data class RemoveUserResult(
    val status: Int
)

internal fun PNRemoveMetadataResult.toPNRemoveUserResult(): RemoveUserResult {
    return RemoveUserResult(status = this.status)
}
