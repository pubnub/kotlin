package com.pubnub.api

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener

actual fun createCommonPubNub(config: PNConfiguration): PubNub {
    return PubNub.create(config)
}

actual fun createEventListener(
    pubnub: PubNub,
    onMessage: (PubNub, PNMessageResult) -> Unit,
    onPresence: (PubNub, PNPresenceEventResult) -> Unit,
    onSignal: (PubNub, PNSignalResult) -> Unit,
    onMessageAction: (PubNub, PNMessageActionResult) -> Unit,
    onObjects: (PubNub, PNObjectEventResult) -> Unit,
    onFile: (PubNub, PNFileEventResult) -> Unit
): EventListener {
    return object : EventListener {
        override fun message(pubnub: PubNub, result: PNMessageResult) {
            onMessage(pubnub, result)
        }

        override fun presence(pubnub: PubNub, result: PNPresenceEventResult) {
            onPresence(pubnub, result)
        }

        override fun signal(pubnub: PubNub, result: PNSignalResult) {
            onSignal(pubnub, result)
        }

        override fun messageAction(pubnub: PubNub, result: PNMessageActionResult) {
            onMessageAction(pubnub, result)
        }

        override fun objects(pubnub: PubNub, result: PNObjectEventResult) {
            onObjects(pubnub, result)
        }

        override fun file(pubnub: PubNub, result: PNFileEventResult) {
            onFile(pubnub, result)
        }
    }
}

actual typealias CustomObject = Any

actual fun createCustomObject(map: Map<String, Any?>): CustomObject {
    return map
}

actual fun createStatusListener(
    pubnub: PubNub,
    onStatus: (PubNub, PNStatus) -> Unit
): StatusListener {
    return object : StatusListener {
        override fun status(pubnub: PubNub, status: PNStatus) {
            onStatus(pubnub, status)
        }
    }
}