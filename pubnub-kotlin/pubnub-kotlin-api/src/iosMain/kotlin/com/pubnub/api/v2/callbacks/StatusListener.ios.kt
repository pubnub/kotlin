package com.pubnub.api.v2.callbacks

import cocoapods.PubNubSwift.StatusListenerObjC
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * Implement this interface and pass it into [com.pubnub.api.v2.callbacks.StatusEmitter.addListener] to listen for
 * PubNub connection status changes.
 */
@OptIn(ExperimentalForeignApi::class)
actual interface StatusListener : Listener {
    val underlying: StatusListenerObjC
    val onStatusChange: (PubNub, PNStatus) -> Unit
}

@OptIn(ExperimentalForeignApi::class)
class StatusListenerImpl(
    override val underlying: StatusListenerObjC,
    override val onStatusChange: (PubNub, PNStatus) -> Unit
): StatusListener {}