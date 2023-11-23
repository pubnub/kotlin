package com.pubnub.internal.v2

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.EventEmitter
import java.util.concurrent.CopyOnWriteArraySet

internal class EventEmitterImpl(
    private val pubNub: PubNub,
    val filter: (PNEvent) -> Boolean = { true }
) : SubscribeCallback(), EventEmitter {
    private val eventListeners = CopyOnWriteArraySet<SubscribeCallback>()
    private var lastSeenTimetoken: Long? = null

    override fun addListener(listener: SubscribeCallback) {
        synchronized(eventListeners) {
            eventListeners.add(listener)
            if (eventListeners.size == 1) {
                pubNub.addListener(this)
            }
        }
    }

    override fun removeListener(listener: SubscribeCallback) {
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

    private fun checkTimetoken(result: PNEvent): Boolean {
        lastSeenTimetoken?.let { lastSeenTimetokenNonNull ->
            result.timetoken?.let { resultTimetokenNonNull ->
                if (resultTimetokenNonNull <= lastSeenTimetokenNonNull) {
                    return false
                }
            }
        }
        lastSeenTimetoken = result.timetoken
        return true
    }

    override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
        if (!filter(pnMessageResult) || !checkTimetoken(pnMessageResult)) {
            return
        }
        eventListeners.forEach {
            it.message(pubnub, pnMessageResult)
        }
    }

    override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
        if (!filter(pnPresenceEventResult) || !checkTimetoken(pnPresenceEventResult)) {
            return
        }
        lastSeenTimetoken?.let { lastSeenTimetokenNonNull ->
            if ((pnPresenceEventResult.timetoken != null) && (pnPresenceEventResult.timetoken <= lastSeenTimetokenNonNull)) {
                return
            }
        }
        lastSeenTimetoken = pnPresenceEventResult.timetoken
        eventListeners.forEach {
            it.presence(pubnub, pnPresenceEventResult)
        }
    }

    override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
        if (!filter(pnSignalResult) || !checkTimetoken(pnSignalResult)) {
            return
        }
        eventListeners.forEach {
            it.signal(pubnub, pnSignalResult)
        }
    }

    override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
        if (!filter(pnMessageActionResult) || !checkTimetoken(pnMessageActionResult)) {
            return
        }
        eventListeners.forEach {
            it.messageAction(pubnub, pnMessageActionResult)
        }
    }

    override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
        if (!filter(objectEvent) || !checkTimetoken(objectEvent)) {
            return
        }
        eventListeners.forEach {
            it.objects(pubnub, objectEvent)
        }
    }

    override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
        if (!filter(pnFileEventResult) || !checkTimetoken(pnFileEventResult)) {
            return
        }
        eventListeners.forEach {
            it.file(pubnub, pnFileEventResult)
        }
    }
}
