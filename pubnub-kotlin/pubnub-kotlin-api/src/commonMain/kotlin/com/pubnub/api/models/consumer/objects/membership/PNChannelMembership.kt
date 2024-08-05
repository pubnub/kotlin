package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.channel.PartialPNChannelMetadata
import com.pubnub.kmp.CustomObject

data class PNChannelMembership(
    val channel: PartialPNChannelMetadata,
    val custom: Map<String, Any?>?,
    val updated: String,
    val eTag: String,
    val status: String?,
) {
    data class Partial(
        val channelId: String,
        override val custom: CustomObject? = null,
        override val status: String? = null,
    ) : ChannelMembershipInput {
        override val channel: String = channelId
    }
}
