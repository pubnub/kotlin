package com.pubnub.internal.v2

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.v2.ChannelOptions
import com.pubnub.api.v2.SubscriptionOptions

internal class Filter(internal val predicate: (PNEvent) -> Boolean) : SubscriptionOptions()

internal class ReceivePresenceEvents : ChannelOptions()
