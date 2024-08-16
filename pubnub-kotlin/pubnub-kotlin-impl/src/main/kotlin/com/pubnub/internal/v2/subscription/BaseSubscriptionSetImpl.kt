package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.SubscribeCapable
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.managers.AnnouncementCallback
import com.pubnub.internal.managers.AnnouncementEnvelope
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import java.util.concurrent.CopyOnWriteArraySet

abstract class BaseSubscriptionSetImpl<SubscriptionT>(
    val pubnub: PubNubImpl,
    initialSubscriptions: Set<SubscriptionInternal>,
) : EventEmitter, SubscribeCapable, AutoCloseable {
    protected val subscriptionsInternal: CopyOnWriteArraySet<SubscriptionInternal> = CopyOnWriteArraySet()
    abstract val subscriptions: Set<SubscriptionT>

    private val eventEmitter = EventEmitterImpl(AnnouncementCallback.Phase.SET, ::accepts)

    private fun accepts(envelope: AnnouncementEnvelope<*>) = subscriptionsInternal.any { subscription -> subscription in envelope.acceptedBy }

    init {
        require(initialSubscriptions.all { it.pubnub == pubnub }) { ERROR_WRONG_PUBNUB_INSTANCE }
        subscriptionsInternal.addAll(initialSubscriptions)
        pubnub.listenerManager.addAnnouncementCallback(eventEmitter)
    }

    fun add(subscription: SubscriptionT) {
        require(subscription is SubscriptionInternal) { ERROR_SUBSCRIPTION_WRONG_CLASS }
        require(subscription.pubnub == pubnub) { ERROR_WRONG_PUBNUB_INSTANCE }
        subscriptionsInternal.add(subscription)
    }

    fun remove(subscription: SubscriptionT) {
        require(subscription is SubscriptionInternal) { ERROR_SUBSCRIPTION_WRONG_CLASS }
        subscriptionsInternal.remove(subscription)
    }

    override fun subscribe(cursor: SubscriptionCursor) {
        subscriptionsInternal.forEach { it.onSubscriptionActive(cursor) }
        pubnub.subscribe(*subscriptionsInternal.toTypedArray(), cursor = cursor)
    }

    override fun unsubscribe() {
        subscriptionsInternal.forEach { it.onSubscriptionInactive() }
        pubnub.unsubscribe(*subscriptionsInternal.toTypedArray())
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

    operator fun plusAssign(subscription: SubscriptionT) {
        add(subscription)
    }

    operator fun minusAssign(subscription: SubscriptionT) {
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
