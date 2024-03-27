package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata

data class PNChannelMembership(
    val channel: PNChannelMetadata?,
    val custom: Any?,
    val updated: String,
    val eTag: String,
    val status: String?,
) {
    companion object {
        fun from(data: com.pubnub.internal.models.consumer.objects.membership.PNChannelMembership): PNChannelMembership {
            return PNChannelMembership(
                data.channel,
                data.custom,
                data.updated,
                data.eTag,
                data.status,
            )
        }
    }

    data class Partial(
        val channelId: String,
        override val custom: Any? = null,
        override val status: String? = null,
    ) : ChannelMembershipInput {
        override val channel: String = channelId
    }
}
