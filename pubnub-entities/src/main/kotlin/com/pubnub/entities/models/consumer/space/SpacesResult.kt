package com.pubnub.entities.models.consumer.space

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult

data class SpacesResult(
    val status: Int,
    val data: Collection<Space>,
    val totalCount: Int?,
    val next: PNPage?,
    val prev: PNPage?
)

internal fun PNChannelMetadataArrayResult?.toPNSpaceArrayResult(): SpacesResult? {
    if (this == null) {
        return null
    }

    val pnSpaceList = data.map { pnChannelMetadata ->
        pnChannelMetadata.toPNSpace()
    }

    return SpacesResult(status = status, data = pnSpaceList, totalCount = totalCount, next = next, prev = prev)
}
