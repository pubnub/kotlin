package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.managers.AnnouncementCallback
import com.pubnub.internal.managers.AnnouncementEnvelope
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import java.util.concurrent.CopyOnWriteArraySet

private const val ERROR_SUBSCRIPTION_WRONG_CLASS =
    "Only Subscriptions returned from objects created" +
        "through the PubNub instance and their methods, such as channel(...).subscription() are supported."
private const val ERROR_WRONG_PUBNUB_INSTANCE =
    "Adding Subscriptions from another PubNub instance to a SubscriptionSet is not allowed."

open class BaseSubscriptionSetImpl(
    private val pubnub: PubNubImpl,
    initialSubscriptions: Set<BaseSubscriptionImpl> = emptySet(),
) : BaseSubscriptionSet(), BaseEventEmitter {
    private val _subscriptions: CopyOnWriteArraySet<BaseSubscriptionImpl> = CopyOnWriteArraySet()
    override val subscriptions get() = _subscriptions.toSet()
    protected val eventEmitter = EventEmitterImpl(AnnouncementCallback.Phase.SET, ::accepts)

    private fun accepts(envelope: AnnouncementEnvelope<*>) =
        subscriptions.any { subscription -> subscription in envelope.acceptedBy }

    init {
        require(initialSubscriptions.all { it.pubnub == pubnub }) { ERROR_WRONG_PUBNUB_INSTANCE }
        _subscriptions.addAll(initialSubscriptions)
        pubnub.listenerManager.addAnnouncementCallback(eventEmitter)
    }

    override fun add(subscription: BaseSubscription) {
        require(subscription is BaseSubscriptionImpl) { ERROR_SUBSCRIPTION_WRONG_CLASS }
        require(subscription.pubnub == pubnub) { ERROR_WRONG_PUBNUB_INSTANCE }
        _subscriptions.add(subscription)
    }

    override fun remove(subscription: BaseSubscription) {
        _subscriptions.remove(subscription)
    }

    override operator fun plus(subscription: BaseSubscription): BaseSubscriptionSet {
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
        pubnub.listenerManager.removeAnnouncementCallback(eventEmitter)
    }

    override fun addListener(listener: BaseEventListener) {
        eventEmitter.addListener(listener)
    }

    override fun removeListener(listener: Listener) {
        eventEmitter.removeListener(listener)
    }

    override fun removeAllListeners() {
        eventEmitter.removeAllListeners()
    }
}
