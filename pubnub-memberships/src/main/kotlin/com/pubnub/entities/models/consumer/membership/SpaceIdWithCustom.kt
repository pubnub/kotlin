package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.membership.PNChannelWithCustom

data class SpaceIdWithCustom(
    val spaceId: String,
    val custom: Map<String, Any>? = null
)

internal fun List<SpaceIdWithCustom>.toPNChannelWithCustomList(): List<PNChannelWithCustom> {
    return map { spaceIdWithCustom -> spaceIdWithCustom.toPNChannelWithCustom() }
}

internal fun SpaceIdWithCustom.toPNChannelWithCustom(): PNChannelWithCustom {
    return PNChannelWithCustom(channel = spaceId, custom = custom)
}
