package com.pubnub.internal.v2.entities

import com.pubnub.api.PubNub
import com.pubnub.api.v2.entities.UuidMetadata
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.v2.subscription.SubscriptionImpl

internal class UuidMetadataImpl(internal val pubnub: PubNub, val channelName: ChannelName) : UuidMetadata {

    override val id: String = channelName.id

    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        val channels = setOf(channelName)
        return SubscriptionImpl(
            pubnub,
            channels = channels,
            options = options.filter { result ->
                // simple channel name or presence channel
                channels.any { it.id == result.channel }
            }
        )
    }
}
