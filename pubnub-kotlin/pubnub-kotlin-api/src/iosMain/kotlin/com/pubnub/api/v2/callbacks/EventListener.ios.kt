package com.pubnub.api.v2.callbacks

import cocoapods.PubNubSwift.KMPEventListener
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * Implement this interface and pass it into [EventEmitter.addListener] to listen for events from the PubNub real-time
 * network.
 */

@OptIn(ExperimentalForeignApi::class)
actual interface EventListener : BaseEventListener {
    val underlying: KMPEventListener
    val onMessage: (PubNub, PNMessageResult) -> Unit
    val onPresence: (PubNub, PNPresenceEventResult) -> Unit
    val onSignal: (PubNub, PNSignalResult) -> Unit
    val onMessageAction: (PubNub, PNMessageActionResult) -> Unit
    val onObjects: (PubNub, PNObjectEventResult) -> Unit
    val onFile: (PubNub, PNFileEventResult) -> Unit
}

@OptIn(ExperimentalForeignApi::class)
class EventListenerImpl(
    override val underlying: KMPEventListener,
    override val onMessage: (PubNub, PNMessageResult) -> Unit,
    override val onPresence: (PubNub, PNPresenceEventResult) -> Unit,
    override val onSignal: (PubNub, PNSignalResult) -> Unit,
    override val onMessageAction: (PubNub, PNMessageActionResult) -> Unit,
    override val onObjects: (PubNub, PNObjectEventResult) -> Unit,
    override val onFile: (PubNub, PNFileEventResult) -> Unit
) : EventListener
