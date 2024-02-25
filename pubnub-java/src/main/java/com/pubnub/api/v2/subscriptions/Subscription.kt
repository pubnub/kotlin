package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener

interface Subscription : BaseSubscription<EventListener>, EventEmitter {
    fun plus(subscription: Subscription): SubscriptionSet
}
