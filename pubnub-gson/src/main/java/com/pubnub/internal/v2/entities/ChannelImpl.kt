package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.subscription.SubscriptionImpl

class ChannelImpl(pubnub: PubNubImpl, channelName: String) :
    BaseChannelImpl<EventListener, Subscription>(
        pubnub.corePubNubClient,
        ChannelName(channelName),
        { channels, channelGroups, options -> SubscriptionImpl(pubnub, channels, channelGroups, options) },
    ),
    Channel
