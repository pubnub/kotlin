package com.pubnub.entities.models.consumer.space

import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

data class RemoveSpaceResult(
    val status: Int
)

internal fun PNRemoveMetadataResult?.toRemoveSpaceResult(): RemoveSpaceResult? {
    return this?.let { RemoveSpaceResult(status = it.status) }
}
