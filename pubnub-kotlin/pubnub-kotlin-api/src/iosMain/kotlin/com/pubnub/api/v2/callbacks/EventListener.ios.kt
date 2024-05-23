package com.pubnub.api.v2.callbacks

import cocoapods.PubNubSwift.EventListenerObjC
import com.pubnub.api.callbacks.Listener
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
open class EventListenerImpl(
    override val underlying: EventListenerObjC
): EventListener {}