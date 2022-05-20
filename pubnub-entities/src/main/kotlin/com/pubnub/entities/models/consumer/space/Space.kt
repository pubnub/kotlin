package com.pubnub.entities.models.consumer.space

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata

data class Space(
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val custom: Any? = null,
    val updated: String? = null,
    val eTag: String? = null
)

internal fun PNChannelMetadata.toPNSpace(): Space {
    return Space(
        id = id,
        name = name,
        description = description,
        custom = custom,
        updated = updated,
        eTag = eTag
    )
}
