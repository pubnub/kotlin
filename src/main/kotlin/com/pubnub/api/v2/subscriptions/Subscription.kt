package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.EventEmitter

abstract class Subscription internal constructor() : EventEmitter, SubscribeCapable, AutoCloseable {
    abstract operator fun plus(subscription: Subscription): SubscriptionSet
}
