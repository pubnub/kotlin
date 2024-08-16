package com.pubnub.internal.java.v2.entities

import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.java.v2.subscription.SubscriptionImpl
import com.pubnub.internal.v2.entities.ChannelGroupImpl
import com.pubnub.internal.v2.entities.ChannelGroupName

class ChannelGroupImpl(pubnub: PubNubForJavaImpl, channelGroupName: ChannelGroupName) :
    ChannelGroupImpl(pubnub, channelGroupName),
    com.pubnub.api.java.v2.entities.ChannelGroup {
    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        return SubscriptionImpl.from(super<ChannelGroupImpl>.subscription(options))
    }

    override fun subscription(): com.pubnub.api.java.v2.subscriptions.Subscription {
        return subscription(EmptyOptions)
    }
}
