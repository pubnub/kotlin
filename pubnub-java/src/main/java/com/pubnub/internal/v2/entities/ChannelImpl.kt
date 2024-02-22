package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.v2.subscription.SubscriptionImpl

class ChannelImpl(pubnub: InternalPubNubClient, channelName: String) : BaseChannelImpl<EventListener, Subscription>(
    pubnub,
    ChannelName(channelName),
    ::SubscriptionImpl
), Channel