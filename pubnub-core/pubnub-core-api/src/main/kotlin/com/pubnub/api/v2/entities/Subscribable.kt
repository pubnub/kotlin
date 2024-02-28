package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

interface Subscribable<EvLis : BaseEventListener> {
    fun subscription(options: SubscriptionOptions = EmptyOptions): BaseSubscription<EvLis>
}
