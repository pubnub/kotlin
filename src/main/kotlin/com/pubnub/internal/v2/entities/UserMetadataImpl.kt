package com.pubnub.internal.v2.entities

import com.pubnub.api.PubNub
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.v2.subscription.SubscriptionImpl

internal class UserMetadataImpl(internal val pubnub: PubNub, val channelName: ChannelName) : UserMetadata {

    override val id: String = channelName.id

    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        val channels = setOf(channelName)
        return SubscriptionImpl(
            pubnub,
            channels = channels,
            options = SubscriptionOptions.filter { result ->
                // simple channel name or presence channel
                channels.any { it.id == result.channel }
            } + options
        )
    }
}
