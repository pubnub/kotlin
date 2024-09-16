package com.pubnub.internal.java.v2.entities

import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.java.v2.subscription.SubscriptionImpl
import com.pubnub.internal.v2.entities.ChannelName
import com.pubnub.internal.v2.entities.UserMetadataImpl

class UserMetadataImpl(pubnub: PubNubForJavaImpl, channelName: ChannelName) :
    UserMetadataImpl(pubnub, channelName),
    com.pubnub.api.java.v2.entities.UserMetadata {
    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        return SubscriptionImpl.from(super<UserMetadataImpl>.subscription(options))
    }

    override fun subscription(): com.pubnub.api.java.v2.subscriptions.Subscription {
        return subscription(EmptyOptions)
    }
}
