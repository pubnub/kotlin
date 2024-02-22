package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.subscription.SubscriptionImpl

class UserMetadataImpl(pubnub: PubNubImpl, id: String) : BaseUserMetadataImpl<EventListener, Subscription>(
    pubnub.internalPubNubClient,
    ChannelName(id),
    { channels, channelGroups, options -> SubscriptionImpl(pubnub, channels, channelGroups, options) }
), UserMetadata