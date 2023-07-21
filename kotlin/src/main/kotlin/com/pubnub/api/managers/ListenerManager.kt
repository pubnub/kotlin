package com.pubnub.api.managers

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.subscribe.eventengine.effect.MessagesConsumer
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import java.util.ArrayList

internal class ListenerManager(val pubnub: PubNub) : MessagesConsumer, StatusConsumer<PNStatus> {
    private val listeners = mutableListOf<SubscribeCallback>()

    fun addListener(listener: SubscribeCallback) {
        synchronized(listeners) {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: Listener) {
        synchronized(listeners) {
            listeners.remove(listener)
        }
    }

    private fun getListeners(): List<SubscribeCallback> {
        val tempCallbackList = ArrayList<SubscribeCallback>()
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
