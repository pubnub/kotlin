package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent
import com.pubnub.api.utils.PatchValue
import com.pubnub.kmp.CustomObject

data class PNChannelMembership(
    val channel: PNChannelMetadata,
    val custom: PatchValue<Map<String, Any?>?>? = null,
    val updated: String,
    val eTag: String,
    val status: PatchValue<String?>? = null,
) {
    data class Partial(
        val channelId: String,
        override val custom: CustomObject? = null,
        override val status: String? = null,
    ) : ChannelMembershipInput {
        override val channel: String = channelId
    }

    // let's not make this public for now, but keep the implementation around in case it's needed
    private operator fun plus(update: PNChannelMembership): PNChannelMembership {
        return PNChannelMembership(
            channel + update.channel,
            update.custom ?: custom,
            update.updated,
            update.eTag,
            update.status ?: status
        )
    }

    /**
     * Merge information from this `PNChannelMembership` with new data from `update`, returning a new `PNChannelMembership` instance.
     */
    operator fun plus(update: PNSetMembershipEvent): PNChannelMembership {
        return PNChannelMembership(
            channel,
            update.custom ?: custom,
            update.updated,
            update.eTag,
            update.status ?: status
        )
    }
}
