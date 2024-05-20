package com.pubnub.kmp

import com.pubnub.api.JsonElementImpl
import com.pubnub.api.JsonElement
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubImpl
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.files.PNDownloadableFile
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
    return object : PubNubJs.ListenerParameters {
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
    }
}

actual fun createStatusListener(
    pubnub: PubNub,
    onStatus: (PubNub, PNStatus) -> Unit
): StatusListener {
    return object : PubNubJs.StatusListenerParameters {
        override val status: ((statusEvent: PubNubJs.StatusEvent) -> Unit) = { statusEvent ->
            onStatus(
                pubnub, PNStatus(
                    enumValueOf(statusEvent.category), //TODO parse category
                    null,
                    statusEvent.currentTimetoken.toString().toLong(),
                    statusEvent.affectedChannels.toList(),
                    statusEvent.affectedChannelGroups.toList()
                )
            )
        }
    }
}

actual fun createCustomObject(map: Map<String, Any?>): CustomObject {
    return CustomObjectImpl(map)
}