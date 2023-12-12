package com.pubnub.internal.v2.entities

import com.pubnub.api.PubNub
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.v2.subscription.SubscriptionImpl

internal class ChannelMetadataImpl(internal val pubnub: PubNub, val channelName: ChannelName) : ChannelMetadata {

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
