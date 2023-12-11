package com.pubnub.internal.v2

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
import com.pubnub.api.v2.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import java.util.concurrent.CopyOnWriteArraySet

internal class EventEmitterImpl(
    private val pubNub: PubNub,
    val filter: (PNEvent) -> Boolean = { true }
) : SubscribeCallback(), EventEmitter {
    private val eventListeners = CopyOnWriteArraySet<EventListener>()
    private var lastTimetoken: Long? = null

    override fun addListener(listener: EventListener) {
        synchronized(eventListeners) {
            eventListeners.add(listener)
            if (eventListeners.size == 1) {
                pubNub.addListener(this)
            }
        }
    }

    override fun removeListener(listener: EventListener) {
        synchronized(eventListeners) {
            eventListeners.remove(listener)
            if (eventListeners.isEmpty()) {
                pubNub.removeListener(this)
            }
        }
    }

    override fun removeAllListeners() {
        synchronized(eventListeners) {
            eventListeners.clear()
            pubNub.removeListener(this)
        }
    }

    // SubscribeCallback

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

    override fun status(pubnub: PubNub, pnStatus: PNStatus) {
        // empty
    }

    override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
        if (!shouldDeliverEvent(pnMessageResult)) return
        eventListeners.forEach {
            it.message(pubnub, pnMessageResult)
        }
    }

    override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
        if (!shouldDeliverEvent(pnPresenceEventResult)) return
        eventListeners.forEach {
            it.presence(pubnub, pnPresenceEventResult)
        }
    }

    override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
        if (!shouldDeliverEvent(pnSignalResult)) return
        eventListeners.forEach {
            it.signal(pubnub, pnSignalResult)
        }
    }

    override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
        if (!shouldDeliverEvent(pnMessageActionResult)) return
        eventListeners.forEach {
            it.messageAction(pubnub, pnMessageActionResult)
        }
    }

    override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
        if (!shouldDeliverEvent(objectEvent)) return
        eventListeners.forEach {
            it.objects(pubnub, objectEvent)
        }
    }

    override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
        if (!shouldDeliverEvent(pnFileEventResult)) return
        eventListeners.forEach {
            it.file(pubnub, pnFileEventResult)
        }
    }

    private fun shouldDeliverEvent(pnFileEventResult: PNEvent): Boolean =
        filter(pnFileEventResult) && checkAndUpdateTimetoken(pnFileEventResult)
}
