package com.pubnub.kmp

import cocoapods.PubNubSwift.PubNubAppContextEventObjC
import cocoapods.PubNubSwift.PubNubConnectionStatusCategoryObjCConnected
import cocoapods.PubNubSwift.PubNubConnectionStatusCategoryObjCConnectionError
import cocoapods.PubNubSwift.PubNubConnectionStatusCategoryObjCDisconnected
import cocoapods.PubNubSwift.PubNubConnectionStatusCategoryObjCDisconnectedUnexpectedly
import cocoapods.PubNubSwift.PubNubConnectionStatusCategoryObjCHeartbeatFailed
import cocoapods.PubNubSwift.PubNubConnectionStatusCategoryObjCHeartbeatSuccess
import cocoapods.PubNubSwift.PubNubConnectionStatusCategoryObjCMalformedResponseCategory
import cocoapods.PubNubSwift.PubNubConnectionStatusCategoryObjCSubscriptionChanged
import cocoapods.PubNubSwift.PubNubEventListenerObjC
import cocoapods.PubNubSwift.PubNubFileChangeEventObjC
import cocoapods.PubNubSwift.PubNubMessageActionObjC
import cocoapods.PubNubSwift.PubNubMessageObjC
import cocoapods.PubNubSwift.PubNubPresenceChangeObjC
import cocoapods.PubNubSwift.PubNubRemoveChannelMetadataResultObjC
import cocoapods.PubNubSwift.PubNubRemoveMembershipResultObjC
import cocoapods.PubNubSwift.PubNubRemoveUUIDMetadataResultObjC
import cocoapods.PubNubSwift.PubNubSetChannelMetadataResultObjC
import cocoapods.PubNubSwift.PubNubSetMembershipResultObjC
import cocoapods.PubNubSwift.PubNubSetUUIDMetadataResultObjC
import cocoapods.PubNubSwift.PubNubStatusListenerObjC
import com.pubnub.api.JsonElementImpl
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.PubNubImpl
import com.pubnub.api.enums.PNStatusCategory
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
import com.pubnub.api.v2.callbacks.StatusListenerImpl
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
        underlying = PubNubEventListenerObjC(
            onMessage = { onMessage(pubnub, createMessageResult(it)) },
            onPresence = { presenceEvents -> createPresenceEventResults(presenceEvents).forEach { onPresence(pubnub, it) } },
            onSignal = { onSignal(pubnub, createSignalResult(it)) },
            onMessageAction = { onMessageAction(pubnub, createMessageActionResult(it)) },
            onAppContext = { value -> createObjectEvent(value)?.let { res -> onObjects(pubnub, res) } },
            onFile = { onFile(pubnub, createFileEventResult(it)) }
        ),
        onMessage = onMessage,
        onPresence = onPresence,
        onSignal = onSignal,
        onMessageAction = onMessageAction,
        onObjects = onObjects,
        onFile = onFile
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
    val messageAction = PNMessageAction(
        type = from!!.actionType(),
        value = from.actionValue(),
        messageTimetoken = from.messageTimetoken().toLong()
    ).apply {
        actionTimetoken = from.actionTimetoken().toLong()
        uuid = from.publisher()
    }

    return PNMessageActionResult(
        result = BasePubSubResult(
            channel = from.channel(),
            subscription = from.subscription(),
            timetoken = from.actionTimetoken().toLong(),
            userMetadata = null,
            publisher = from.publisher()
        ),
        event = from.event(),
        data = messageAction
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createPresenceEventResults(from: List<*>?): List<PNPresenceEventResult> {
    return from.filterAndMap { rawValue: PubNubPresenceChangeObjC ->
        PNPresenceEventResult(
            event = rawValue.event(),
            uuid = rawValue.uuid(),
            timestamp = rawValue.timetoken()?.longValue,
            occupancy = rawValue.occupancy()?.intValue,
            state = JsonElementImpl(rawValue.state()),
            channel = rawValue.channel().orEmpty(),
            subscription = rawValue.subscription(),
            timetoken = rawValue.timetoken()?.longValue,
            join = rawValue.join()?.filterIsInstance<String>(),
            leave = rawValue.leave()?.filterIsInstance<String>(),
            timeout = rawValue.timeout()?.filterIsInstance<String>(),
            hereNowRefresh = rawValue.refreshHereNow()?.boolValue,
            userMetadata = JsonElementImpl(rawValue.userMetadata())
        )
    }.toList()
}

@OptIn(ExperimentalForeignApi::class)
private fun createFileEventResult(from: PubNubFileChangeEventObjC?): PNFileEventResult {
    return PNFileEventResult(
        channel = from!!.channel(),
        timetoken = from.timetoken()?.longValue,
        publisher = from.publisher(),
        message = from.message(),
        jsonMessage = JsonElementImpl(from.message()),
        file = PNDownloadableFile(
            id = from.file().id(),
            name = from.file().name(),
            url = from.file().url()?.absoluteString.orEmpty()
        )
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createObjectEvent(from: PubNubAppContextEventObjC?): PNObjectEventResult? {
    return mapAppContextEvent(from)?.let {
        PNObjectEventResult(
            result = BasePubSubResult(
                channel = from!!.channel(),
                subscription = from.subscription(),
                timetoken = from.timetoken()?.longValue,
                userMetadata = JsonElementImpl(from.userMetadata()),
                publisher = from.publisher(),
            ),
            extractedMessage = it
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun mapAppContextEvent(from: PubNubAppContextEventObjC?): PNObjectEventMessage? {
    when (from) {
        is PubNubSetUUIDMetadataResultObjC ->
            return PNSetUUIDMetadataEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                data = PNUUIDMetadata(
                    id = from.metadata().id(),
                    name = from.metadata().name(),
                    externalId = from.metadata().externalId(),
                    profileUrl = from.metadata().profileUrl(),
                    email = from.metadata().email(),
                    custom = from.metadata().custom()?.safeCast(),
                    updated = from.metadata().updated(),
                    eTag = from.metadata().eTag(),
                    type = from.metadata().type(),
                    status = from.metadata().status()
                )
            )
        is PubNubRemoveUUIDMetadataResultObjC ->
            return PNDeleteUUIDMetadataEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                uuid = from.uuid()
            )
        is PubNubSetChannelMetadataResultObjC ->
            return PNSetChannelMetadataEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                data = PNChannelMetadata(
                    id = from.metadata().id(),
                    name = from.metadata().name(),
                    description = from.metadata().descr(),
                    custom = from.metadata().custom()?.safeCast(),
                    updated = from.metadata().updated(),
                    eTag = from.metadata().eTag(),
                    type = from.metadata().type(),
                    status = from.metadata().status()
                )
            )
        is PubNubRemoveChannelMetadataResultObjC ->
            return PNDeleteChannelMetadataEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                channel = from.channel()
            )
        is PubNubSetMembershipResultObjC ->
            return PNSetMembershipEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                data = PNSetMembershipEvent(
                    channel = from.metadata().channelMetadataId(),
                    uuid = from.metadata().uuidMetadataId(),
                    custom = from.metadata().custom()?.safeCast(),
                    eTag = from.metadata().eTag().orEmpty(),
                    updated = from.metadata().updated().orEmpty(),
                    status = from.metadata().status().orEmpty()
                )
            )
        is PubNubRemoveMembershipResultObjC ->
            return PNDeleteMembershipEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                data = PNDeleteMembershipEvent(
                    channelId = from.channelId(),
                    uuid = from.uuid()
                )
            )
        else -> return null
    }
}

@OptIn(ExperimentalForeignApi::class)
actual fun createStatusListener(
    pubnub: PubNub,
    onStatus: (PubNub, PNStatus) -> Unit
): StatusListener {
    return StatusListenerImpl(
        underlying = PubNubStatusListenerObjC(onStatusChange = { status ->
            when (status?.category()) {
                PubNubConnectionStatusCategoryObjCConnected ->
                    PNStatusCategory.PNConnectedCategory
                PubNubConnectionStatusCategoryObjCDisconnected ->
                    PNStatusCategory.PNDisconnectedCategory
                PubNubConnectionStatusCategoryObjCDisconnectedUnexpectedly ->
                    PNStatusCategory.PNUnexpectedDisconnectCategory
                PubNubConnectionStatusCategoryObjCConnectionError ->
                    PNStatusCategory.PNConnectionError
                PubNubConnectionStatusCategoryObjCMalformedResponseCategory ->
                    PNStatusCategory.PNMalformedResponseCategory
                PubNubConnectionStatusCategoryObjCHeartbeatFailed ->
                    PNStatusCategory.PNHeartbeatFailed
                PubNubConnectionStatusCategoryObjCHeartbeatSuccess ->
                    PNStatusCategory.PNHeartbeatSuccess
                PubNubConnectionStatusCategoryObjCSubscriptionChanged ->
                    PNStatusCategory.PNSubscriptionChanged
                else -> null
            }?.let { category ->
                onStatus(
                    pubnub,
                    PNStatus(
                        category = category,
                        exception = status?.error()?.let { error -> PubNubException(errorMessage = error.localizedDescription) },
                        currentTimetoken = status?.currentTimetoken()?.longValue(),
                        affectedChannels = status?.affectedChannels()?.filterIsInstance<String>()?.toSet() ?: emptySet(),
                        affectedChannelGroups = status?.affectedChannelGroups()?.filterIsInstance<String>()?.toSet() ?: emptySet()
                    )
                )
            }
        }),
        onStatusChange = onStatus
    )
}

actual fun createCustomObject(map: Map<String, Any?>): CustomObject {
    return CustomObject(map)
}
