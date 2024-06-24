package com.pubnub.api.v2.callbacks

import cocoapods.PubNubSwift.PubNubStatusListenerObjC
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.kmp.PubNub
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * Implement this interface and pass it into [com.pubnub.api.v2.callbacks.StatusEmitter.addListener] to listen for
 * PubNub connection status changes.
 */
@OptIn(ExperimentalForeignApi::class)
actual interface StatusListener : Listener {
    val underlying: PubNubStatusListenerObjC
    val onStatusChange: (PubNub, PNStatus) -> Unit
}

@OptIn(ExperimentalForeignApi::class)
class StatusListenerImpl(
    override val underlying: PubNubStatusListenerObjC,
    override val onStatusChange: (PubNub, PNStatus) -> Unit
): StatusListener {}