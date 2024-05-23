package com.pubnub.kmp

import cocoapods.PubNubSwift.EventListenerObjC
import cocoapods.PubNubSwift.PubNubMessageActionObjC
import cocoapods.PubNubSwift.PubNubMessageObjC
import cocoapods.PubNubSwift.PubNubPresenceEventResultObjC
import com.pubnub.api.JsonElement
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubImpl
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
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
            onPresence = { presenceEvents ->
                createPresenceEventResults(presenceEvents).forEach {
                    onPresence(pubnub, it)
                }
            },
            onSignal = {
                onSignal(pubnub, createSignalResult(it))
            },
            onMessageAction = {
                onMessageAction(pubnub, createMessageActionResult(it, "added"))
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
        error = null // TODO: Map error from Swift SDK to PubNubError in Kotlin SDK
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

@OptIn(ExperimentalForeignApi::class)
private fun createPresenceEventResults(from: List<*>?): List<PNPresenceEventResult> {
    return (from as? List<PubNubPresenceEventResultObjC>)?.let {
        it.map { item: PubNubPresenceEventResultObjC ->
            PNPresenceEventResult(
                event = item.event(),
                uuid = item.uuid(),
                timestamp = item.timestamp()?.longValue,
                occupancy = item.occupancy()?.intValue,
                state = item.state() as JsonElement,
                channel = item.channel() ?: "",
                subscription = item.subscription(),
                timetoken = item.timetoken()?.longValue,
                join = item.join() as? List<String>,
                leave = item.leave() as? List<String>,
                timeout = item.timeout() as? List<String>,
                hereNowRefresh = item.refreshHereNow()?.boolValue,
                userMetadata = item.userMetadata()
            )
        }
    } ?: emptyList()
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