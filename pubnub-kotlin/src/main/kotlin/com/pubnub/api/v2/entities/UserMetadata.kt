package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.SubscriptionFactory
import com.pubnub.internal.v2.entities.BaseUserMetadataImpl
import com.pubnub.internal.v2.entities.ChannelName

class UserMetadata(
    pubnub: PubNubImpl,
    channelName: ChannelName,
    subscriptionFactory: SubscriptionFactory<Subscription>
) :
    BaseUserMetadataImpl<Subscription>(pubnub, channelName, subscriptionFactory)
