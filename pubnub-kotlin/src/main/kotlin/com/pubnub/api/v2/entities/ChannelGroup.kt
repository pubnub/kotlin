package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.SubscriptionFactory
import com.pubnub.internal.v2.entities.BaseChannelGroupImpl
import com.pubnub.internal.v2.entities.ChannelGroupName

class ChannelGroup(
    pubnub: PubNubImpl,
    channelGroupName: ChannelGroupName,
    subscriptionFactory: SubscriptionFactory<Subscription>
) :
    BaseChannelGroupImpl<Subscription>(pubnub, channelGroupName, subscriptionFactory)
