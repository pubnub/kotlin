package com.pubnub.user.models.consumer

import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

data class RemoveUserResult(
    val status: Int
)

internal fun PNRemoveMetadataResult.toRemoveUserResult(): RemoveUserResult {
    return RemoveUserResult(status = this.status)
}
