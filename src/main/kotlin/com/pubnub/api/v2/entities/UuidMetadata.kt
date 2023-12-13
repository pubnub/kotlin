package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

interface UuidMetadata : Subscribable {
    val id: String
    override fun subscription(options: SubscriptionOptions): Subscription
}
