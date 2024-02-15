package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.SubscriptionFactory
import com.pubnub.internal.v2.entities.BaseChannelImpl
import com.pubnub.internal.v2.entities.ChannelName

class Channel(pubnub: PubNubImpl, channelName: ChannelName, subscriptionFactory: SubscriptionFactory<Subscription>) :
    BaseChannelImpl<Subscription>(pubnub, channelName, subscriptionFactory)
