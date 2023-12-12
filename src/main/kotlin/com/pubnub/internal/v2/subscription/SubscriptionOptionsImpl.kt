package com.pubnub.internal.v2.subscription

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.v2.subscriptions.ChannelOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

internal class Filter(internal val predicate: (PNEvent) -> Boolean) : SubscriptionOptions()

internal class ReceivePresenceEvents : ChannelOptions()
