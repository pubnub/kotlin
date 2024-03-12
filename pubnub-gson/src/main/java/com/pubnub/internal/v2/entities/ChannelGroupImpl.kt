package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.subscription.SubscriptionImpl

class ChannelGroupImpl(pubnub: PubNubImpl, channelGroupName: String) :
    BaseChannelGroupImpl<EventListener, Subscription>(
        pubnub.pubNubCore,
        ChannelGroupName(channelGroupName),
        { channels, channelGroups, options -> SubscriptionImpl(pubnub, channels, channelGroups, options) },
    ),
    ChannelGroup {
    override fun subscription(): Subscription {
        return super.subscription(EmptyOptions)
    }
}
