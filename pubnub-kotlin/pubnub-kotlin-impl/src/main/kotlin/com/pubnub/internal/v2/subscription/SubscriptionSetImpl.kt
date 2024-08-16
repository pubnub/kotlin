package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.managers.AnnouncementCallback
import com.pubnub.internal.managers.AnnouncementEnvelope
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.collections.remove

open class SubscriptionSetImpl(
    val pubnub: PubNubImpl,
    initialSubscriptions: Set<SubscriptionInternal>,
) : SubscriptionSet {
    private val _subscriptions: CopyOnWriteArraySet<SubscriptionInternal> = CopyOnWriteArraySet()
    override val subscriptions: Set<Subscription> get() = _subscriptions.toSet() as Set<Subscription>
    private val eventEmitter = EventEmitterImpl(AnnouncementCallback.Phase.SET, ::accepts)

    private fun accepts(envelope: AnnouncementEnvelope<*>) = subscriptions.any { subscription -> subscription in envelope.acceptedBy }

    init {
        require(initialSubscriptions.all { it.pubnub == pubnub }) { ERROR_WRONG_PUBNUB_INSTANCE }
        _subscriptions.addAll(initialSubscriptions)
        pubnub.listenerManager.addAnnouncementCallback(eventEmitter)
    }

    override fun add(subscription: Subscription) {
        require(subscription is SubscriptionInternal) { ERROR_SUBSCRIPTION_WRONG_CLASS }
        require(subscription.pubnub == pubnub) { ERROR_WRONG_PUBNUB_INSTANCE }
        _subscriptions.add(subscription)
    }

    fun addInternal(subscriptionInternal: SubscriptionInternal) {
        _subscriptions.add(subscriptionInternal)
    }

    override fun remove(subscription: Subscription) {
        _subscriptions.remove(subscription)
    }

    fun removeInternal(subscriptionInternal: SubscriptionInternal) {
        _subscriptions.remove(subscriptionInternal)
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

    override fun addListener(listener: EventListener) {
        eventEmitter.addListener(listener)
    }

    override fun removeListener(listener: Listener) {
        eventEmitter.removeListener(listener)
    }

    override fun removeAllListeners() {
        eventEmitter.removeAllListeners()
    }

    override operator fun plusAssign(subscription: Subscription) {
        add(subscription)
    }

    override fun minusAssign(subscription: Subscription) {
        remove(subscription)
    }

    private val emitterHelper = EmitterHelper(eventEmitter)
    override var onMessage: ((PNMessageResult) -> Unit)? by emitterHelper::onMessage
    override var onPresence: ((PNPresenceEventResult) -> Unit)? by emitterHelper::onPresence
    override var onSignal: ((PNSignalResult) -> Unit)? by emitterHelper::onSignal
    override var onMessageAction: ((PNMessageActionResult) -> Unit)? by emitterHelper::onMessageAction
    override var onObjects: ((PNObjectEventResult) -> Unit)? by emitterHelper::onObjects
    override var onFile: ((PNFileEventResult) -> Unit)? by emitterHelper::onFile
}

private const val ERROR_SUBSCRIPTION_WRONG_CLASS =
    "Only Subscriptions returned from objects created" +
        "through the PubNub instance and their methods, such as channel(...).subscriptions() are supported."
private const val ERROR_WRONG_PUBNUB_INSTANCE =
    "Adding Subscriptions from another PubNub instance to a SubscriptionSet is not allowed."
