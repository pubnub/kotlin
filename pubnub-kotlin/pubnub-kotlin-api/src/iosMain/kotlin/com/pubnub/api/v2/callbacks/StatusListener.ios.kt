package com.pubnub.api.v2.callbacks

import cocoapods.PubNubSwift.StatusListenerObjC
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.PNStatus
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * Implement this interface and pass it into [com.pubnub.api.v2.callbacks.StatusEmitter.addListener] to listen for
 * PubNub connection status changes.
 */
@OptIn(ExperimentalForeignApi::class)
actual interface StatusListener : Listener {
    val underlying: StatusListenerObjC
}

@OptIn(ExperimentalForeignApi::class)
class StatusListenerImpl(
    override val underlying: StatusListenerObjC,
): StatusListener {}