package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

interface BaseChannelMetadata : Subscribable {
    val id: String
    override fun subscription(options: SubscriptionOptions): BaseSubscription
}
