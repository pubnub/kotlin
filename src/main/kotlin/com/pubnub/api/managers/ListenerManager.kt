package com.pubnub.api.managers

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.subscribe.eventengine.effect.MessagesConsumer
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.internal.v2.subscription.SubscriptionImpl
import java.util.concurrent.CopyOnWriteArrayList

internal class ListenerManager(val pubnub: PubNub) : MessagesConsumer, StatusConsumer {

    // TODO this should probably be a set, but for backward compatibility leaving as list for now
    private val listeners = CopyOnWriteArrayList<Listener>()
    private val statusListeners get() = listeners.filterIsInstance<StatusListener>()
    private val eventListeners get() = listeners.filterIsInstance<EventListener>()

    fun addListener(listener: SubscribeCallback) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun removeAllListeners() {
        listeners.clear()
    }

    // for use by v2 listeners
    private val announcementCallbacks = CopyOnWriteArrayList<AnnouncementCallback>()
    private val subscriptionCallbacks get() = announcementCallbacks.filter { it.phase == AnnouncementCallback.Phase.SUBSCRIPTION }
    private val setCallbacks get() = announcementCallbacks.filter { it.phase == AnnouncementCallback.Phase.SET }

    fun addAnnouncementCallback(listener: AnnouncementCallback) {
        announcementCallbacks.add(listener)
    }

    fun removeAnnouncementCallback(listener: AnnouncementCallback) {
        announcementCallbacks.remove(listener)
    }

    override fun announce(status: PNStatus) {
        statusListeners.forEach { it.status(pubnub, status) }
    }

    override fun announce(message: PNMessageResult) {
        eventListeners.forEach { it.message(pubnub, message) }
        val envelope = AnnouncementEnvelope(message)
        subscriptionCallbacks.forEach { it.message(pubnub, envelope) }
        setCallbacks.forEach { it.message(pubnub, envelope) }
    }

    override fun announce(presence: PNPresenceEventResult) {
        eventListeners.forEach { it.presence(pubnub, presence) }
        val envelope = AnnouncementEnvelope(presence)
        subscriptionCallbacks.forEach { it.presence(pubnub, envelope) }
        setCallbacks.forEach { it.presence(pubnub, envelope) }
    }

    override fun announce(signal: PNSignalResult) {
        eventListeners.forEach { it.signal(pubnub, signal) }
        val envelope = AnnouncementEnvelope(signal)
        subscriptionCallbacks.forEach { it.signal(pubnub, envelope) }
        setCallbacks.forEach { it.signal(pubnub, envelope) }
    }

    override fun announce(messageAction: PNMessageActionResult) {
        eventListeners.forEach { it.messageAction(pubnub, messageAction) }
        val envelope = AnnouncementEnvelope(messageAction)
        subscriptionCallbacks.forEach { it.messageAction(pubnub, envelope) }
        setCallbacks.forEach { it.messageAction(pubnub, envelope) }
    }

    override fun announce(pnObjectEventResult: PNObjectEventResult) {
        eventListeners.forEach { it.objects(pubnub, pnObjectEventResult) }
        val envelope = AnnouncementEnvelope(pnObjectEventResult)
        subscriptionCallbacks.forEach { it.objects(pubnub, envelope) }
        setCallbacks.forEach { it.objects(pubnub, envelope) }
    }

    override fun announce(pnFileEventResult: PNFileEventResult) {
        eventListeners.forEach { it.file(pubnub, pnFileEventResult) }
        val envelope = AnnouncementEnvelope(pnFileEventResult)
        subscriptionCallbacks.forEach { it.file(pubnub, envelope) }
        setCallbacks.forEach { it.file(pubnub, envelope) }
    }

    fun addListener(listener: StatusListener) {
        listeners.add(listener)
    }

    fun addListener(listener: EventListener) {
        listeners.add(listener)
    }
}

internal data class AnnouncementEnvelope<T : PNEvent>(
    val event: T
) {
    val acceptedBy = mutableSetOf<SubscriptionImpl>()
}

internal interface AnnouncementCallback {
    enum class Phase { SUBSCRIPTION, SET }

    val phase: Phase
    fun message(pubnub: PubNub, envelope: AnnouncementEnvelope<PNMessageResult>)
    fun presence(pubnub: PubNub, envelope: AnnouncementEnvelope<PNPresenceEventResult>)
    fun signal(pubnub: PubNub, envelope: AnnouncementEnvelope<PNSignalResult>)
    fun messageAction(pubnub: PubNub, envelope: AnnouncementEnvelope<PNMessageActionResult>)
    fun objects(pubnub: PubNub, envelope: AnnouncementEnvelope<PNObjectEventResult>)
    fun file(pubnub: PubNub, envelope: AnnouncementEnvelope<PNFileEventResult>)
}
