package com.pubnub.api.v2

abstract class SubscriptionSet internal constructor() : EventEmitter, SubscribeCapable, AutoCloseable {
    abstract fun add(subscription: Subscription)
    abstract fun remove(subscription: Subscription)
}
