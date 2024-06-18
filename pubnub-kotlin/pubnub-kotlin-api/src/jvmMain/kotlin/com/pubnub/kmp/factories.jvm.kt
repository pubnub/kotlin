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
import com.pubnub.api.PubNub
import com.pubnub.kmp.PubNub as PubNubKmp

actual fun createPubNub(config: PNConfiguration): PubNubKmp {
    return PubNub.create(config)
}

actual fun createEventListener(
    pubnub: PubNubKmp,
    onMessage: (PubNubKmp, PNMessageResult) -> Unit,
    onPresence: (PubNubKmp, PNPresenceEventResult) -> Unit,
    onSignal: (PubNubKmp, PNSignalResult) -> Unit,
    onMessageAction: (PubNubKmp, PNMessageActionResult) -> Unit,
    onObjects: (PubNubKmp, PNObjectEventResult) -> Unit,
    onFile: (PubNubKmp, PNFileEventResult) -> Unit
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

actual fun createStatusListener(
    pubnub: PubNubKmp,
    onStatus: (PubNubKmp, PNStatus) -> Unit
): StatusListener {
    return object : StatusListener {
        override fun status(pubnub: PubNub, status: PNStatus) {
            onStatus(pubnub, status)
        }
    }
}

actual fun createCustomObject(map: Map<String, Any?>): CustomObject {
    return map
}