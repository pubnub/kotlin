package com.pubnub.internal.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.managers.AnnouncementCallback
import com.pubnub.api.managers.AnnouncementEnvelope
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import java.util.concurrent.CopyOnWriteArraySet

internal class EventEmitterImpl(
    override val phase: AnnouncementCallback.Phase,
    val accepts: (AnnouncementEnvelope<out PNEvent>) -> Boolean = { true }
) : EventEmitter, AnnouncementCallback {

    private val listeners = CopyOnWriteArraySet<EventListener>()

    override fun addListener(listener: EventListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun removeAllListeners() {
        listeners.clear()
    }

    // EventEmitter
    fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
        listeners.forEach {
            it.message(pubnub, pnMessageResult)
        }
    }

    fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
        listeners.forEach {
            it.presence(pubnub, pnPresenceEventResult)
        }
    }

    fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
        listeners.forEach {
            it.signal(pubnub, pnSignalResult)
        }
    }

    fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
        listeners.forEach {
            it.messageAction(pubnub, pnMessageActionResult)
        }
    }

    fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
        listeners.forEach {
            it.objects(pubnub, objectEvent)
        }
    }

    fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
        listeners.forEach {
            it.file(pubnub, pnFileEventResult)
        }
    }

    // AnnouncementCallback

    override fun message(pubnub: PubNub, envelope: AnnouncementEnvelope<PNMessageResult>) {
        if (accepts(envelope)) {
            message(pubnub, envelope.event)
        }
    }

    override fun presence(pubnub: PubNub, envelope: AnnouncementEnvelope<PNPresenceEventResult>) {
        if (accepts(envelope)) {
            presence(pubnub, envelope.event)
        }
    }

    override fun signal(pubnub: PubNub, envelope: AnnouncementEnvelope<PNSignalResult>) {
        if (accepts(envelope)) {
            signal(pubnub, envelope.event)
        }
    }

    override fun messageAction(pubnub: PubNub, envelope: AnnouncementEnvelope<PNMessageActionResult>) {
        if (accepts(envelope)) {
            messageAction(pubnub, envelope.event)
        }
    }

    override fun objects(pubnub: PubNub, envelope: AnnouncementEnvelope<PNObjectEventResult>) {
        if (accepts(envelope)) {
            objects(pubnub, envelope.event)
        }
    }

    override fun file(pubnub: PubNub, envelope: AnnouncementEnvelope<PNFileEventResult>) {
        if (accepts(envelope)) {
            file(pubnub, envelope.event)
        }
    }
}
