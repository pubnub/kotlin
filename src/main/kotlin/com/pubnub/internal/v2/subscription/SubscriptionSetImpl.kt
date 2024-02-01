package com.pubnub.internal.v2.subscription

import com.pubnub.api.PubNub
import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import org.jetbrains.annotations.TestOnly
import java.util.concurrent.CopyOnWriteArraySet

private const val ERROR_SUBSCRIPTION_WRONG_CLASS =
    "Only Subscriptions returned from objects created" +
        "through the PubNub instance and their methods, such as channel(...).subscription() are supported."
private const val ERROR_WRONG_PUBNUB_INSTANCE =
    "Adding Subscriptions from another PubNub instance to a SubscriptionSet is not allowed."

internal class SubscriptionSetImpl(
    private val pubnub: PubNub,
    initialSubscriptions: Set<SubscriptionImpl> = emptySet(),
) : SubscriptionSet(), EventEmitter {

    @get:TestOnly
    internal val eventEmitter = EventEmitterImpl(pubnub) { event ->
        subscriptionSet.any { subscription -> subscription.accepts(event) }
    }
    private val subscriptionSet: CopyOnWriteArraySet<SubscriptionImpl> = CopyOnWriteArraySet()

    init {
        require(initialSubscriptions.all { it.pubnub == pubnub }) { ERROR_WRONG_PUBNUB_INSTANCE }
        subscriptionSet.addAll(initialSubscriptions)
    }

    override val subscriptions get() = subscriptionSet.toSet()

    override fun add(subscription: Subscription) {
        require(subscription is SubscriptionImpl) { ERROR_SUBSCRIPTION_WRONG_CLASS }
        require(subscription.pubnub == pubnub) { ERROR_WRONG_PUBNUB_INSTANCE }
        subscriptionSet.add(subscription)
    }

    override fun remove(subscription: Subscription) {
        subscriptionSet.remove(subscription)
    }

    override fun plus(subscription: Subscription): SubscriptionSet {
        add(subscription)
        return this
    }

    override fun subscribe(cursor: SubscriptionCursor) {
        pubnub.subscribe(*subscriptionSet.toTypedArray(), cursor = cursor)
        subscriptionSet.forEach { it.deliverEventsFrom = cursor }
    }

    override fun unsubscribe() {
        pubnub.unsubscribe(*subscriptionSet.toTypedArray())
        subscriptionSet.forEach { it.deliverEventsFrom = null }
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
