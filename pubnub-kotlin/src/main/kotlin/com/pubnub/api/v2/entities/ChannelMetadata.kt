package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.SubscriptionFactory
import com.pubnub.internal.v2.entities.BaseChannelMetadataImpl
import com.pubnub.internal.v2.entities.ChannelName

class ChannelMetadata(pubnub: PubNubImpl, channelName: ChannelName, subscriptionFactory: SubscriptionFactory<Subscription>) :
    BaseChannelMetadataImpl<Subscription>(pubnub, channelName, subscriptionFactory)
