package com.pubnub.entities.models.consumer.space

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult

data class PNSpaceResult(
    val status: Int,
    val data: PNSpace?
)

internal fun PNChannelMetadataResult?.toPNSpaceResult(): PNSpaceResult? {
    if (this == null) {
        return null
    }

    return PNSpaceResult(status = status, data = data?.toPNSpace())
}
