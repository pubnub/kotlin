package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

interface Subscribable {
    fun subscription(options: SubscriptionOptions = SubscriptionOptions.Default): Subscription
}