package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

interface UuidMetadata {
    val id: String
    fun subscription(options: SubscriptionOptions = SubscriptionOptions.Default): Subscription
}
