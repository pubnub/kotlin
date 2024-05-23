package com.pubnub.kmp

import cocoapods.PubNubSwift.EventListenerObjC
import cocoapods.PubNubSwift.PubNubMessageActionObjC
import cocoapods.PubNubSwift.PubNubMessageObjC
import com.pubnub.api.JsonElement
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubImpl
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.PubSubResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.EventListenerImpl
import com.pubnub.api.v2.callbacks.StatusListener
import kotlinx.cinterop.ExperimentalForeignApi

actual fun createPubNub(config: PNConfiguration): PubNub {
    return PubNubImpl(config)
}

@OptIn(ExperimentalForeignApi::class)
actual fun createEventListener(
    pubnub: PubNub,
    onMessage: (PubNub, PNMessageResult) -> Unit,
    onPresence: (PubNub, PNPresenceEventResult) -> Unit,
    onSignal: (PubNub, PNSignalResult) -> Unit,
    onMessageAction: (PubNub, PNMessageActionResult) -> Unit,
    onObjects: (PubNub, PNObjectEventResult) -> Unit,
    onFile: (PubNub, PNFileEventResult) -> Unit
): EventListener {
    return EventListenerImpl(
        underlying = EventListenerObjC(
            onMessage = {
                onMessage(pubnub, createMessageResult(it))
            },
            onPresence = {},
            onSignal = {
                createSignalResult(it)
            },
            onMessageActionAdded = {
                onMessageAction(pubnub, createMessageActionResult(it, "added"))
            },
            onMessageActionRemoved = {
                onMessageAction(pubnub, createMessageActionResult(it, "removed"))
            },
            onAppContext = {},
            onFile = {}
        )
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createMessageResult(from: PubNubMessageObjC?): PNMessageResult {
    return PNMessageResult(
        basePubSubResult = BasePubSubResult(
            channel = from?.channel() ?: "",
            subscription = from?.subscription(),
            timetoken = from?.published()?.toLong(),
            userMetadata = from?.metadata() as? JsonElement,
            publisher = from?.publisher()
        ),
        message = from?.payload() as JsonElement,
        error = null // TODO: Map error from Swift SDK to Kotlin's PubNubError
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createSignalResult(from: PubNubMessageObjC?): PNSignalResult {
    return PNSignalResult(
        basePubSubResult = BasePubSubResult(
            channel = from?.channel() ?: "",
            subscription = from?.subscription(),
            timetoken = from?.published()?.toLong(),
            userMetadata = from?.metadata() as? JsonElement,
            publisher = from?.publisher()
        ),
        message = from?.payload() as JsonElement
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createMessageActionResult(from: PubNubMessageActionObjC?, event: String): PNMessageActionResult {
    return PNMessageActionResult(
        result = BasePubSubResult(
            channel = from?.channel() ?: "",
            subscription = from?.subscription(),
            timetoken = from?.actionTimetoken()?.toLong(),
            userMetadata = null,
            publisher = from?.publisher()
        ),
        event = event,
        data = PNMessageAction(
            type = from?.actionType() ?: "",
            value = from?.actionValue() ?: "",
            messageTimetoken = from?.messageTimetoken()?.toLong() ?: 0
        )
    )
}

actual fun createStatusListener(
    pubnub: PubNub,
    onStatus: (PubNub, PNStatus) -> Unit
): StatusListener {
    TODO("Not yet implemented")
}

actual fun createCustomObject(map: Map<String, Any?>): CustomObject {
    TODO("Not yet implemented")
}