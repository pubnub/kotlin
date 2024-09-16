package com.pubnub.internal.java.v2.entities

import com.pubnub.api.java.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.java.v2.subscription.SubscriptionImpl
import com.pubnub.internal.v2.entities.ChannelName

class ChannelMetadataImpl(pubnub: PubNubForJavaImpl, channelName: ChannelName) :
    com.pubnub.internal.v2.entities.ChannelMetadataImpl(pubnub, channelName),
    com.pubnub.api.java.v2.entities.ChannelMetadata {
    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        return SubscriptionImpl.from(super<com.pubnub.internal.v2.entities.ChannelMetadataImpl>.subscription(options))
    }

    override fun subscription(): Subscription {
        return subscription(EmptyOptions)
    }
}
