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

    @Volatile
    private var onMessageListener: Listener? = null

    @Volatile
    private var onPresenceListener: Listener? = null

    @Volatile
    private var onSignalListener: Listener? = null

    @Volatile
    private var onMessageActionListener: Listener? = null

    @Volatile
    private var onObjectListener: Listener? = null

    @Volatile
    private var onFileListener: Listener? = null

    override var onMessage: ((PubNub, PNMessageResult) -> Unit)? = null
        @Synchronized
        set(value) {
            onMessageListener = handleListenerRegistration(onMessageListener, value) { pubNub, result ->
                (result as? PNMessageResult)?.let { value?.invoke(pubNub, it) }
            }
        }

    override var onPresence: ((PubNub, PNPresenceEventResult) -> Unit)? = null
        @Synchronized
        set(value) {
            onPresenceListener = handleListenerRegistration(onPresenceListener, value) { pubNub, result ->
                (result as? PNPresenceEventResult)?.let { value?.invoke(pubNub, it) }
            }
        }

    override var onSignal: ((PubNub, PNSignalResult) -> Unit)? = null
        @Synchronized
        set(value) {
            onSignalListener = handleListenerRegistration(onSignalListener, value) { pubNub, result ->
                (result as? PNSignalResult)?.let { value?.invoke(pubNub, it) }
            }
        }

    override var onMessageAction: ((PubNub, PNMessageActionResult) -> Unit)? = null
        @Synchronized
        set(value) {
            onMessageActionListener = handleListenerRegistration(onMessageActionListener, value) { pubNub, result ->
                (result as? PNMessageActionResult)?.let { value?.invoke(pubNub, it) }
            }
        }

    override var onObjects: ((PubNub, PNObjectEventResult) -> Unit)? = null
        @Synchronized
        set(value) {
            onObjectListener = handleListenerRegistration(onObjectListener, value) { pubNub, result ->
                (result as? PNObjectEventResult)?.let { value?.invoke(pubNub, it) }
            }
        }

    override var onFile: ((PubNub, PNFileEventResult) -> Unit)? = null
        @Synchronized
        set(value) {
            onFileListener = handleListenerRegistration(onFileListener, value) { pubNub, result ->
                (result as? PNFileEventResult)?.let { value?.invoke(pubNub, it) }
            }
        }

    private fun <T> handleListenerRegistration(
        currentListener: Listener?,
        newValue: ((PubNub, T) -> Unit)?,
        eventHandler: (PubNub, Any) -> Unit
    ): EventListener? {
        currentListener?.let { removeListener(it) }

        return if (newValue == null) {
            null
        } else {
            val newEventListener = object : EventListener {
                override fun message(pubnub: PubNub, result: PNMessageResult) {
                    eventHandler(pubnub, result)
                }

                override fun presence(pubnub: PubNub, result: PNPresenceEventResult) {
                    eventHandler(pubnub, result)
                }

                override fun signal(pubnub: PubNub, result: PNSignalResult) {
                    eventHandler(pubnub, result)
                }

                override fun messageAction(pubnub: PubNub, result: PNMessageActionResult) {
                    eventHandler(pubnub, result)
                }

                override fun objects(pubnub: PubNub, result: PNObjectEventResult) {
                    eventHandler(pubnub, result)
                }

                override fun file(pubnub: PubNub, result: PNFileEventResult) {
                    eventHandler(pubnub, result)
                }
            }
            addListener(newEventListener)
            newEventListener
        }
    }

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
