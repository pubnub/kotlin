package com.pubnub.internal

import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName

typealias SubscriptionFactory<T> = (
    channels: Set<ChannelName>,
    channelGroups: Set<ChannelGroupName>,
    options: SubscriptionOptions
) -> T