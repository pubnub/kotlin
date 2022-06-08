package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership

internal fun List<Membership.Partial>.toPNChannelWithCustomList(): List<PNChannelMembership.Partial> {
    return map { it.toPNChannelWithCustom() }
}

internal fun Membership.Partial.toPNChannelWithCustom(): PNChannelMembership.Partial {
    return PNChannelMembership.Partial(channelId = spaceId!!, custom = custom)
}
