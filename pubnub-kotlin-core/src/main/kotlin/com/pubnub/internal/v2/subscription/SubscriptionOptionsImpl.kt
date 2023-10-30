package com.pubnub.internal.v2.subscription

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

internal class FilterImpl(internal val predicate: (PNEvent) -> Boolean) : SubscriptionOptions()

internal object ReceivePresenceEventsImpl : SubscriptionOptions()
