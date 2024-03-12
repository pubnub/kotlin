package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.subscription.SubscriptionImpl

class UserMetadataImpl(pubnub: PubNubImpl, id: String) :
    BaseUserMetadataImpl<EventListener, Subscription>(
        pubnub.pubNubCore,
        ChannelName(id),
        { channels, channelGroups, options -> SubscriptionImpl(pubnub, channels, channelGroups, options) },
    ),
    UserMetadata {
    override fun subscription(): Subscription {
        return super.subscription(EmptyOptions)
    }
}
