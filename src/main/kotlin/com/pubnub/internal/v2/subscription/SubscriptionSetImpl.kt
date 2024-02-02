package com.pubnub.internal.v2.subscription

import com.pubnub.api.PubNub
import com.pubnub.api.managers.AnnouncementCallback
import com.pubnub.api.managers.AnnouncementEnvelope
import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
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
    private val _subscriptions: CopyOnWriteArraySet<SubscriptionImpl> = CopyOnWriteArraySet()
    private val eventEmitter = EventEmitterImpl(pubnub, AnnouncementCallback.Phase.SET, ::accepts)

    override val subscriptions get() = _subscriptions.toSet()

    init {
        require(initialSubscriptions.all { it.pubnub == pubnub }) { ERROR_WRONG_PUBNUB_INSTANCE }
        _subscriptions.addAll(initialSubscriptions)
        pubnub.listenerManager.addAnnouncementCallback(eventEmitter)
    }

    private fun accepts(envelope: AnnouncementEnvelope<*>) =
        subscriptions.any { subscription -> subscription in envelope.acceptedBy }

    override fun add(subscription: Subscription) {
        require(subscription is SubscriptionImpl) { ERROR_SUBSCRIPTION_WRONG_CLASS }
        require(subscription.pubnub == pubnub) { ERROR_WRONG_PUBNUB_INSTANCE }
        _subscriptions.add(subscription)
    }

    override fun remove(subscription: Subscription) {
        _subscriptions.remove(subscription)
    }

    override operator fun plus(subscription: Subscription): SubscriptionSet {
        add(subscription)
        return this
    }

    override fun subscribe(cursor: SubscriptionCursor) {
        _subscriptions.forEach { it.onSubscriptionActive(cursor) }
        pubnub.subscribe(*_subscriptions.toTypedArray(), cursor = cursor)
    }

    override fun unsubscribe() {
        _subscriptions.forEach { it.onSubscriptionInactive() }
        pubnub.unsubscribe(*_subscriptions.toTypedArray())
    }

    override fun close() {
        unsubscribe()
        eventEmitter.removeAllListeners()
        pubnub.listenerManager.removeAnnouncementCallback(eventEmitter)
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
