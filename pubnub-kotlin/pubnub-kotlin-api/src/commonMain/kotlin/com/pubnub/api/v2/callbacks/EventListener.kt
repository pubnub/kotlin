package com.pubnub.api.v2.callbacks

/**
 * Implement this interface and pass it into [EventEmitter.addListener] to listen for events from the PubNub real-time
 * network.
 */
expect interface EventListener : BaseEventListener
