package com.pubnub.internal.v2.subscription

import com.pubnub.api.PubNub
import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import java.util.concurrent.CopyOnWriteArraySet

private const val ERROR_SUBSCRIPTION_WRONG_CLASS =
    "Only Subscriptions returned from objects created" +
        "through the PubNub instance and their methods, such as channel(...).subscription() are supported."
private const val ERROR_WRONG_PUBNUB_INSTANCE =
    "Adding Subscriptions from another PubNub instance to a SubscriptionSet is not allowed."

internal class SubscriptionSetImpl(
    private val pubNub: PubNub,
    subscriptions: Set<SubscriptionImpl> = emptySet(),
) : SubscriptionSet(), EventEmitter {
    private val eventEmitter: EventEmitterImpl = EventEmitterImpl(pubNub) { event ->
        subscriptionSet.any { subscription -> subscription.accepts(event) }
    }
    private val subscriptionSet: CopyOnWriteArraySet<SubscriptionImpl> = CopyOnWriteArraySet()

    init {
        require(subscriptions.all { it.pubNub == pubNub }) { ERROR_WRONG_PUBNUB_INSTANCE }
        subscriptionSet.addAll(subscriptions)
    }

    override fun add(subscription: Subscription) {
        require(subscription is SubscriptionImpl) { ERROR_SUBSCRIPTION_WRONG_CLASS }
        require(subscription.pubNub == pubNub) { ERROR_WRONG_PUBNUB_INSTANCE }
        subscriptionSet.add(subscription)
    }

    override fun remove(subscription: Subscription) {
        subscriptionSet.remove(subscription)
    }

    override fun subscribe(cursor: SubscriptionCursor?) {
        pubNub.subscribe(*subscriptionSet.toTypedArray(), cursor = cursor)
    }

    override fun unsubscribe() {
        pubNub.unsubscribe(*subscriptionSet.toTypedArray())
    }

    override fun close() {
        unsubscribe()
        eventEmitter.removeAllListeners()
    }

    override fun addListener(listener: EventListener) {
        eventEmitter.addListener(listener)
    }

    override fun removeListener(listener: EventListener) {
        eventEmitter.removeListener(listener)
    }

    override fun removeAllListeners() {
        eventEmitter.removeAllListeners()
    }
}
