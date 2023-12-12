package com.pubnub.internal.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusEmitter
import com.pubnub.api.v2.callbacks.StatusListener
import java.util.concurrent.CopyOnWriteArraySet

interface Emitter<T> {
    fun addListener(listener: T)
    fun removeListener(listener: T)
    fun removeAllListeners()
}

internal abstract class EmitterImpl<T>(
    protected val pubnub: PubNub
) : Emitter<T> {
    abstract val subscribeCallback: SubscribeCallback
    protected val listeners = CopyOnWriteArraySet<T>()

    override fun addListener(listener: T) {
        synchronized(listeners) {
            listeners.add(listener)
            if (listeners.size == 1) {
                pubnub.addListener(subscribeCallback)
            }
        }
    }

    override fun removeListener(listener: T) {
        synchronized(listeners) {
            listeners.remove(listener)
            if (listeners.isEmpty()) {
                pubnub.removeListener(subscribeCallback)
            }
        }
    }

    override fun removeAllListeners() {
        synchronized(listeners) {
            listeners.clear()
            pubnub.removeListener(subscribeCallback)
        }
    }
}

internal class StatusEmitterImpl(
    pubnub: PubNub
) : EmitterImpl<StatusListener>(pubnub), StatusEmitter {
    override val subscribeCallback: SubscribeCallback = object : SubscribeCallback() {
        override fun status(pubnub: PubNub, pnStatus: PNStatus) {
            listeners.forEach {
                it.status(pubnub, pnStatus)
            }
        }
    }
}

internal class EventEmitterImpl(
    pubnub: PubNub,
    val filter: (PNEvent) -> Boolean = { true }
) : EmitterImpl<EventListener>(pubnub), EventEmitter {
    private var lastTimetoken: Long? = null

    private fun checkAndUpdateTimetoken(result: PNEvent): Boolean {
        lastTimetoken?.let { lastTimetokenNonNull ->
            result.timetoken?.let { resultTimetokenNonNull ->
                if (resultTimetokenNonNull <= lastTimetokenNonNull) {
                    return false
                }
            }
        }
        lastTimetoken = result.timetoken
        return true
    }

    override val subscribeCallback: SubscribeCallback = object : SubscribeCallback() {
        override fun status(pubnub: PubNub, pnStatus: PNStatus) {
            // empty
        }

        override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
            if (!shouldDeliverEvent(pnMessageResult)) return
            listeners.forEach {
                it.message(pubnub, pnMessageResult)
            }
        }

        override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
            if (!shouldDeliverEvent(pnPresenceEventResult)) return
            listeners.forEach {
                it.presence(pubnub, pnPresenceEventResult)
            }
        }

        override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
            if (!shouldDeliverEvent(pnSignalResult)) return
            listeners.forEach {
                it.signal(pubnub, pnSignalResult)
            }
        }

        override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
            if (!shouldDeliverEvent(pnMessageActionResult)) return
            listeners.forEach {
                it.messageAction(pubnub, pnMessageActionResult)
            }
        }

        override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
            if (!shouldDeliverEvent(objectEvent)) return
            listeners.forEach {
                it.objects(pubnub, objectEvent)
            }
        }

        override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
            if (!shouldDeliverEvent(pnFileEventResult)) return
            listeners.forEach {
                it.file(pubnub, pnFileEventResult)
            }
        }
    }

    private fun shouldDeliverEvent(pnFileEventResult: PNEvent): Boolean =
        filter(pnFileEventResult) && checkAndUpdateTimetoken(pnFileEventResult)
}
