package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.v2.subscription.SubscriptionImpl

class ChannelGroupImpl(pubnub: InternalPubNubClient, channelGroupName: ChannelGroupName) : BaseChannelGroupImpl<EventListener, Subscription>(
    pubnub,
    channelGroupName,
    ::SubscriptionImpl
), ChannelGroup