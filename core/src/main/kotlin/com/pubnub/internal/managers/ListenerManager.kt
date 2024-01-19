package com.pubnub.internal.managers

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.internal.callbacks.Listener
import com.pubnub.internal.callbacks.SubscribeCallback
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.internal.subscribe.eventengine.effect.MessagesConsumer
import com.pubnub.internal.subscribe.eventengine.effect.StatusConsumer

class ListenerManager<T>(val pubnub: T) : MessagesConsumer, StatusConsumer {
    private val listeners = mutableListOf<SubscribeCallback<T>>()

    fun addListener(listener: SubscribeCallback<T>) {
        synchronized(listeners) {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: Listener) {
        synchronized(listeners) {
            listeners.remove(listener)
        }
    }

    private fun getListeners(): List<SubscribeCallback<T>> {
        val tempCallbackList = ArrayList<SubscribeCallback<T>>()
        synchronized(listeners) {
            tempCallbackList.addAll(listeners)
        }
        return tempCallbackList
    }

    override fun announce(status: PNStatus) {
        getListeners().forEach { it.status(pubnub, status) }
    }

    override fun announce(message: PNMessageResult) {
        getListeners().forEach { it.message(pubnub, message) }
    }

    override fun announce(presence: PNPresenceEventResult) {
        getListeners().forEach { it.presence(pubnub, presence) }
    }

    override fun announce(signal: PNSignalResult) {
        getListeners().forEach { it.signal(pubnub, signal) }
    }

    override fun announce(messageAction: PNMessageActionResult) {
        getListeners().forEach { it.messageAction(pubnub, messageAction) }
    }

    override fun announce(pnObjectEventResult: PNObjectEventResult) {
        getListeners().forEach { it.objects(pubnub, pnObjectEventResult) }
    }

    override fun announce(pnFileEventResult: PNFileEventResult) {
        getListeners().forEach { it.file(pubnub, pnFileEventResult) }
    }
}
