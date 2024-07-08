package com.pubnub.kmp

import com.pubnub.api.JsonElementImpl
import com.pubnub.api.PubNubImpl
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.files.PNDownloadableFile
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
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
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import PubNub as PubNubJs

actual fun createPubNub(config: PNConfiguration): PubNub {
    return PubNubImpl(config)
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
    val listener = object : PubNubJs.ListenerParameters, EventListener {
        override val message: (PubNubJs.MessageEvent) -> Unit = { messageEvent ->
            onMessage(
                pubnub, PNMessageResult(
                    BasePubSubResult(
                        messageEvent.channel,
                        messageEvent.subscription,
                        messageEvent.timetoken.toLong(),
                        JsonElementImpl(messageEvent.userMetadata),
                        messageEvent.publisher
                    ),
                    JsonElementImpl(messageEvent.message),
                    null //TODO kmp error
                )
            )
        }
        override val presence: ((presenceEvent: PubNubJs.PresenceEvent) -> Unit) = { presenceEvent ->
            onPresence(
                pubnub, PNPresenceEventResult(
                    presenceEvent.action,
                    presenceEvent.uuid,
                    presenceEvent.timestamp.toLong(),
                    presenceEvent.occupancy.toInt(),
                    JsonElementImpl(presenceEvent.state),
                    presenceEvent.channel,
                    presenceEvent.subscription,
                    presenceEvent.timetoken.toLong(),
                )
            )

        }
        override val signal: ((signalEvent: PubNubJs.SignalEvent) -> Unit) = { signalEvent ->
            onSignal(
                pubnub, PNSignalResult(
                    BasePubSubResult(
                        signalEvent.channel,
                        signalEvent.subscription,
                        signalEvent.timetoken.toLong(),
                        null,
                        signalEvent.publisher
                    ),
                    JsonElementImpl(signalEvent.message),
                )
            )
        }

        override val messageAction: ((messageActionEvent: PubNubJs.MessageActionEvent) -> Unit) =
            { messageActionEvent ->
                onMessageAction(pubnub, PNMessageActionResult(
                    BasePubSubResult(
                        messageActionEvent.channel,
                        messageActionEvent.subscription,
                        messageActionEvent.timetoken.toLong(),
                        null,
                        messageActionEvent.publisher
                    ),
                    messageActionEvent.event,
                    PNMessageAction(
                        messageActionEvent.data.type,
                        messageActionEvent.data.value,
                        messageActionEvent.data.messageTimetoken.toLong()
                    ).apply {
                        actionTimetoken = messageActionEvent.data.messageTimetoken.toLong()
                    }
                ))
            }
        override val file: ((fileEvent: PubNubJs.FileEvent) -> Unit) = { fileEvent ->
            onFile(
                pubnub, PNFileEventResult(
                    fileEvent.channel,
                    fileEvent.timetoken.toLong(),
                    fileEvent.publisher,
                    fileEvent.message,
                    PNDownloadableFile(fileEvent.file.id, fileEvent.file.name, fileEvent.file.url),
                    JsonElementImpl(fileEvent.message),
                    fileEvent.subscription,
                    null // TODO kmp error
                )
            )
        }
        override val objects = { event: PubNubJs.BaseObjectsEvent ->
            val eventAndType = event.message.event to event.message.type
            onObjects(pubnub, PNObjectEventResult(
                BasePubSubResult(
                    event.channel,
                    event.subscription,
                    event.timetoken.toLong(),
                    null,
                    event.publisher
                ),
                when (eventAndType) {
                    "set" to "channel" -> PNSetChannelMetadataEventMessage(
                        event.message.source,
                        event.message.version,
                        event.message.event,
                        event.message.type,
                        event.message.data.unsafeCast<PubNubJs.ChannelMetadataObject>().toChannelMetadata()
                    )

                    "set" to "uuid" -> PNSetUUIDMetadataEventMessage(
                        event.message.source,
                        event.message.version,
                        event.message.event,
                        event.message.type,
                        event.message.data.unsafeCast<PubNubJs.UUIDMetadataObject>().toPNUUIDMetadata()
                    )

                    "set" to "membership" -> event.message.data.unsafeCast<PubNubJs.SetMembershipObject>().let {
                        PNSetMembershipEventMessage(
                            event.message.source,
                            event.message.version,
                            event.message.event,
                            event.message.type,
                            PNSetMembershipEvent(
                                it.channel.id,
                                it.uuid.id,
                                it.custom?.toMap(),
                                it.eTag,
                                it.updated,
                                null //todo missing
                            )
                        )
                    }

                    "delete" to "channel" -> PNDeleteChannelMetadataEventMessage(
                        event.message.source,
                        event.message.version,
                        event.message.event,
                        event.message.type,
                        event.message.data.asDynamic().id
                    )

                    "delete" to "uuid" -> PNDeleteUUIDMetadataEventMessage(
                        event.message.source,
                        event.message.version,
                        event.message.event,
                        event.message.type,
                        event.message.data.asDynamic().id
                    )

                    "delete" to "membership" -> PNDeleteMembershipEventMessage(
                        event.message.source,
                        event.message.version,
                        event.message.event,
                        event.message.type,
                        event.message.data.unsafeCast<PubNubJs.DeleteMembershipObject>().let {
                            PNDeleteMembershipEvent(
                                it.channel.id,
                                it.uuid.id
                            )
                        }
                    )

                    else -> throw IllegalStateException("Bad object event")
                }
            ))
        }
    }
    return listener
}

actual fun createStatusListener(
    pubnub: PubNub,
    onStatus: (PubNub, PNStatus) -> Unit
): StatusListener {
    val listener = object : PubNubJs.StatusListenerParameters, StatusListener {
        override val status: ((statusEvent: PubNubJs.StatusEvent) -> Unit) = { statusEvent ->
            val category = try {
                enumValueOf<PNStatusCategory>(statusEvent.category) //TODO parse category
            } catch (e: Exception) {
                null
            }
            if (category != null) {
                onStatus(
                    pubnub, PNStatus(
                        category,
                        null,
                        statusEvent.currentTimetoken.toString().toLongOrNull(),
                        statusEvent.affectedChannels?.toList() ?: emptyList(),
                        statusEvent.affectedChannelGroups?.toList() ?: emptyList()
                    )
                )
            }
        }
    }
    return listener
}

actual fun createCustomObject(map: Map<String, Any?>): CustomObject {
    return CustomObjectImpl(map)
}