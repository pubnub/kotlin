package com.pubnub.kmp

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

actual fun createPubNub(config: PNConfiguration): PubNub {
    return com.pubnub.api.PubNub.create(config)
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
        override fun message(pubnub: com.pubnub.api.PubNub, result: PNMessageResult) {
            onMessage(pubnub, result)
        }

        override fun presence(pubnub: com.pubnub.api.PubNub, result: PNPresenceEventResult) {
            onPresence(pubnub, result)
        }

        override fun signal(pubnub: com.pubnub.api.PubNub, result: PNSignalResult) {
            onSignal(pubnub, result)
        }

        override fun messageAction(pubnub: com.pubnub.api.PubNub, result: PNMessageActionResult) {
            onMessageAction(pubnub, result)
        }

        override fun objects(pubnub: com.pubnub.api.PubNub, result: PNObjectEventResult) {
            onObjects(pubnub, result)
        }

        override fun file(pubnub: com.pubnub.api.PubNub, result: PNFileEventResult) {
            onFile(pubnub, result)
        }
    }
}

actual fun createStatusListener(
    pubnub: PubNub,
    onStatus: (PubNub, PNStatus) -> Unit
): StatusListener {
    return object : StatusListener {
        override fun status(pubnub: com.pubnub.api.PubNub, status: PNStatus) {
            onStatus(pubnub, status)
        }
    }
}

actual fun createCustomObject(map: Map<String, Any?>): CustomObject {
    return map
}