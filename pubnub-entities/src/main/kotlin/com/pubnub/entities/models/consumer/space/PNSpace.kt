package com.pubnub.entities.models.consumer.space

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata

data class PNSpace(
    val id: String,
    val name: String?,
    val description: String?,
    val custom: Any?,
    val updated: String?,
    val eTag: String?
)

internal fun PNChannelMetadata.toPNSpace(): PNSpace {
    return PNSpace(
        id = id,
        name = name,
        description = description,
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}