package com.pubnub.internal.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.channel.NewPNChannelMetadata

data class PNChannelMembership(
    val channel: NewPNChannelMetadata,
    val custom: Map<String, Any?>?,
    val updated: String,
    val eTag: String,
    val status: String?,
) {
    data class Partial(
        val channelId: String,
        override val custom: Any? = null,
        override val status: String? = null,
    ) : ChannelMembershipInput {
        override val channel: String = channelId
    }
}
