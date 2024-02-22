package com.pubnub.internal.callbacks

import com.pubnub.api.BasePubNub
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.consumer.objects.toApi

class DelegatingSubscribeCallback(private val listener: SubscribeCallback) : com.pubnub.internal.callbacks.SubscribeCallback {

    override fun status(pubnub: BasePubNub<*,*,*,*,*,*,*,*>, status: PNStatus) {
        listener.status(pubnub as PubNub, status)
    }

    override fun message(pubnub: BasePubNub<*,*,*,*,*,*,*,*>, event: PNMessageResult) {
        listener.message(pubnub as PubNubImpl, event)
    }

    override fun presence(pubnub: BasePubNub<*,*,*,*,*,*,*,*>, event: PNPresenceEventResult) {
        listener.presence(pubnub as PubNubImpl, event)
    }

    override fun signal(pubnub: BasePubNub<*,*,*,*,*,*,*,*>, event: PNSignalResult) {
        listener.signal(pubnub as PubNubImpl, event)
    }

    override fun messageAction(pubnub: BasePubNub<*,*,*,*,*,*,*,*>, event: PNMessageActionResult) {
        listener.messageAction(pubnub as PubNubImpl, event)
    }

    override fun objects(
        pubnub: BasePubNub<*,*,*,*,*,*,*,*>,
        event: com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult
    ) {
        listener.objects(pubnub as PubNubImpl, event.toApi())
    }

    override fun file(pubnub: BasePubNub<*,*,*,*,*,*,*,*>, event: PNFileEventResult) {
        listener.file(pubnub as PubNubImpl, event)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other || this.listener === other) return true
        if (other !is DelegatingSubscribeCallback) return false

        if (listener != other.listener) return false

        return true
    }

    override fun hashCode(): Int {
        return listener.hashCode()
    }
}