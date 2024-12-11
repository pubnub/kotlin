package com.pubnub.kmp

import cocoapods.PubNubSwift.KMPAppContextEventResult
import cocoapods.PubNubSwift.KMPConnectionStatusCategoryConnected
import cocoapods.PubNubSwift.KMPConnectionStatusCategoryConnectionError
import cocoapods.PubNubSwift.KMPConnectionStatusCategoryDisconnected
import cocoapods.PubNubSwift.KMPConnectionStatusCategoryDisconnectedUnexpectedly
import cocoapods.PubNubSwift.KMPConnectionStatusCategoryHeartbeatFailed
import cocoapods.PubNubSwift.KMPConnectionStatusCategoryHeartbeatSuccess
import cocoapods.PubNubSwift.KMPConnectionStatusCategoryMalformedResponseCategory
import cocoapods.PubNubSwift.KMPConnectionStatusCategorySubscriptionChanged
import cocoapods.PubNubSwift.KMPEventListener
import cocoapods.PubNubSwift.KMPFileChangeEvent
import cocoapods.PubNubSwift.KMPMessage
import cocoapods.PubNubSwift.KMPMessageAction
import cocoapods.PubNubSwift.KMPPresenceChange
import cocoapods.PubNubSwift.KMPRemoveChannelMetadataResult
import cocoapods.PubNubSwift.KMPRemoveMembershipResult
import cocoapods.PubNubSwift.KMPRemoveUUIDMetadataResult
import cocoapods.PubNubSwift.KMPSetChannelMetadataResult
import cocoapods.PubNubSwift.KMPSetMembershipResult
import cocoapods.PubNubSwift.KMPSetUUIDMetadataResult
import cocoapods.PubNubSwift.KMPStatusListener
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
import com.pubnub.api.utils.PatchValue
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
        underlying = KMPEventListener(
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
private fun createMessageResult(from: KMPMessage?): PNMessageResult {
    return PNMessageResult(
        basePubSubResult = BasePubSubResult(
            channel = from!!.channel(),
            subscription = from.subscription(),
            timetoken = from.published().toLong(),
            userMetadata = JsonElementImpl(from.metadata()),
            publisher = from.publisher()
        ),
        message = JsonElementImpl(from.payload()),
        customMessageType = from.customMessageType(),
        error = null // TODO: Map error from Swift SDK to PubNubError in Kotlin SDK
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createSignalResult(from: KMPMessage?): PNSignalResult {
    return PNSignalResult(
        basePubSubResult = BasePubSubResult(
            channel = from!!.channel(),
            subscription = from.subscription(),
            timetoken = from.published().toLong(),
            userMetadata = JsonElementImpl(from.metadata()),
            publisher = from.publisher()
        ),
        message = JsonElementImpl(from.payload()),
        customMessageType = from.customMessageType()
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createMessageActionResult(from: KMPMessageAction?): PNMessageActionResult {
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
    return from.filterAndMap { rawValue: KMPPresenceChange ->
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
private fun createFileEventResult(from: KMPFileChangeEvent?): PNFileEventResult {
    return PNFileEventResult(
        channel = from!!.channel(),
        timetoken = from.timetoken()?.longValue,
        publisher = from.publisher(),
        message = from.message(),
        customMessageType = from.customMessageType(),
        jsonMessage = JsonElementImpl(from.message()),
        file = PNDownloadableFile(
            id = from.file().id(),
            name = from.file().name(),
            url = from.file().url()?.absoluteString.orEmpty()
        )
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun createObjectEvent(from: KMPAppContextEventResult?): PNObjectEventResult? {
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

// TODO: PatchValue should consider cases where there is no response for a given field
@OptIn(ExperimentalForeignApi::class)
private fun mapAppContextEvent(from: KMPAppContextEventResult?): PNObjectEventMessage? {
    when (from) {
        is KMPSetUUIDMetadataResult ->
            return PNSetUUIDMetadataEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                data = PNUUIDMetadata(
                    id = from.metadata().id(),
                    name = if (from.metadata().hasName()) {
                        PatchValue.of(from.metadata().name())
                    } else {
                        null
                    },
                    externalId = if (from.metadata().hasExternalId()) {
                        PatchValue.of(from.metadata().externalId())
                    } else {
                        null
                    },
                    profileUrl = if (from.metadata().hasProfileUrl()) {
                        PatchValue.of(from.metadata().profileUrl())
                    } else {
                        null
                    },
                    email = if (from.metadata().hasEmail()) {
                        PatchValue.of(from.metadata().email())
                    } else {
                        null
                    },
                    custom = if (from.metadata().hasCustom()) {
                        PatchValue.of(from.metadata().custom()?.safeCast<String, Any?>())
                    } else {
                        null
                    },
                    updated = from.metadata().updated()?.let { PatchValue.of(it) },
                    eTag = from.metadata().eTag()?.let { PatchValue.of(it) },
                    type = if (from.metadata().hasType()) {
                        PatchValue.of(from.metadata().type())
                    } else {
                        null
                    },
                    status = if (from.metadata().hasStatus()) {
                        PatchValue.of(from.metadata().status())
                    } else {
                        null
                    }
                )
            )
        is KMPRemoveUUIDMetadataResult ->
            return PNDeleteUUIDMetadataEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                uuid = from.uuid()
            )
        is KMPSetChannelMetadataResult ->
            return PNSetChannelMetadataEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                data = PNChannelMetadata(
                    id = from.metadata().id(),
                    name = if (from.metadata().hasName()) {
                        PatchValue.of(from.metadata().name())
                    } else {
                        null
                    },
                    description = if (from.metadata().hasDescr()) {
                        PatchValue.of(from.metadata().descr())
                    } else {
                        null
                    },
                    custom = if (from.metadata().hasCustom()) {
                        PatchValue.of(from.metadata().custom()?.safeCast<String, Any?>())
                    } else {
                        null
                    },
                    updated = from.metadata().updated()?.let { PatchValue.of(it) },
                    eTag = from.metadata().eTag()?.let { PatchValue.of(it) },
                    type = if (from.metadata().hasType()) {
                        PatchValue.of(from.metadata().type())
                    } else {
                        null
                    },
                    status = if (from.metadata().hasStatus()) {
                        PatchValue.of(from.metadata().status())
                    } else {
                        null
                    }
                )
            )
        is KMPRemoveChannelMetadataResult ->
            return PNDeleteChannelMetadataEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                channel = from.channel()
            )
        is KMPSetMembershipResult ->
            return PNSetMembershipEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                data = PNSetMembershipEvent(
                    channel = from.metadata().channelMetadataId(),
                    uuid = from.metadata().uuidMetadataId(),
                    custom = PatchValue.of(from.metadata().custom()?.safeCast()),
                    eTag = from.metadata().eTag().orEmpty(),
                    updated = from.metadata().updated().orEmpty(),
                    status = PatchValue.of(from.metadata().status()),
                    type = PatchValue.of(from.metadata().type())
                )
            )
        is KMPRemoveMembershipResult ->
            return PNDeleteMembershipEventMessage(
                source = from.source(),
                version = from.version(),
                event = from.event(),
                type = from.type(),
                data = PNDeleteMembershipEvent(
                    channelId = from.channelId(),
                    uuid = from.userId()
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
        underlying = KMPStatusListener(onStatusChange = { status ->
            when (status?.category()) {
                KMPConnectionStatusCategoryConnected ->
                    PNStatusCategory.PNConnectedCategory
                KMPConnectionStatusCategoryDisconnected ->
                    PNStatusCategory.PNDisconnectedCategory
                KMPConnectionStatusCategoryDisconnectedUnexpectedly ->
                    PNStatusCategory.PNUnexpectedDisconnectCategory
                KMPConnectionStatusCategoryConnectionError ->
                    PNStatusCategory.PNConnectionError
                KMPConnectionStatusCategoryMalformedResponseCategory ->
                    PNStatusCategory.PNMalformedResponseCategory
                KMPConnectionStatusCategoryHeartbeatFailed ->
                    PNStatusCategory.PNHeartbeatFailed
                KMPConnectionStatusCategoryHeartbeatSuccess ->
                    PNStatusCategory.PNHeartbeatSuccess
                KMPConnectionStatusCategorySubscriptionChanged ->
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
