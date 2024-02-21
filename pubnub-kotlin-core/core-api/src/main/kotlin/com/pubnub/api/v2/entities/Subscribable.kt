package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

interface Subscribable {
    fun subscription(options: SubscriptionOptions = EmptyOptions): BaseSubscription
}
