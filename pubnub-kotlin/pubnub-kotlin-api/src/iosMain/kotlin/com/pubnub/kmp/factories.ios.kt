package com.pubnub.kmp

import cocoapods.PubNubSwift.EventListenerObjC
import cocoapods.PubNubSwift.PubNubDeleteChannelMetadataEventMessageObjC
import cocoapods.PubNubSwift.PubNubDeleteMembershipEventMessageObjC
import cocoapods.PubNubSwift.PubNubDeleteUUIDMetadataEventMessageObjC
import cocoapods.PubNubSwift.PubNubFileEventResultObjC
import cocoapods.PubNubSwift.PubNubMessageActionObjC
import cocoapods.PubNubSwift.PubNubMessageObjC
import cocoapods.PubNubSwift.PubNubObjectEventMessageObjC
import cocoapods.PubNubSwift.PubNubObjectEventResultObjC
import cocoapods.PubNubSwift.PubNubPresenceEventResultObjC
import cocoapods.PubNubSwift.PubNubSetChannelMetadataEventMessageObjC
import cocoapods.PubNubSwift.PubNubSetMembershipEventMessageObjC
import cocoapods.PubNubSwift.PubNubSetUUIDMetadataEventMessageObjC
import com.pubnub.api.JsonElementImpl
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubImpl
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.files.PNDownloadableFile
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEvent
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage
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
        onMessage = onMessage,
        onMessageAction = onMessageAction,
        onPresence = onPresence,
        onObjects = onObjects,
        onFile = onFile,
        onSignal = onSignal,
        underlying = EventListenerObjC(
            onMessage = { onMessage(pubnub, createMessageResult(it)) },
            onPresence = { presenceEvents -> createPresenceEventResults(presenceEvents).forEach { onPresence(pubnub, it) } },
            onSignal = { onSignal(pubnub, createSignalResult(it)) },
            onMessageAction = { onMessageAction(pubnub, createMessageActionResult(it)) },
            onAppContext = {  if (createObjectEvent(it) != null) { createObjectEvent(it) } else {} },
            onFile = { onFile(pubnub, createFileEventResult(it)) }
        )
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createMessageResult(from: PubNubMessageObjC?): PNMessageResult {
    return PNMessageResult(
        basePubSubResult = BasePubSubResult(
            channel = from!!.channel(),
            subscription = from.subscription(),
            timetoken = from.published().toLong(),
            userMetadata = JsonElementImpl(from.metadata()),
            publisher = from.publisher()
        ),
        message = JsonElementImpl(from.payload()),
        error = null // TODO: Map error from Swift SDK to PubNubError in Kotlin SDK
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createSignalResult(from: PubNubMessageObjC?): PNSignalResult {
    return PNSignalResult(
        basePubSubResult = BasePubSubResult(
            channel = from!!.channel(),
            subscription = from.subscription(),
            timetoken = from.published().toLong(),
            userMetadata = JsonElementImpl(from.metadata()),
            publisher = from.publisher()
        ),
        message = JsonElementImpl(from.payload())
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createMessageActionResult(from: PubNubMessageActionObjC?): PNMessageActionResult {
    return PNMessageActionResult(
        result = BasePubSubResult(
            channel = from!!.channel(),
            subscription = from.subscription(),
            timetoken = from.actionTimetoken().toLong(),
            userMetadata = null,
            publisher = from.publisher()
        ),
        event = from.event(),
        data = PNMessageAction(
            type = from.actionType(),
            value = from.actionValue(),
            messageTimetoken = from.messageTimetoken().toLong()
        )
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createPresenceEventResults(from: List<*>?): List<PNPresenceEventResult> {
    return (from as List<PubNubPresenceEventResultObjC>).let {
        it.map { item: PubNubPresenceEventResultObjC ->
            PNPresenceEventResult(
                event = item.event(),
                uuid = item.uuid(),
                timestamp = item.timetoken()?.longValue,
                occupancy = item.occupancy()?.intValue,
                state = JsonElementImpl(item.state()),
                channel = item.channel() ?: "",
                subscription = item.subscription(),
                timetoken = item.timetoken()?.longValue,
                join = item.join() as? List<String>,
                leave = item.leave() as? List<String>,
                timeout = item.timeout() as? List<String>,
                hereNowRefresh = item.refreshHereNow()?.boolValue,
                userMetadata = JsonElementImpl(item.userMetadata())
            )
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun createFileEventResult(from: PubNubFileEventResultObjC?): PNFileEventResult {
    return PNFileEventResult(
        channel = from!!.channel(),
        timetoken = from.timetoken()?.longValue,
        publisher = from.publisher(),
        message = from.message(),
        jsonMessage = JsonElementImpl(from.message()),
        file = PNDownloadableFile(
            id = from.file().id(),
            name = from.file().name(),
            url = from.file().url()
        )
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createObjectEvent(from: PubNubObjectEventResultObjC?): PNObjectEventResult? {
    return mapObjectMessage(from?.message())?.let {
        PNObjectEventResult(
            result = BasePubSubResult(
                channel = from!!.channel(),
                subscription =  from.subscription(),
                timetoken = from.timetoken()?.longValue,
                userMetadata = JsonElementImpl(from.userMetadata()),
                publisher = from.publisher(),
            ), extractedMessage = it
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun mapObjectMessage(from: PubNubObjectEventMessageObjC?): PNObjectEventMessage? {
    if (from is PubNubSetUUIDMetadataEventMessageObjC) {
        return PNSetUUIDMetadataEventMessage(
            source = from.source(),
            version = from.version(),
            event = from.event(),
            type = from.type(),
            data = PNUUIDMetadata(
                id = from.data().id(),
                name = from.data().name(),
                externalId = from.data().externalId(),
                profileUrl = from.data().profileUrl(),
                email = from.data().email(),
                custom = from.data().custom() as? Map<String, Any?>,
                updated = from.data().updated(),
                eTag = from.data().eTag(),
                type = from.data().type(),
                status = from.data().status()
            )
        )
    } else if (from is PubNubDeleteUUIDMetadataEventMessageObjC) {
        return PNDeleteUUIDMetadataEventMessage(
            source = from.source(),
            version = from.version(),
            event = from.event(),
            type = from.type(),
            uuid = from.uuid()
        )
    } else if (from is PubNubSetChannelMetadataEventMessageObjC) {
        return PNSetChannelMetadataEventMessage(
            source = from.source(),
            version = from.version(),
            event = from.event(),
            type = from.type(),
            data = PNChannelMetadata(
                id = from.data().id(),
                name = from.data().name(),
                description = from.data().descr(),
                custom = from.data().custom() as? Map<String, Any?>,
                updated = from.data().updated(),
                eTag = from.data().eTag(),
                type = from.data().type(),
                status = from.data().status()
            )
        )
    } else if (from is PubNubDeleteChannelMetadataEventMessageObjC) {
        return PNDeleteChannelMetadataEventMessage(
            source = from.source(),
            version = from.version(),
            event = from.event(),
            type = from.type(),
            channel = from.channel()
        )
    } else if (from is PubNubSetMembershipEventMessageObjC) {
        return PNSetMembershipEventMessage(
            source = from.source(),
            version = from.version(),
            event = from.event(),
            type = from.type(),
            data = PNSetMembershipEvent(
                channel = from.data().channel(),
                uuid = from.data().uuid(),
                custom = from.data().custom() as Map<String, Any?>?,
                eTag = from.data().eTag(),
                updated = from.data().updated(),
                status = from.data().status()
            )
        )
    } else if (from is PubNubDeleteMembershipEventMessageObjC) {
        return PNDeleteMembershipEventMessage(
            source = from.source(),
            version = from.version(),
            event = from.event(),
            type = from.type(),
            data = PNDeleteMembershipEvent(
                channelId = from.data().channelId(),
                uuid = from.data().uuid()
            )
        )
    } else {
        return null
    }
}

actual fun createStatusListener(
    pubnub: PubNub,
    onStatus: (PubNub, PNStatus) -> Unit
): StatusListener {
    TODO("Not yet implemented")
}

actual fun createCustomObject(map: Map<String, Any?>): CustomObject {
    return CustomObject(map)
}