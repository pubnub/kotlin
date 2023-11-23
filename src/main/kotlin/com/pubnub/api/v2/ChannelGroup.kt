package com.pubnub.api.v2

interface ChannelGroup {
    val name: String
    fun subscription(options: SubscriptionOptions = SubscriptionOptions.Default): Subscription
}
