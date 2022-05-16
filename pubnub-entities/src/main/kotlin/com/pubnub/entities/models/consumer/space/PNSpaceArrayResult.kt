package com.pubnub.entities.models.consumer.space

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult

data class PNSpaceArrayResult(
    val status: Int,
    val data: Collection<PNSpace>,
    val totalCount: Int?,
    val next: PNPage?,
    val prev: PNPage?
)

internal fun PNChannelMetadataArrayResult?.toPNSpaceArrayResult(): PNSpaceArrayResult? {
    if (this == null) {
        return null
    }

    val pnSpaceList = data.map { pnChannelMetadata ->
        pnChannelMetadata.toPNSpace()
    }

    return PNSpaceArrayResult(status = status, data = pnSpaceList, totalCount = totalCount, next = next, prev = prev)
}
