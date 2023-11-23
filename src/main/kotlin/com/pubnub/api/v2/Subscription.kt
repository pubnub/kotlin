package com.pubnub.api.v2

abstract class Subscription internal constructor() : EventEmitter, SubscribeCapable, AutoCloseable {
    abstract operator fun plus(subscription: Subscription): SubscriptionSet
}
