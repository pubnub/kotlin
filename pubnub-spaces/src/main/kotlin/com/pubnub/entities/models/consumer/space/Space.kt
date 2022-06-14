package com.pubnub.entities.models.consumer.space

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata

data class Space(
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val custom: Map<String, Any>? = null,
    val updated: String? = null,
    val eTag: String? = null,
    val type: String? = null,
    val status: String? = null
)

fun PNChannelMetadata.toSpace(): Space {
    return Space(
        id = id,
        name = name,
        description = description,
        custom = custom as Map<String, Any>?,
        updated = updated,
        eTag = eTag,
        type = type,
        status = status
    )
}
