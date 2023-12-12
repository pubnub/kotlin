package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.EventEmitter

abstract class SubscriptionSet internal constructor() : EventEmitter, SubscribeCapable, AutoCloseable {
    abstract fun add(subscription: Subscription)
    abstract fun remove(subscription: Subscription)
}
