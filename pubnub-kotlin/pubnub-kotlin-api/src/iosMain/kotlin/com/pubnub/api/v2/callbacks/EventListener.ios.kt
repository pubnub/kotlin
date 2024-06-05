package com.pubnub.api.v2.callbacks

import cocoapods.PubNubSwift.EventListenerObjC
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.Listener
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
actual interface EventListener : Listener {
    val underlying: EventListenerObjC
}
@OptIn(ExperimentalForeignApi::class)
class EventListenerImpl(
    override val underlying: EventListenerObjC,
): EventListener {}