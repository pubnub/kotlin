package com.pubnub.internal.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.utils.PatchValue

data class PNChannelMembership(
    val channel: PNChannelMetadata,
    val custom: PatchValue<Map<String, Any?>?>? = null,
    val updated: String,
    val eTag: String,
    val status: PatchValue<String?>? = null,
) {
    data class Partial(
        val channelId: String,
        override val custom: Any? = null,
        override val status: String? = null,
    ) : ChannelMembershipInput {
        override val channel: String = channelId
    }
}
