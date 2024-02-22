package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.v2.subscription.SubscriptionImpl

class UserMetadataImpl(pubnub: InternalPubNubClient, id: String) : BaseUserMetadataImpl<EventListener, Subscription>(
    pubnub,
    ChannelName(id),
    ::SubscriptionImpl
), UserMetadata