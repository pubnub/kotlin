package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener

interface SubscriptionSet : BaseSubscriptionSet<EventListener, Subscription>, EventEmitter {
    fun plus(subscription: Subscription): SubscriptionSet
}
