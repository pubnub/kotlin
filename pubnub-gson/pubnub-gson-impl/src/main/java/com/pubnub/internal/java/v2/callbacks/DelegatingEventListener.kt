package com.pubnub.internal.java.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener

/**
 * Implement this interface and pass it into [EventEmitter.addListener] to listen for events from the PubNub real-time
 * network.
 */
data class DelegatingEventListener(
    private val listener: com.pubnub.api.java.v2.callbacks.EventListener,
    private val pubnubJava: com.pubnub.api.java.PubNub,
) : EventListener {
    override fun message(pubnub: PubNub, result: PNMessageResult) {
        listener.message(pubnubJava, result)
    }

    override fun presence(
        pubnub: PubNub,
        result: PNPresenceEventResult,
    ) {
        listener.presence(pubnubJava, result)
    }

    override fun signal(pubnub: PubNub, result: PNSignalResult) {
        listener.signal(pubnubJava, result)
    }

    override fun messageAction(
        pubnub: PubNub,
        result: PNMessageActionResult,
    ) {
        listener.messageAction(pubnubJava, result)
    }

    override fun file(
        pubnub: PubNub,
        result: PNFileEventResult,
    ) {
        listener.file(pubnubJava, result)
    }

    override fun objects(pubnub: PubNub, result: PNObjectEventResult) {
        Converters.objects(result, listener, pubnubJava)
    }
}
