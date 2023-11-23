package com.pubnub.api.v2

interface Channel {
    val name: String
    fun subscription(options: SubscriptionOptions = SubscriptionOptions.Default): Subscription
}
