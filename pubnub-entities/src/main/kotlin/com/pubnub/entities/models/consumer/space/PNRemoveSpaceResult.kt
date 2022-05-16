package com.pubnub.entities.models.consumer.space

import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

data class PNRemoveSpaceResult(
    val status: Int
)

internal fun PNRemoveMetadataResult?.toPNRemoveSpaceResult(): PNRemoveSpaceResult? {
    return this?.let { PNRemoveSpaceResult(status = it.status) }
}