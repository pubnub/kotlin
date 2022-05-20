package com.pubnub.entities.models.consumer.space

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult

data class SpaceResult(
    val status: Int,
    val data: Space?
)

internal fun PNChannelMetadataResult?.toPNSpaceResult(): SpaceResult? {
    if (this == null) {
        return null
    }

    return SpaceResult(status = status, data = data?.toPNSpace())
}
