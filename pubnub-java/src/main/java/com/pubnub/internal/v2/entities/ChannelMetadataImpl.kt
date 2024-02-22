package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.v2.subscription.SubscriptionImpl

class ChannelMetadataImpl(pubnub: InternalPubNubClient, id: String) : BaseChannelMetadataImpl<EventListener, Subscription>(
    pubnub,
    ChannelName(id),
    ::SubscriptionImpl
), ChannelMetadata