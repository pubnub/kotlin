package com.pubnub.internal.v2.callbacks

import com.pubnub.api.BasePubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.internal.managers.AnnouncementCallback
import com.pubnub.internal.managers.AnnouncementEnvelope
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult
import java.util.concurrent.CopyOnWriteArraySet

class EventEmitterImpl internal constructor(
    override val phase: AnnouncementCallback.Phase,
    val accepts: (AnnouncementEnvelope<out PNEvent>) -> Boolean = { true }
) : BaseEventEmitter<InternalEventListener>, AnnouncementCallback {

    private val listeners = CopyOnWriteArraySet<InternalEventListener>()

    override fun addListener(listener: InternalEventListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun removeAllListeners() {
        listeners.clear()
    }

    // EventEmitter
    fun message(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, pnMessageResult: PNMessageResult) {
        listeners.forEach {
            it.message(pubnub, pnMessageResult)
        }
    }

    fun presence(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, pnPresenceEventResult: PNPresenceEventResult) {
        listeners.forEach {
            it.presence(pubnub, pnPresenceEventResult)
        }
    }

    fun signal(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, pnSignalResult: PNSignalResult) {
        listeners.forEach {
            it.signal(pubnub, pnSignalResult)
        }
    }

    fun messageAction(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, pnMessageActionResult: PNMessageActionResult) {
        listeners.forEach {
            it.messageAction(pubnub, pnMessageActionResult)
        }
    }

    fun objects(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, objectEvent: PNObjectEventResult) {
        listeners.forEach {
            it.objects(pubnub, objectEvent)
        }
    }

    fun file(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, pnFileEventResult: PNFileEventResult) {
        listeners.forEach {
            it.file(pubnub, pnFileEventResult)
        }
    }

    // AnnouncementCallback

    override fun message(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, envelope: AnnouncementEnvelope<PNMessageResult>) {
        if (accepts(envelope)) {
            message(pubnub, envelope.event)
        }
    }

    override fun presence(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, envelope: AnnouncementEnvelope<PNPresenceEventResult>) {
        if (accepts(envelope)) {
            presence(pubnub, envelope.event)
        }
    }

    override fun signal(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, envelope: AnnouncementEnvelope<PNSignalResult>) {
        if (accepts(envelope)) {
            signal(pubnub, envelope.event)
        }
    }

    override fun messageAction(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, envelope: AnnouncementEnvelope<PNMessageActionResult>) {
        if (accepts(envelope)) {
            messageAction(pubnub, envelope.event)
        }
    }

    override fun objects(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, envelope: AnnouncementEnvelope<PNObjectEventResult>) {
        if (accepts(envelope)) {
            objects(pubnub, envelope.event)
        }
    }

    override fun file(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, envelope: AnnouncementEnvelope<PNFileEventResult>) {
        if (accepts(envelope)) {
            file(pubnub, envelope.event)
        }
    }
}
